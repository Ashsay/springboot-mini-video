package com.wx.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.UUID;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.immoc.enums.VideoStatusEnum;
import com.wx.pojo.Bgm;
import com.wx.pojo.Comments;
import com.wx.pojo.Videos;
import com.wx.service.BgmService;
import com.wx.service.VideoService;
import com.wx.utils.FetchVideoCover;
import com.wx.utils.MergeVideoMp3;
import com.wx.utils.PagedResult;
import com.wx.utils.WxJSONResult;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@Api(value = "视频相关业务接口", tags = {"视频业务相关的controller"})
@RequestMapping("/video")
public class VideoController extends BasicController {
	
	@Autowired
	private VideoService videoService;
	
	@Autowired
	private BgmService bgmService;

	@ApiOperation(value = "用户上传视频", notes = "用户上传视频的接口")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "userId", value = "用户id", required = true, dataType = "String", paramType = "query"),
		@ApiImplicitParam(name = "bgmId", value = "背景音乐id", required = false, dataType = "String", paramType = "query"),
		@ApiImplicitParam(name = "videoSeconds", value = "视频播放长度", required = true, dataType = "Double", paramType = "query"),
		@ApiImplicitParam(name = "videoWidth", value = "视频宽度", required = true, dataType = "Integer", paramType = "query"),
		@ApiImplicitParam(name = "videoHeight", value = "视频高度", required = true, dataType = "Integer", paramType = "query"),
		@ApiImplicitParam(name = "desc", value = "视频描述", required = false, dataType = "String", paramType = "query")
	})
	@PostMapping(value = "/upload", headers = "content-type=multipart/form-data")
	public WxJSONResult uploadFace(String userId, String bgmId,  double videoSeconds, int videoWidth, int videoHeight, String desc,
			@ApiParam(value = "短视频", required = true) MultipartFile file) throws Exception {
		
		if (StringUtils.isBlank(userId)) {
			return WxJSONResult.errorMsg("用户ID不能为空");
		}
		
		//保存到数据库中的相对路径
		String uploadPathDB = "/" + userId + "/video";
		String coverPathDB = "/" + userId + "/video";
		
		FileOutputStream fileOutputStream = null;
		InputStream inputStream = null;
		
		String finalVideoPath = "";
		
		try {
			if(file != null) {
				
				String fileName = file.getOriginalFilename();
								
				String arrayFilenameItem[] =  fileName.split("\\.");
				String fileNamePrefix = "";
				for (int i = 0 ; i < arrayFilenameItem.length-1 ; i ++) {
					fileNamePrefix += arrayFilenameItem[i];
				}
				// fix bug: 解决小程序端OK，PC端不OK的bug，原因：PC端和小程序端对临时视频的命名不同
				// String fileNamePrefix = fileName.split("\\.")[0];	
				
				if(StringUtils.isNotBlank(fileName)) {
					//文间上传的绝对路径
					finalVideoPath = FILE_SPACE + uploadPathDB + "/" + fileName;
					uploadPathDB += ("/" + fileName);
					coverPathDB = coverPathDB + "/" + fileNamePrefix + ".jpg";
					
					File outFile = new File(finalVideoPath);
					if(outFile.getParentFile() != null || !outFile.getParentFile().isDirectory()) {
						//创建父文件夹
						outFile.getParentFile().mkdirs();
					}
					
					fileOutputStream = new FileOutputStream(outFile);
					inputStream = file.getInputStream();
					IOUtils.copy(inputStream, fileOutputStream);
				}
			}else {
				WxJSONResult.errorMsg("上传出错...");
			}
		} catch (Exception e) {
			
			e.printStackTrace();
			return WxJSONResult.errorMsg("上传失败 ....");
			
		} finally {
			if(fileOutputStream != null) {
				fileOutputStream.flush();
				fileOutputStream.close();
			}
		}
		
		if(StringUtils.isNotBlank(bgmId)) {
			Bgm bgm = bgmService.queryBgmById(bgmId);
			String mp3InputPath = FILE_SPACE + bgm.getPath();
			
			MergeVideoMp3 tool = new MergeVideoMp3(FFMPEG_EXE);
			String videoInputPath = finalVideoPath;
			
			String videoOutputNameString = UUID.randomUUID().toString() + ".mp4";
			uploadPathDB = "/" + userId + "/video" + "/" + videoOutputNameString;
			finalVideoPath = FILE_SPACE + uploadPathDB;
			
			tool.convertor(videoInputPath, mp3InputPath, videoSeconds, finalVideoPath);
		}
		
		System.out.println("uploadPathDB=" + uploadPathDB);
		System.out.println("finalVideoPath=" + finalVideoPath);
		
		// 对视频进行截图
		FetchVideoCover videoInfo = new FetchVideoCover(FFMPEG_EXE);
		videoInfo.getCover(finalVideoPath, FILE_SPACE + coverPathDB);
		
		//保存视频信息到数据库
		Videos videos = new Videos();
		videos.setAudioId(bgmId);
		videos.setUserId(userId);
		videos.setVideoDesc(desc);
		videos.setVideoSeconds((float)videoSeconds);
		videos.setVideoWidth(videoWidth);
		videos.setVideoHeight(videoHeight);
		videos.setVideoPath(uploadPathDB);
		videos.setCoverPath(coverPathDB);
		videos.setStatus(VideoStatusEnum.SUCCESS.value);
		videos.setCreateTime(new Date());
		
		String videoId = videoService.saveVideo(videos);
		
		return WxJSONResult.ok(videoId);
	}
	
	@ApiOperation(value = "用户上封面频", notes = "用户上传封面的接口")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "videoId", value = "视频主键id", required = true, dataType = "String", paramType = "form"),
		@ApiImplicitParam(name = "userId", value = "用户id", required = true, dataType = "String", paramType = "form"),
	})
	@PostMapping(value = "/uploadCover", headers = "content-type=multipart/form-data")
	public WxJSONResult uploadCover(String userId, String videoId,
					@ApiParam(value = "视频封面", required = true) 
					MultipartFile file) throws Exception {
		
		// 文件保存的命名空间
		String fileSpace = FILE_SPACE;
		if (StringUtils.isBlank(videoId) || StringUtils.isBlank(userId)) {
			return WxJSONResult.errorMsg("用户和视频ID不能为空");
		}
		
		//保存到数据库中的相对路径
		String uploadPathDB = "/" + userId + "/video";
		
		FileOutputStream fileOutputStream = null;
		try {
			if(file != null) {
				InputStream inputStream = null;
				
				String fileName = file.getOriginalFilename();
				System.out.println(fileName + " fileName");
				if(StringUtils.isNotBlank(fileName)) {
					//文间上传的绝对路径
					String finalCoverPath = fileSpace + uploadPathDB + "/" + fileName;
					uploadPathDB += ("/" + fileName);
					
					File outFile = new File(finalCoverPath);
					if(outFile.getParentFile() != null || outFile.getParentFile().isDirectory()) {
						//创建父文件夹
						outFile.getParentFile().mkdirs();
					}
					
					fileOutputStream = new FileOutputStream(outFile);
					inputStream = file.getInputStream();
					IOUtils.copy(inputStream, fileOutputStream);
				}
			}else {
				WxJSONResult.errorMsg("上传出错...");
			}
		} catch (Exception e) {
			
			e.printStackTrace();
			return WxJSONResult.errorMsg("上传失败 ....");
			
		} finally {
			if(fileOutputStream != null) {
				fileOutputStream.flush();
				fileOutputStream.close();
			}
		}
		
		videoService.updateVideo(videoId, uploadPathDB);
		
		return WxJSONResult.ok();
		
	}
	
	/**
	 * @Description: 搜索视屏列表接口
	 * isSaveRecord: 1 - 需要保存
	 * 				 0 - 不需要保存
	 */
	@PostMapping(value = "/showAll")
	public WxJSONResult showAll(@RequestBody Videos video, Integer isSaveRecord, Integer page, Integer pageSize) throws Exception {
		
		if(page == null) {
			page = 1;
		}
		
		if(pageSize == null) {
			pageSize = PAGE_SIZE;
		}
		
		PagedResult result = videoService.getAllVideos(video, isSaveRecord, page, pageSize);
		return WxJSONResult.ok(result);
		
	}
	
	@PostMapping(value = "/showMyLike")
	public WxJSONResult showMyLike(String userId, Integer page, Integer pageSize) throws Exception {
		
		if(page == null) {
			page = 1;
		}
		
		if(pageSize == null) {
			pageSize = PAGE_SIZE;
		}
		
		PagedResult videoList = videoService.queryLikeVideos(userId, page, pageSize);
		return WxJSONResult.ok(videoList);
		
	}
	
	@PostMapping(value = "/showMyFollow")
	public WxJSONResult showMyFollow(String userId, Integer page, Integer pageSize) throws Exception {
		
		if(page == null) {
			page = 1;
		}
		
		if(pageSize == null) {
			pageSize = PAGE_SIZE;
		}
		
		PagedResult videoList = videoService.queryMyFollowerVideo(userId, page, pageSize);
		return WxJSONResult.ok(videoList);
		
	}
	
	@PostMapping("/hot")
	public WxJSONResult hot() throws Exception {
		return WxJSONResult.ok(videoService.getHotWords());
	}
	
	@PostMapping("/userLike")
	public WxJSONResult userLike(String userId, String videoId, String videoCreaterId) throws Exception {
		videoService.userLikeVideo(userId, videoId, videoCreaterId);
		return WxJSONResult.ok();
	}
	
	@PostMapping("/userUnlike")
	public WxJSONResult userUnlike(String userId, String videoId, String videoCreaterId) throws Exception {
		videoService.userUnLikeVideo(userId, videoId, videoCreaterId);
		return WxJSONResult.ok();
	}
	
	@PostMapping("/saveComment")
	public WxJSONResult saveComment(@RequestBody Comments comment, String fatherCommentId, String toUserId) throws Exception {
		if(fatherCommentId != null && toUserId != null) {
			comment.setFatherCommentId(fatherCommentId);
			comment.setToUserId(toUserId);
		}
		videoService.saveComment(comment);
		return WxJSONResult.ok();
	}
	
	@PostMapping("/getVideoComments")
	public WxJSONResult getVideoComments(String videoId, Integer page, Integer pageSize) throws Exception {
		
		if(StringUtils.isBlank(videoId)) {
			return WxJSONResult.ok();
		}
		
		if(page == null) {
			page = 1;
		}
		
		if(pageSize == null) {
			pageSize = 10;
		}
		
		PagedResult list = videoService.getAllComments(videoId, page, pageSize);
		
		return WxJSONResult.ok(list);
		
	}
	
}

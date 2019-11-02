package com.wx.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.wx.pojo.Person;
import com.wx.pojo.UsersReport;
import com.wx.pojo.vo.PersonVo;
import com.wx.pojo.vo.PublisherVideo;
import com.wx.service.UserService;
import com.wx.utils.WxJSONResult;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(value = "用户业务相关的接口", tags = {"用户相关业务的接contoller"})
@RequestMapping("/user")
public class UserController extends BasicController {

	@Autowired
	private UserService userService;
	
	@ApiOperation(value = "用户上传头像", notes = "用户上传头像的接口")
	@ApiImplicitParam(name = "userId", value = "用户id", required = true, dataType = "String", paramType = "query")
	@PostMapping("/uploadFace")
	public WxJSONResult uploadFace(String userId,@RequestParam("file") MultipartFile[] files) throws Exception {
		
		// 文件保存的命名空间
		String fileSpace = "/Users/ashsay/Documents/workspace-sts-3.9.10.RELEASE/wx-video-dev/uploads";
		if (StringUtils.isBlank(userId)) {
			return WxJSONResult.errorMsg("用户ID不能为空");
		}
		
		//保存到数据库中的相对路径
		String uploadPathDB = "/" + userId + "/face";
		
		FileOutputStream fileOutputStream = null;
		try {
			if(files != null && files.length > 0) {
				InputStream inputStream = null;
				
				String fileName = files[0].getOriginalFilename();
				if(StringUtils.isNotBlank(fileName)) {
					//文间上传的绝对路径
					String finalFacePath = fileSpace + uploadPathDB + "/" + fileName;
					uploadPathDB += ("/" + fileName);
					
					File outFile = new File(finalFacePath);
					if(outFile.getParentFile() != null || outFile.getParentFile().isDirectory()) {
						//创建父文件夹
						outFile.getParentFile().mkdirs();
					}
					
					fileOutputStream = new FileOutputStream(outFile);
					inputStream = files[0].getInputStream();
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
		
		Person person = new Person();
		person.setId(userId);
		person.setFaceImage(uploadPathDB);
		userService.updateUserInfo(person);
		
		return WxJSONResult.ok(person.getFaceImage());
	}
	
	@ApiOperation(value = "用户查询信息", notes = "用户查询信息接口")
	@ApiImplicitParam(name = "userId", value = "用户id", required = true, dataType = "String", paramType = "query")
	@PostMapping("/query")
	public WxJSONResult query(String userId, String fanId) throws Exception {
		
		if(StringUtils.isBlank(userId)) {
			return WxJSONResult.errorMsg("用户ID不能为空");
		}
		
		Person personInfo = userService.queryUserInfo(userId);
		PersonVo personVo = new PersonVo();
		BeanUtils.copyProperties(personInfo, personVo);
		
		personVo.setFollow(userService.queryIfFollow(userId, fanId));
		
		return WxJSONResult.ok(personVo);
		
	}
	
	@ApiOperation(value = "查询视频发布者的信息")
	@PostMapping("/queryPublisher")
	public WxJSONResult queryPublisher(String loginUserId, String videoId, String publishUserId) throws Exception {
		
		if(StringUtils.isBlank(publishUserId)) {
			return WxJSONResult.errorMsg("");
		}
		
		// 1.查询视频发布者的信息
		Person personInfo = userService.queryUserInfo(publishUserId);
		PersonVo publisher = new PersonVo();
		BeanUtils.copyProperties(personInfo, publisher);
		
		// 2.查询点赞
		boolean userLikeVideo = userService.isUserLikeVideo(loginUserId, videoId);
		
		PublisherVideo bean = new PublisherVideo();
		bean.setPublisher(publisher);
		bean.setUserLikeVideo(userLikeVideo);
		
		return WxJSONResult.ok(bean);
		
	}
	
	@PostMapping("/beyourfans")
	public WxJSONResult beyourfans(String userId, String fanId) throws Exception {
		
		if(StringUtils.isBlank(userId) || StringUtils.isBlank(fanId)) {
			return WxJSONResult.errorMsg("");
		}
		
		userService.saveUserFanRelation(userId, fanId);
		
		return WxJSONResult.ok("关注成功....");
		
	}
	
	
	@PostMapping("/dontyourfans")
	public WxJSONResult dontyourfans(String userId, String fanId) throws Exception {
		
		if(StringUtils.isBlank(userId) || StringUtils.isBlank(fanId)) {
			return WxJSONResult.errorMsg("");
		}
		
		userService.deleteUserFanRelation(userId, fanId);
		
		return WxJSONResult.ok("关注成功....");
		
	}
	
	@PostMapping("/reportUser")
	public WxJSONResult reportUser(@RequestBody UsersReport usersReport) throws Exception {
		
		userService.reportUser(usersReport);
		
		return WxJSONResult.errorMsg("举报成功, 之后会将信息反馈给您");
		
	}
	
}

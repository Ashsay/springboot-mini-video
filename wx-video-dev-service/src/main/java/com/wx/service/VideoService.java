package com.wx.service;

import java.util.List;

import com.wx.pojo.Comments;
import com.wx.pojo.Videos;
import com.wx.utils.PagedResult;

public interface VideoService {
	
	/**
	 * @Description: 保存视频
	 */
	public String saveVideo(Videos video);
	
	/**
	 * @Description: 保存视频封面
	 */
	public void updateVideo(String videoId, String coverPath);
	
	/**
	 * @Description: 分页查询视频列表
	 */
	public PagedResult getAllVideos(Videos video, Integer isSaveRecord, Integer page, Integer pageSize);
	
	/**
	 * @Description: 热搜索词
	 */
	 public List<String> getHotWords();
	 
	 /**
	  * @Description: 用户喜欢视频
	  */
	 public void userLikeVideo(String userId, String videoId, String videoCreaterId);
	 
	 /**
	  * @Description: 用户取消喜欢视频
	  */
	 public void userUnLikeVideo(String userId, String videoId, String videoCreaterId);
	 
	 /**
	  * @Description: 用户收藏视频列表
	  */
	 public PagedResult queryLikeVideos(String userId, Integer page, Integer pageSize);
	 
	 /**
	  * @Description: 用户的粉丝列表
	  */
	 public PagedResult queryMyFollowerVideo(String userId, Integer page, Integer pageSize);
	 
	 /**
	  * @Description: 用户评论
	  */
	 public void saveComment(Comments comment);
	 
	 /**
	  * @Description: 留言分页
	  */
	 public PagedResult getAllComments(String videoId, Integer page, Integer pageSize);
	
}

package com.wx.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.wx.pojo.Videos;
import com.wx.pojo.vo.VideosVo;
import com.wx.utils.MyMapper;

public interface VideosMapperCustom extends MyMapper<Videos> {
	
	/**
	 * @Description: 查询视频列表
	 * @param videoDesc
	 * @param userId
	 * @return
	 */
	public List<VideosVo> queryAllVideos(@org.apache.ibatis.annotations.Param("videoDesc") String videoDesc,
			@Param("userId") String userId);
	
	/**
	 * @Description: 对喜欢的视频数量进行累加
	 * @param videoId
	 */
	public void addVideoLikeCount(String videoId);
	
	/**
	 * @Description: 对喜欢的视频数量进行累计减
	 * @param videoId
	 */
	public void reduceVideoLikeCount(String videoId);
	
	/**
	 * @Description: 查询收藏的视频
	 * @param userId
	 */
	public List<VideosVo> queryMyLikeVideos(String userId);
	
	/**
	 * @Description: 查询粉丝列表
	 * @param userId
	 */
	public List<VideosVo> queryMyFollowerVideo(String userId);
	
}
package com.wx.mapper;

import java.util.List;

import com.wx.pojo.Comments;
import com.wx.pojo.vo.CommentsVO;
import com.wx.utils.MyMapper;

public interface CommentsMapperCustom extends MyMapper<Comments> {
	
	public List<CommentsVO> queryComments(String videoId);
	
}
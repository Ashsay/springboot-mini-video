package com.wx.mapper;

import com.wx.pojo.Person;
import com.wx.utils.MyMapper;

public interface PersonMapper extends MyMapper<Person> {
	
	/**
	 * @Description: 用户喜欢数量累加
	 * @param userId
	 */
	public void addReceiveLikeCount(String userId);
	
	/**
	 * @Description: 用户受喜欢数量累减
	 * @param userId
	 */
	public void reduceReceiveLikeCount(String userId);
	
	/**
	 * @Description: 添加粉丝数量
	 * @param userId
	 */
	public void addFansCount(String userId);
	
	/**
	 * @Description: 添加关注数量
	 * @param userId
	 */
	public void addFollersCount(String userId);
	
	/**
	 * @Description: 减少粉丝数量
	 * @param userId
	 */
	public void reduceFansCount(String userId);
	
	/**
	 * @Description: 减少关注数量
	 * @param userId
	 */
	public void reduceFollersCount(String userId);
	
}
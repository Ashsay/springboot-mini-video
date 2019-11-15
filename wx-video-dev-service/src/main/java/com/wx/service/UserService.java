package com.wx.service;

import com.wx.pojo.Person;
import com.wx.pojo.UsersReport;;

public interface UserService {
	
	/**
	 * @Description: 判断用户是否存在
	 */
	public boolean queryUserNameIsExist( String username );
	
	/**
	 * @description: 保存用户
	 */
	public void saveUser( Person person );
	
	/**
	 * @Description: 更新用户
	 */
	public void updateUserInfo( Person person );
	
	/**
	 * @Description: 查询用户
	 */
	public Person queryUserForLogin(String username, String password);
	
	/**
	 * @Description: 查询用户信息
	 */
	public Person queryUserInfo(String userId);
	
	/**
	 * @Description: 查询用户是否喜欢点赞视频
	 */
	public boolean isUserLikeVideo(String userId, String videoId);
	
	/**
	 * @Description: 增加用户和粉丝的关系
	 * @param userId
	 * @param fanId
	 */
	public void saveUserFanRelation(String userId, String fanId);
	
	/**
	 * @Description: 减少用户和粉丝的关系
	 * @param userId
	 * @param fanId
	 */
	public void deleteUserFanRelation(String userId, String fanId);
	
	/**
	 * @Description: 查询用户是否关注
	 * @param userId
	 * @param fanId
	 * @return
	 */
	public boolean queryIfFollow(String userId, String fanId);
	
	/**
	 * @Description: 用户举报
	 * @param usersReport
	 */
	public void reportUser(UsersReport usersReport);
	
}

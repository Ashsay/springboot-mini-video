package com.wx.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.wx.mapper.PersonMapper;
import com.wx.mapper.UsersFansMapper;
import com.wx.mapper.UsersLikeVideosMapper;
import com.wx.mapper.UsersReportMapper;
import com.wx.pojo.Person;
import com.wx.pojo.UsersFans;
import com.wx.pojo.UsersLikeVideos;
import com.wx.pojo.UsersReport;
import com.wx.service.UserService;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

@Service
public class UserServiceImp implements UserService {
	
	@Autowired
	private PersonMapper personMapper;
	
	@Autowired
	private UsersFansMapper usersFansMapper;
	
	@Autowired
	private UsersLikeVideosMapper usersLikeVideosMapper;
	
	@Autowired
	private UsersReportMapper usersReportMapper;
	
	@Autowired
	private Sid sid;

	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public boolean queryUserNameIsExist(String username) {
		// TODO Auto-generated method stub
		Person person = new Person();
		person.setUsername(username);
		Person result = personMapper.selectOne(person);
		return result == null ? false : true;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public void saveUser(Person person) {
		// TODO Auto-generated method stub
		String userId = sid.nextShort();
		person.setId(userId);
		personMapper.insert(person);
	}
	
	@Override
	public void updateUserInfo(Person person) {
		// TODO Auto-generated method stub
		Example userExample = new Example(Person.class);
		Criteria criteria = userExample.createCriteria();
		criteria.andEqualTo("id",person.getId());
		personMapper.updateByExampleSelective(person, userExample);
	}
	
	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public Person queryUserForLogin( String username, String password ) {
		
		Example userExample = new Example(Person.class);
		Criteria criteria = userExample.createCriteria();
		criteria.andEqualTo("username",username);
		criteria.andEqualTo("password", password);
		Person result = personMapper.selectOneByExample(userExample);
		
		return result;
		
	}
	
	@Transactional(propagation = Propagation.SUPPORTS)
	@Override 
	public Person queryUserInfo(String userId) {		
		Example userExample = new Example(Person.class);
		Criteria criteria = userExample.createCriteria();
		criteria.andEqualTo("id",userId);
		Person person = personMapper.selectOneByExample(userExample);
		return person;
	}
	
	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public boolean isUserLikeVideo(String userId, String videoId) {
		
		if(StringUtils.isBlank(videoId) || StringUtils.isBlank(userId)) {
			return false;
		}
		
		Example example = new Example(UsersLikeVideos.class);
		Criteria criteria = example.createCriteria();		
		
		criteria.andEqualTo("userId", userId);
		criteria.andEqualTo("videoId", videoId);
		
		List<UsersLikeVideos> list = usersLikeVideosMapper.selectByExample(example);
		
		if(list != null && list.size() > 0) {
			return true;
		}
		
		return false;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public void saveUserFanRelation(String userId, String fanId) {
		
		String relId = sid.nextShort();
		
		UsersFans userFan = new UsersFans();
		userFan.setId(relId);
		userFan.setUserId(userId);
		userFan.setFanId(fanId);
		usersFansMapper.insert(userFan);
		
		personMapper.addFansCount(userId);
		personMapper.addFollersCount(fanId);
		
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public void deleteUserFanRelation(String userId, String fanId) {
		
		Example example = new Example(UsersFans.class);
		Criteria criteria = example.createCriteria();
		
		criteria.andEqualTo("userId", userId);
		criteria.andEqualTo("fanId", fanId);
		
		usersFansMapper.deleteByExample(example);
		
		personMapper.reduceFansCount(userId);
		personMapper.reduceFollersCount(fanId);
		
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public boolean queryIfFollow(String userId, String fanId) {
		
		Example example = new Example(UsersFans.class);
		Criteria criteria = example.createCriteria();
		
		criteria.andEqualTo("userId", userId);
		criteria.andEqualTo("fanId", fanId);
		List<UsersFans> list = usersFansMapper.selectByExample(example);
		
		if(list != null && !list.isEmpty() && list.size() > 0) {
			return true;
		}
		
		return false;
		
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public void reportUser(UsersReport usersReport) {
		
		String urId = sid.nextShort();
		usersReport.setId(urId);
		usersReport.setCreateDate(new Date());
		
		usersReportMapper.insert(usersReport);
		
	}

	

}

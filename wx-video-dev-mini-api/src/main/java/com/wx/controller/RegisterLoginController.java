package com.wx.controller;

import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.wx.pojo.Person;
import com.wx.pojo.vo.PersonVo;
import com.wx.service.UserService;
import com.wx.utils.MD5Utils;
import com.wx.utils.WxJSONResult;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(value="用户注册登录接口", tags = {"注册和登入的Controller"})
public class RegisterLoginController extends BasicController {
	
	@Autowired
	private UserService userService;
	
	@ApiOperation(value = "用户注册", notes = "用户登入注册")
	@PostMapping("/register")
	public WxJSONResult register(@RequestBody Person person) throws Exception {
		
		// 1. 判断用户名密码是否为空
		if(person.getUsername() == null || person.getPassword() == null) {
			return WxJSONResult.errorMsg("用户名和密码不能为空");
		}
		
		// 2. 判断用户是否存在
		boolean userNameIsExist = userService.queryUserNameIsExist(person.getUsername());
		
		// 3. 保存用户，注册信息
		if (!userNameIsExist) {
			person.setNickname(person.getUsername());
			person.setPassword(MD5Utils.getMD5Str(person.getPassword()));
			person.setFansCounts(0);
			person.setReceiveLikeCounts(0);
			person.setFollowCounts(0);
			userService.saveUser(person);
		} else {
			return WxJSONResult.errorMap("用户名已经存在，请换一个");
		}
		
		person.setPassword("");
		
//		String uniqueToken = UUID.randomUUID().toString();
//		redis.set(USER_REDIS_SESSION + person.getId(), uniqueToken, 1000 * 60 * 30);
//		
//		PersonVo personVo = new PersonVo();
//		BeanUtils.copyProperties(person, personVo);
//		personVo.setUserToken(uniqueToken);
		
		PersonVo personVo = setUserRedisSessionToken(person);
		
		return WxJSONResult.ok(personVo);
	}
	
	public PersonVo setUserRedisSessionToken(Person personModel) {
		
		String uniqueToken = UUID.randomUUID().toString();
		redis.set(USER_REDIS_SESSION + ":" + personModel.getId(), uniqueToken, 1000 * 60 * 30);
		
		PersonVo personVo = new PersonVo();
		BeanUtils.copyProperties(personModel, personVo);
		personVo.setUserToken(uniqueToken);
		
		return personVo;
		
	}
	
	@ApiOperation(value = "用户登录", notes = "用户登入注册")
	@PostMapping("/login")
	public WxJSONResult login(@RequestBody Person person) throws Exception {
		
		String username = person.getUsername();
		//String password = person.getPassword();
		
		// 1. 判断用户名密码是否为空
		if(person.getUsername() == null || person.getPassword() == null) {
			return WxJSONResult.errorMsg("用户名和密码不能为空");
		}
		// 2. 判断用户是否存在
		Person userResult = userService.queryUserForLogin(username,MD5Utils.getMD5Str(person.getPassword()));
		
		// 3. 返回
		if (userResult != null) {
			userResult.setPassword("");
			PersonVo personVo = setUserRedisSessionToken(userResult);
			return WxJSONResult.ok(personVo);
		} else {
			return WxJSONResult.errorMap("用户名和密码不正确，请重试...");
		}
	}
	
	@ApiOperation(value = "用户注销", notes = "用户注销接口")
	@ApiImplicitParam(name = "userId", value = "用户id", required = true, dataType = "String", paramType = "query")
	@PostMapping("/logout")
	public WxJSONResult logout(String userId) throws Exception {
		redis.del(USER_REDIS_SESSION + ":" + userId);
		return WxJSONResult.errorMap("退出成功");
	}
	
}

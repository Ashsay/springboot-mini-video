package com.wx.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.wx.utils.RedisOperator;

@RestController
public class BasicController {

	@Autowired
	public RedisOperator redis;
	
	public static final String USER_REDIS_SESSION = "user-redis-session";
	
	//文件保存命名空间
	public static final String FFMPEG_EXE = "/usr/local/Cellar/ffmpeg/4.2.1/bin/ffmpeg";
	
	//单页文件大小
	public static final Integer PAGE_SIZE = 5; 
	
	// 文件保存的命名空间
	public static final String FILE_SPACE = "/Users/ashsay/Documents/workspace-sts-3.9.10.RELEASE/wx-video-dev/uploads";
	
}

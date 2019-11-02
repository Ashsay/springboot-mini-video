package com.wx.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wx.service.BgmService;
import com.wx.utils.WxJSONResult;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(value = "背景音乐业务相关的接口", tags = {"背景音乐相关业务的接contoller"})
@RequestMapping("/bgm")
public class BgmController {

	@Autowired
	private BgmService bgmService;
	
	@ApiOperation(value = "获取背景音乐列表", notes = "获取背景音乐列表接口")
	@GetMapping("/list")
	public WxJSONResult list() {
		return WxJSONResult.ok(bgmService.queryBgmList());
	}
	
}

package com.wx.service;

import java.util.List;

import com.wx.pojo.Bgm;

public interface BgmService {
	
	/**
	 * @Description: 查询背景音乐列表
	 */
	public List<Bgm> queryBgmList();
	
	/**
	 * @Description: 用ID查询BGM
	 */
	public Bgm queryBgmById(String bgmId);
	
}

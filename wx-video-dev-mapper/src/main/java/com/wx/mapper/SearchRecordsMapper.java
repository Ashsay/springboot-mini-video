package com.wx.mapper;

import java.util.List;

import com.wx.pojo.SearchRecords;
import com.wx.utils.MyMapper;

public interface SearchRecordsMapper extends MyMapper<SearchRecords> {
	
	public List<String> getHotWords();
	
}
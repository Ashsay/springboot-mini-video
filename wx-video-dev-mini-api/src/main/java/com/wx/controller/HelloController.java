package com.wx.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("th")
public class HelloController {

	@RequestMapping("/hello")
	public Object hello() {
		return "Hello SpringBoot";
	}
	
}

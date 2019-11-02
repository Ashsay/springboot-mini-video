package com.wx.controller.interceptor;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.wx.utils.JsonUtils;
import com.wx.utils.RedisOperator;
import com.wx.utils.WxJSONResult;

public class MiniInterceptor implements HandlerInterceptor {
	
	@Autowired
	public RedisOperator redis;
	
	public static final String USER_REDIS_SESSION = "user-redis-session";

	/**
	 * 	拦截请求，调用controller之前拦截
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// TODO Auto-generated method stub
		
		String userId = request.getHeader("userId");
		String userToken = request.getHeader("userToken");
		
		if(StringUtils.isNotBlank(userId) && StringUtils.isNoneBlank(userToken)) {
			String uniqueToken = redis.get(USER_REDIS_SESSION + ":" + userId);
			if(StringUtils.isEmpty(uniqueToken) && StringUtils.isBlank(uniqueToken)) {
				System.out.println("请登录");
				returnErrorResponse(response, new WxJSONResult().errorTokenMsg("请登录"));
				return false;
			}else {
				if(!uniqueToken.equals(userToken)) {
					System.out.println("账号已经被登录");
					returnErrorResponse(response, new WxJSONResult().errorTokenMsg("账号已经被登录"));
					return false;
				}
			}

		}else {
			returnErrorResponse(response, new WxJSONResult().errorTokenMsg("请登录"));
			System.out.println("请登录");
			return false;
		}
		/**
		 * false : 请求被拦截，返回
		 * true : 请求OK，通过
		 */
		return true;
	}
	
	public void returnErrorResponse(HttpServletResponse response, WxJSONResult result) 
			throws IOException, UnsupportedEncodingException {
		OutputStream out=null;
		try{
		    response.setCharacterEncoding("utf-8");
		    response.setContentType("text/json");
		    out = response.getOutputStream();
		    out.write(JsonUtils.objectToJson(result).getBytes("utf-8"));
		    out.flush();
		} finally{
		    if(out!=null){
		        out.close();
		    }
		}
	}

	/**
	 * 请求controller之后，渲染视图之前
	 */
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
		HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
	}

	/**
	 * 请求controller之后，视图渲染之后
	 */
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub
		HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
	}

	
	
}

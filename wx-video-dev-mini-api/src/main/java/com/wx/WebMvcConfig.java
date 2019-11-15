package com.wx;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.wx.controller.interceptor.MiniInterceptor;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
	
	//内部工作区域调用
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
		        .addResourceLocations("file:/usr/local/tomcat8/")
		        .addResourceLocations("file:/usr/local/tomcat8/uploads/")
                .addResourceLocations("file:/Users/ashsay/Documents/workspace-sts-3.9.10.RELEASE/wx-video-dev/uploads/")
                .addResourceLocations("classpath:/META-INF/resources/");
    }
    
    @Bean(initMethod = "init")
    public ZKCuratorClient zKCuratorClient() {
    	return new ZKCuratorClient();
    }
	
    @Bean
    public MiniInterceptor miniInterceptor() {
    	return new MiniInterceptor();
    }

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		// TODO Auto-generated method stub
		registry.addInterceptor(miniInterceptor()).addPathPatterns("/user/**")
												  .addPathPatterns("/video/userLike","user/userUnlike")
												  .excludePathPatterns("/user/queryPublisher");
		WebMvcConfigurer.super.addInterceptors(registry);
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		// TODO Auto-generated method stub
        registry.addMapping("/**").allowedOrigins("*")
							      .allowedMethods("POST", "GET", "PUT", "OPTIONS", "DELETE")
							      .maxAge(3600)
							      .allowCredentials(true);
		WebMvcConfigurer.super.addCorsMappings(registry);
	}
    
}

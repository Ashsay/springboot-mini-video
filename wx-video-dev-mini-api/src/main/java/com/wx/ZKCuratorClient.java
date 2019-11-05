package com.wx;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ZKCuratorClient {
	
	private CuratorFramework client = null;
	final static Logger log = LoggerFactory.getLogger(ZKCuratorClient.class);
	
	public static final String ZOOKEEPER_SERVER = "106.13.167.110:2181";
	
	public void init() {
		
		if(client != null) {
			return ;
		}
		
		RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 5);
		
		client = CuratorFrameworkFactory.builder().connectString(ZOOKEEPER_SERVER)
				.sessionTimeoutMs(10000).retryPolicy(retryPolicy).namespace("test").build();
		
		client.start();
		
		try {
			String testNode = new String(client.getData().forPath("/test"));
			log.info("测试数据#######{}",testNode);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}
	
}

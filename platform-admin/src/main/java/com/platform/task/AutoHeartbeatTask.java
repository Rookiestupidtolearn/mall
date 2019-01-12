package com.platform.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component("autoHeartbeatTask")
public class AutoHeartbeatTask {
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	public void doJob(){
		logger.info("autoHeartbeatTask心跳正在进行。");
	}
}

package com.platform.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.platform.service.ApiOrderService;
import com.platform.service.SysUserService;

@Component("checkOrderValidTask")
public class AutoCheckOrderValidedTask {
	

    @Autowired
    private ApiOrderService orderService;
    
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	 public Object checkOrderValid() {
		 logger.info("【查询订单有效性】任务开始。。。。");
	        try {
	            Thread.sleep(1000L);
	        } catch (InterruptedException e) {
	            e.printStackTrace();
	        }
		 Object obj = orderService.checkOrderValid();
		 return obj;
	 }
	 
	 
}

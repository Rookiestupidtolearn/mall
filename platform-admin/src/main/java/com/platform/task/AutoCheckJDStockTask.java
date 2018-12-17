package com.platform.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.platform.youle.service.ApiJDGoodsService;

@Component("autoCheckJDStockTask")
public class AutoCheckJDStockTask {
	@Autowired
	private ApiJDGoodsService apiJDGoodsService;
	
	private Logger logger = LoggerFactory.getLogger(getClass());

	 public Object checkJdStore() {
		 logger.info("【定时查询三方商品库存】任务开始。。。。");
	        try {
	            Thread.sleep(1000L);
	        } catch (InterruptedException e) {
	            e.printStackTrace();
	        }
		 Object obj = apiJDGoodsService.checkJdStore();
		 return obj;
	 }
}

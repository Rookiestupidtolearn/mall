package com.platform.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.platform.youle.service.AbsApiCartPriceService;

@Component("autoCheckCartPricesTask")
public class AutoCheckCartPricesTask {
	@Autowired
	private AbsApiCartPriceService absApiCartPriceService;
	private Logger logger = LoggerFactory.getLogger(getClass());

	 public Object quertAllJDCartPrices() {
		 logger.info("【定时查询购物车商品】任务开始。。。。");
	        try {
	            Thread.sleep(1000L);
	        } catch (InterruptedException e) {
	            e.printStackTrace();
	        }
		 Object obj = absApiCartPriceService.quertAllJDCartPrices();
		 return obj;
	 }
}

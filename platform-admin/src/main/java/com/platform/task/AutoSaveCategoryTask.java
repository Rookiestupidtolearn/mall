package com.platform.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.platform.youle.service.ApiJDGoodsService;

@Component("autoSaveCategoryTask")
public class AutoSaveCategoryTask {
	@Autowired
	private ApiJDGoodsService apiJDGoodsService;
	
	private Logger logger = LoggerFactory.getLogger(getClass());

	 public Object saveCategory() {
		 logger.info("【定时保存三方返回分类】任务开始。。。。");
	        try {
	            Thread.sleep(1000L);
	        } catch (InterruptedException e) {
	            e.printStackTrace();
	        }
		 Object obj = apiJDGoodsService.saveCategory();
		 return obj;
	 }
}

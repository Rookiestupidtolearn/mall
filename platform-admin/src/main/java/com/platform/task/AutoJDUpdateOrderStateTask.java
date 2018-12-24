package com.platform.task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.platform.cache.J2CacheUtils;
import com.platform.entity.OrderVo;

/**
 * 定时更新订单的状态
 * @author Administrator
 *
 */
@Component("autoJDUpdateOrderStateTask")
public class AutoJDUpdateOrderStateTask {
	private Logger logger = LoggerFactory.getLogger(getClass());
	public static final String autoJDUpdateOrderStateTask = "autoJDUpdateOrderStateTask";
	
	public void  jdUpdateOrderState(){
		logger.info("【 定时更新订单的状态】autoJDUpdateOrderStateTask ，start...");
		// 记录任务开始跑
		boolean isExcep = false;
		try {
			// 创建同步锁,防止任务阻塞重复执行
			Object  value =  J2CacheUtils.get(autoJDUpdateOrderStateTask);
			if (value != null) {
				isExcep = true;
				logger.error("autoJDUpdateOrderStateTask_error!,redis锁未释放");
			} else {
				J2CacheUtils.put(autoJDUpdateOrderStateTask,1);
			}
            //处理业务下订单
		   
			
			
			logger.info("定时更新三方订单状态任务end********###");
		
		} catch (Exception e) {
			logger.error("autoJDUpdateOrderStateTask_error", e);
		} finally {
			if (!isExcep) {
				J2CacheUtils.remove(autoJDUpdateOrderStateTask);
			}
			
		}
		
        
		
	}

}

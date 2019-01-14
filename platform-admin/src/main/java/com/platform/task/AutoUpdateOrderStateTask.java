package com.platform.task;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.platform.cache.J2CacheUtils;
import com.platform.entity.OrderVo;
import com.platform.service.ApiOrderService;
import com.platform.utils.DateUtils;

/**
 * 定时更新订单的状态
 * @author Administrator
 *
 */
@Component("autoUpdateOrderStateTask")
public class AutoUpdateOrderStateTask {
 
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	public static final String autoUpdateOrderStateTask = "autoUpdateOrderStateTask";
	
    @Autowired
    private ApiOrderService orderService;
	
	public void updateOrderState(){
		logger.info("【 定时更新订单的状态】autoUpdateOrderStateTask ，start...");
		// 记录任务开始跑
		boolean isExcep = false;
		try {
			// 创建同步锁,防止任务阻塞重复执行
			Object  value =  J2CacheUtils.get(autoUpdateOrderStateTask);
			if (value != null) {
				isExcep = true;
				logger.error("autoUpdateOrderStateTask!,redis锁未释放");
			} else {
				J2CacheUtils.put(autoUpdateOrderStateTask,1);
			}
            //处理业务
			//定时修改订单状态：超过发货7天后，用户未点击确认收货，手动修改为已收货
			Map<String, Object> map = new HashMap<>();
			map.put("orderStatus", 201);
			map.put("orderType", 1);
			map.put("payStatus", 1);
			List<OrderVo>  orderList = orderService.queryList(map);
			for (OrderVo order : orderList) {
				//判断距离现在是否超过7天
				 int cha = DateUtils.getBetweenDateByType(new Date(), order.getAdd_time(), "day");
				if (cha >7) {
					logger.info("开始修改订单的状态为收货，订单的id"+order.getId());
					order.setOrder_status(301);  //用户确认收货
					order.setConfirm_time(new Date());
					orderService.update(order);
				
				}
			
			}
			
			logger.info("定时更新订单的状态end********###");
		
		} catch (Exception e) {
			logger.error("autoUpdateOrderStateTask", e);
		} finally {
			if (!isExcep) {
				J2CacheUtils.remove(autoUpdateOrderStateTask);
			}
			
		}
		
        
		
	}

}

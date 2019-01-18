package com.platform.task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.platform.cache.J2CacheUtils;
import com.platform.dao.JdOrderMapper;
import com.platform.entity.JdOrderVo;
import com.platform.youle.entity.ReponseOrderDetailEntity;
import com.platform.youle.service.AbsApiOrderService;

/**
 * 定时更新物流状态
 * @author Administrator
 *
 */
public class AutoUpdateLogisticsStateTask {

	private Logger logger = LoggerFactory.getLogger(getClass());
    
	public static final String autoUpdateLogisticsStateTask = "autoUpdateLogisticsStateTask";

	@Autowired
	private JdOrderMapper jdOrderMapper;
	
	@Autowired
	private AbsApiOrderService orderService;
	
	public void updateLogisticsState(){
		logger.info("更新物流信息开始:"+autoUpdateLogisticsStateTask);
		
		// 记录任务开始跑
		boolean isExcep = false;
		try {
			// 创建同步锁,防止任务阻塞重复执行
			Object  value =  J2CacheUtils.get(autoUpdateLogisticsStateTask);
			if (value != null) {
				isExcep = true;
				logger.error("autoUpdateLogisticsStateTask!,redis锁未释放");
			} else {
				J2CacheUtils.put(autoUpdateLogisticsStateTask,1);
			}
            //处理业务
			Map<String, Object> map = new HashMap<>();
			map.put("jdSatus", "waiting_shipment");
 			 List<JdOrderVo> list = jdOrderMapper.queryList(map);
			if (!CollectionUtils.isEmpty(list)) {
				//查询京东的订单状态
				for (JdOrderVo jdOrder : list) {
					ReponseOrderDetailEntity resultOrderDetail  = orderService.detail(jdOrder.getOrderKey());
					if (resultOrderDetail != null) {
						
					}
				}
			
				
				
				
			}
 			 
			
			logger.info("更新物流信息 end********###");
		
		} catch (Exception e) {
			logger.error("autoUpdateLogisticsStateTask", e);
		} finally {
			if (!isExcep) {
				J2CacheUtils.remove(autoUpdateLogisticsStateTask);
			}
			
		}
		
	}

}

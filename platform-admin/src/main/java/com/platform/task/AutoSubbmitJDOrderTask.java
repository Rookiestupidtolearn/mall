package com.platform.task;

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
import com.platform.service.JdOrderService;

@Component("autoSubbmitJDOrderTask")
public class AutoSubbmitJDOrderTask {
	private Logger logger = LoggerFactory.getLogger(getClass());
	public static final String SUBBMIT_JDORDER_TASK = "SUBBMIT_JDORDER_TASK_KEY";
	@Autowired
	private JdOrderService jdOrderService;
	@Autowired
	private ApiOrderService apiOrderService;
	
	
	public void  subbmitJDOrder(){
		logger.info("【创建三方订单任务开始】SUBBMIT_JDORDER_TASK_KEY ，start...");
		// 记录任务开始跑
	
		boolean isExcep = false;
		try {
			// 创建同步锁,防止任务阻塞重复执行
			Object  value =  J2CacheUtils.get(SUBBMIT_JDORDER_TASK);
			if (value != null) {
				isExcep = true;
				logger.error("SUBBMIT_JDORDER_TASK_KEY_error!,redis锁未释放");
				return;
			} else {
				J2CacheUtils.put(SUBBMIT_JDORDER_TASK,1);
			}
            //处理业务下订单
			Map<String, Object> map = new HashMap<>();
			map.put("orderStatus", 200);
			map.put("order_type", 1);
			map.put("pay_status", 1);
			List<OrderVo> orderList = 	apiOrderService.queryList(map);
			for(OrderVo orderVo : orderList ){
				if (orderVo.getAddress_id() ==null) {
					logger.error("订单收货地址Address_id不能为空");
					continue ;
				}
				jdOrderService.jdOrderCreate(orderVo);
			}
			logger.info("创建三方订单任务end********###");
		
		} catch (Exception e) {
			logger.error("SUBBMIT_JDORDER_TASK_KEY_error", e);
			e.printStackTrace();
		} finally {
			if (!isExcep) {
				J2CacheUtils.remove(SUBBMIT_JDORDER_TASK);
			}
			logger.info("创建三方订单任务end********###");
		}
        
		
	}
}

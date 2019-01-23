package com.platform.task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.platform.cache.J2CacheUtils;
import com.platform.dao.ApiOrderGoodsMapper;
import com.platform.dao.ApiOrderMapper;
import com.platform.dao.JdOrderMapper;
import com.platform.entity.JdOrderVo;
import com.platform.entity.OrderGoodsVo;
import com.platform.entity.OrderVo;
import com.platform.youle.entity.ReponseOrderDetailEntity;
import com.platform.youle.entity.ResponseOrderTrackEntity;
import com.platform.youle.service.AbsApiOrderService;

/**
 * 定时更新物流状态
 * 
 * @author Administrator
 *
 */
@Component("autoUpdateLogisticsStateTask")
public class AutoUpdateLogisticsStateTask {

	private Logger logger = LoggerFactory.getLogger(getClass());

	public static final String autoUpdateLogisticsStateTask = "autoUpdateLogisticsStateTask";

	@Autowired
	private JdOrderMapper jdOrderMapper;

	@Autowired
	private AbsApiOrderService orderService;

	@Autowired
	private ApiOrderGoodsMapper apiOrderGoodsMapper;
	
	@Autowired
	private ApiOrderMapper apiOrderMapper;
	
	public void updateLogisticsState() {
		logger.info("更新物流信息开始:" + autoUpdateLogisticsStateTask);

		// 记录任务开始跑
		boolean isExcep = false;
		try {
			// 创建同步锁,防止任务阻塞重复执行
			Object value = J2CacheUtils.get(autoUpdateLogisticsStateTask);
			if (value != null) {
				isExcep = true;
				logger.error("autoUpdateLogisticsStateTask!,redis锁未释放");
			} else {
				J2CacheUtils.put(autoUpdateLogisticsStateTask, 1);
			}
			// 处理业务
			Map<String, Object> map = new HashMap<>();
			map.put("jdStatus", "waiting_shipment");
			List<JdOrderVo> list = jdOrderMapper.queryList(map);
			if (!CollectionUtils.isEmpty(list)) {
				// 查询京东的订单状态
				for (JdOrderVo jdOrder : list) {
					ReponseOrderDetailEntity resultOrderDetail = orderService.detail(jdOrder.getOrderKey());
					if (resultOrderDetail == null) {
						logger.info("未查询到京东的订单详情，暂不做处理");
						continue;
					}
					if (!resultOrderDetail.getRESPONSE_STATUS().equals("true")) {
						logger.info("查询京东的订单详情状态失败！，暂不做处理");
						continue;
					}
					
					if (resultOrderDetail.getRESULT_DATA() == null) {
						logger.info("查询京东的订单详情状态为空！，暂不做处理，三方单号为：" + jdOrder.getOrderKey());
						continue;
					}
					
					String status = resultOrderDetail.getRESULT_DATA().getStatus();
					if (status.equals("waiting_shipment")) {
						logger.info("查询京东的订单详情状态为等待发货！，暂不做处理");
						continue;
					}
					if (status.equals("canceld")) {
						logger.info("查询京东的订单详情状态为订单作废！，暂不做处理");
						jdOrder.setJdStatus("canceld");
						jdOrder.setOrderStatus(1);
						 jdOrderMapper.update(jdOrder);
						continue;
					}
					if (status.equals("cancled_before_pay")) {
						logger.info("查询京东的订单详情状态为订单关闭！，暂不做处理");
						jdOrder.setJdStatus("cancled_before_pay");
						jdOrder.setOrderStatus(1);
						 jdOrderMapper.update(jdOrder);
						continue;
					}
					// 查询物流信息
					ResponseOrderTrackEntity response = orderService.orderTrack(jdOrder.getThirdOrder());
					if (!response.getRESPONSE_STATUS().equals("true")) {
						logger.info("查询京东的订单详情状态失败:" + response.getERROR_MESSAGE());
						continue;
					}
					// 查询是否有物流单号
					if (response.getRESULT_DATA() == null) {
						logger.info("查询京东的订单详情状态失败:" + response.getERROR_MESSAGE());
						continue;
					}
					String shipment_order = response.getRESULT_DATA().getShipment_order();
					String  shipmentName = response.getRESULT_DATA().getShipment_name();
					if (StringUtils.isNotEmpty(shipment_order)) {
						// 更新jd_order,nideshop_order_goods
						jdOrder.setJdStatus(status);
						jdOrder.setOrderStatus(3);// 已发货
					   this.updateLogisticsNo(jdOrder, shipment_order, shipmentName);
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

	@Transactional
   private void updateLogisticsNo(JdOrderVo jdOrderVo,String logisticsNo,String shipmentName){
	     logger.info("开始更新物流状态京东订单号"+jdOrderVo.getThirdOrder()+"，订单的id"+jdOrderVo.getId());
	
		 //查询子订单
		 Map<String, Object> map  = new HashMap<>();
		 map.put("orderSn", jdOrderVo.getThirdOrder());
		 //查询本地订单
		 List<OrderVo> shopOrder  = apiOrderMapper.queryList(map);
		Integer orderId = null;
		 if (!CollectionUtils.isEmpty(shopOrder)) {
			 orderId = shopOrder.get(0).getId();
		}
		 if (orderId == null) {
			logger.info("未查询到本地订单"+jdOrderVo.getThirdOrder());
			return ;
		}
		 Map<String, Object> mapGoods  = new HashMap<>();
		 mapGoods.put("order_id", orderId);
		 List<OrderGoodsVo> orderGoodsVos =  apiOrderGoodsMapper.queryList(mapGoods);
		 if (!CollectionUtils.isEmpty(orderGoodsVos)) {
			  for (OrderGoodsVo  orderGoods :orderGoodsVos) {
				 if (StringUtils.isEmpty(orderGoods.getLogistics_no())) {
					 orderGoods.setLogistics_no(logisticsNo);
					 orderGoods.setShipment_name(shipmentName);
					 apiOrderGoodsMapper.update(orderGoods);
				}
				  
			}
			 
		}
		 jdOrderMapper.update(jdOrderVo);
   }
	
	
}

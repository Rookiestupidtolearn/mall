package com.platform.task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.platform.cache.J2CacheUtils;
import com.platform.dao.ApiOrderGoodsMapper;
import com.platform.dao.JdOrderMapper;
import com.platform.entity.JdOrderVo;
import com.platform.entity.OrderGoodsVo;
import com.platform.youle.entity.ReponseOrderDetailEntity;
import com.platform.youle.entity.ResponseOrderTrackEntity;
import com.platform.youle.service.AbsApiOrderService;

/**
 * 定时更新物流状态
 * 
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

	@Autowired
	private ApiOrderGoodsMapper apiOrderGoodsMapper;
	
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
			map.put("jdSatus", "waiting_shipment");
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
					if (StringUtils.isEmpty(resultOrderDetail.getStatus())) {
						logger.info("查询京东的订单详情状态为空！，暂不做处理，三方单号为：" + jdOrder.getOrderKey());
						continue;
					}
					String status = resultOrderDetail.getStatus();
					if (status.equals("waiting_shipment")) {
						logger.info("查询京东的订单详情状态为等待发货！，暂不做处理");
						continue;
					}
					if (status.equals("canceld")) {
						logger.info("查询京东的订单详情状态为订单作废！，暂不做处理");
						continue;
					}
					if (status.equals("cancled_before_pay")) {
						logger.info("查询京东的订单详情状态为订单关闭！，暂不做处理");
						continue;
					}
					// 查询物流信息
					ResponseOrderTrackEntity response = orderService.orderTrack(jdOrder.getThirdOrder());
					if (!response.getRESPONSE_STATUS().equals("true")) {
						logger.info("查询京东的订单详情状态失败:" + response.getERROR_MESSAGE());
						continue;
					}
					// 查询是否有物流单号
					String shipment_order = response.getShipment_order();
					if (StringUtils.isNotEmpty(shipment_order)) {
						// 更新jd_order,nideshop_order_goods
						jdOrder.setJdStatus(status);
						jdOrder.setOrderStatus(3);// 已发货
					   this.updateLogisticsNo(jdOrder, response.getShipment_order(), response.getShipment_name());
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
		 jdOrderMapper.update(jdOrderVo);
		 //查询子订单
		 List<OrderGoodsVo> orderGoodsVos =  apiOrderGoodsMapper.queryOrderGoods(jdOrderVo.getId());
		 if (!CollectionUtils.isEmpty(orderGoodsVos)) {
			  for (OrderGoodsVo  orderGoods :orderGoodsVos) {
				 if (StringUtils.isEmpty(orderGoods.getLogistics_no())) {
					 orderGoods.setLogistics_no(logisticsNo);
					 orderGoods.setShipment_name(shipmentName);
					 apiOrderGoodsMapper.update(orderGoods);
				}
				  
			}
			 
		}
		 
   }
	
	
}

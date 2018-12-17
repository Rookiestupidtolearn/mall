package com.platform.dao;

import java.util.List;

import com.platform.entity.OrderGoodsVo;

/**
 * 
 * 
 * @author lipengjun
 * @email 939961241@qq.com
 * @date 2017-08-11 09:16:46
 */
public interface ApiOrderGoodsMapper extends BaseDao<OrderGoodsVo> {
	
	/**
	 * 根据orderId查询子订单个数
	 * @param userId
	 * @return
	 */
	List<OrderGoodsVo> queryOrderGoods(Integer orderId);
	/**
	 * 根据orderId查询子订单个数
	 * @param orderId
	 * @return
	 */
	List<OrderGoodsVo> queryOrderDeliveredGoods(Integer orderId);
}

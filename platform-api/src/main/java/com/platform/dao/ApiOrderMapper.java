package com.platform.dao;

import com.platform.entity.OrderVo;

/**
 * 
 * 
 * @author lipengjun
 * @email 939961241@qq.com
 * @date 2017-08-11 09:16:46
 */
public interface ApiOrderMapper extends BaseDao<OrderVo> {
	/**
	 * 根据userid查询订单
	 * @param userId
	 * @return
	 */
	OrderVo queryOrderInfo(Long userId);
}

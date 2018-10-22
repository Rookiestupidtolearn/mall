package com.platform.dao;

import java.util.List;
import java.util.Map;

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
	 * @return
	 * @param userId
	 */
	OrderVo queryOrderInfo(Long userId);
	/**
	 * 根据请求参数查询订单列表
	 * @param params
	 * @return
	 */
	List<OrderVo> queryList(Map<String,Object> params);
}

package com.platform.youle.service;

import java.util.Calendar;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.platform.youle.entity.*;
import com.platform.youle.util.TokenUtil;

/**
 * 抽象类
 */
public abstract class AbsApiGoodsService implements IApiFuncServicein {

	/**
	 *
	 * 初始请求参数
	 * 
	 * @param entity
	 */
	@Override
	public void initRequestParam(RequestBaseEntity entity) {
		entity.setWid(TokenUtil.wid);
		entity.setTimestamp(String.valueOf(Calendar.getInstance().getTimeInMillis()));
		entity.setToken(TokenUtil.token);
	}

	/**
	 * 实体转map
	 * 
	 * @param entity
	 * @return
	 * @throws Exception
	 */
	@Override
	public Map<String, Object> objectToMap(RequestBaseEntity entity) throws Exception {
		String str = JSON.toJSONString(entity);
		Map<String, Object> map = (Map<String, Object>) JSON.parse(str);
		return map;
	}

	/**
	 * 获取时间戳
	 * 
	 * @return
	 */
	@Override
	public String getTimestamp() {
		return String.valueOf(Calendar.getInstance().getTimeInMillis());
	}

	/**
	 * 1.1获取所有商品ID
	 * 
	 * @return
	 */
	protected abstract ResponseBaseEntity<?> getAllProductIds();

	/**
	 * 1.2分页获取当前页商品ID, 每页数据100条
	 * 
	 * @param page
	 * @return
	 */
	protected abstract ResponseProductEntity getProductIdsByPage(Integer page);

	/**
	 * 1.3获取单个商品详情
	 * 
	 * @param productId
	 * @return
	 */
	protected abstract ResponseSkuDetailEntity getSkuDetail(Long productId);

	/**
	 * 1.4单个查询商品库存
	 * 
	 * @param pid
	 * @param num
	 * @param address
	 * @return
	 */
	protected abstract ResponseProductEntity stock(String pid, Integer num, String address);

	/**
	 * 1.5批量查询商品库存
	 * 
	 * @param pid_nums
	 * @param address
	 * @return
	 */
	protected abstract ResponseProductStockBatchEntity stockBatch(String pid_nums, String address);

	/**
	 * 1.6查询商品可售状态
	 * 
	 * @param 商品ID
	 * @return
	 */
	protected abstract ResponseSaleStatusEntity getsaleStatus(Integer pid);

	/**
	 * 1.7查询商品协议价
	 * 
	 * @param pid
	 * @return
	 */
	protected abstract ResponseGetPriceEntity getPrice(Integer pid);

	/**
	 * 1.8批量查询商品可售状态
	 * 
	 * @param pids
	 * @return
	 */

	protected abstract ResponseBaseEntity<?> batchSaleStatus(String pids);

	/**
	 * 1.9批量查询商品协议价
	 * 
	 * @param pids
	 * @return
	 */
	protected abstract ResponseBaseEntity<?> batchGetPrice(String pids);

}

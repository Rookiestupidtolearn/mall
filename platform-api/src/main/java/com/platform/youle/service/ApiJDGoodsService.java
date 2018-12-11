package com.platform.youle.service;

import com.alibaba.fastjson.JSONObject;

public interface ApiJDGoodsService {
	/**
	 * 三方返回商品入库
	 * @return
	 */
	public JSONObject saveGoods();
	/**
	 * 三方返回分类入库
	 * @return
	 */
	public JSONObject saveCategory();
//	/**
//	 * 查询三方库存
//	 * @param productId
//	 * @param num
//	 * @return
//	 */
//	public JSONObject checkJdGoodNum(Long productId,Integer num);
	/**
	 * 定时保存三方产品id
	 * @return
	 */
	public JSONObject saveJdProductIds();
	/**
	 * 定时查询三方库存
	 * @return
	 */
	public JSONObject checkJdStore();
}

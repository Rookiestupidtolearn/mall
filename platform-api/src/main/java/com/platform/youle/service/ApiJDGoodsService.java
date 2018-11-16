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
}

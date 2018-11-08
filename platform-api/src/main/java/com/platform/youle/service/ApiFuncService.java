package com.platform.youle.service;

import com.platform.youle.entity.RequestSkuDetailEntity;
import com.platform.youle.entity.ResponseSkuDetailEntity;

public interface ApiFuncService {
	
	
	/**
	 * 获取单个商品详情
	 * @param entity
	 * @return
	 */
	public ResponseSkuDetailEntity getSkuDetail(RequestSkuDetailEntity entity);
		
}

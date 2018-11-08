package com.platform.youle.service;

import com.platform.youle.entity.RequestBaseEntity;
import com.platform.youle.entity.ResponseBaseEntity;

public interface ApiFuncService {
	
	
	/**
	 * 获取单个商品详情
	 * @param entity
	 * @param pid
	 * @return
	 */
	public ResponseBaseEntity getSkuDetail(RequestBaseEntity entity,Long pid);
		
}

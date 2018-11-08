package com.platform.youle.service;

import java.util.Map;

import com.platform.youle.entity.RequestSkuDetailEntity;
import com.platform.youle.entity.ResponseSkuDetailEntity;

import com.platform.youle.entity.ResponseBaseEntity;
import com.platform.youle.entity.ResponseProductEntity;

public interface ApiFuncService {
   
	/**
	 * 1.1获取所有商品ID
	 * @return
	 */
	public ResponseBaseEntity getAllProductIds();
    
	/**
	 * 1.2分页获取当前页商品ID, 每页数据100条
	 * @param page
	 * @return
	 */
	public 	ResponseProductEntity getProductIdsByPage(Integer page);
	
	public ResponseProductEntity stock(
			String pid,
			String num,
			String address
			);






	
	
	/**
	 * 获取单个商品详情
	 * @param entity
	 * @return
	 */
	public ResponseSkuDetailEntity getSkuDetail(Map<String,Object> params);
		
}

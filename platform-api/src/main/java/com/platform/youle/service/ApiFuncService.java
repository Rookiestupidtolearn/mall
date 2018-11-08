package com.platform.youle.service;

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
}

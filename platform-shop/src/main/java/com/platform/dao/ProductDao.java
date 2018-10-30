package com.platform.dao;

import java.util.Map;

import com.platform.entity.ProductEntity;

/**
 * Dao
 *
 * @author lipengjun
 * @email 939961241@qq.com
 * @date 2017-08-30 14:31:21
 */
public interface ProductDao extends BaseDao<ProductEntity> {
	
	//校验商品规格是否存在
	int findProductIsHave(Map paramMap);

}

package com.platform.dao;

import java.util.List;

import com.platform.entity.CartEntity;

/**
 * 
 * 
 * @author lipengjun
 * @email 939961241@qq.com
 * @date 2017-08-13 10:41:06
 */
public interface CartDao extends BaseDao<CartEntity> {

	/**
	 * 根据商品id查询购物车中对应信息
	 * @param goodsId
	 * @return
	 */
	List<CartEntity> queryCartListByGoodsId(Integer goodsId);
	
}

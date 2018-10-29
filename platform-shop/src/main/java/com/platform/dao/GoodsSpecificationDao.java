package com.platform.dao;

import java.util.List;

import com.platform.entity.GoodsSpecificationEntity;

/**
 * 商品对应规格表值表Dao
 *
 * @author lipengjun
 * @email 939961241@qq.com
 * @date 2017-08-31 11:15:55
 */
public interface GoodsSpecificationDao extends BaseDao<GoodsSpecificationEntity> {

	public List<GoodsSpecificationEntity> querySpecificationByGoodId(Integer goodId);

	public List<GoodsSpecificationEntity> findgoodsSpecification(String[] goodsSpecificationIdsArray);
}

package com.platform.dao;

import java.util.List;

import com.platform.entity.GoodsCouponConfigEntity;

/**
 * 产品-平台币配置表
Dao
 *
 * @author lipengjun
 * @email 939961241@qq.com
 * @date 2018-10-12 11:42:14
 */
public interface GoodsCouponConfigDao extends BaseDao<GoodsCouponConfigEntity> {

	List<GoodsCouponConfigEntity> selectGoodsIdsById(Integer[] ids);

}

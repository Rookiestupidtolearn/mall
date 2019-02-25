package com.platform.dao;

import com.platform.entity.IntegralEntity;

/**
 * 用户积分表Dao
 *
 * @author lipengjun
 * @email 939961241@qq.com
 * @date 2019-02-19 11:37:52
 */
public interface ApiIntegralDao extends BaseDao<IntegralEntity> {

    IntegralEntity queryObjectByUserId(Integer userId);

}

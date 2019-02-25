package com.platform.dao;

import com.platform.entity.VplusRecordInfoEntity;

/**
 * Vplus开通续费记录表Dao
 *
 * @author lipengjun
 * @email 939961241@qq.com
 * @date 2019-02-21 13:58:03
 */
public interface ApiVplusRecordInfoDao extends BaseDao<VplusRecordInfoEntity> {


    VplusRecordInfoEntity queryVplusRecordInfoByYeepayTradeNo(String yeePayTradeNO);

}

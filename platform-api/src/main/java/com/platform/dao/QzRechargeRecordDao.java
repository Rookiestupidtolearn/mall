package com.platform.dao;

import com.platform.entity.QzRechargeRecordEntity;

/**
 * 用户充值记录Dao
 *
 * @author lipengjun
 * @email 939961241@qq.com
 * @date 2018-12-17 13:35:56
 */
public interface QzRechargeRecordDao extends BaseDao<QzRechargeRecordEntity> {

	QzRechargeRecordEntity queryByThirdTradeNo(String thirdTradeNo);
}

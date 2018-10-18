package com.platform.dao;

import java.util.List;
import java.util.Map;

import com.platform.entity.QzRechargeRecordEntity;

/**
 * 用户充值记录Dao
 *
 * @author lipengjun
 * @email 939961241@qq.com
 * @date 2018-10-13 18:37:01
 */
public interface QzRechargeRecordDao extends BaseDao<QzRechargeRecordEntity> {
	
	List<QzRechargeRecordEntity> queryAuditList(Map<String, Object> map);

	int queryAuditTotal(Map<String, Object> map);
	
	 int save(QzRechargeRecordEntity t);
}

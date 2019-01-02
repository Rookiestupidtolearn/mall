package com.platform.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.platform.entity.QzMoneyRecordEntity;

/**
 * 用户资金流水Dao
 *
 * @author lipengjun
 * @email 939961241@qq.com
 * @date 2018-10-13 18:37:01
 */
public interface QzMoneyRecordMapper extends BaseDao<QzMoneyRecordEntity> {
	/**
	 * 查询流水列表
	 * @param map
	 * @return
	 */
	List<QzMoneyRecordEntity> queryMoneyRecords(Map<String,Object> map);
	
	QzMoneyRecordEntity queryLastMoneyRecord(@Param("shopUserId") Integer shopUserId);
}

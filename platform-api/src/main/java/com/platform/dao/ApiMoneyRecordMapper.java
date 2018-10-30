
package com.platform.dao;

import com.platform.entity.QzMoneyRecordVo;

public interface ApiMoneyRecordMapper extends BaseDao<QzMoneyRecordVo>{
	//保存资金流水
	int save(QzMoneyRecordVo qzMoneyRecordVo);
}

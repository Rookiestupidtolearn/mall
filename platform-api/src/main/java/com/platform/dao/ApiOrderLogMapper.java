package com.platform.dao;

import com.platform.entity.OrderLogVo;

public interface ApiOrderLogMapper  extends BaseDao<OrderLogVo>{
	int save(OrderLogVo orderLogVo);
}

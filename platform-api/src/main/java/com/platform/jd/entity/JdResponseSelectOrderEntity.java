package com.platform.jd.entity;

import java.util.List;

/**
 * 查询订单信息响应实体
 *
 */
public class JdResponseSelectOrderEntity extends JdResponseBaseEntity {
	
	private List<JdResponseSelectOrderInfoEntity> info; //账户信息

	public List<JdResponseSelectOrderInfoEntity> getInfo() {
		return info;
	}

	public void setInfo(List<JdResponseSelectOrderInfoEntity> info) {
		this.info = info;
	}
	
	
}

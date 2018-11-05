package com.platform.jd.entity;

import java.util.List;

/**
 *查询用户消费记录响应实体
 *
 */
public class JdResponseSelectSpendEntity extends JdResponseBaseEntity {
	
	private List<JdResponseSelectSpendInfoEntity> info; //账户信息	

	public List<JdResponseSelectSpendInfoEntity> getInfo() {
		return info;
	}

	public void setInfo(List<JdResponseSelectSpendInfoEntity> info) {
		this.info = info;
	}
	
	
}

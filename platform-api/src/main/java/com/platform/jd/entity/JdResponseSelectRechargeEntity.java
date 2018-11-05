package com.platform.jd.entity;

import java.util.List;

/**
 * 查询用户充值响应实体
 *
 */
public class JdResponseSelectRechargeEntity extends JdResponseBaseEntity {
	private List<JdResponseSelectRechargeInfoEntity> info; //账户信息

	public List<JdResponseSelectRechargeInfoEntity> getInfo() {
		return info;
	}

	public void setInfo(List<JdResponseSelectRechargeInfoEntity> info) {
		this.info = info;
	}
}

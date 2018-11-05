package com.platform.jd.entity;

import java.util.List;

/**
 * 预存款下单响应实体
 *
 */
public class JdResponseOrderSubmitEntity extends JdResponseBaseEntity {
	
	private String jd_msg; //	京东错误提示
	private List<JdResponseOrderSubmitInfoEntity> info; //订单信息
	public String getJd_msg() {
		return jd_msg;
	}
	public void setJd_msg(String jd_msg) {
		this.jd_msg = jd_msg;
	}
	public List<JdResponseOrderSubmitInfoEntity> getInfo() {
		return info;
	}
	public void setInfo(List<JdResponseOrderSubmitInfoEntity> info) {
		this.info = info;
	}
	
	
}

package com.platform.jd.entity;

/**
 * 确认订单响应实体
 *
 */
public class JdResponseConfirmOrderEntity extends JdResponseBaseEntity {
	
	private String jd_msg; //	京东错误提示
	
	public String getJd_msg() {
		return jd_msg;
	}
	public void setJd_msg(String jd_msg) {
		this.jd_msg = jd_msg;
	}
}	

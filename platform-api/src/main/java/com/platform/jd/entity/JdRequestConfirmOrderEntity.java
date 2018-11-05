package com.platform.jd.entity;

/**
 * 确认订单请求实体
 *
 */
public class JdRequestConfirmOrderEntity extends JdRequestBaseEntity {
	
	private String ordersn; //	必须	礼管家平台订单号

	public String getOrdersn() {
		return ordersn;
	}

	public void setOrdersn(String ordersn) {
		this.ordersn = ordersn;
	}
	
}	

package com.platform.jd.entity;

/**
 *获取 礼管家平台订单号响应实体
 *
 */
public class JdResponseGetOrderSnEntity extends JdResponseBaseEntity {
	
	private String thirdsn;//	第三方订单号
	private String ordersn; //	礼管家平台订单号
	public String getThirdsn() {
		return thirdsn;
	}
	public void setThirdsn(String thirdsn) {
		this.thirdsn = thirdsn;
	}
	public String getOrdersn() {
		return ordersn;
	}
	public void setOrdersn(String ordersn) {
		this.ordersn = ordersn;
	}
	
}

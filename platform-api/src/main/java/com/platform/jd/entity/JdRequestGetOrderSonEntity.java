package com.platform.jd.entity;

/**
 *查询京东子订单订单编号请求实体
 *
 */
public class JdRequestGetOrderSonEntity extends JdRequestBaseEntity {
	
	private String  ordersn;//	必须	礼管家平台订单号

	public String getOrdersn() {
		return ordersn;
	}

	public void setOrdersn(String ordersn) {
		this.ordersn = ordersn;
	}
	
}
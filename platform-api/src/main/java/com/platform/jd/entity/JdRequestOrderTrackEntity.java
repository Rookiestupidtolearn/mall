package com.platform.jd.entity;

/**
 * 查询配送信息请求实体
 *
 */
public class JdRequestOrderTrackEntity extends JdRequestBaseEntity {
	private String ordersn; //	必须	礼管家平台订单号

	public String getOrdersn() {
		return ordersn;
	}

	public void setOrdersn(String ordersn) {
		this.ordersn = ordersn;
	}
	
}
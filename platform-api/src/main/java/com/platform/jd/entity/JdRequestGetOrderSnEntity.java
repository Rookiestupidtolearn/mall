package com.platform.jd.entity;

/**
 * 获取 礼管家平台订单号请求实体
 *
 */
public class JdRequestGetOrderSnEntity extends JdRequestBaseEntity {
	private String thirdsn;//	必须	第三方订单号

	public String getThirdsn() {
		return thirdsn;
	}

	public void setThirdsn(String thirdsn) {
		this.thirdsn = thirdsn;
	}
	
}

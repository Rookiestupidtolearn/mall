package com.platform.youle.entity;

public class RequestCancelByOrderKeyEntity extends RequestBaseEntity {
	
	private String thirdOrder; //	必须	第三方订单号
	private String orderKey; //	必须	云中鹤订单号
	
	public String getThirdOrder() {
		return thirdOrder;
	}
	public void setThirdOrder(String thirdOrder) {
		this.thirdOrder = thirdOrder;
	}
	public String getOrderKey() {
		return orderKey;
	}
	public void setOrderKey(String orderKey) {
		this.orderKey = orderKey;
	}
}

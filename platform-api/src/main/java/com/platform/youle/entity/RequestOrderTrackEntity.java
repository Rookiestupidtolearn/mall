package com.platform.youle.entity;

public class RequestOrderTrackEntity extends RequestBaseEntity{
	//第三方订单号
	private String thirdOrder;
    private String orderKey;
	
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

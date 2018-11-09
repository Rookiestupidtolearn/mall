package com.platform.youle.entity;

/**
 * 6.5 获取京东商品订单售后单概要信息
 * @author Administrator
 *
 */
public class RequestSaleBasisEntity extends RequestBaseEntity {

	private String yzhOrderKey;

	/**
	 * @return the yzhOrderKey
	 */
	public String getYzhOrderKey() {
		return yzhOrderKey;
	}

	/**
	 * @param yzhOrderKey the yzhOrderKey to set
	 */
	public void setYzhOrderKey(String yzhOrderKey) {
		this.yzhOrderKey = yzhOrderKey;
	}
	
	
}

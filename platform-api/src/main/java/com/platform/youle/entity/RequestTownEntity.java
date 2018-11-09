package com.platform.youle.entity;

/**
 * 获取四级地址(镇/街道)
 *
 */
public class RequestTownEntity extends RequestBaseEntity {
	
	private int county; //	必须	县/区Code

	public int getCounty() {
		return county;
	}

	public void setCounty(int county) {
		this.county = county;
	}
	
}

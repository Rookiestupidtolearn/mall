package com.platform.youle.entity;

/**
 * 获取二地址(城市)
 *
 */
public class RequestCityEntity extends RequestBaseEntity {
	private int province ;	//必须	省份Code

	public int getProvince() {
		return province;
	}

	public void setProvince(int province) {
		this.province = province;
	}
	
	
}

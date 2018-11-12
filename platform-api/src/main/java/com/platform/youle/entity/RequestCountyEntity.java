package com.platform.youle.entity;

/**
 * 
 * 获取三级地址(县/区)
 */
public class RequestCountyEntity extends RequestBaseEntity {
	
	private int city; //	必须	城市Code

	public int getCity() {
		return city;
	}

	public void setCity(int city) {
		this.city = city;
	}
	
	
}

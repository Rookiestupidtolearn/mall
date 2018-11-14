package com.platform.youle.entity;

public class ResponseRegionEntity{
		
	private int code; //地址ID
	private String name	; //名称
	private String type	;//	类型(province: 省, city(市), county(区/县),  town(乡/镇))
	
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	

}

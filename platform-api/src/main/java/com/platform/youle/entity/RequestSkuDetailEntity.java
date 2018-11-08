package com.platform.youle.entity;

public class RequestSkuDetailEntity extends RequestBaseEntity{
	//商品id
	private Long pid;
	//手机号
	private String mobile;
	public Long getPid() {
		return pid;
	}
	public void setPid(Long pid) {
		this.pid = pid;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	
	
}

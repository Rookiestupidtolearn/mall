package com.platform.youle.entity;

public class RequestProductStockBatchEntity extends RequestBaseEntity{
	private String pid_nums;
	private String address;
	public String getPid_nums() {
		return pid_nums;
	}
	public void setPid_nums(String pid_nums) {
		this.pid_nums = pid_nums;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}

	
}

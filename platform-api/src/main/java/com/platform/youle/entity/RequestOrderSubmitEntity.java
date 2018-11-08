package com.platform.youle.entity;

public class RequestOrderSubmitEntity extends RequestBaseEntity {
	private String  thirdOrder;
	private String pid_nums;
	private String  receiverName;
	private Integer province;
	private Integer city;
	private Integer county;
	private Integer town;
	private String address;
	private String mobile;
	private String email;
	private String remark;
	public String getThirdOrder() {
		return thirdOrder;
	}
	public void setThirdOrder(String thirdOrder) {
		this.thirdOrder = thirdOrder;
	}
	public String getPid_nums() {
		return pid_nums;
	}
	public void setPid_nums(String pid_nums) {
		this.pid_nums = pid_nums;
	}
	public String getReceiverName() {
		return receiverName;
	}
	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}
	public Integer getProvince() {
		return province;
	}
	public void setProvince(Integer province) {
		this.province = province;
	}
	public Integer getCity() {
		return city;
	}
	public void setCity(Integer city) {
		this.city = city;
	}
	public Integer getCounty() {
		return county;
	}
	public void setCounty(Integer county) {
		this.county = county;
	}
	public Integer getTown() {
		return town;
	}
	public void setTown(Integer town) {
		this.town = town;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}

	
	
}

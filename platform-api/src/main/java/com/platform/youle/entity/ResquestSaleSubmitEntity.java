package com.platform.youle.entity;

/**
 * 6.2 提交订单售后申请
 * @author Administrator
 *
 */
public class ResquestSaleSubmitEntity  extends RequestBaseEntity{
	private String thirdOrder;
	private Integer productId;
	private Integer amount; //数量
	private String type;
	private String reason;
	private String userRemark;
	public String getThirdOrder() {
		return thirdOrder;
	}
	public void setThirdOrder(String thirdOrder) {
		this.thirdOrder = thirdOrder;
	}
	public Integer getProductId() {
		return productId;
	}
	public void setProductId(Integer productId) {
		this.productId = productId;
	}
	public Integer getAmount() {
		return amount;
	}
	public void setAmount(Integer amount) {
		this.amount = amount;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getUserRemark() {
		return userRemark;
	}
	public void setUserRemark(String userRemark) {
		this.userRemark = userRemark;
	}
	
	

	
}

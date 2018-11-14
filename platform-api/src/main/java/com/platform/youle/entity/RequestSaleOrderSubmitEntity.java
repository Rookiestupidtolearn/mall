package com.platform.youle.entity;

/**
 * 6.4 提交京东商品订单售后服务申请
 * @author Administrator
 *
 */
public class RequestSaleOrderSubmitEntity extends RequestBaseEntity {

   private String	yzhOrderKey;
   private Integer productId;
 	private Integer amount;
 	private String type;
 	private String reason;
 	private String userRemark;
 	private String pickwareType;
 	private String province;
 	private String city;
 	private String county;
 	private String town;
 	private String	address;
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
	/**
	 * @return the productId
	 */
	public Integer getProductId() {
		return productId;
	}
	/**
	 * @param productId the productId to set
	 */
	public void setProductId(Integer productId) {
		this.productId = productId;
	}
	/**
	 * @return the amount
	 */
	public Integer getAmount() {
		return amount;
	}
	/**
	 * @param amount the amount to set
	 */
	public void setAmount(Integer amount) {
		this.amount = amount;
	}
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * @return the reason
	 */
	public String getReason() {
		return reason;
	}
	/**
	 * @param reason the reason to set
	 */
	public void setReason(String reason) {
		this.reason = reason;
	}
	/**
	 * @return the userRemark
	 */
	public String getUserRemark() {
		return userRemark;
	}
	/**
	 * @param userRemark the userRemark to set
	 */
	public void setUserRemark(String userRemark) {
		this.userRemark = userRemark;
	}
	/**
	 * @return the pickwareType
	 */
	public String getPickwareType() {
		return pickwareType;
	}
	/**
	 * @param pickwareType the pickwareType to set
	 */
	public void setPickwareType(String pickwareType) {
		this.pickwareType = pickwareType;
	}
	/**
	 * @return the province
	 */
	public String getProvince() {
		return province;
	}
	/**
	 * @param province the province to set
	 */
	public void setProvince(String province) {
		this.province = province;
	}
	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}
	/**
	 * @param city the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}
	/**
	 * @return the county
	 */
	public String getCounty() {
		return county;
	}
	/**
	 * @param county the county to set
	 */
	public void setCounty(String county) {
		this.county = county;
	}
	/**
	 * @return the town
	 */
	public String getTown() {
		return town;
	}
	/**
	 * @param town the town to set
	 */
	public void setTown(String town) {
		this.town = town;
	}
	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}
	/**
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}

 	
}

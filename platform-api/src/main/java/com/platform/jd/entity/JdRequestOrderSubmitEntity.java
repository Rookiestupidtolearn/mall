package com.platform.jd.entity;

import java.math.BigDecimal;
import java.util.List;

/**
 * 预存款下单请求实体
 *
 */
public class JdRequestOrderSubmitEntity extends JdRequestBaseEntity {
	private String thirdsn;	//	第三方订单号
	private String ordersn; //	必须	礼管家平台订单号
	private List<JdRequestGetNewStockByIdSkuNumsEntity> sku; //	必须	[{"skuId":商品编号, "num":商品数量}](最高支持50种商品)
	private BigDecimal order_amount; //	订单金额 （协议价总额）
	private String name; //	收货人姓名
	private String mobile; //	收货人联系电话
	private String province;//	一级地址
	private String city; //	二级地址
	private String county; //	三级地址
	private String town;// 非必须	四级地址(如果该地区有四级地址，则必须传递四级地址)
	private String address; //	详细地址
	public String getThirdsn() {
		return thirdsn;
	}
	public void setThirdsn(String thirdsn) {
		this.thirdsn = thirdsn;
	}
	public String getOrdersn() {
		return ordersn;
	}
	public void setOrdersn(String ordersn) {
		this.ordersn = ordersn;
	}
	public List<JdRequestGetNewStockByIdSkuNumsEntity> getSku() {
		return sku;
	}
	public void setSku(List<JdRequestGetNewStockByIdSkuNumsEntity> sku) {
		this.sku = sku;
	}
	public BigDecimal getOrder_amount() {
		return order_amount;
	}
	public void setOrder_amount(BigDecimal order_amount) {
		this.order_amount = order_amount;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String aprovince) {
		this.province = aprovince;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getCounty() {
		return county;
	}
	public void setCounty(String county) {
		this.county = county;
	}
	public String getTown() {
		return town;
	}
	public void setTown(String town) {
		this.town = town;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	
	
	
}

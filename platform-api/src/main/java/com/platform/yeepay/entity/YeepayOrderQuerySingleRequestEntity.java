package com.platform.yeepay.entity;

import java.io.Serializable;

/**
 * 4.2 订单查询接口
 * @author Administrator
 *
 */
public class YeepayOrderQuerySingleRequestEntity implements Serializable {
	private String merchantaccount ;//商户编号string √ 商户编号
	private String orderid; //商户订单号string √ 请输入需要查询的订单号
	private String  sign ; //签名信息string √
	public String getMerchantaccount() {
		return merchantaccount;
	}
	public void setMerchantaccount(String merchantaccount) {
		this.merchantaccount = merchantaccount;
	}
	public String getOrderid() {
		return orderid;
	}
	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}

	 
}

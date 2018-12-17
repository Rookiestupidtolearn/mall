package com.platform.yeepay.entity;

import java.io.Serializable;

/**
 * 4.1订单支付回调实体
 * @author Administrator
 *
 */
public class YeepayOrderPayCallbackEntity implements Serializable {
	private String  merchantaccount; // 商户编号string 商户编号
	private String yborderid ; //  易宝交易流水号long 易宝流水号，易宝中唯一
	private String orderid; //   商户订单号string 原值返回
	private String bankcode; //   银行编码string 支付卡所属银行的编码，如ICBC
	private String bank; //    银行名称string 支付卡所属银行的名称
	private String lastno; //  卡号后4 位string 支付卡卡号后4 位
	private String cardtype; //  卡类型int 支付卡的类型，1 为借记卡，2 为信用卡
	private String amount ; // 订单金额int 以「分」为单位的整型
	private String status ; // 订单状态int 1：成功
	private String sign;  //  签名信息string
	public String getMerchantaccount() {
		return merchantaccount;
	}
	public void setMerchantaccount(String merchantaccount) {
		this.merchantaccount = merchantaccount;
	}
	public String getYborderid() {
		return yborderid;
	}
	public void setYborderid(String yborderid) {
		this.yborderid = yborderid;
	}
	public String getOrderid() {
		return orderid;
	}
	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}
	public String getBankcode() {
		return bankcode;
	}
	public void setBankcode(String bankcode) {
		this.bankcode = bankcode;
	}
	public String getBank() {
		return bank;
	}
	public void setBank(String bank) {
		this.bank = bank;
	}
	public String getLastno() {
		return lastno;
	}
	public void setLastno(String lastno) {
		this.lastno = lastno;
	}
	public String getCardtype() {
		return cardtype;
	}
	public void setCardtype(String cardtype) {
		this.cardtype = cardtype;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	
	
}

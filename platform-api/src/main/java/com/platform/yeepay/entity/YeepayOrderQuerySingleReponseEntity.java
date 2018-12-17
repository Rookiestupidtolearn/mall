package com.platform.yeepay.entity;

import java.io.Serializable;

/**
 * 订单查询响应接口
 * @author Administrator
 *
 */
public class YeepayOrderQuerySingleReponseEntity implements Serializable {
	private  String  merchantaccount ; //商户编号string 商户编号
	private  String orderid ; //商户订单号string 商户订单号
	private  String yborderid; // 易宝流水号string 在易宝系统中唯一标识一笔订单
	private  String amount ; //订单金额int 单位：分
	private  String currency; // 交易币种int 156 – 人民币
	private  String sourcefee ; //付款方手续费int 单位：分
	private  String targetfee ; // 收款方手续费int 单位：分
	private  String sourceamount ; // 付款方实付金额int 单位：分
	private  String targetamount; //  收款方实收金额int 单位：分
	private  String ordertime ; // 下单时间int 时间戳，如：1361324896
	private  String closetime; // 交易时间int 交易时间
	private  String type ; //支付类型int1：银行卡2：非银行卡
	private  String bank ; // 银行名称string 银行名称，如工商银行。
	private  String bankcode ; // 银行缩写string 银行缩写，如ICBC
	private  String bankcardtype ; // 银行卡类型int1：储蓄卡2：信用卡
	private  String status ; // 订单状态int0：未支1：支付成功2 : 已撤销【表示订单已过有效期】3：阻断交易- 订单因为高风险而被阻断4：失败5：处理中
	private  String refundtotal ; //累计退款金额int 单位：分
	private  String productcatalog ; //商品类别码int 详见附录：5.5 商品类别码表
	private  String productname ; //商品名称string 商品名称
	private  String productdesc ; //商品描述string 商品描述
	private  String sign ; //签名信息string
	private  String error_code ; //错误码string 详见附录：5.8 返回码列表
	private  String error ; // 错误信息string 错误信息
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
	public String getYborderid() {
		return yborderid;
	}
	public void setYborderid(String yborderid) {
		this.yborderid = yborderid;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getSourcefee() {
		return sourcefee;
	}
	public void setSourcefee(String sourcefee) {
		this.sourcefee = sourcefee;
	}
	public String getTargetfee() {
		return targetfee;
	}
	public void setTargetfee(String targetfee) {
		this.targetfee = targetfee;
	}
	public String getSourceamount() {
		return sourceamount;
	}
	public void setSourceamount(String sourceamount) {
		this.sourceamount = sourceamount;
	}
	public String getTargetamount() {
		return targetamount;
	}
	public void setTargetamount(String targetamount) {
		this.targetamount = targetamount;
	}
	public String getOrdertime() {
		return ordertime;
	}
	public void setOrdertime(String ordertime) {
		this.ordertime = ordertime;
	}
	public String getClosetime() {
		return closetime;
	}
	public void setClosetime(String closetime) {
		this.closetime = closetime;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getBank() {
		return bank;
	}
	public void setBank(String bank) {
		this.bank = bank;
	}
	public String getBankcode() {
		return bankcode;
	}
	public void setBankcode(String bankcode) {
		this.bankcode = bankcode;
	}
	public String getBankcardtype() {
		return bankcardtype;
	}
	public void setBankcardtype(String bankcardtype) {
		this.bankcardtype = bankcardtype;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getRefundtotal() {
		return refundtotal;
	}
	public void setRefundtotal(String refundtotal) {
		this.refundtotal = refundtotal;
	}
	public String getProductcatalog() {
		return productcatalog;
	}
	public void setProductcatalog(String productcatalog) {
		this.productcatalog = productcatalog;
	}
	public String getProductname() {
		return productname;
	}
	public void setProductname(String productname) {
		this.productname = productname;
	}
	public String getProductdesc() {
		return productdesc;
	}
	public void setProductdesc(String productdesc) {
		this.productdesc = productdesc;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public String getError_code() {
		return error_code;
	}
	public void setError_code(String error_code) {
		this.error_code = error_code;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	
	
}

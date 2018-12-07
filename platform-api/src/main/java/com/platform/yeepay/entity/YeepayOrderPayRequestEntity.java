package com.platform.yeepay.entity;

import java.io.Serializable;

/**
 * 4.1 订单支付请求接口
 * @author Administrator
 *
 */
public class YeepayOrderPayRequestEntity implements Serializable {

	private String merchantaccount; //商户编号
	private String  orderid ; //商户订单号  √ 商户生成的唯一订单号，最长50 位
	private String  transtime; // 交易发生时间  √ 时间戳，例如：1361324896，精确到秒
	private String  amount;  // 交易金额int √ 以「分」为单位的整型，必须大于零
	private String  Currency;  // 交易币种int × 默认值：156；表示币种为人民币
	private String  productcatalog ; //商品类别码string √ 详见附录：5.6 商品类别码表
	private String  productname; // 商品名称string √
	private String  productdesc ; // 商品描述string ×
	private String  identitytype ; // 用户标识类型int √
	private String  identityid; // 用户标识string √ 传openId.
	private String  appId ; //微信公众号string × 微信公众号
	private String terminaltype ;// 终端标识类型int √ 	0、IMEI；1、MAC；2、UUID；3、OTHER
	private String  terminalid  ;// 终端标识ID string √ 如果有值则terminaltype 也 	必须有值
	private String  userip; // 用户ip 地址string √ 用户支付时使用的网络终端IP
	private String paytool;// 支付工具string ×
	private String  directpaytype;// 直联编码int ×  	0：默认1：微信支付2：支付宝支付3：一键支付
	private String  userua;// 终端设备UA string × 用户终端设备UA
	private String  fcallbackurl;//  页面回调地址string
	
	private String callbackurl ;// 后台回调地址string × 用来通知商户支付结果
	private String paytypes;// 支付方式string × 格式：1|2|3|4 1- 借记卡支付；2- 信用卡支付；3- 手机充值卡支付；4- 游戏点卡支付

	private String orderexpdate;// 订单有效期int ×
	
	private String cardno;// 银行卡号string ×
	
	private String idcardtype;// 证件类型string × 01：身份证，当前只支持身份证
	private String idcard ;//证件号string × 证件号
	private String owner;// 持卡人姓名string × 持卡人姓名
	private String version ;//收银台版本int ×0：wap1：pc ，当为扫码支付时收银台版本必传且值为1
	private String sign;// 签名信息string √
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
	public String getTranstime() {
		return transtime;
	}
	public void setTranstime(String transtime) {
		this.transtime = transtime;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getCurrency() {
		return Currency;
	}
	public void setCurrency(String currency) {
		Currency = currency;
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
	public String getIdentitytype() {
		return identitytype;
	}
	public void setIdentitytype(String identitytype) {
		this.identitytype = identitytype;
	}
	public String getIdentityid() {
		return identityid;
	}
	public void setIdentityid(String identityid) {
		this.identityid = identityid;
	}
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getTerminaltype() {
		return terminaltype;
	}
	public void setTerminaltype(String terminaltype) {
		this.terminaltype = terminaltype;
	}
	public String getTerminalid() {
		return terminalid;
	}
	public void setTerminalid(String terminalid) {
		this.terminalid = terminalid;
	}
	public String getUserip() {
		return userip;
	}
	public void setUserip(String userip) {
		this.userip = userip;
	}
	public String getPaytool() {
		return paytool;
	}
	public void setPaytool(String paytool) {
		this.paytool = paytool;
	}
	public String getDirectpaytype() {
		return directpaytype;
	}
	public void setDirectpaytype(String directpaytype) {
		this.directpaytype = directpaytype;
	}
	public String getUserua() {
		return userua;
	}
	public void setUserua(String userua) {
		this.userua = userua;
	}
	public String getFcallbackurl() {
		return fcallbackurl;
	}
	public void setFcallbackurl(String fcallbackurl) {
		this.fcallbackurl = fcallbackurl;
	}
	public String getCallbackurl() {
		return callbackurl;
	}
	public void setCallbackurl(String callbackurl) {
		this.callbackurl = callbackurl;
	}
	public String getPaytypes() {
		return paytypes;
	}
	public void setPaytypes(String paytypes) {
		this.paytypes = paytypes;
	}
	public String getOrderexpdate() {
		return orderexpdate;
	}
	public void setOrderexpdate(String orderexpdate) {
		this.orderexpdate = orderexpdate;
	}
	public String getCardno() {
		return cardno;
	}
	public void setCardno(String cardno) {
		this.cardno = cardno;
	}
	public String getIdcardtype() {
		return idcardtype;
	}
	public void setIdcardtype(String idcardtype) {
		this.idcardtype = idcardtype;
	}
	public String getIdcard() {
		return idcard;
	}
	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}

	
}

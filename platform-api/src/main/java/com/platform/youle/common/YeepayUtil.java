package com.platform.youle.common;

import com.platform.youle.util.PropertiesUtil;

public class YeepayUtil {
    
	//商户号
	public static final String merchantaccount = PropertiesUtil.getValue("yeepay.properties","merchantaccount");
	//商户私钥
	public static final String merchantPrivateKey= PropertiesUtil.getValue("yeepay.properties","merchantPrivateKey");

	//易宝公玥
	public static final String  yeepayPublicKey= PropertiesUtil.getValue("yeepay.properties","yeepayPublicKey");
	//4.1 订单支付接口请求地址
	public static final String  PayApi= PropertiesUtil.getValue("yeepay.properties","PayApi");

	//4.2 订单查询接口请求地址
	public static final String  QueryOrderApi=PropertiesUtil.getValue("yeepay.properties","QueryOrderApi");

	//4.3 消费对账文件下载接口请求地址
	public static final String  PayClearDataApi=PropertiesUtil.getValue("yeepay.properties","PayClearDataApi");
	//4.4 单笔退款接口请求地址
	public static final String  RefundApi=PropertiesUtil.getValue("yeepay.properties","RefundApi");
	//4.5 退款查询接口请求地址
	public static final String  QueryRefundApi=PropertiesUtil.getValue("yeepay.properties","QueryRefundApi");
	//4.6 退款对账文件下载接口
	public static final String  RefundClearDataApi=PropertiesUtil.getValue("yeepay.properties","RefundClearDataApi");
	//4.7 银行卡信息查询接口请求地址
	public static final String  CheckBankcardApi=PropertiesUtil.getValue("yeepay.properties","CheckBankcardApi");
	//4.8 查询绑卡信息列表
	public static final String  QueryBankCardListApi=PropertiesUtil.getValue("yeepay.properties","QueryBankCardListApi");
	//4.9 解绑卡
	public static final String  UnbindCardApi=PropertiesUtil.getValue("yeepay.properties","UnbindCardApi");

}

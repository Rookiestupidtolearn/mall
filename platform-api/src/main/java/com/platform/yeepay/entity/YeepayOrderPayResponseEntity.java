package com.platform.yeepay.entity;

import java.io.Serializable;

/**
 * 订单支付响应接口
 * @author Administrator
 *
 */
public class YeepayOrderPayResponseEntity implements Serializable {
	private String merchantaccount;// 商户编号string 商户编号
	private String yborderid;// 易宝交易流水号long 易宝流水号，易宝中唯一
	private String orderid ;//商户订单号string 原值返回
	private String payurl ;//支付链接string 1、移动收银台：返回的链接为需要进行支付的易宝支付收银台地址,商户需要将浏览
	private String imghexstr ; //二维码字符串string
	private String sign ;//
	private String error_code; //返回码string 错误码
	private String error_msg ;// 错误描述string 失败时有错误描述
	
	
}

package com.platform.yeepay.entity;

import java.io.Serializable;
/**
 * 消费对账文件下载接口
 * @author Administrator
 *
 */
public class YeepayPayClearDataRequestEntity implements Serializable {
	private String  merchantaccount;// 商户编号string √ 商户编号
	private String  startdate ;// 查询起始时间string √ 格式：2015-01-01
	private String enddate ; // 查询结束时间string √ 格式：2015-01-31
	private String sign ;// 签名信息string √
	public String getMerchantaccount() {
		return merchantaccount;
	}
	public void setMerchantaccount(String merchantaccount) {
		this.merchantaccount = merchantaccount;
	}
	public String getStartdate() {
		return startdate;
	}
	public void setStartdate(String startdate) {
		this.startdate = startdate;
	}
	public String getEnddate() {
		return enddate;
	}
	public void setEnddate(String enddate) {
		this.enddate = enddate;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	
	
}

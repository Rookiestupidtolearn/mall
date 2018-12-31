package com.platform.thirdrechard.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * 预支付请求实体
 * @author zct
 *
 */
public class RequestPreRechargeEntity implements Serializable{

	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 商户号
	 */
	@NotEmpty(message=" 商户号不能为空")
	private String  appId;
	
	
	@NotEmpty(message="手机号码不能为空")
    @Pattern(regexp = "^\\d{11}$", message = "手机号码格式错误")
	private  String phone;
	
	
	/**
	 * 渠道号
	 */
	@NotEmpty(message="渠道号不能为空")
	private String  channelNo;
	/**
	 * 请求序列号
	 */
	@NotEmpty(message="requestId不能为空")
	private String  requestId;
	
	/**
	 * 订单号
	 */
	@NotEmpty(message="订单号不能为空")
	private String orderNo;
	
	/**
	 * 订单金额
	 */
	//@Pattern(regexp = "^[0-9]*[1-9][0-9]*$", message = "金额必须是正整数")
    //@NotEmpty(message="金额必须是正整数")
	private BigDecimal amount;
	
	/**
	 * 签名
	 */
	private String signature;
	
	
	public String regSignVal(){
		String src = appId + "|" + channelNo + "|"+requestId +"|"+ orderNo + "|" + amount ;
		return src;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getChannelNo() {
		return channelNo;
	}

	public void setChannelNo(String channelNo) {
		this.channelNo = channelNo;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

}

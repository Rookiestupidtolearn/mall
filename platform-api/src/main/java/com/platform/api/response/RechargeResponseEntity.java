package com.platform.api.response;

public class RechargeResponseEntity {

	/**
	 * 200 充值成功
	 * error
	 * 1000 解析密文失败
	 */
	private String code;
	private String msg;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	
}

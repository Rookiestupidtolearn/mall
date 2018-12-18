package com.platform.api.response;

public class RechargeResponseEntity {

	/**
	 * success 充值成功
	 * error  充值失败 
	 * 1000 解析密文失败
	 * 1001 手机号错误 
	 * 1002 金额错误  10万
	 * 1003 订单编号错误 ,最长50位
	 * 1004 充值渠错误 ,2-奇速贷，3-其他
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

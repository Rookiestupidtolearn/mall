package com.platform.thirdrechard.entity;

public class RechargeResponseEntity {

	/**
	 * success 充值成功
	 * error  充值失败 
	 * 900 正在处理中
	 * 1000 解析密文失败
	 * 1001 手机号错误 
	 * 1002 金额错误 
	 * 1003 订单编号错误 ,最长50位
	 * 1004 充值渠错误 
	 * 1005 重复下订单
	 * 1006 校验url失败
	 * 1007 校验会员卡类型失败
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

	@Override
	public String toString() {
		return "RechargeResponseEntity{" +
				"code='" + code + '\'' +
				", msg='" + msg + '\'' +
				'}';
	}
}

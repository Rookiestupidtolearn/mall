
package com.platform.yeepay.entity;

import java.io.Serializable;

/**
 * 错误返回
 * @author Administrator
 *
 */
public class YeepayErrorResponseEntity  implements Serializable {
	private String error_code; // 错误码string 详见附录：5.8 返回码列表
	private String error;// 错误信息string 错误信息
	private String sign ;//签名
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
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	
	
}

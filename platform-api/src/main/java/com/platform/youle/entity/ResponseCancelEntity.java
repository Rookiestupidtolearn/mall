package com.platform.youle.entity;

import java.util.List;

/**
 * 取消订单接口（不支持京东及严选产品）
 *
 */
public class ResponseCancelEntity{
	private boolean RESPONSE_STATUS	;	//true, false
	private String ERROR_CODE; //	String	错误状态码(当RESPONSE_STATUS为false时有值)
	private String ERROR_MESSAGE; //	Stirng	错误提示信息(当RESPONSE_STATUS为false时有值)
	private String third_order;//	String	第三方订单号
	private List<String> order_keys; //	被取消的订单号
	
	
	public boolean isRESPONSE_STATUS() {
		return RESPONSE_STATUS;
	}
	public void setRESPONSE_STATUS(boolean rESPONSE_STATUS) {
		RESPONSE_STATUS = rESPONSE_STATUS;
	}
	public String getERROR_CODE() {
		return ERROR_CODE;
	}
	public void setERROR_CODE(String eRROR_CODE) {
		ERROR_CODE = eRROR_CODE;
	}
	public String getERROR_MESSAGE() {
		return ERROR_MESSAGE;
	}
	public void setERROR_MESSAGE(String eRROR_MESSAGE) {
		ERROR_MESSAGE = eRROR_MESSAGE;
	}
	public String getThird_order() {
		return third_order;
	}
	public void setThird_order(String third_order) {
		this.third_order = third_order;
	}
	public List<String> getOrder_keys() {
		return order_keys;
	}
	public void setOrder_keys(List<String> order_keys) {
		this.order_keys = order_keys;
	}
}

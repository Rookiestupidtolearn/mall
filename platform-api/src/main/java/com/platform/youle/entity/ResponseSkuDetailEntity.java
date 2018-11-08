package com.platform.youle.entity;

import java.util.List;

public class ResponseSkuDetailEntity {
	//true, false
	private boolean RESPONSE_STATUS;
	//错误状态码(当RESPONSE_STATUS为false时有值)
	private String ERROR_CODE;
	//错误提示信息(当RESPONSE_STATUS为false时有值)
	private String ERROR_MESSAGE;
	//基础数据
	private String RESULT_DATA;
	//产品数据
	private List<String> PRODUCT_DATA;
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
	
	public List<String> getPRODUCT_DATA() {
		return PRODUCT_DATA;
	}
	public void setPRODUCT_DATA(List<String> pRODUCT_DATA) {
		PRODUCT_DATA = pRODUCT_DATA;
	}
	public String getRESULT_DATA() {
		return RESULT_DATA;
	}
	public void setRESULT_DATA(String rESULT_DATA) {
		RESULT_DATA = rESULT_DATA;
	}
	
	
}

package com.platform.youle.entity;


public class ResponseBaseEntity<T>{
	
	private String RESPONSE_STATUS;
	private String ERROR_CODE;
	private String ERROR_MESSAGE;
	private T RESULT_DATA;

	public T getRESULT_DATA() {
		return RESULT_DATA;
	}

	public void setRESULT_DATA(T RESULT_DATA) {
		this.RESULT_DATA = RESULT_DATA;
	}

	public String getERROR_CODE() {
		return ERROR_CODE;
	}

	public void setERROR_CODE(String ERROR_CODE) {
		this.ERROR_CODE = ERROR_CODE;
	}

	public String getERROR_MESSAGE() {
		return ERROR_MESSAGE;
	}

	public void setERROR_MESSAGE(String ERROR_MESSAGE) {
		this.ERROR_MESSAGE = ERROR_MESSAGE;
	}

	public String getRESPONSE_STATUS() {
		return RESPONSE_STATUS;
	}

	public void setRESPONSE_STATUS(String RESPONSE_STATUS) {
		this.RESPONSE_STATUS = RESPONSE_STATUS;
	}
}

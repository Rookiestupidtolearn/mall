package com.platform.youle.entity;

public class ResponseBaseEntity {

	private String RESPONSE_STATUS;
	private String ERROR_CODE;
	private String ERROR_MESSAGE;
	private String TOTAL_AMOUNT;
	private String RESULT_DATA;
	public String getRESPONSE_STATUS() {
		return RESPONSE_STATUS;
	}
	public void setRESPONSE_STATUS(String rESPONSE_STATUS) {
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
	public String getTOTAL_AMOUNT() {
		return TOTAL_AMOUNT;
	}
	public void setTOTAL_AMOUNT(String tOTAL_AMOUNT) {
		TOTAL_AMOUNT = tOTAL_AMOUNT;
	}
	public String getRESULT_DATA() {
		return RESULT_DATA;
	}
	public void setRESULT_DATA(String rESULT_DATA) {
		RESULT_DATA = rESULT_DATA;
	}
	
	

}

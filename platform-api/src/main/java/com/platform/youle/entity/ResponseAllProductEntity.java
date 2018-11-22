package com.platform.youle.entity;

import java.io.Serializable;
import java.util.List;

public class ResponseAllProductEntity implements Serializable{
	private static final long serialVersionUID = 1L;
	private boolean RESPONSE_STATUS;
	private String ERROR_CODE;
	private String ERROR_MESSAGE;
	private Long TOTAL_AMOUNT;
	private List RESULT_DATA;
	
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
	public Long getTOTAL_AMOUNT() {
		return TOTAL_AMOUNT;
	}
	public void setTOTAL_AMOUNT(Long tOTAL_AMOUNT) {
		TOTAL_AMOUNT = tOTAL_AMOUNT;
	}
	public List getRESULT_DATA() {
		return RESULT_DATA;
	}
	public void setRESULT_DATA(List rESULT_DATA) {
		RESULT_DATA = rESULT_DATA;
	}
	public boolean isRESPONSE_STATUS() {
		return RESPONSE_STATUS;
	}
	public void setRESPONSE_STATUS(boolean rESPONSE_STATUS) {
		RESPONSE_STATUS = rESPONSE_STATUS;
	}
	@Override
	public String toString() {
		return "ResponseAllProductEntity [RESPONSE_STATUS=" + RESPONSE_STATUS + ", ERROR_CODE=" + ERROR_CODE
				+ ", ERROR_MESSAGE=" + ERROR_MESSAGE + ", TOTAL_AMOUNT=" + TOTAL_AMOUNT + ", RESULT_DATA=" + RESULT_DATA
				+ "]";
	}
	
	
	
	
}

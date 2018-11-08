package com.platform.youle.entity;

import java.util.List;

public class ResponseProductEntity {

	private String RESPONSE_STATUS;
	private String  ERROR_CODE;
	private String ERROR_MESSAGE;
	private Integer  TOTAL_PAGE;
	private Integer PAGE;
	private List<Integer> RESULT_DATA;
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
	public Integer getTOTAL_PAGE() {
		return TOTAL_PAGE;
	}
	public void setTOTAL_PAGE(Integer tOTAL_PAGE) {
		TOTAL_PAGE = tOTAL_PAGE;
	}
	public Integer getPAGE() {
		return PAGE;
	}
	public void setPAGE(Integer pAGE) {
		PAGE = pAGE;
	}
	public List<Integer> getRESULT_DATA() {
		return RESULT_DATA;
	}
	public void setRESULT_DATA(List<Integer> rESULT_DATA) {
		RESULT_DATA = rESULT_DATA;
	}

	
}

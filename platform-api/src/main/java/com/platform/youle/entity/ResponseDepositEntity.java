package com.platform.youle.entity;

import java.util.Map;

public class ResponseDepositEntity extends ResponseBaseEntity<Map<String,String>>{
	private double REMAIN;//用户余额
	
	private double CREDIT_TOTAL;//授信额度
	
	private double CREDIT_USED;//已授信额度

	public double getREMAIN() {
		return REMAIN;
	}

	public void setREMAIN(double rEMAIN) {
		REMAIN = rEMAIN;
	}

	public double getCREDIT_TOTAL() {
		return CREDIT_TOTAL;
	}

	public void setCREDIT_TOTAL(double cREDIT_TOTAL) {
		CREDIT_TOTAL = cREDIT_TOTAL;
	}

	public double getCREDIT_USED() {
		return CREDIT_USED;
	}

	public void setCREDIT_USED(double cREDIT_USED) {
		CREDIT_USED = cREDIT_USED;
	}
	
	
}

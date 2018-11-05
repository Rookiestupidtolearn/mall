package com.platform.jd.entity;

import java.math.BigDecimal;
import java.util.List;

/**
 * 账户信息
 *
 */
public class JdResponseSelectRechargeInfoEntity {
	private BigDecimal account; //	账户余额
	private BigDecimal total_recharge; //	充值总额
	private BigDecimal total_spend; //	消费总额
	private Integer total_num; //	总记录数
	private List<JdResponseSelectRechargeInfoRecordEntity> record;//	充值记录数组：[{"amount":"充值金额","created":"充值时间戳"},{"amount":"10.00","created":"1445307289"}]
	public BigDecimal getAccount() {
		return account;
	}
	public void setAccount(BigDecimal account) {
		this.account = account;
	}
	public BigDecimal getTotal_recharge() {
		return total_recharge;
	}
	public void setTotal_recharge(BigDecimal total_recharge) {
		this.total_recharge = total_recharge;
	}
	public BigDecimal getTotal_spend() {
		return total_spend;
	}
	public void setTotal_spend(BigDecimal total_spend) {
		this.total_spend = total_spend;
	}
	public Integer getTotal_num() {
		return total_num;
	}
	public void setTotal_num(Integer total_num) {
		this.total_num = total_num;
	}
	public List<JdResponseSelectRechargeInfoRecordEntity> getRecord() {
		return record;
	}
	public void setRecord(List<JdResponseSelectRechargeInfoRecordEntity> record) {
		this.record = record;
	}


	

}

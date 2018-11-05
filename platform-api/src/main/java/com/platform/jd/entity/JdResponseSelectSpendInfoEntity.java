package com.platform.jd.entity;

import java.math.BigDecimal;
import java.util.List;

/**
 * 用户消费记录--账户信息
 *
 */
public class JdResponseSelectSpendInfoEntity {
	private BigDecimal account; //	账户余额
	private BigDecimal total_recharge; //	充值总额
	private BigDecimal total_spend; //	消费总额
	private Integer total_num; //	总记录数;
	private List<JdResponseSelectSpendInfoRecordEntity> record; //消费记录数组：[{"amount":"消费金额","created":"消费时间","ordersn":"礼管家平台订单号","thirdsn":"第三方订单号"}]
							/*amount 2016年1月10日起，为订单金额+运费，之前为订单金额，不加运费。*/
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
	public List<JdResponseSelectSpendInfoRecordEntity> getRecord() {
		return record;
	}
	public void setRecord(List<JdResponseSelectSpendInfoRecordEntity> record) {
		this.record = record;
	}
	
	
}

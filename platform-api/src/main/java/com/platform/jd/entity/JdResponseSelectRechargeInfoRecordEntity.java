package com.platform.jd.entity;

import java.math.BigDecimal;

/**
	查询用户充值记录 -- 充值记录实体
 *
 */
public class JdResponseSelectRechargeInfoRecordEntity {
	
	private BigDecimal amount; //充值金额
	
	private String created; //充值时间戳

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getCreated() {
		return created;
	}

	public void setCreated(String created) {
		this.created = created;
	}
	
	
}

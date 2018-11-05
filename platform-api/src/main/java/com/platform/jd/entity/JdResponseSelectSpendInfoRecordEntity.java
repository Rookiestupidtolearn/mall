package com.platform.jd.entity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 用户消费记录--账户信息 -- 消费记录
 *
 */
public class JdResponseSelectSpendInfoRecordEntity {
	
	private BigDecimal amount; //消费金额
	private Date created; //消费时间
	private String ordersn; //礼管家平台订单号
	private String thirdsn; //第三方订单号
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	public String getOrdersn() {
		return ordersn;
	}
	public void setOrdersn(String ordersn) {
		this.ordersn = ordersn;
	}
	public String getThirdsn() {
		return thirdsn;
	}
	public void setThirdsn(String thirdsn) {
		this.thirdsn = thirdsn;
	}
	
	
}

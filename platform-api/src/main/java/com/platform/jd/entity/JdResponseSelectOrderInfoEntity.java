package com.platform.jd.entity;

import java.math.BigDecimal;
import java.util.List;

public class JdResponseSelectOrderInfoEntity {
	private String ordersn; //	礼管家平台订单号
	private String thirdsn; //	第三方订单号
	private BigDecimal order_amount; //	订单金额
	private Integer state; //	是否已确认 0：未确认，1：已确认
	private List<JdResponseOrderSubmitInfoSkuEntity> sku; //	[{"sku":"京东商品编号","num":"购买数量","price":"协议价","jdPrice":"京东价"}]
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
	public BigDecimal getOrder_amount() {
		return order_amount;
	}
	public void setOrder_amount(BigDecimal order_amount) {
		this.order_amount = order_amount;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public List<JdResponseOrderSubmitInfoSkuEntity> getSku() {
		return sku;
	}
	public void setSku(List<JdResponseOrderSubmitInfoSkuEntity> sku) {
		this.sku = sku;
	}
	
	
}

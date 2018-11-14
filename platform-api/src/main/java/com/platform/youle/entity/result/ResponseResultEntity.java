package com.platform.youle.entity.result;

import java.math.BigDecimal;

public class ResponseResultEntity {
	 private  Boolean  order_split;
	 private String order_key;
	 private BigDecimal  order_total_price;
	 private BigDecimal order_product_price;
	 private BigDecimal order_shipment_fee;
	public Boolean getOrder_split() {
		return order_split;
	}
	public void setOrder_split(Boolean order_split) {
		this.order_split = order_split;
	}
	public String getOrder_key() {
		return order_key;
	}
	public void setOrder_key(String order_key) {
		this.order_key = order_key;
	}
	public BigDecimal getOrder_total_price() {
		return order_total_price;
	}
	public void setOrder_total_price(BigDecimal order_total_price) {
		this.order_total_price = order_total_price;
	}
	public BigDecimal getOrder_product_price() {
		return order_product_price;
	}
	public void setOrder_product_price(BigDecimal order_product_price) {
		this.order_product_price = order_product_price;
	}
	public BigDecimal getOrder_shipment_fee() {
		return order_shipment_fee;
	}
	public void setOrder_shipment_fee(BigDecimal order_shipment_fee) {
		this.order_shipment_fee = order_shipment_fee;
	}

}

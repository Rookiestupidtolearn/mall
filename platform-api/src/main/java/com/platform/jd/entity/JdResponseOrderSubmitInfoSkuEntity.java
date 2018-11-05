package com.platform.jd.entity;

import java.math.BigDecimal;

/**
 * 预存款订单信息 -- sku
 *
 */
public class JdResponseOrderSubmitInfoSkuEntity {
	private String sku; //商品编号
	private Integer num; //购买数量
	private BigDecimal price; //协议价
	private BigDecimal jdPrice; //京东价
	public String getSku() {
		return sku;
	}
	public void setSku(String sku) {
		this.sku = sku;
	}
	public Integer getNum() {
		return num;
	}
	public void setNum(Integer num) {
		this.num = num;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public BigDecimal getJdPrice() {
		return jdPrice;
	}
	public void setJdPrice(BigDecimal jdPrice) {
		this.jdPrice = jdPrice;
	}
	
	
}

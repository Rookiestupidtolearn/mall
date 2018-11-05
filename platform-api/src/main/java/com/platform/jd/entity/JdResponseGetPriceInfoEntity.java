package com.platform.jd.entity;

import java.math.BigDecimal;

/**
 * 商品价格
 *
 */
public class JdResponseGetPriceInfoEntity {
	
	private String skuId;//	商品编号
	private BigDecimal jdPrice; //	京东价
	private BigDecimal price; //	协议价
	public String getSkuId() {
		return skuId;
	}
	public void setSkuId(String skuId) {
		this.skuId = skuId;
	}
	public BigDecimal getJdPrice() {
		return jdPrice;
	}
	public void setJdPrice(BigDecimal jdPrice) {
		this.jdPrice = jdPrice;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	
	
}

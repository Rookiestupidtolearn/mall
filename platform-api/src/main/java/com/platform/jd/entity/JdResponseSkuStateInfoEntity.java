package com.platform.jd.entity;

public class JdResponseSkuStateInfoEntity {
	private String sku;//	商品编号
	private Integer state;//	上下架状态	1为上架，0为下架
	public String getSku() {
		return sku;
	}
	public void setSku(String sku) {
		this.sku = sku;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	
	
}

package com.platform.jd.entity;

/**
 * 分页获取所有sku响应实体
 *
 */
public class GetSkuResponseEntity extends JdResponseBaseEntity {
	private Integer total_page;//	总页数
	private String skus;//	所有商品编号，商品编号之间分隔符为’,’
	
	public Integer getTotal_page() {
		return total_page;
	}
	public void setTotal_page(Integer total_page) {
		this.total_page = total_page;
	}
	public String getSkus() {
		return skus;
	}
	public void setSkus(String skus) {
		this.skus = skus;
	}
	
	
}

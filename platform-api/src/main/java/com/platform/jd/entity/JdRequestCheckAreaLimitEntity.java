package com.platform.jd.entity;

/**
 * 商品购买区域限制请求实体
 *
 */
public class JdRequestCheckAreaLimitEntity extends JdRequestBaseEntity{
	
	private String area; //	格式：1_0_0 (分别代表1、2、3级地址) 
	private String skuIds; // 商品编号，支持批量，以’,’分隔 (最高支持100个商品)  
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getSkuIds() {
		return skuIds;
	}
	public void setSkuIds(String skuIds) {
		this.skuIds = skuIds;
	}
	
}
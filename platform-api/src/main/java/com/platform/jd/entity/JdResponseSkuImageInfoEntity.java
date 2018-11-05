package com.platform.jd.entity;

/**
 * 图片信息
 *
 */
public class JdResponseSkuImageInfoEntity {
	private String skuId;//	商品编号
	private String path; //	图片路径
	private Integer isPrimary;//	是否是主图，1为主图，0为附图
	private String orderSort; //	图片排序
	public String getSkuId() {
		return skuId;
	}
	public void setSkuId(String skuId) {
		this.skuId = skuId;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public Integer getIsPrimary() {
		return isPrimary;
	}
	public void setIsPrimary(Integer isPrimary) {
		this.isPrimary = isPrimary;
	}
	public String getOrderSort() {
		return orderSort;
	}
	public void setOrderSort(String orderSort) {
		this.orderSort = orderSort;
	}
	
	
}

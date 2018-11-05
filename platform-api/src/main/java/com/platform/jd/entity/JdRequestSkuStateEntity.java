package com.platform.jd.entity;

/**
 * 获取商品上下架状态请求实体
 *
 */
public class JdRequestSkuStateEntity extends JdRequestBaseEntity {
	
	private String sku; //商品编号，支持批量，以，分隔  (最高支持100个商品)

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}
	
	
}

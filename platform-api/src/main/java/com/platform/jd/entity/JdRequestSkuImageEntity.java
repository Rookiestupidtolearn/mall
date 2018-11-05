package com.platform.jd.entity;

/**
 * 获取所有图片信息请求实体
 *
 */
public class JdRequestSkuImageEntity extends JdRequestBaseEntity {
	private String sku; //商品编号，支持批量，以，分隔  (最高支持100个商品)

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}
	
	
}

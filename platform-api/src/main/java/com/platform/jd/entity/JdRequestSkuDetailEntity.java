package com.platform.jd.entity;

/**
 * 获取商品详细信息接口请求实体
 *
 */
public class JdRequestSkuDetailEntity extends JdRequestBaseEntity{
	
	private String sku; //商品编号，只支持单个查询

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}
	
	
}

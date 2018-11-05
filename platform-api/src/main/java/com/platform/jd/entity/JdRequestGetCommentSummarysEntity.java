package com.platform.jd.entity;

/**
 * 商品好评度查询请求实体
 *
 */
public class JdRequestGetCommentSummarysEntity extends JdRequestBaseEntity {
	
	private String sku; //商品编号，支持批量，以，分隔  (最高支持50个商品)

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}
	
	
}

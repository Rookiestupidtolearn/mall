package com.platform.jd.entity;

/**
 * 批量查询协议价价格请求接口
 *
 */
public class JdRequestGetPriceEntity extends JdRequestBaseEntity {
	
	private String sku; //商品编号，支持批量，以，分隔  (最高支持100个商品)

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}
	

}

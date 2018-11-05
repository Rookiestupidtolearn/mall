package com.platform.jd.entity;

/**
 * 分页获取所有sku请求实体
 *
 */
public class GetSkuRequestEntity extends JdRequestBaseEntity{
	private Integer page; //页码

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}
	
	
}

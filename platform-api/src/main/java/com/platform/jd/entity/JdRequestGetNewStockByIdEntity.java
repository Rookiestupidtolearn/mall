package com.platform.jd.entity;

import java.util.List;

/**
 * 批量获取库存接口
 *
 */
public class JdRequestGetNewStockByIdEntity extends JdRequestBaseEntity{
	private String 	area; //	格式：1_0_0 (分别代表1、2、3级地址) \
	private List<JdRequestGetNewStockByIdSkuNumsEntity> skuNums;//	商品和数量 [{skuId: 569172,num:101}] （数组和json字符都可以）
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public List<JdRequestGetNewStockByIdSkuNumsEntity> getSkuNums() {
		return skuNums;
	}
	public void setSkuNums(List<JdRequestGetNewStockByIdSkuNumsEntity> skuNums) {
		this.skuNums = skuNums;
	}
	
	
}

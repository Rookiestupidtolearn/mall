package com.platform.jd.entity;

/**
 * 是否受限结果
 *
 */
public class JdResponseCheckAreaLimitInfoEntity {
	private Integer skuId; //	商品编号
	private boolean isAreaRestrict; //	是否受限，false 不受限，true区域购买受限
	public Integer getSkuId() {
		return skuId;
	}
	public void setSkuId(Integer skuId) {
		this.skuId = skuId;
	}
	public boolean isAreaRestrict() {
		return isAreaRestrict;
	}
	public void setAreaRestrict(boolean isAreaRestrict) {
		this.isAreaRestrict = isAreaRestrict;
	}
	
	
}

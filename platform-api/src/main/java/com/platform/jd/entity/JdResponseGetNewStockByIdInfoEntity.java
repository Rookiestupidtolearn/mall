package com.platform.jd.entity;

/**
 * 库存结果
 *
 */
public class JdResponseGetNewStockByIdInfoEntity {
	private Integer areaId; //	配送地址id
	private Integer skuId;//	商品编号
	private Integer stockStateId;	//库存状态编号 33,39,40,36,34
	private String StockStateDesc; //库存状态描述 
									/*33  有货 现货-下单立即发货 
									39 有货 在途-正在内部配货，预计2~6天到达本仓库
									40 有货 可配货-下单后从有货仓库配货
									36 预订
									34 无货*/
	
	private Integer remainNum;//	剩余数量 -1未知；当库存小于5时展示真实库存数量

	public Integer getAreaId() {
		return areaId;
	}

	public void setAreaId(Integer areaId) {
		this.areaId = areaId;
	}

	public Integer getSkuId() {
		return skuId;
	}

	public void setSkuId(Integer skuId) {
		this.skuId = skuId;
	}

	public Integer getStockStateId() {
		return stockStateId;
	}

	public void setStockStateId(Integer stockStateId) {
		this.stockStateId = stockStateId;
	}

	public String getStockStateDesc() {
		return StockStateDesc;
	}

	public void setStockStateDesc(String stockStateDesc) {
		StockStateDesc = stockStateDesc;
	}

	public Integer getRemainNum() {
		return remainNum;
	}

	public void setRemainNum(Integer remainNum) {
		this.remainNum = remainNum;
	}
	
	
}

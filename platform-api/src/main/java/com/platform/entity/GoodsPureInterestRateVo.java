package com.platform.entity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 商品毛利率
 *
 */
public class GoodsPureInterestRateVo {
	
	private Integer id;
	private Integer goodsId; //商品id
	private Integer productId; // 商品规格id
	private BigDecimal retailPrice; //结算价(零售价)
	private BigDecimal marketPrice; //指导价(市场价)
	private double pureInterestRate; //毛利率
	private Date createTime;
	private Date updateTime;
	private Long createUserId; //创建人id
	private String memo; //备注
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(Integer goodsId) {
		this.goodsId = goodsId;
	}
	public Integer getProductId() {
		return productId;
	}
	public void setProductId(Integer productId) {
		this.productId = productId;
	}
	public BigDecimal getRetailPrice() {
		return retailPrice;
	}
	public void setRetailPrice(BigDecimal retailPrice) {
		this.retailPrice = retailPrice;
	}
	public BigDecimal getMarketPrice() {
		return marketPrice;
	}
	public void setMarketPrice(BigDecimal marketPrice) {
		this.marketPrice = marketPrice;
	}
	public double getPureInterestRate() {
		return pureInterestRate;
	}
	public void setPureInterestRate(double pureInterestRate) {
		this.pureInterestRate = pureInterestRate;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Long getCreateUserId() {
		return createUserId;
	}
	public void setCreateUserId(Long createUserId) {
		this.createUserId = createUserId;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	
	
	

}

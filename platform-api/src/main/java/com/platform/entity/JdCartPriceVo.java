package com.platform.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class JdCartPriceVo implements Serializable {
	
	private static final long serialVersionUID = 1L;
	// 用户id
	private Long user_id;
	// 商品编号
	private String goods_sn;
	// 京东市场价
	private BigDecimal market_price;
	// 京东实际价
	private BigDecimal retail_price;
	// 本地
	private BigDecimal raw_market_price;
	// 原实际价格
	private BigDecimal raw_retail_price;
	// 创建时间
	private Date create_time;

	public Long getUser_id() {
		return user_id;
	}

	public void setUser_id(Long user_id) {
		this.user_id = user_id;
	}

	public String getGoods_sn() {
		return goods_sn;
	}

	public void setGoods_sn(String goods_sn) {
		this.goods_sn = goods_sn;
	}

	public BigDecimal getMarket_price() {
		return market_price;
	}

	public void setMarket_price(BigDecimal market_price) {
		this.market_price = market_price;
	}

	public BigDecimal getRetail_price() {
		return retail_price;
	}

	public void setRetail_price(BigDecimal retail_price) {
		this.retail_price = retail_price;
	}

	public BigDecimal getRaw_market_price() {
		return raw_market_price;
	}

	public void setRaw_market_price(BigDecimal raw_market_price) {
		this.raw_market_price = raw_market_price;
	}

	public BigDecimal getRaw_retail_price() {
		return raw_retail_price;
	}

	public void setRaw_retail_price(BigDecimal raw_retail_price) {
		this.raw_retail_price = raw_retail_price;
	}

	public Date getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}

}

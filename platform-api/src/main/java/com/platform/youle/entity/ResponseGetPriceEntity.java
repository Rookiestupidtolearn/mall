package com.platform.youle.entity;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 查询商品协议价 实体
 */
public class ResponseGetPriceEntity extends  ResponseBaseEntity<Map<String,String>>{

    /**
     *协议价格
     */
    private BigDecimal retail_price;

    /**
     *市场参考价
     */
    private BigDecimal market_price;


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
}

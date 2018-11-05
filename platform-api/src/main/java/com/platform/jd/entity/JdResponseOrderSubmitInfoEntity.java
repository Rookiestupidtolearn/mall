package com.platform.jd.entity;

import java.math.BigDecimal;
import java.util.List;

/**
 * 订单信息
 *
 */
public class JdResponseOrderSubmitInfoEntity {
	private String ordersn; //	礼管家平台订单号
	private String thirdsn; //	第三方订单号
	private BigDecimal order_amount; //	订单金额 （协议价总额）
	private List<JdResponseOrderSubmitInfoSkuEntity> sku	;//[{"sku":商品编号,"num":购买数量,"price":协议价,"jdPrice":京东价}]
	private BigDecimal freight; //	运费
	private String jdOrderId; //	京东订单号
	public String getOrdersn() {
		return ordersn;
	}
	public void setOrdersn(String ordersn) {
		this.ordersn = ordersn;
	}
	public String getThirdsn() {
		return thirdsn;
	}
	public void setThirdsn(String thirdsn) {
		this.thirdsn = thirdsn;
	}
	public BigDecimal getOrder_amount() {
		return order_amount;
	}
	public void setOrder_amount(BigDecimal order_amount) {
		this.order_amount = order_amount;
	}
	public List<JdResponseOrderSubmitInfoSkuEntity> getSku() {
		return sku;
	}
	public void setSku(List<JdResponseOrderSubmitInfoSkuEntity> sku) {
		this.sku = sku;
	}
	public BigDecimal getFreight() {
		return freight;
	}
	public void setFreight(BigDecimal freight) {
		this.freight = freight;
	}
	public String getJdOrderId() {
		return jdOrderId;
	}
	public void setJdOrderId(String jdOrderId) {
		this.jdOrderId = jdOrderId;
	}
	
	
}

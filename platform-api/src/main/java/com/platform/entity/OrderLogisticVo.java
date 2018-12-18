package com.platform.entity;

import java.io.Serializable;

public class OrderLogisticVo implements Serializable{
	private static final long serialVersionUID = 1L;
	
	//快递公司
	private String shipmentName;
	//快递单号
	private String shipmentOrder;
	//逾期收货时间
	private String arrivalTime;
	//商品名称
	private String goodName;
	//商品URL
	private String goodUrl;
	//单号id
	private String orderId;
	public String getShipmentName() {
		return shipmentName;
	}
	public void setShipmentName(String shipmentName) {
		this.shipmentName = shipmentName;
	}
	public String getShipmentOrder() {
		return shipmentOrder;
	}
	public void setShipmentOrder(String shipmentOrder) {
		this.shipmentOrder = shipmentOrder;
	}
	public String getArrivalTime() {
		return arrivalTime;
	}
	public void setArrivalTime(String arrivalTime) {
		this.arrivalTime = arrivalTime;
	}
	public String getGoodName() {
		return goodName;
	}
	public void setGoodName(String goodName) {
		this.goodName = goodName;
	}
	public String getGoodUrl() {
		return goodUrl;
	}
	public void setGoodUrl(String goodUrl) {
		this.goodUrl = goodUrl;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	
	
	
}

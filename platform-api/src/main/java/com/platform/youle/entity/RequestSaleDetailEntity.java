package com.platform.youle.entity;

/**
 * 6.3 查询售后订单详情
 * @author Administrator
 *
 */
public class RequestSaleDetailEntity extends  RequestBaseEntity{
   private String serviceOrder;

public String getServiceOrder() {
	return serviceOrder;
}

public void setServiceOrder(String serviceOrder) {
	this.serviceOrder = serviceOrder;
}
   
   
}

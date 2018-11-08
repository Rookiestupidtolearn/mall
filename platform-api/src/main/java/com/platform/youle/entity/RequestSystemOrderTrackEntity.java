package com.platform.youle.entity;


/**
 *订单物流信息接口
 */

public  class RequestSystemOrderTrackEntity extends  RequestBaseEntity {

    /**
     *订单ID
     */

    private  String orderKey;

    public String getOrderKey() {
        return orderKey;
    }

    public void setOrderKey(String orderKey) {
        this.orderKey = orderKey;
    }
}

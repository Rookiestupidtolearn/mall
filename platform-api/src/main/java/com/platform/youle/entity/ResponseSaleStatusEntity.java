package com.platform.youle.entity;

import java.util.Map;

/**
 * 查询商品可售状态 响应实体
 */
public class ResponseSaleStatusEntity extends  ResponseBaseEntity<Map<String,String>>{

    //状态
    private  Boolean status;

    //描述信息
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

}
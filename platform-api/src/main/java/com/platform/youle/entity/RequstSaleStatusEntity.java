package com.platform.youle.entity;

/**
 *查询商品可售状态
 */
public class RequstSaleStatusEntity extends RequestBaseEntity {

    /**
     *商品ID
     */
    private  Integer pid;


    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }
}

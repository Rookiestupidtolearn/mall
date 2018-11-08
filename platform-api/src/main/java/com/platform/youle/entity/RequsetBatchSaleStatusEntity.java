package com.platform.youle.entity;


/**
 * 批量查询商品可售状态实体
 */
public class RequsetBatchSaleStatusEntity extends RequestBaseEntity {

    /**
     * ID 串
     */
    private String pids ;

    public String getPids() {
        return pids;
    }

    public void setPids(String pids) {
        this.pids = pids;
    }
}

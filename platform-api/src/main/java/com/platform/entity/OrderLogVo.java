package com.platform.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 实体
 * 表名 nideshop_order_log
 *
 * @author lipengjun
 * @email 939961241@qq.com
 * @date 2018-12-13 10:10:44
 */
public class OrderLogVo implements Serializable {
    private static final long serialVersionUID = 1L;

    //主键id
    private Integer id;
    //本地订单号
    private String orderSn;
    //三方订单号
    private String orderKey;
    //本地订单状态
    private Integer orderState;
    //三方订单状态
    private Integer thirdOrderState;
    //备注
    private String remark;
    //创建时间
    private Date createTime;
    //更新时间
    private Date updateTime;

    /**
     * 设置：主键id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取：主键id
     */
    public Integer getId() {
        return id;
    }
    /**
     * 设置：本地订单号
     */
    public void setOrderSn(String orderSn) {
        this.orderSn = orderSn;
    }

    /**
     * 获取：本地订单号
     */
    public String getOrderSn() {
        return orderSn;
    }
    /**
     * 设置：三方订单号
     */
    public void setOrderKey(String orderKey) {
        this.orderKey = orderKey;
    }

    /**
     * 获取：三方订单号
     */
    public String getOrderKey() {
        return orderKey;
    }
    /**
     * 设置：本地订单状态
     */
    public void setOrderState(Integer orderState) {
        this.orderState = orderState;
    }

    /**
     * 获取：本地订单状态
     */
    public Integer getOrderState() {
        return orderState;
    }
    /**
     * 设置：三方订单状态
     */
    public void setThirdOrderState(Integer thirdOrderState) {
        this.thirdOrderState = thirdOrderState;
    }

    /**
     * 获取：三方订单状态
     */
    public Integer getThirdOrderState() {
        return thirdOrderState;
    }
    /**
     * 设置：备注
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * 获取：备注
     */
    public String getRemark() {
        return remark;
    }
    /**
     * 设置：创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取：创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }
    /**
     * 设置：更新时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 获取：更新时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }
}

package com.platform.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 产品-平台币配置表实 体
 * 表名 goods_coupon_config
 *
 * @author lipengjun
 * @email 939961241@qq.com
 * @date 2018-10-12 11:42:14
 */
public class GoodsCouponConfigEntity  implements Serializable {
	
    private static final long serialVersionUID = 1L;

    //
    private Integer id;
    //产品ID
    private Integer goodsId;
    //配比值
    private double goodValue;
    //
    private Long createUserId;
    //
    private Long updateUserId;
    //
    private Date updateTime;
    //
    private Long createUserDeptId;
    //
    private String delFlag;

    /**
     * 设置：
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取：
     */
    public Integer getId() {
        return id;
    }
    /**
     * 设置：产品ID
     */
    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    /**
     * 获取：产品ID
     */
    public Integer getGoodsId() {
        return goodsId;
    }
    /**
     * 设置：配比值
     */
    public void setGoodValue(double goodValue) {
        this.goodValue = goodValue;
    }

    /**
     * 获取：配比值
     */
    public double getGoodValue() {
        return goodValue;
    }
    /**
     * 设置：
     */
    public void setCreateUserId(Long createUserId) {
        this.createUserId = createUserId;
    }

    /**
     * 获取：
     */
    public Long getCreateUserId() {
        return createUserId;
    }
    /**
     * 设置：
     */
    public void setUpdateUserId(Long updateUserId) {
        this.updateUserId = updateUserId;
    }

    /**
     * 获取：
     */
    public Long getUpdateUserId() {
        return updateUserId;
    }
    /**
     * 设置：
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 获取：
     */
    public Date getUpdateTime() {
        return updateTime;
    }
    /**
     * 设置：
     */
    public void setCreateUserDeptId(Long createUserDeptId) {
        this.createUserDeptId = createUserDeptId;
    }

    /**
     * 获取：
     */
    public Long getCreateUserDeptId() {
        return createUserDeptId;
    }
    /**
     * 设置：
     */
    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    /**
     * 获取：
     */
    public String getDelFlag() {
        return delFlag;
    }
}

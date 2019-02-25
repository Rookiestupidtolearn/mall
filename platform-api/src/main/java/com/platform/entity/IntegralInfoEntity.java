package com.platform.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 会员积分记录表实体
 * 表名 nideshop_integral_info
 *
 * @author lipengjun
 * @email 939961241@qq.com
 * @date 2019-02-19 11:37:52
 */
public class IntegralInfoEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    //
    private Long id;
    //用户id
    private Integer userId;
    //类型(1:购物支付)
    private String type;
    //当次积分
    private Integer atPresentIntegral;
    //创建时间
    private Date createDate;
    //操作后积分
    private Integer integral;
    //收支类型(1:收入 2:支出)
    private String accountingType;
    //备注
    private String memo;
    //删除标识(0:未删除 1:删除)
    private Integer isDelete;

    /**
     * 设置：
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取：
     */
    public Long getId() {
        return id;
    }
    /**
     * 设置：用户id
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * 获取：用户id
     */
    public Integer getUserId() {
        return userId;
    }
    /**
     * 设置：类型(1:购物支付)
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * 获取：类型(1:购物支付)
     */
    public String getType() {
        return type;
    }
    /**
     * 设置：当次积分
     */
    public void setAtPresentIntegral(Integer atPresentIntegral) {
        this.atPresentIntegral = atPresentIntegral;
    }

    /**
     * 获取：当次积分
     */
    public Integer getAtPresentIntegral() {
        return atPresentIntegral;
    }
    /**
     * 设置：创建时间
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    /**
     * 获取：创建时间
     */
    public Date getCreateDate() {
        return createDate;
    }
    /**
     * 设置：操作后积分
     */
    public void setIntegral(Integer integral) {
        this.integral = integral;
    }

    /**
     * 获取：操作后积分
     */
    public Integer getIntegral() {
        return integral;
    }
    /**
     * 设置：收支类型(1:收入 2:支出)
     */
    public void setAccountingType(String accountingType) {
        this.accountingType = accountingType;
    }

    /**
     * 获取：收支类型(1:收入 2:支出)
     */
    public String getAccountingType() {
        return accountingType;
    }
    /**
     * 设置：备注
     */
    public void setMemo(String memo) {
        this.memo = memo;
    }

    /**
     * 获取：备注
     */
    public String getMemo() {
        return memo;
    }
    /**
     * 设置：删除标识(0:未删除 1:删除)
     */
    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    /**
     * 获取：删除标识(0:未删除 1:删除)
     */
    public Integer getIsDelete() {
        return isDelete;
    }
}

package com.platform.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 用户充值记录实体
 * 表名 third_recharge_record
 *
 * @author lipengjun
 * @email 939961241@qq.com
 * @date 2018-12-20 16:00:02
 */
public class ThirdRechargeRecordEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    //主键
    private Integer id;
    //手机号
    private String mobile;
    //状态：0-初始，1-成功，2-失败
    private String state;
    //金额
    private BigDecimal amount;
    //三方订单号
    private String thirdTradeNo;
    //系统订单号
    private String tradeNo;
    //1-会员卡;2-展期卡
    private String cardType;
    //平台类型
    private String platformType;
    //版本号
    private Integer version;
    //修改时间
    private Date updateTime;
    //创建时间
    private Date createTime;

    /**
     * 设置：主键
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取：主键
     */
    public Integer getId() {
        return id;
    }
    /**
     * 设置：手机号
     */
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    /**
     * 获取：手机号
     */
    public String getMobile() {
        return mobile;
    }
    /**
     * 设置：状态：0-初始，1-成功，2-失败
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     * 获取：状态：0-初始，1-成功，2-失败
     */
    public String getState() {
        return state;
    }
    /**
     * 设置：金额
     */
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    /**
     * 获取：金额
     */
    public BigDecimal getAmount() {
        return amount;
    }
    /**
     * 设置：三方订单号
     */
    public void setThirdTradeNo(String thirdTradeNo) {
        this.thirdTradeNo = thirdTradeNo;
    }

    /**
     * 获取：三方订单号
     */
    public String getThirdTradeNo() {
        return thirdTradeNo;
    }
    /**
     * 设置：系统订单号
     */
    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    /**
     * 获取：系统订单号
     */
    public String getTradeNo() {
        return tradeNo;
    }
    /**
     * 设置：1-会员卡;2-展期卡
     */
    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    /**
     * 获取：1-会员卡;2-展期卡
     */
    public String getCardType() {
        return cardType;
    }
    /**
     * 设置：平台类型
     */
    public void setPlatformType(String platformType) {
        this.platformType = platformType;
    }

    /**
     * 获取：平台类型
     */
    public String getPlatformType() {
        return platformType;
    }
    /**
     * 设置：版本号
     */
    public void setVersion(Integer version) {
        this.version = version;
    }

    /**
     * 获取：版本号
     */
    public Integer getVersion() {
        return version;
    }
    /**
     * 设置：修改时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 获取：修改时间
     */
    public Date getUpdateTime() {
        return updateTime;
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
}

package com.platform.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 易宝支付订单表实体
 * 表名 yee_trade_order
 *
 * @author lipengjun
 * @email 939961241@qq.com
 * @date 2018-12-09 13:31:06
 */
public class YeeTradeOrderEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    //主键
    private Integer id;
    //交易流水号(传给易宝)
    private String yeeOrderNo;
    //交易流水号(关联订单)
    private String tradeNo;
    //用户ID
    private Integer userId;
    //订单状态 0-初始化
    private Integer payStatus;
    //支付方式 0-默认,1-微信支付,2-支付宝支付,3-一键支付
    private Integer payType;
    //支付总金额
    private BigDecimal amount;
    //请求参数
    private String requestParam;
    //响应结果
    private String responseMsg;
    //删除 0-正常 1-删除
    private Integer isDeleted;
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
     * 设置：交易流水号(传给易宝)
     */
    public void setYeeOrderNo(String yeeOrderNo) {
        this.yeeOrderNo = yeeOrderNo;
    }

    /**
     * 获取：交易流水号(传给易宝)
     */
    public String getYeeOrderNo() {
        return yeeOrderNo;
    }
    /**
     * 设置：交易流水号(关联订单)
     */
    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    /**
     * 获取：交易流水号(关联订单)
     */
    public String getTradeNo() {
        return tradeNo;
    }
    /**
     * 设置：用户ID
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * 获取：用户ID
     */
    public Integer getUserId() {
        return userId;
    }
    /**
     * 设置：订单状态 0-初始化
     */
    public void setPayStatus(Integer payStatus) {
        this.payStatus = payStatus;
    }

    /**
     * 获取：订单状态 0-初始化
     */
    public Integer getPayStatus() {
        return payStatus;
    }
    /**
     * 设置：支付方式 0-默认,1-微信支付,2-支付宝支付,3-一键支付
     */
    public void setPayType(Integer payType) {
        this.payType = payType;
    }

    /**
     * 获取：支付方式 0-默认,1-微信支付,2-支付宝支付,3-一键支付
     */
    public Integer getPayType() {
        return payType;
    }
    /**
     * 设置：支付总金额
     */
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    /**
     * 获取：支付总金额
     */
    public BigDecimal getAmount() {
        return amount;
    }
    /**
     * 设置：请求参数
     */
    public void setRequestParam(String requestParam) {
        this.requestParam = requestParam;
    }

    /**
     * 获取：请求参数
     */
    public String getRequestParam() {
        return requestParam;
    }
    /**
     * 设置：响应结果
     */
    public void setResponseMsg(String responseMsg) {
        this.responseMsg = responseMsg;
    }

    /**
     * 获取：响应结果
     */
    public String getResponseMsg() {
        return responseMsg;
    }
    /**
     * 设置：删除 0-正常 1-删除
     */
    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    /**
     * 获取：删除 0-正常 1-删除
     */
    public Integer getIsDeleted() {
        return isDeleted;
    }

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
    
    
}

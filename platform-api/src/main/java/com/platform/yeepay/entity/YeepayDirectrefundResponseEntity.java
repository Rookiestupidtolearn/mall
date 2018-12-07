package com.platform.yeepay.entity;


import java.io.Serializable;

/**
 * 4.4 订单退款接口 响应实体
 *
 * @author zct
 */
public class YeepayDirectrefundResponseEntity implements Serializable {

    /**
     * 商户编号
     */
    private String merchantaccount;

    /**
     * 退款请求号
     */
    private String orderid;

    /**
     * 易宝退款流水号
     */
    private String yborderid;
    /**
     * 易宝流水
     */
    private String origyborderid;

    /**
     * 已退金额
     */
    private String amount;

    /**
     * 退款手续费
     */
    private String fee;

    /**
     * 交易币种
     */
    private String currency;

    /**
     * 退款时间
     */
    private String timestamp;

    /**
     * 剩余金额
     */
    private String remain;

    /**
     * 签名信息
     */
    private String sign;

    public String getMerchantaccount() {
        return merchantaccount;
    }

    public void setMerchantaccount(String merchantaccount) {
        this.merchantaccount = merchantaccount;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getYborderid() {
        return yborderid;
    }

    public void setYborderid(String yborderid) {
        this.yborderid = yborderid;
    }

    public String getOrigyborderid() {
        return origyborderid;
    }

    public void setOrigyborderid(String origyborderid) {
        this.origyborderid = origyborderid;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getRemain() {
        return remain;
    }

    public void setRemain(String remain) {
        this.remain = remain;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}

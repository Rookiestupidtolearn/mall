package com.platform.yeepay.entity;

import java.io.Serializable;

/**
 * 4.5退款查询
 */
public class YeepayRefundsingleResponseEntity implements Serializable {


    /**
     * 商户编号
     */
    private String merchantaccount;

    /**
     * 退款请求号 原值返回
     */
    private String  orderi;

    /**
     * 退款请求号 该笔订单的退款流水号
     */
    private String yborderid;

    /**
     *易宝流水号  该笔订单的支付流水号
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
     * 退款请求时间
     */
    private String ordertime;

    /**
     * 退款处理时间
     */
    private String closetime;

    /**
     * 退款状态
     */
    private String    status;

    /**
     * 退款原因
     */
    private String cause;

    /**
     * 签名信息
     */
    private String     sign;

    /**
     * 错误码
     */
    private String    error_code;

    /**
     * 错误信息
     */
    private String     error;

    public String getMerchantaccount() {
        return merchantaccount;
    }

    public void setMerchantaccount(String merchantaccount) {
        this.merchantaccount = merchantaccount;
    }

    public String getOrderi() {
        return orderi;
    }

    public void setOrderi(String orderi) {
        this.orderi = orderi;
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

    public String getOrdertime() {
        return ordertime;
    }

    public void setOrdertime(String ordertime) {
        this.ordertime = ordertime;
    }

    public String getClosetime() {
        return closetime;
    }

    public void setClosetime(String closetime) {
        this.closetime = closetime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getError_code() {
        return error_code;
    }

    public void setError_code(String error_code) {
        this.error_code = error_code;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}

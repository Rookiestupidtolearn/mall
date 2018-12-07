package com.platform.yeepay.entity;

import java.io.Serializable;


/**
 * 订单退款接口 请求实体
 *
 * @author zct
 */
public class YeepayDirectRefundRequestEntity implements Serializable {

    /**
     * 商户编号
     */
    private String merchantaccount;
    /**
     * 退款请求号
     */
    private String orderid;

    /**
     * 易宝流水号
     */
    private String origyborderid;

    /**
     * amount 退款金额  单位：分
     */
    private String amount;

    /**
     * 交易币种  156；
     */
    private String currency;


    /**
     * 退款说明
     */

    private String cause;

    /**
     *
     */
    private String  error_code;
    /**
     * 错误信息
     */
    private String error;


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

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
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

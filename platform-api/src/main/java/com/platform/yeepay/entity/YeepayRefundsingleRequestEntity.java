package com.platform.yeepay.entity;


import java.io.Serializable;

/**
 *4.5 退款查询接口 请求实体
 * @author zct
 *  */
public class YeepayRefundsingleRequestEntity implements Serializable {

    /**
     * 商户编号
     */
   private String  merchantaccount;

    /**
     * 退款请求号
     */
    private String  orderid;

    /**
     * 签名
     *
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

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}

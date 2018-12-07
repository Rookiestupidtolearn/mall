package com.platform.yeepay.entity;

import java.io.Serializable;

/**
 *
 * @author zct
 */
public class YeepayRefundcleardataRequestEntity implements Serializable{


    /**
     * 商户编号
     */
    private String  merchantaccount;

    /**
     * 查询起始时间
     */
    private String   startdate;

    /**
     * 查询结束时间
     */
    private String enddate;

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

    public String getStartdate() {
        return startdate;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

    public String getEnddate() {
        return enddate;
    }

    public void setEnddate(String enddate) {
        this.enddate = enddate;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}

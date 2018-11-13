package com.platform.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 京东订单表实体
 * 表名 jd_order
 *
 * @author lipengjun
 * @email 939961241@qq.com
 * @date 2018-11-12 15:02:37
 */
public class JdOrderVo implements Serializable {
    private static final long serialVersionUID = 1L;

  //主键
    private Integer id;
    //会员id
    private Integer shopUserId;
    //订单状态 0-初始,5-失败，9-成功
    private Integer orderStatus;
    //第三方订单号（本系统的）
    private String thirdOrder;
    //订单编号（三方返回的）
    private String orderKey;
    //商品ID_数量
    private String pidNums;
    //收货地址
    private String receiverName;
    //省, 区域信息ID
    private String province;
    //市, 区域信息ID
    private String city;
    //县, 区域信息ID
    private String county;
    //乡/镇, 区域信息ID
    private String town;
    //详细地址
    private String address;
    //手机号
    private String mobile;
    //收货人邮箱
    private String email;
    //订单备注信息
    private String remarke;
    //错误状态码
    private String errorCode;
    //错误信息
    private String errorMessage;
    //true, false
    private String responseStatus;
    //产品价格
    private BigDecimal orderProductPrice;
    //快递费用
    private BigDecimal orderShipmentFee;
    //订单总金额
    private BigDecimal orderTotalPrice;
    //订单是否被拆分 true: 被拆分为多个订单, false: 未拆分
    private Boolean orderSplit;
    //创建时间
    private Date createTime;
    //是否删除 0-正常 1-删除
    private Boolean isDeleted = false;

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
     * 设置：会员id
     */
    public void setShopUserId(Integer shopUserId) {
        this.shopUserId = shopUserId;
    }

    /**
     * 获取：会员id
     */
    public Integer getShopUserId() {
        return shopUserId;
    }
    /**
     * 设置：订单状态
     */
    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    /**
     * 获取：订单状态
     */
    public Integer getOrderStatus() {
        return orderStatus;
    }
    /**
     * 设置：第三方订单号（本系统的）
     */
    public void setThirdOrder(String thirdOrder) {
        this.thirdOrder = thirdOrder;
    }

    /**
     * 获取：第三方订单号（本系统的）
     */
    public String getThirdOrder() {
        return thirdOrder;
    }
    /**
     * 设置：订单编号（三方返回的）
     */
    public void setOrderKey(String orderKey) {
        this.orderKey = orderKey;
    }

    /**
     * 获取：订单编号（三方返回的）
     */
    public String getOrderKey() {
        return orderKey;
    }
    /**
     * 设置：商品ID_数量
     */
    public void setPidNums(String pidNums) {
        this.pidNums = pidNums;
    }

    /**
     * 获取：商品ID_数量
     */
    public String getPidNums() {
        return pidNums;
    }
    /**
     * 设置：收货地址
     */
    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    /**
     * 获取：收货地址
     */
    public String getReceiverName() {
        return receiverName;
    }
    /**
     * 设置：省, 区域信息ID
     */
    public void setProvince(String province) {
        this.province = province;
    }

    /**
     * 获取：省, 区域信息ID
     */
    public String getProvince() {
        return province;
    }
    /**
     * 设置：市, 区域信息ID
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * 获取：市, 区域信息ID
     */
    public String getCity() {
        return city;
    }
    /**
     * 设置：县, 区域信息ID
     */
    public void setCounty(String county) {
        this.county = county;
    }

    /**
     * 获取：县, 区域信息ID
     */
    public String getCounty() {
        return county;
    }
    /**
     * 设置：乡/镇, 区域信息ID
     */
    public void setTown(String town) {
        this.town = town;
    }

    /**
     * 获取：乡/镇, 区域信息ID
     */
    public String getTown() {
        return town;
    }
    /**
     * 设置：详细地址
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * 获取：详细地址
     */
    public String getAddress() {
        return address;
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
     * 设置：收货人邮箱
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * 获取：收货人邮箱
     */
    public String getEmail() {
        return email;
    }
    /**
     * 设置：订单备注信息
     */
    public void setRemarke(String remarke) {
        this.remarke = remarke;
    }

    /**
     * 获取：订单备注信息
     */
    public String getRemarke() {
        return remarke;
    }
    /**
     * 设置：错误状态码
     */
    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    /**
     * 获取：错误状态码
     */
    public String getErrorCode() {
        return errorCode;
    }
    /**
     * 设置：错误信息
     */
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    /**
     * 获取：错误信息
     */
    public String getErrorMessage() {
        return errorMessage;
    }
    /**
     * 设置：true, false
     */
    public void setResponseStatus(String responseStatus) {
        this.responseStatus = responseStatus;
    }

    /**
     * 获取：true, false
     */
    public String getResponseStatus() {
        return responseStatus;
    }
    /**
     * 设置：快递费用
     */
    public void setOrderShipmentFee(BigDecimal orderShipmentFee) {
        this.orderShipmentFee = orderShipmentFee;
    }

    /**
     * 获取：快递费用
     */
    public BigDecimal getOrderShipmentFee() {
        return orderShipmentFee;
    }
    /**
     * 设置：订单总金额
     */
    public void setOrderTotalPrice(BigDecimal orderTotalPrice) {
        this.orderTotalPrice = orderTotalPrice;
    }

    /**
     * 获取：订单总金额
     */
    public BigDecimal getOrderTotalPrice() {
        return orderTotalPrice;
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

	public BigDecimal getOrderProductPrice() {
		return orderProductPrice;
	}

	public void setOrderProductPrice(BigDecimal orderProductPrice) {
		this.orderProductPrice = orderProductPrice;
	}

	public Boolean getOrderSplit() {
		return orderSplit;
	}

	public void setOrderSplit(Boolean orderSplit) {
		this.orderSplit = orderSplit;
	}

	public Boolean getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}
    
    
}

package com.platform.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 用户充值记录实体
 * 表名 qz_recharge_record
 *
 * @author lipengjun
 * @email 939961241@qq.com
 * @date 2018-10-13 14:11:37
 */
public class QzRechargeRecordEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    //主键
    private Integer id;
    //会员id
    //手机号
    private String mobile;
    private Integer shopUserId;
    private String shopUserName;
    private String UserPhone;
    //状态：0-初始，1-通过，2-拒绝
    private String state;
    //操作人id
    private Long operateId;
    //审核时间
    private Date operateTime;
    //金额
    private BigDecimal amount;
    //备注
    private String memo;
    //创建时间
    private Date createTime;
    //修改时间
    private Date updateTime;
    //流水号
    private String tradeNo;
    //充值类型 1-后台充值 2-奇速贷充值
    private Integer rechargeType;
    //审核人id
    private Long auditId;
    private String operate;

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
     * 设置：状态：0-初始，1-通过，2-拒绝
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     * 获取：状态：0-初始，1-通过，2-拒绝
     */
    public String getState() {
        return state;
    }
    /**
     * 设置：操作人id
     */
    public void setOperateId(Long operateId) {
        this.operateId = operateId;
    }

    /**
     * 获取：操作人id
     */
    public Long getOperateId() {
        return operateId;
    }
    /**
     * 设置：审核时间
     */
    public void setOperateTime(Date operateTime) {
        this.operateTime = operateTime;
    }

    /**
     * 获取：审核时间
     */
    public Date getOperateTime() {
        return operateTime;
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
     * 设置：流水号
     */
    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    /**
     * 获取：流水号
     */
    public String getTradeNo() {
        return tradeNo;
    }
    /**
     * 设置：审核人id
     */
    public void setAuditId(Long auditId) {
        this.auditId = auditId;
    }

    /**
     * 获取：审核人id
     */
    public Long getAuditId() {
        return auditId;
    }

	public String getShopUserName() {
		return shopUserName;
	}

	public void setShopUserName(String shopUserName) {
		this.shopUserName = shopUserName;
	}

	public String getUserPhone() {
		return UserPhone;
	}

	public void setUserPhone(String userPhone) {
		UserPhone = userPhone;
	}

	public String getOperate() {
		return operate;
	}

	public void setOperate(String operate) {
		this.operate = operate;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public Integer getRechargeType() {
		return rechargeType;
	}

	public void setRechargeType(Integer rechargeType) {
		this.rechargeType = rechargeType;
	}
    
    
}

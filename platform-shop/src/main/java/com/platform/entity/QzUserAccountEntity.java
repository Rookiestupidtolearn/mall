package com.platform.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 用户账户实体
 * 表名 qz_user_account
 *
 * @author lipengjun
 * @email 939961241@qq.com
 * @date 2018-10-13 11:28:14
 */
public class QzUserAccountEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    //主键
    private Long id;
    //用户余额
    private BigDecimal amount;
    //修改时间
    private Date lastUpdateTime;
    //创建时间
    private Date createTime;
    
    private Integer shopUserId;
    
    /**
     * 设置：主键
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取：主键
     */
    public Long getId() {
        return id;
    }
    /**
     * 设置：用户余额
     */
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    /**
     * 获取：用户余额
     */
    public BigDecimal getAmount() {
        return amount;
    }
    /**
     * 设置：修改时间
     */
    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    /**
     * 获取：修改时间
     */
    public Date getLastUpdateTime() {
        return lastUpdateTime;
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

	public Integer getShopUserId() {
		return shopUserId;
	}

	public void setShopUserId(Integer shopUserId) {
		this.shopUserId = shopUserId;
	}
    
}

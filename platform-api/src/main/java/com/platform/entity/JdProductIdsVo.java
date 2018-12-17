package com.platform.entity;

import java.io.Serializable;
import java.util.Date;

public class JdProductIdsVo implements Serializable {
	private static final long serialVersionUID = 1L;

	// 主键id
	private Integer id;
	// 三方产品id
	private Integer productId;
	// 创建时间
	private Date createTime;
	//删除标识
	private String deleteFlag;
	//更新时间
	private Date updateTime;

	/**
	 * 设置：主键id
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * 获取：主键id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * 设置：三方产品id
	 */
	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	/**
	 * 获取：三方产品id
	 */
	public Integer getProductId() {
		return productId;
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

	public String getDeleteFlag() {
		return deleteFlag;
	}

	public void setDeleteFlag(String deleteFlag) {
		this.deleteFlag = deleteFlag;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	
}

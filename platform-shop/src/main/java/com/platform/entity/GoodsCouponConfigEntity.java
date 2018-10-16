package com.platform.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 产品-平台币配置表实 体
 * 表名 goods_coupon_config
 *
 * @author lipengjun
 * @email 939961241@qq.com
 * @date 2018-10-12 11:42:14
 */
public class GoodsCouponConfigEntity  implements Serializable {
	
    private static final long serialVersionUID = 1L;

    //
    private Integer id;
    //产品ID
    private Integer goodsId;
    //配比值
    private double goodValue;
    //
    private Long createUserId;
    //
    private Long updateUserId;
    //
    private Date updateTime;
    //
    private Long createUserDeptId;
    //
    private String delFlag;
    
    //部门名称
    private String deptName;
    
    //名称
    private String name;
    
    //品牌Id
    private Integer brandId;
    
    //商品类型
    private String categoryName;
    
    
    //商品类型Id
    private Integer categoryId;
    
    //属性类别
    private String attributeCategoryName;
 
    //品牌
    private String brandName;
    
    
    public Integer getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getAttributeCategoryName() {
		return attributeCategoryName;
	}

	public void setAttributeCategoryName(String attributeCategoryName) {
		this.attributeCategoryName = attributeCategoryName;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	//属性类别
    private Integer attributeCategory;
    
    public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	/**
     * 设置：
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取：
     */
    public Integer getId() {
        return id;
    }
    /**
     * 设置：产品ID
     */
    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    /**
     * 获取：产品ID
     */
    public Integer getGoodsId() {
        return goodsId;
    }
    /**
     * 设置：配比值
     */
    public void setGoodValue(double goodValue) {
        this.goodValue = goodValue;
    }

    /**
     * 获取：配比值
     */
    public double getGoodValue() {
        return goodValue;
    }
    /**
     * 设置：
     */
    public void setCreateUserId(Long createUserId) {
        this.createUserId = createUserId;
    }

    public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getBrandId() {
		return brandId;
	}

	public void setBrandId(Integer brandId) {
		this.brandId = brandId;
	}

	public Integer getAttributeCategory() {
		return attributeCategory;
	}

	public void setAttributeCategory(Integer attributeCategory) {
		this.attributeCategory = attributeCategory;
	}

	/**
     * 获取：
     */
    public Long getCreateUserId() {
        return createUserId;
    }
    /**
     * 设置：
     */
    public void setUpdateUserId(Long updateUserId) {
        this.updateUserId = updateUserId;
    }

    /**
     * 获取：
     */
    public Long getUpdateUserId() {
        return updateUserId;
    }
    /**
     * 设置：
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 获取：
     */
    public Date getUpdateTime() {
        return updateTime;
    }
    /**
     * 设置：
     */
    public void setCreateUserDeptId(Long createUserDeptId) {
        this.createUserDeptId = createUserDeptId;
    }

    /**
     * 获取：
     */
    public Long getCreateUserDeptId() {
        return createUserDeptId;
    }
    /**
     * 设置：
     */
    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    /**
     * 获取：
     */
    public String getDelFlag() {
        return delFlag;
    }
}

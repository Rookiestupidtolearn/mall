package com.platform.youle.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 实体
 * 表名 goods_image_path
 *
 * @author lipengjun
 * @email 939961241@qq.com
 * @date 2018-11-08 14:39:05
 */
public class GoodsImagePathVo implements Serializable {
    private static final long serialVersionUID = 1L;

    //主键id
    private Long id;
    //图片路径
    private String imageUrl;
    //图片顺序
    private Integer orderSort;
    //商品id 唯一标识
    private Long productId;
    //创建时间
    private Date createTime;
    //创建者
    private Long createBy;
    //更新时间
    private Date updateTime;

    /**
     * 设置：主键id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取：主键id
     */
    public Long getId() {
        return id;
    }
    /**
     * 设置：图片路径
     */
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    /**
     * 获取：图片路径
     */
    public String getImageUrl() {
        return imageUrl;
    }
    /**
     * 设置：图片顺序
     */
    public void setOrderSort(Integer orderSort) {
        this.orderSort = orderSort;
    }

    /**
     * 获取：图片顺序
     */
    public Integer getOrderSort() {
        return orderSort;
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
     * 设置：创建者
     */
    public void setCreateBy(Long createBy) {
        this.createBy = createBy;
    }

    /**
     * 获取：创建者
     */
    public Long getCreateBy() {
        return createBy;
    }
    /**
     * 设置：更新时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 获取：更新时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}
    
}


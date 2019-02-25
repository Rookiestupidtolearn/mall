package com.platform.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户积分表实体
 * 表名 nideshop_integral
 *
 * @author lipengjun
 * @email 939961241@qq.com
 * @date 2019-02-19 11:37:52
 */
public class IntegralEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    //
    private Long id;
    //用户id
    private Integer userId;
    //用户可用积分
    private Integer integral;
    //创建时间
    private Date createDate;
    //删除标识(0:未删除 1:删除)
    private Integer isDelete;

    /**
     * 设置：
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取：
     */
    public Long getId() {
        return id;
    }
    /**
     * 设置：用户id
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * 获取：用户id
     */
    public Integer getUserId() {
        return userId;
    }
    /**
     * 设置：用户可用积分
     */
    public void setIntegral(Integer integral) {
        this.integral = integral;
    }

    /**
     * 获取：用户可用积分
     */
    public Integer getIntegral() {
        return integral;
    }
    /**
     * 设置：创建时间
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    /**
     * 获取：创建时间
     */
    public Date getCreateDate() {
        return createDate;
    }
    /**
     * 设置：删除标识(0:未删除 1:删除)
     */
    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    /**
     * 获取：删除标识(0:未删除 1:删除)
     */
    public Integer getIsDelete() {
        return isDelete;
    }
}

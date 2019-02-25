package com.platform.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 会员成长值记录表实体
 * 表名 nideshop_member_growth_info
 *
 * @author lipengjun
 * @email 939961241@qq.com
 * @date 2019-02-19 11:37:52
 */
public class MemberGrowthInfoEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    //
    private Long id;
    //用户id
    private Integer userId;
    //类型(1:支付增加成长值 2:充值增加成长值)
    private String type;
    //当次成长值
    private String atPresentGrowthValue;
    //创建时间
    private Date createDate;
    //操作后成长值
    private String growthValue;
    //备注
    private String memo;
    //删除标识
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
     * 设置：类型(1:支付增加成长值 2:充值增加成长值)
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * 获取：类型(1:支付增加成长值 2:充值增加成长值)
     */
    public String getType() {
        return type;
    }
    /**
     * 设置：当次成长值
     */
    public void setAtPresentGrowthValue(String atPresentGrowthValue) {
        this.atPresentGrowthValue = atPresentGrowthValue;
    }

    /**
     * 获取：当次成长值
     */
    public String getAtPresentGrowthValue() {
        return atPresentGrowthValue;
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
     * 设置：操作后成长值
     */
    public void setGrowthValue(String growthValue) {
        this.growthValue = growthValue;
    }

    /**
     * 获取：操作后成长值
     */
    public String getGrowthValue() {
        return growthValue;
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
     * 设置：删除标识
     */
    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    /**
     * 获取：删除标识
     */
    public Integer getIsDelete() {
        return isDelete;
    }
}

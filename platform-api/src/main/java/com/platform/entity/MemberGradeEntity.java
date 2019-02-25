package com.platform.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 会员等级表实体
 * 表名 nideshop_member_grade
 *
 * @author lipengjun
 * @email 939961241@qq.com
 * @date 2019-02-20 10:12:40
 */
public class MemberGradeEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    //
    private Long id;
    //vip等级
    private String grade;
    //成长值要求
    private String claim;
    //成长值比例
    private BigDecimal growthValueRatio;
    //克拉支付折扣比例
    private BigDecimal kelaPayRatio;
    //权益期限系数(用来计算vclub赠送期限)
    private BigDecimal giveDeadlineRatio;
    //创建时间
    private Date createTime;
    //删除标识(0:未删除 1:删除)
    private Integer isDelete;
    //备注
    private String memo;

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
     * 设置：vip等级
     */
    public void setGrade(String grade) {
        this.grade = grade;
    }

    /**
     * 获取：vip等级
     */
    public String getGrade() {
        return grade;
    }
    /**
     * 设置：成长值要求
     */
    public void setClaim(String claim) {
        this.claim = claim;
    }

    /**
     * 获取：成长值要求
     */
    public String getClaim() {
        return claim;
    }
    /**
     * 设置：成长值比例
     */
    public void setGrowthValueRatio(BigDecimal growthValueRatio) {
        this.growthValueRatio = growthValueRatio;
    }

    /**
     * 获取：成长值比例
     */
    public BigDecimal getGrowthValueRatio() {
        return growthValueRatio;
    }
    /**
     * 设置：克拉支付折扣比例
     */
    public void setKelaPayRatio(BigDecimal kelaPayRatio) {
        this.kelaPayRatio = kelaPayRatio;
    }

    /**
     * 获取：克拉支付折扣比例
     */
    public BigDecimal getKelaPayRatio() {
        return kelaPayRatio;
    }
    /**
     * 设置：权益期限系数(用来计算vclub赠送期限)
     */
    public void setGiveDeadlineRatio(BigDecimal giveDeadlineRatio) {
        this.giveDeadlineRatio = giveDeadlineRatio;
    }

    /**
     * 获取：权益期限系数(用来计算vclub赠送期限)
     */
    public BigDecimal getGiveDeadlineRatio() {
        return giveDeadlineRatio;
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
}

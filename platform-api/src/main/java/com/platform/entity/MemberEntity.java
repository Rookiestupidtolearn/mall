package com.platform.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 会员表实体
 * 表名 nideshop_member
 *
 * @author lipengjun
 * @email 939961241@qq.com
 * @date 2019-02-19 11:37:51
 */
public class MemberEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    //
    private Long id;
    //用户id
    private Integer userId;
    //会员等级
    private String memberGrade;
    //是否vip(0:否 1:是)
    private Integer isVip;
    //是否vplus (0:否 1:是)
    private Integer isVplus;
    //vplus开通时间
    private Date vplusBeginDate;
    //vplus到期时间
    private Date vplusEndDate;
    //会员成长值
    private Long membersGrowthValue;
    //版本号
    private String version;
    //删除标识(0:未删除 1:已删除)
    private Integer isDelete;
    //会员手机号
    private String mobile;

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
     * 设置：会员等级
     */
    public void setMemberGrade(String memberGrade) {
        this.memberGrade = memberGrade;
    }

    /**
     * 获取：会员等级
     */
    public String getMemberGrade() {
        return memberGrade;
    }
    /**
     * 设置：是否vip(0:否 1:是)
     */
    public void setIsVip(Integer isVip) {
        this.isVip = isVip;
    }

    /**
     * 获取：是否vip(0:否 1:是)
     */
    public Integer getIsVip() {
        return isVip;
    }
    /**
     * 设置：是否vplus (0:否 1:是)
     */
    public void setIsVplus(Integer isVplus) {
        this.isVplus = isVplus;
    }

    /**
     * 获取：是否vplus (0:否 1:是)
     */
    public Integer getIsVplus() {
        return isVplus;
    }
    /**
     * 设置：vplus开通时间
     */
    public void setVplusBeginDate(Date vplusBeginDate) {
        this.vplusBeginDate = vplusBeginDate;
    }

    /**
     * 获取：vplus开通时间
     */
    public Date getVplusBeginDate() {
        return vplusBeginDate;
    }
    /**
     * 设置：vplus到期时间
     */
    public void setVplusEndDate(Date vplusEndDate) {
        this.vplusEndDate = vplusEndDate;
    }

    /**
     * 获取：vplus到期时间
     */
    public Date getVplusEndDate() {
        return vplusEndDate;
    }
    /**
     * 设置：会员成长值
     */
    public void setMembersGrowthValue(Long membersGrowthValue) {
        this.membersGrowthValue = membersGrowthValue;
    }

    /**
     * 获取：会员成长值
     */
    public Long getMembersGrowthValue() {
        return membersGrowthValue;
    }
    /**
     * 设置：版本号
     */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * 获取：版本号
     */
    public String getVersion() {
        return version;
    }
    /**
     * 设置：删除标识(0:未删除 1:已删除)
     */
    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    /**
     * 获取：删除标识(0:未删除 1:已删除)
     */
    public Integer getIsDelete() {
        return isDelete;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    @Override
    public String toString() {
        return "MemberEntity{" +
                "id=" + id +
                ", userId=" + userId +
                ", memberGrade='" + memberGrade + '\'' +
                ", isVip=" + isVip +
                ", isVplus=" + isVplus +
                ", vplusBeginDate=" + vplusBeginDate +
                ", vplusEndDate=" + vplusEndDate +
                ", membersGrowthValue=" + membersGrowthValue +
                ", version='" + version + '\'' +
                ", isDelete=" + isDelete +
                '}';
    }
}

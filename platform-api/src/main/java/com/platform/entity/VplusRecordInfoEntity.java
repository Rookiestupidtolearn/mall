package com.platform.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Vplus开通续费记录表实体
 * 表名 vplus_record_info
 *
 * @author lipengjun
 * @email 939961241@qq.com
 * @date 2019-02-21 14:08:12
 */
public class VplusRecordInfoEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    //
    private Long id;
    //用户id
    private Integer userId;
    //类型(1:开通 2:续费)
    private String type;
    //创建时间
    private Date createDate;
    //
    private Date openDate;
    //vplus到期时间
    private Date endDate;
    //续费vplus对应的id
    private String vplusId;
    //充值金额
    private BigDecimal money;
    //备注
    private String memo;
    //续费状态 0 初始 1 充值成功 2 充值失败 
    private Integer state;
    //生成流水号关联易宝订单
    private String yeepayTradeno;
    //删除标识(0:未删除 1:已删除)
    private Integer isDelete;
   //平台类型  1 本平台 2 三方平台
    private Integer  platFormType;

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
     * 设置：类型(1:开通 2:续费)
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * 获取：类型(1:开通 2:续费)
     */
    public String getType() {
        return type;
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
     * 设置：
     */
    public void setOpenDate(Date openDate) {
        this.openDate = openDate;
    }

    /**
     * 获取：
     */
    public Date getOpenDate() {
        return openDate;
    }
    /**
     * 设置：vplus到期时间
     */
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    /**
     * 获取：vplus到期时间
     */
    public Date getEndDate() {
        return endDate;
    }
    /**
     * 设置：续费vplus对应的id
     */
    public void setVplusId(String vplusId) {
        this.vplusId = vplusId;
    }

    /**
     * 获取：续费vplus对应的id
     */
    public String getVplusId() {
        return vplusId;
    }
    /**
     * 设置：充值金额
     */
    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    /**
     * 获取：充值金额
     */
    public BigDecimal getMoney() {
        return money;
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
     * 设置：续费状态 0 初始 1 充值成功 2 充值失败 
     */
    public void setState(Integer state) {
        this.state = state;
    }

    /**
     * 获取：续费状态 0 初始 1 充值成功 2 充值失败 
     */
    public Integer getState() {
        return state;
    }
    /**
     * 设置：生成流水号关联易宝订单
     */
    public void setYeepayTradeno(String yeepayTradeno) {
        this.yeepayTradeno = yeepayTradeno;
    }

    /**
     * 获取：生成流水号关联易宝订单
     */
    public String getYeepayTradeno() {
        return yeepayTradeno;
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

    public Integer getPlatFormType() {
        return platFormType;
    }

    public void setPlatFormType(Integer platFormType) {
        this.platFormType = platFormType;
    }
}

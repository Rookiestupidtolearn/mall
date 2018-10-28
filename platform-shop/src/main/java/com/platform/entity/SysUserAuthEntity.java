package com.platform.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户信息认证实体
 * 表名 sys_user_auth
 *
 * @author lipengjun
 * @email 939961241@qq.com
 * @date 2018-10-26 11:03:04
 */
public class SysUserAuthEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    //
    private Integer id;
    //
    private Integer userId;
    //
    private Integer deptId;
    //
    private String name;
    
    /**
     * 用户账号
     */
    private String useraccount;
    //
    private String sex;
    //
    private String cardId;
    //
    private String addressCompony;
    //
    private String phone;
    //
    private Long createUserId;
    //
    private Date createTime;
    //
    private Long updateUserId;
    //
    private String delflag;
    //
    private Date updteTime;
    
    /**
     * 验证码
     */
    private String checkCode;
    
    
    

    public String getCheckCode() {
		return checkCode;
	}

	public void setCheckCode(String checkCode) {
		this.checkCode = checkCode;
	}

	public String getUseraccount() {
		return useraccount;
	}

	public void setUseraccount(String useraccount) {
		this.useraccount = useraccount;
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
     * 设置：
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * 获取：
     */
    public Integer getUserId() {
        return userId;
    }
    /**
     * 设置：
     */
    public void setDeptId(Integer deptId) {
        this.deptId = deptId;
    }

    /**
     * 获取：
     */
    public Integer getDeptId() {
        return deptId;
    }
    /**
     * 设置：
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取：
     */
    public String getName() {
        return name;
    }
    /**
     * 设置：
     */
    public void setSex(String sex) {
        this.sex = sex;
    }

    /**
     * 获取：
     */
    public String getSex() {
        return sex;
    }
    /**
     * 设置：
     */
    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    /**
     * 获取：
     */
    public String getCardId() {
        return cardId;
    }
    /**
     * 设置：
     */
    public void setAddressCompony(String addressCompony) {
        this.addressCompony = addressCompony;
    }

    /**
     * 获取：
     */
    public String getAddressCompony() {
        return addressCompony;
    }
    /**
     * 设置：
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * 获取：
     */
    public String getPhone() {
        return phone;
    }
    /**
     * 设置：
     */
    public void setCreateUserId(Long createUserId) {
        this.createUserId = createUserId;
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
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取：
     */
    public Date getCreateTime() {
        return createTime;
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
    public void setDelflag(String delflag) {
        this.delflag = delflag;
    }

    /**
     * 获取：
     */
    public String getDelflag() {
        return delflag;
    }
    /**
     * 设置：
     */
    public void setUpdteTime(Date updteTime) {
        this.updteTime = updteTime;
    }

    /**
     * 获取：
     */
    public Date getUpdteTime() {
        return updteTime;
    }
}

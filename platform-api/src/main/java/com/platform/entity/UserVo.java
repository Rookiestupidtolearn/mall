package com.platform.entity;

import java.io.Serializable;
import java.util.Date;


/**
 * @author lipengjun
 * @email 939961241@qq.com
 * @date 2017-08-15 08:03:41
 */
public class UserVo implements Serializable {
    private static final long serialVersionUID = 1L;

    //
    private Long userId;
    //
    private String username;
    //
    private String password;
    //
    private Integer gender;
    //
    private Date birthday;
    //
    private Date registerTime;
    //
    private Date lastLoginTime;
    //
    private String lastLoginIp;
    //
    private Integer userLevelId;
    //
    private String nickname;
    //
    private String mobile;
    //
    private String registerIp;
    //
    private String avatar;
    //
    private String weixinOpenid;
    //身份证号
    private String idcard;

	//vip等级
	private String vipGrade;
	//vplus等级
	private String vplusGrade;
	//是否是vplus
	private Integer isVplus;
	//会员成长值
	private Long membersGrowthValue;
	//vplus到期时间
	private Date vplusEndDate;
	//会员积分值
	private Integer integral;

	public String getVipGrade() {
		return vipGrade;
	}

	public void setVipGrade(String vipGrade) {
		this.vipGrade = vipGrade;
	}

	public String getVplusGrade() {
		return vplusGrade;
	}

	public void setVplusGrade(String vplusGrade) {
		this.vplusGrade = vplusGrade;
	}

	public Integer getIsVplus() {
		return isVplus;
	}

	public void setIsVplus(Integer isVplus) {
		this.isVplus = isVplus;
	}

	public Long getMembersGrowthValue() {
		return membersGrowthValue;
	}

	public void setMembersGrowthValue(Long membersGrowthValue) {
		this.membersGrowthValue = membersGrowthValue;
	}

	public Date getVplusEndDate() {
		return vplusEndDate;
	}

	public void setVplusEndDate(Date vplusEndDate) {
		this.vplusEndDate = vplusEndDate;
	}

	public Integer getIntegral() {
		return integral;
	}

	public void setIntegral(Integer integral) {
		this.integral = integral;
	}

	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Integer getGender() {
		return gender;
	}
	public void setGender(Integer gender) {
		this.gender = gender;
	}
	public Date getBirthday() {
		return birthday;
	}
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	public Date getRegisterTime() {
		return registerTime;
	}
	public void setRegisterTime(Date registerTime) {
		this.registerTime = registerTime;
	}
	public Date getLastLoginTime() {
		return lastLoginTime;
	}
	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}
	public String getLastLoginIp() {
		return lastLoginIp;
	}
	public void setLastLoginIp(String lastLoginIp) {
		this.lastLoginIp = lastLoginIp;
	}
	public Integer getUserLevelId() {
		return userLevelId;
	}
	public void setUserLevelId(Integer userLevelId) {
		this.userLevelId = userLevelId;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getRegisterIp() {
		return registerIp;
	}
	public void setRegisterIp(String registerIp) {
		this.registerIp = registerIp;
	}
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	public String getWeixinOpenid() {
		return weixinOpenid;
	}
	public void setWeixinOpenid(String weixinOpenid) {
		this.weixinOpenid = weixinOpenid;
	}
	public String getIdcard() {
		return idcard;
	}
	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}
    
    
}

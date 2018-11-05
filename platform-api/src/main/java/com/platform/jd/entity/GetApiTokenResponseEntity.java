package com.platform.jd.entity;

import java.util.Date;

/**
 * 获取token响应实体
 *
 */
public class GetApiTokenResponseEntity extends JdResponseBaseEntity {
	
	private String token;//token
	private Date time; //	当前时间
	
	
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	
	
}

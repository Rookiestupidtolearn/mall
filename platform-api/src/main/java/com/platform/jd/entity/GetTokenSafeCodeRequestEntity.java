package com.platform.jd.entity;

/**
 * 获取安全码请求实体
 *
 */
public class GetTokenSafeCodeRequestEntity {
	
	private String func;	//方法名
	private String username;	//登录用户名 (由礼管家提供)
	private String password;	//登录密码 (由礼管家提供)
	private String api_name;	//接口帐号 (由礼管家提供)
	private String api_secret; //接口密码 (由礼管家提供)
	
	
	
	public GetTokenSafeCodeRequestEntity() {
		super();
		// TODO Auto-generated constructor stub
	}
	public GetTokenSafeCodeRequestEntity(String func, String username, String password, String api_name,
			String api_secret) {
		super();
		this.func = func;
		this.username = username;
		this.password = password;
		this.api_name = api_name;
		this.api_secret = api_secret;
	}
	public String getFunc() {
		return func;
	}
	public void setFunc(String func) {
		this.func = func;
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
	public String getApi_name() {
		return api_name;
	}
	public void setApi_name(String api_name) {
		this.api_name = api_name;
	}
	public String getApi_secret() {
		return api_secret;
	}
	public void setApi_secret(String api_secret) {
		this.api_secret = api_secret;
	}
}

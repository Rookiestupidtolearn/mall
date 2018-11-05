package com.platform.jd.entity;

public class JdRequestBaseEntity {
	
	//方法名
	public static String func;
	//token
	public static String token;
	
	
	public static String getFunc() {
		return func;
	}
	public static void setFunc(String func) {
		JdRequestBaseEntity.func = func;
	}
	public static String getToken() {
		return token;
	}
	public static void setToken(String token) {
		JdRequestBaseEntity.token = token;
	}
	
	
}

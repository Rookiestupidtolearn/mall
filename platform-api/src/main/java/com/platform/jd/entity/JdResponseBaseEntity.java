package com.platform.jd.entity;

public class JdResponseBaseEntity {
	
	//结果码
	public static String result;
	//提示信息
	public static String msg;
	
	public static String getResult() {
		return result;
	}
	public static void setResult(String result) {
		JdResponseBaseEntity.result = result;
	}
	public static String getMsg() {
		return msg;
	}
	public static void setMsg(String msg) {
		JdResponseBaseEntity.msg = msg;
	}
	
	
	
}

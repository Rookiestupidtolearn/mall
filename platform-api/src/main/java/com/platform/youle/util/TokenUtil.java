package com.platform.youle.util;

import java.util.Date;

import com.github.pagehelper.util.StringUtil;

public class TokenUtil {
	
	public static void main(String[] args) {
		System.out.println(getToken());
	}
	
	/**
	 * 获取token
	 */
	public static String getToken(){
		String[] jdParam =  PropertiesUtil.getValues("jd.properties", new String[]{"wid","accessToken"});
		if(jdParam.length == 2){ //获取到value值
			String token = jdParam[0]+jdParam [1]+new Date().getTime();
			//调用MD5加密
			token = MD5util.encodeByMD5(token).toUpperCase();
			if(StringUtil.isNotEmpty(token)){
				return token;
			}
		}
		return "";
	}
}

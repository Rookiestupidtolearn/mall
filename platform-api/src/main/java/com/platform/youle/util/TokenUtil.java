package com.platform.youle.util;

import java.util.Calendar;

import com.platform.utils.StringUtils;

public class TokenUtil {

    private static String wid ;

    private static String accessToken ;

    public static String token ;

    static {

        wid = PropertiesUtil.getValue("youle.properties","wid");
        accessToken = PropertiesUtil.getValue("youle.properties","accessToken");
        token = getToken();
    }

	public static void main(String[] args) {
		System.out.println(token);
	}
	
	/**
	 * 获取token
	 */
	private static String getToken(){

	    if(StringUtils.isNullOrEmpty(token)){
            StringBuffer  tokenStr = new StringBuffer("");
            tokenStr.append(wid);
            tokenStr.append(accessToken);
            tokenStr.append(Calendar.getInstance().getTimeInMillis());
            token = MD5util.encodeByMD5(tokenStr.toString()).toUpperCase();
        }

		return token;
	}






}

package com.platform.youle.util;

import java.util.Calendar;

import com.platform.utils.StringUtils;

public class TokenUtil {

    public static String wid ;

    public static String accessToken ;

    public static String token ;
    
    public static Long currentTime;



    static {

        wid = PropertiesUtil.getValue("youle.properties","wid");
        accessToken = PropertiesUtil.getValue("youle.properties","accessToken");
        token = getToken();
    }

	public static void main(String[] args) {
		System.out.println(wid);
		System.out.println(Calendar.getInstance().getTimeInMillis() / 1000);
	}
	
	/**
	 * 获取token
	 */
	private static String getToken(){
		currentTime = Calendar.getInstance().getTimeInMillis();
	    if(StringUtils.isNullOrEmpty(token)){
            StringBuffer  tokenStr = new StringBuffer("");
            tokenStr.append(wid);
            tokenStr.append(accessToken);
            tokenStr.append(currentTime);
            token = MD5util.encodeByMD5(tokenStr.toString()).toUpperCase();
        }
		return token;
	}






}

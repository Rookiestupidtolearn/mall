package com.platform.util;

import org.apache.commons.lang.StringUtils;

public class IdcardUtils {
	
	public static boolean cardCodeVerify(String cardcode) {
        if (StringUtils.isEmpty(cardcode)) {
			return false;
		}
        if (cardcode.length()<18) {
        	return false;
		}
        //第二代身份证正则表达式(18位)
        String isIDCard2 ="^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])((\\d{4})|\\d{3}[A-Z])$";

        //验证身份证
        if (! cardcode.matches(isIDCard2)) {
            return false;
        }
	    int i = 0;
	    String r = "error";
	    String lastnumber = "";

	    i += Integer.parseInt(cardcode.substring(0, 1)) * 7;
	    i += Integer.parseInt(cardcode.substring(1, 2)) * 9;
	    i += Integer.parseInt(cardcode.substring(2, 3)) * 10;
	    i += Integer.parseInt(cardcode.substring(3, 4)) * 5;
	    i += Integer.parseInt(cardcode.substring(4, 5)) * 8;
	    i += Integer.parseInt(cardcode.substring(5, 6)) * 4;
	    i += Integer.parseInt(cardcode.substring(6, 7)) * 2;
	    i += Integer.parseInt(cardcode.substring(7, 8)) * 1;
	    i += Integer.parseInt(cardcode.substring(8, 9)) * 6;
	    i += Integer.parseInt(cardcode.substring(9, 10)) * 3;
	    i += Integer.parseInt(cardcode.substring(10,11)) * 7;
	    i += Integer.parseInt(cardcode.substring(11,12)) * 9;
	    i += Integer.parseInt(cardcode.substring(12,13)) * 10;
	    i += Integer.parseInt(cardcode.substring(13,14)) * 5;
	    i += Integer.parseInt(cardcode.substring(14,15)) * 8;
	    i += Integer.parseInt(cardcode.substring(15,16)) * 4;
	    i += Integer.parseInt(cardcode.substring(16,17)) * 2;
	    i = i % 11;
	    lastnumber =cardcode.substring(17,18);
	    if (i == 0) {
	        r = "1";
	    }
	    if (i == 1) {
	        r = "0";
	    }
	    if (i == 2) {
	        r = "x";
	    }
	    if (i == 3) {
	        r = "9";
	    }
	    if (i == 4) {
	        r = "8";
	    }
	    if (i == 5) {
	        r = "7";
	    }
	    if (i == 6) {
	        r = "6";
	    }
	    if (i == 7) {
	        r = "5";
	    }
	    if (i == 8) {
	        r = "4";
	    }
	    if (i == 9) {
	        r = "3";
	    }
	    if (i == 10) {
	        r = "2";
	    }
	    if (r.equals(lastnumber.toLowerCase())) {
	        return true;
	    }
	    return false;
	}
	
	public static void main(String[] args) {
	System.out.println(cardCodeVerify("130425198801062011"));
	}
}

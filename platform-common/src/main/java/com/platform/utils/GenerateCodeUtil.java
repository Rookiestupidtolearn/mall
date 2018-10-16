package com.platform.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

import org.apache.commons.lang.RandomStringUtils;

public class GenerateCodeUtil {

	/**
	 * 生成对应长度的验证码(数字)
	 * @param length
	 * @return
	 */
	public static String randomNumStr(int length){
		return RandomStringUtils.randomNumeric(length);
	}

	/**
	 * 生成对应长度的随机字符串（数字+字母）
	 * @param length
	 * @return
	 */
	public static String randomAlphanumeric(int length){
		return RandomStringUtils.randomAlphanumeric(length);
	}
	/**
	 * 21位长度=时间戳+4位随机数
	 * yyyyMMddHHmmssSSS+4位随机数
	 * 生成流水号
	 * @return
	 */
	public synchronized static String buildBizNo() {
		//TODO 交易流水号优化
		return getDateTimeStampSN();
	}
	/**
	 * 获取本地交易流水号，编号前缀"9"，该流水号不会传递给富友，只适合本地业务
	 * 22位数字=9+21位
	 * @return
	 */
	public synchronized static String genBizNoLocal(){
		String prefix = "9";
		return prefix.concat(buildBizNo());
	}
	/**
	 * 用户token
	 * @param prefix
	 * @return
	 */
	public synchronized static String genToken(String prefix){
		//FIXME 集群下唯一uid优化
		return prefix + "_" + getUUID();
	}



	/**
	 * 获取自定义会员uid
	 * 小写字符串:生成23位字符串，12位时间戳_10位随机数，示例 201706081228_6jixfoql3l
	 * @return
	 */
	public synchronized static String getUserUID(){
		 SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmm");
	      
		return formatter.format(new Date()) + "_" + randomAlphanumeric(10).toLowerCase();
	}

	/**
	 * 生成一个UUID数据(32位)
	 * @return
	 */
	public static String getUUID(){
		UUID uuid = UUID.randomUUID();      
	    return  uuid.toString().replace("-", "").toLowerCase();
	}
	
	/**
	 * 获取一个随机的int值(包括aStart 和 aEnd )
	 * @param aStart
	 * @param aEnd
	 * @param aRandom
	 * @return
	 */
	public static int getRandomInt(int aStart, int aEnd, Random aRandom) {
		if (aStart > aEnd) {
			throw new IllegalArgumentException("Start cannot exceed End.");
		}
		long range = (long) aEnd - (long) aStart + 1;
		long fraction = (long) (range * aRandom.nextDouble());
		int randomNumber = (int) (fraction + aStart);
		return randomNumber;
	}
	/**
	 * 获取一个时间戳+随机数(21位)
	 * @return
	 */
	public static String getDateTimeStampSN(){
		 SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String dateTime= formatter.format(new Date());
		String random = randomNumStr(4);
		return dateTime + random;
	}

	/**
	 * 获取指定长度的随机英文字母
	 * @param length
	 * @return
	 */
	public static String getRandomLetters(int length){
		String[] strs = {"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z"};
		Random random = new Random();
		String result = "";
		for(int i = 0 ; i < length; i ++){
			result = result + strs[random.nextInt(26)];
		}
		return result;
	}

	/**
	 * 获取随机昵称、用户名
	 * @return
	 */
	public static String randomUserName(){
		String[] strs = {"0","1","2","3","4","5","6","7","8","9","a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z"};
		Random random = new Random();
		String result = "";
		for(int i = 0 ; i < 9; i ++){
			result = result + strs[random.nextInt(36)];
		}
		return "R_" + result;
	}


}

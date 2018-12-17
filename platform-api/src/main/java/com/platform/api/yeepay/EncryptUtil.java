package com.platform.api.yeepay;
import java.util.TreeMap;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

import sun.misc.BASE64Decoder;

public class EncryptUtil {
    private static final String KEY = "aecdefgabovpfgdf";  
    private static final String ALGORITHMSTR = "AES/ECB/PKCS5Padding";  
    public static String base64Encode(byte[] bytes){  
        return Base64.encodeBase64String(bytes);  
    }  
    public static byte[] base64Decode(String base64Code) throws Exception{  
        return new BASE64Decoder().decodeBuffer(base64Code);  
    }  
    public static byte[] aesEncryptToBytes(String content, String encryptKey) throws Exception {  
        KeyGenerator kgen = KeyGenerator.getInstance("AES");  
        kgen.init(128);  
        Cipher cipher = Cipher.getInstance(ALGORITHMSTR);  
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(encryptKey.getBytes(), "AES"));  

        return cipher.doFinal(content.getBytes("utf-8"));  
    }  
    public static String aesEncrypt(String content, String encryptKey) throws Exception {  
        return base64Encode(aesEncryptToBytes(content, encryptKey));  
    }  
    public static String aesDecryptByBytes(byte[] encryptBytes, String decryptKey) throws Exception {  
        KeyGenerator kgen = KeyGenerator.getInstance("AES");  
        kgen.init(128);  

        Cipher cipher = Cipher.getInstance(ALGORITHMSTR);  
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(decryptKey.getBytes(), "AES"));  
        byte[] decryptBytes = cipher.doFinal(encryptBytes);  

        return new String(decryptBytes);  
    }  
   
    /*
     * 解密
     */
    public static String aesDecrypt(String encryptStr) throws Exception {  
        return aesDecryptByBytes(base64Decode(encryptStr), KEY);  
    }  


    /**
     * 测试
     * 
     */
    public static void main(String[] args) throws Exception {
    	//使用TreeMap
		TreeMap<String, Object> treeMap	= new TreeMap<String, Object>();
		treeMap.put("amount", 			1);
		treeMap.put("mobile", "13391506299");
		treeMap.put("thirdTradeNo", "123123123");
		treeMap.put("rechargeType", "2");//充值类型
		
		String content = treeMap.toString();
        System.out.println("加密前：" + content);  

        System.out.println("加密密钥和解密密钥：" + KEY);  

        String encrypt = aesEncrypt(content, KEY);  
        System.out.println(encrypt.length()+":加密后：" + encrypt);  

        String decrypt = aesDecrypt(encrypt);  
        System.out.println("解密后：" + decrypt);  
    }
}
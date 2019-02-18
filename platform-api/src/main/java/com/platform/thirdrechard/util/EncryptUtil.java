package com.platform.thirdrechard.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.binary.Base64;
import sun.misc.BASE64Decoder;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;
import java.util.TreeMap;

public class EncryptUtil {
    private static final String KEY = "qzcdefgabovpfgdf";  
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
//    	//使用TreeMap
		TreeMap<String, Object> treeMap	= new TreeMap<String, Object>();
//		treeMap.put("amount", 			1);
		treeMap.put("mobile", "15236177556");
//		treeMap.put("thirdTradeNo", "123123123332sa");
//		treeMap.put("platformType", "2");//充值类型
//		treeMap.put("cardType", "2");//充值类型
//		treeMap.put("queryCheckUrl", "http://sulaiqian.huaqianyueshang.com/qsdMallOrder/queryCardOrder");//充值类型
		JSONObject json =new JSONObject(treeMap);
		String content = json.toString();
        System.out.println("加密前：" + content);  

        System.out.println("加密密钥和解密密钥：" + KEY);  

        String encrypt = aesEncrypt(content, KEY);  
        System.out.println(encrypt.length()+":加密后：" + encrypt);  

        String decrypt = aesDecrypt(encrypt);  
        System.out.println("解密后：" + decrypt);  
    	
    //	{"amount":"359","cardType":"1","mobile":"15228131703","platformType":"suyouqian","queryCheckUrl":"http://sulaiqian.huaqianyueshang.com/qsdMallOrder/queryCardOrder","thirdTradeNo":"3g4no5h730n5xBE8Qpp3TdG09c3n5m"}

    	
//    	QueryMemberCardRequest vo = new QueryMemberCardRequest ();
//        vo.setCardTradeNbr ("3g4no5h730n5xBE8Qpp3TdG09c3n5m");
//        vo.setCardTradeType ("1");
//        vo.setUserMobile ("17732087731");
//         String  str1 = HttpCommonUtils.sendPost ("http://sulaiqian.huaqianyueshang.com/qsdMallOrder/queryCardOrder",JSONObject.toJSONString (vo));
// 		if (str1.contains("html")) {
//		System.out.println(str1);
//		}
//         JSONObject a = JSONObject.parseObject(str1);
//    	 
//    	System.out.println(str1);
    	
    }
}
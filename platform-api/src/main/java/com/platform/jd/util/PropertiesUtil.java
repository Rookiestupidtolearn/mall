package com.platform.jd.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class PropertiesUtil {
    private static Properties prop = null;
   
    /**
     *  通过key获取属性值
     * @param fileName文件名 需指定properties文件,且此properties文件需在resource目录下
     * @param key 
     * @return
     */
    public static String getValue(String fileName,String key) {
    	try {
	        prop = new Properties();
	        prop.load(PropertiesUtil.class.getClassLoader().getResourceAsStream(fileName));
	    } catch (FileNotFoundException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
    	if(null == prop){
    		return null;
    	}
        return prop.getProperty(key);
    }
}

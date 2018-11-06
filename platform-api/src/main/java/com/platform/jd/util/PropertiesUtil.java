package com.platform.jd.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class PropertiesUtil {
    private static Properties prop = null;
    
    
    /**
     * 通过key获取单个属性值
     * @param fileName 文件名 需指定properties文件,且此properties文件需在resource目录下
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
   
    /**
     *  通过key数组获取属性数组
     * @param fileName 文件名 需指定properties文件,且此properties文件需在resource目录下
     * @param key 
     * @return
     */
    public static String[] getValues(String fileName,String[] keys) {
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
    	String[] values = new String[keys.length];
    	if(keys.length > 0){
    		for(int i = 0; i < keys.length; i++){
    			values[i] = prop.getProperty(keys[i]);
    		}
    	}
        return values;
    }
    
    /**
     * 获取properties属性文件
     * @param fileName 文件名 需指定properties文件,且此properties文件需在resource目录下
     * @return Properties
     */
    public static Properties getProperties(String fileName){
    	try {
    		prop = new Properties();
			prop.load(PropertiesUtil.class.getClassLoader().getResourceAsStream(fileName));
		} catch (IOException e) {
			e.printStackTrace();
		}
    	return prop;
    }
}

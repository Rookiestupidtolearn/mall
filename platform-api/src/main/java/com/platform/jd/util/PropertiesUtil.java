package com.platform.jd.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class PropertiesUtil {
    private static Properties prop = null;
    //静态块中的内容会在类别加载的时候先被执行
    static {
    	loadProps();
    }
 
    /**
     * 通过key获取属性值
     * @param key
     * @return
     */
    public static String getValue(String key) {
    	if(null == prop){
    		loadProps();
    	}
        return prop.getProperty(key);
    }
    
    synchronized static private void loadProps(){
	    try {
	        prop = new Properties();
	        prop.load(PropertiesUtil.class.getClassLoader().getResourceAsStream("jd.properties"));
	    } catch (FileNotFoundException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
    }

}

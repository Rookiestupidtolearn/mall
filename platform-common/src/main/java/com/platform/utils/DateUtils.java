package com.platform.utils;

import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期处理
 *
 * @author lipengjun
 * @email 939961241@qq.com
 * @date 2016年12月21日 下午12:53:33
 */
public class DateUtils {
    // 日志
    private static final Logger logger = Logger.getLogger(DateUtils.class);

    /**
     * 时间格式(yyyy-MM-dd)
     */
    public final static String DATE_PATTERN = "yyyy-MM-dd";
    public final static String DATE_PATTERN_CHINA = "yyyy年MM月dd日";
    /**
     * 无分隔符日期格式 "yyyyMMddHHmmssSSS"
     */
    public static String DATE_TIME_PATTERN_YYYY_MM_DD_HH_MM_SS_SSS = "yyyyMMddHHmmssSSS";
    /**
     * 时间格式(yyyy-MM-dd HH:mm:ss)
     */
    public final static String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    // 日期转换格式数组
    public static String[][] regularExp = new String[][]{

            // 默认格式
            {"\\d{4}-((([0][1,3-9]|[1][0-2]|[1-9])-([0-2]\\d|[3][0,1]|[1-9]))|((02|2)-(([1-9])|[0-2]\\d)))\\s+([0,1]\\d|[2][0-3]|\\d):([0-5]\\d|\\d):([0-5]\\d|\\d)",
                    DATE_TIME_PATTERN},
            // 仅日期格式 年月日
            {"\\d{4}-((([0][1,3-9]|[1][0-2]|[1-9])-([0-2]\\d|[3][0,1]|[1-9]))|((02|2)-(([1-9])|[0-2]\\d)))",
                    DATE_PATTERN},
            //  带毫秒格式
            {"\\d{4}((([0][1,3-9]|[1][0-2]|[1-9])([0-2]\\d|[3][0,1]|[1-9]))|((02|2)(([1-9])|[0-2]\\d)))([0,1]\\d|[2][0-3])([0-5]\\d|\\d)([0-5]\\d|\\d)\\d{1,3}",
                    DATE_TIME_PATTERN_YYYY_MM_DD_HH_MM_SS_SSS}
    };

    public static String format(Date date) {
        return format(date, DATE_PATTERN);
    }

    public static String formatChina(Date date) {
        return format(date, DATE_PATTERN_CHINA);
    }
    public static String format(Date date, String pattern) {
        if (date != null) {
            SimpleDateFormat df = new SimpleDateFormat(pattern);
            return df.format(date);
        }
        return null;
    }

    public static String timeToStr(Long time, String pattern) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        if (time.toString().length() < 13) {
            time = time * 1000L;
        }
        Date date = new Date(time);
        String value = dateFormat.format(date);
        return value;
    }

    public static long strToTime(String timeStr) {
        Date time = strToDate(timeStr);
        return time.getTime() / 1000;
    }


    /**
     * 转换为时间类型格式
     *
     * @param strDate 日期
     * @return
     */
    public static Date strToDate(String strDate) {
        try {
            String strType = getDateFormat(strDate);
            SimpleDateFormat sf = new SimpleDateFormat(strType);
            return new Date((sf.parse(strDate).getTime()));
        } catch (Exception e) {
            return null;
        }
    }


    /**
     * 根据传入的日期格式字符串，获取日期的格式
     *
     * @return 秒
     */
    public static String getDateFormat(String date_str) {
        String style = null;
        if (StringUtils.isEmpty(date_str)) {
            return null;
        }
        boolean b = false;
        for (int i = 0; i < regularExp.length; i++) {
            b = date_str.matches(regularExp[i][0]);
            if (b) {
                style = regularExp[i][1];
            }
        }
        if (StringUtils.isEmpty(style)) {
            logger.info("date_str:" + date_str);
            logger.info("日期格式获取出错，未识别的日期格式");
        }
        return style;
    }

    /**
     * 将字符串类型的转换成Date类型
     *
     * @param dateStr
     *            字符串类型的日期 yyyy-MM-dd
     * @return Date类型的日期
     * @throws ParseException
     */
    public static Date convertStringToDate(String dateStr) {
        // 返回的日期
        Date resultDate = null;
        try {
            // 日期格式转换
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            resultDate = sdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return resultDate;
    }

    /**
     * 得到两个时间之间的差距，根据type可返回分别以天，时，分为单位的整数
     * 
     * @param d1 被减数
     * @param d2 减数
     * @param type
     *            day\minute\hour\second
     * @return
     */
    public static int getBetweenDateByType(Date d1, Date d2, String type) {
    	//.getTime()获得的是毫秒，/1000是秒，/60是分钟，/60是小时，/24是天
        long time1 = d1.getTime();
        long time2 = d2.getTime();
        long m = (time1 - time2) / 1000;
        int mSecond = Integer.parseInt(String.valueOf(m));
        int nDay = mSecond / (24 * 60 * 60);
        int nHour = mSecond/ (60 * 60);
        int nMinute = mSecond/ 60;
       
        if ("day".equals(type)) {
            return nDay;
        } else if ("minute".equals(type)) {
            return nMinute;
        } else if ("hour".equals(type)) {
            return nHour;
        } else if ("second".equals(type)) {
            return mSecond;
        }
        return nMinute;
    }
    
    /**
     * 
     * @param date
     * @param type
     *  day\minute\hour\second
     * @return
     */
    public static Date dateAddTime (Date date,int add,String type){
    	   Calendar calendar = Calendar.getInstance ();
    	   calendar.setTime(date);
        if ("day".equals(type)) {
        	 calendar.add (Calendar.DATE, add);
            return calendar.getTime();
        } else if ("minute".equals(type)) {
            calendar.add (Calendar.MINUTE, add);
            return calendar.getTime();
        } else if ("hour".equals(type)) {
        	  calendar.add (Calendar.HOUR, add);
              return calendar.getTime();
        } else if ("second".equals(type)) {
        	  calendar.add (Calendar.SECOND, add);
              return calendar.getTime();
        }
     
        return date;
    }
    
    public static void main(String[] args) {
       String starttime = "2018-12-21 22:59:59";
      Date startDate = DateUtils.strToDate(starttime);
   
      String endttime = "2018-12-21 23:59:59";
      Date endDate = DateUtils.strToDate(endttime);
      
System.out.println(DateUtils.dateAddTime(new Date(), 60, "second"));       
        

    }
}

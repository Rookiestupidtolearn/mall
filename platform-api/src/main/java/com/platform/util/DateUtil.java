package com.platform.util;

import java.util.Calendar;
import java.util.Date;

/**
 * 日期处理工具
 */
public class DateUtil {

    /**
     * time日期后+num天后的23：59：59
     * @param time
     * @param dayNum
     * @return
     */
    public static Date plusDay(Date time,int dayNum){
        Calendar ca = Calendar.getInstance();
        ca.add(Calendar.DATE, 5);// num为增加的天数，可以改变的
        ca.set(ca.get(Calendar.YEAR), ca.get(Calendar.MONTH), ca.get(Calendar.DAY_OF_MONTH),
                23, 59, 59);
        time = ca.getTime();
        return time;
    }

    public static void main(String[] args) {
       Date day= DateUtil.plusDay(new Date(),3);
        System.out.println(day);
    }

}

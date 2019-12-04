/**
 * Copyright 2019 TPRI. All Rights Reserved.
 */
package com.dekalong.gqqtmonitor.util.timeutil;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * <B>概要说明：</B><BR>
 * @author Long（Long）
 * @since 2019年2月22日
 * 
 */
public class TimeFactory {
 

	  @SuppressWarnings("unused")
	public static String timeMillisToData(long totalMilliSeconds) {
		 DateFormat dateFormatterChina = DateFormat.getDateTimeInstance(DateFormat.MEDIUM,DateFormat.MEDIUM);//格式化输出
	        TimeZone timeZoneChina = TimeZone.getTimeZone("Asia/Shanghai");//获取时区 这句加上，很关键。
	        dateFormatterChina.setTimeZone(timeZoneChina);//设置系统时区
	        long totalSeconds = totalMilliSeconds / 1000;
	        //求出现在的秒
	        long currentSecond = totalSeconds % 60;
	        //求出现在的分
	        long totalMinutes = totalSeconds / 60;
			long currentMinute = totalMinutes % 60;
	        //求出现在的小时
	        long totalHour = totalMinutes / 60;
	        long currentHour = totalHour % 24;
	        Date nowTime = new Date(System.currentTimeMillis());
	        SimpleDateFormat sdFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:dd");
	        String retStrFormatNowDate = sdFormatter.format(nowTime);
	          
	       return  retStrFormatNowDate;
	}
	  

	/**
	 * 得到当前的时间 Date
	 * @return
	 */
	public static Date getCurrentDate() {
		return str2Date(getCurrentTimeStr());
    }
	/**
	 * 得到当前的时间 字符串
	 * @return
	 */
	public static String getCurrentTimeStr() {
		return timeMillisToData(System.currentTimeMillis());
	}  
	/**
	 * 得到当前的时间 字符串 的23:59:59版本
	 * @return
	 */
	public static String getCurrentTimeStrZero() {
		String str=timeMillisToData(System.currentTimeMillis());
		str=str.substring(0, 10)+" 23:59:59";
		return str;
	}  
	public static Date str2Date(String str) {
		 try {
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
    }  
	public static String date2Str(Date date) {
		try {
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
  /**
   * 字符串补全  2019-8-09  2019-08-09
   * @param str
   * @return
   */
	public static String timeStrMachining(String str) {
		if(str.trim().length()<=10) {
			str=str.trim()+" 00:00:00";
		}
		if(str.length()==19) {
			return str;
		}else {
			return date2Str(str2Date(str));
		}
		
	}
	/**
	 * 得到指定日期之前的时间
	 * @param ctimer
	 * @param beforeTime
	 * @return
	 */
	public static String beforeTime(String ctimer,int beforeTime) {
		Calendar now = Calendar.getInstance();
		now.setTime(str2Date(ctimer));
		now.add(Calendar.DAY_OF_MONTH, -beforeTime);
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(now.getTime());
	}
	/**
	 * 得到两个日期相差的天数
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public static int differentDays(String startTime,String endTime) {
		   Calendar startCalendar = Calendar.getInstance(); 
		   startCalendar.setTime(str2Date(startTime));
	    	Calendar endCalendar = Calendar.getInstance();
	    	endCalendar.setTime(str2Date(endTime));
	    	int days = ((int) (startCalendar.getTime().getTime() / 1000) - (int) (endCalendar.getTime().getTime() / 1000)) / (60 * 60 * 24);
		return days;
	}
	
	public static void main(String[] args) {
//		System.out.println(beforeTime("2018-08-31 08:12:30",3650));
//		int days=differentDays("2018-08-31 08:12:30", "2017-08-31 08:12:30");
//		System.out.println(days);
		String str=timeStrMachining("2019-5-5 00:00:00");
		String str1=beforeTime(str, 30);
		System.out.println(str);
		System.out.println(str1);
	}
}

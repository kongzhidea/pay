package com.kk.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

	public static final String formatDefaultTimestamp = "yyyy-MM-dd HH:mm:ss.SSS";

	public static final String formatDefaultTime = "yyyy-MM-dd HH:mm:ss";

	public static final String formatDefaultDate = "yyyy-MM-dd";

    public static final String formatDefaultDateMonth = "yyyy-MM";

	public static final String formatDefaultDate2 = "yyyy.MM.dd";

	public static final String formatDefaultHour = "HH:mm:ss";
	
	public static final String formatDefaultDayAndHour = "MM-dd";
	
	private DateUtil() {}

	/**
	 * yyyy-MM-dd HH:mm:ss.SSS
	 * 
	 * @param date
	 * @return
	 */
	public static String defaultTimestamp(Date date) {
		return format(date, new SimpleDateFormat(formatDefaultTimestamp));
	}

	/**
	 * yyyy-MM-dd HH:mm:ss
	 * 
	 * @param date
	 * @return
	 */
	public static String defaultTime(Date date) {
		return format(date, new SimpleDateFormat(formatDefaultTime));
	}

    /**
	 * yyyy-MM-dd HH:mm:ss
	 *
	 * @param date
	 * @return
	 */
	public static String defaultDateMonth(Date date) {
		return format(date, new SimpleDateFormat(formatDefaultDateMonth));
	}

	/**
	 * yyyy-MM-dd HH:mm:ss
	 * 
	 * @param time
	 *            ,精确到 ms
	 * @return
	 */
	public static String defaultTime(long time) {
		return format(new Date(time), new SimpleDateFormat(formatDefaultTime));
	}

	/**
	 * yyyy-MM-dd
	 * 
	 * @param date
	 * @return
	 */
	public static String defaultDate(Date date) {
		return format(date, new SimpleDateFormat(formatDefaultDate));
	}
	
	/**
	 * yyyy-MM-dd
	 * 
	 * @param time
	 * @return
	 */
	public static String defaultDate(long time) {
		return format(new Date(time), new SimpleDateFormat(formatDefaultDate));
	}

	/**
	 * yyyy.MM.dd
	 * 
	 * @param time
	 * @return
	 */
	public static String defaultDate2(long time) {
		return format(new Date(time), new SimpleDateFormat(formatDefaultDate2));
	}

	/**
	 * HH:mm:ss
	 * 
	 * @param date
	 * @return
	 */
	public static String defaultThour(Date date) {
		return format(date, new SimpleDateFormat(formatDefaultHour));
	}

	public static String format(Date aDate, SimpleDateFormat aFormat) {
		if (aDate == null || aFormat == null) {
			return "";
		}
		synchronized (aFormat) {
			return aFormat.format(aDate);
		}
	}

	public static Date parse(String ds, SimpleDateFormat aFormat) {
		if (ds == null || aFormat == null) {
			return null;
		}
		synchronized (aFormat) {
			try {
				return aFormat.parse(ds);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			return null;
		}
	}

	public static Date parse(String ds) {
		SimpleDateFormat format = new SimpleDateFormat(formatDefaultDate);
		return parse(ds, format);
	}

	/**
	 * 当前时间
	 * 
	 * @return
	 */
	public static Date now() {
		return new Date();
	}

	/**
	 * 当前时间的unix时间戳，精确到s
	 * 
	 * @return
	 */
	public static long nowTime() {
		return System.currentTimeMillis() / 1000;
	}

	/**
	 * 明天时间,day+1
	 * 
	 * @return
	 */
	public static Date tommorrow() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, 1);
		return calendar.getTime();
	}

	/**
	 * 设置 某月某日
	 * 
	 * @param month
	 * @param day
	 * @return
	 */
	public static Date getDate(int month, int day) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.MONTH, month - 1);
		calendar.set(Calendar.DAY_OF_MONTH, day);
		return calendar.getTime();
	}

	/**
	 * 设置 时间
	 * 
	 * @param month
	 * @param day
	 * @return
	 */
	public static Date getDate(int month, int day, int hour, int minit) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.MONTH, month - 1);
		calendar.set(Calendar.DAY_OF_MONTH, day);
		calendar.set(Calendar.HOUR_OF_DAY, hour);
		calendar.set(Calendar.MINUTE, minit);
		return calendar.getTime();
	}

	/**
	 * 设置 时间
	 * 
	 * @param month
	 * @param day
	 * @return
	 */
	public static Date getDate(int year, int month, int day, int hour,
			int minit, int second) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month - 1);
		calendar.set(Calendar.DAY_OF_MONTH, day);
		calendar.set(Calendar.HOUR_OF_DAY, hour);
		calendar.set(Calendar.MINUTE, minit);
		calendar.set(Calendar.SECOND, second);
		return calendar.getTime();
	}

	/**
	 * 往后推month月
	 * 
	 * @return
	 */
	public static Date addMonth(int month) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, month);
		return calendar.getTime();
	}

	public static Date addMonth(Date date, int month) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);		
		calendar.add(Calendar.MONTH, month);
		return calendar.getTime();
	}

	/**
	 * 往后推day日
	 * 
	 * @return
	 */
	public static Date addDay(int day) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, day);
		return calendar.getTime();
	}

	public static Date addDay(Date date,int day) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, day);
		return calendar.getTime();
	}

	public static String parseSeconds(long miliseconds){
		long second = miliseconds / 1000;
		long day = second / 3600 / 24;
		long hour = second / 3600 % 24;
		long minute = second / 60 % 60;
		long sec = second % 60;

		StringBuilder buffer = new StringBuilder();
		if (day > 0) {
			buffer.append(day).append("天");
		}
		if (hour > 0){
			buffer.append(hour).append("时");
		}
		buffer.append(minute).append("分").append(sec).append("秒");
		return buffer.toString();
	}
	
	/**
	 * 取当天0点时间
	 * 
	 * @return
	 */
	public static Date getMorning() {
		return getMorning(new Date());
	}

	/**
	 * 取0点时间
	 * 
	 * @param date
	 * @return
	 */
	public static Date getMorning(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}
	
	/**
	 * 取明天0点时间
	 * 
	 * @return
	 */
	public static Date getNextMorning() {
		return getNextMorning(new Date());
	}

	/**
	 * 取第二天0点时间 
	 * 
	 * @param date
	 * @return
	 */
	public static Date getNextMorning(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.MILLISECOND, 0);
		cal.add(Calendar.DATE, 1);
		return cal.getTime();
	}

	/**
	 * 取本月第一天0点时间
	 * 
	 * @return
	 */
	public static Date getFirstDayMorningOfMonth() {
	    return getFirstDayMorningOfMonth(new Date());
	}

	/**
	 * 取当月第一天0点时间
	 * 
	 * @param date
	 * @return
	 */
	public static Date getFirstDayMorningOfMonth(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.MILLISECOND, 0);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		return cal.getTime();
	}

	/**
	 * 取昨天0点时间
	 * 
	 * @return
	 */
	public static Date getPreviousDayMorning() {
	    return getPreviousDayMorning(new Date());
	}

	/**
	 * 取前一天0点时间
	 * 
	 * @param date
	 * @return
	 */
	public static Date getPreviousDayMorning(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.MILLISECOND, 0);
		cal.add(Calendar.DATE, -1);
		return cal.getTime();
	}

	/**
	 * 计算两个日期之间相差的天数
	 *
	 * @param smdate 较小的时间
	 * @param bdate  较大的时间
	 * @return 相差天数
	 * @throws ParseException
	 */
	public static int daysBetween(Date smdate, Date bdate) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			smdate = sdf.parse(sdf.format(smdate));
			bdate = sdf.parse(sdf.format(bdate));
			Calendar cal = Calendar.getInstance();
			cal.setTime(smdate);
			long time1 = cal.getTimeInMillis();
			cal.setTime(bdate);
			long time2 = cal.getTimeInMillis();
			long between_days = (time2 - time1) / (1000 * 3600 * 24);

			return Integer.parseInt(String.valueOf(between_days));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return -1;
	}

}

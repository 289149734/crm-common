/**
 * 
 */
package com.sjy.util;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @copyright(c) Copyright SJY Corporation 2016.
 * 
 * @since 2016年12月20日
 * @author liyan
 * @e-mail 289149734@qq.com
 * 
 */
public class DateUtils extends org.apache.commons.lang.time.DateUtils {
	private static Map<String, ThreadLocal<SimpleDateFormat>> dateFormats = new HashMap<String, ThreadLocal<SimpleDateFormat>>();
	private static Map<String, ThreadLocal<DecimalFormat>> numberFormats = new HashMap<String, ThreadLocal<DecimalFormat>>();
	public final static String START_TIME = "start_time";
	public final static String END_TIME = "end_time";
	
	/**FORMAT:yyyy-MM-dd*/
	public final static String DATE_FORMAT="yyyy-MM-dd";
	/**FORMAT:yyyyMMdd*/
	public static final String DATE_FORMAT_STR = "yyyyMMdd";
	/**FORMAT:yyyy-MM-dd HH:mm:ss*/
	public final static String DATETIME_FORMAT="yyyy-MM-dd HH:mm:ss";
	/**FORMAT:yyyyMMddHHmmss*/
    public static final String DATETIME_FORMAT_STR = "yyyyMMddHHmmss";
	// 以毫秒表示的时间
	private static final long DAY_IN_MILLIS = 24 * 3600 * 1000;
	private static final long HOUR_IN_MILLIS = 3600 * 1000;
	private static final long MINUTE_IN_MILLIS = 60 * 1000;
	private static final long SECOND_IN_MILLIS = 1000;

	public static String format(Date d, String pattern) {
		DateFormat format = new SimpleDateFormat(pattern);
		return format.format(d);
	}

	public static String format(Object obj, String format) {
		if (obj == null)
			return null;
		if (obj instanceof Date)
			return formatDate(format, (Date) obj);
		if (obj instanceof Number)
			return formatNumber(format, ((Number) obj).doubleValue());
		return obj.toString();
	}

	public static String formatNumber(String pattern, double number) {
		DecimalFormat format = getNumberFormatter(pattern);
		return format.format(number);
	}

	private static DecimalFormat getNumberFormatter(final String format) {
		ThreadLocal<DecimalFormat> formatLocal = numberFormats.get(format);
		if (formatLocal == null) {
			formatLocal = new ThreadLocal<DecimalFormat>() {
				protected DecimalFormat initialValue() {
					return new DecimalFormat(format);
				}
			};
			numberFormats.put(format, formatLocal);
		}
		return formatLocal.get();
	}

	/**
	 * 可以临时指定一个format
	 * 
	 * @param format
	 * @param date
	 * @return
	 */
	public static String formatDate(String format, Date date) {
		if (date == null)
			return null;
		SimpleDateFormat formatter = getDateFormatter(format);
		return formatter.format(date);
	}

	private static SimpleDateFormat getDateFormatter(final String format) {
		ThreadLocal<SimpleDateFormat> formatLocal = dateFormats.get(format);
		if (formatLocal == null) {
			formatLocal = new ThreadLocal<SimpleDateFormat>() {
				protected SimpleDateFormat initialValue() {
					return new SimpleDateFormat(format);
				}
			};
			dateFormats.put(format, formatLocal);
		}
		return formatLocal.get();
	}

	public static Date parse(String str, String pattern) {
		DateFormat format = new SimpleDateFormat(pattern);
		try {
			return format.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String getCurrentDateAndTime() {
		DateFormat format = new SimpleDateFormat(DateUtils.DATETIME_FORMAT);
		return format.format(new Date());
	}

	/**
	 * 需要重星期一开始计算
	 * 
	 * @param d
	 * @return
	 */
	public static Date getFirstDayOfWeek(Date d) {
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		c.set(Calendar.DAY_OF_WEEK, c.getActualMinimum(Calendar.DAY_OF_WEEK) + 1);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		return c.getTime();
	}

	/**
	 * 需要最后一天是星期天
	 * 
	 * @param d
	 * @return
	 */
	public static Date getLastDayOfWeek(Date d) {
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		c.set(Calendar.DAY_OF_WEEK, c.getActualMaximum(Calendar.DAY_OF_WEEK));
		c.set(Calendar.HOUR_OF_DAY, 23);
		c.set(Calendar.MINUTE, 59);
		c.set(Calendar.SECOND, 59);
		c.set(Calendar.MILLISECOND, 999);
		return getNextDay(c.getTime());
	}

	public static Date getFirstDayOfMonth(Date d) {
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		c.set(Calendar.DAY_OF_MONTH, c.getActualMinimum(Calendar.DAY_OF_MONTH));
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		return c.getTime();
	}

	public static Date getLastDayOfMonth(Date d) {
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
		c.set(Calendar.HOUR_OF_DAY, 23);
		c.set(Calendar.MINUTE, 59);
		c.set(Calendar.SECOND, 59);
		c.set(Calendar.MILLISECOND, 999);
		return c.getTime();
	}

	public static Date getFirstDayOfYear(Date d) {
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		c.set(Calendar.DAY_OF_YEAR, c.getActualMinimum(Calendar.DAY_OF_YEAR));
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		return c.getTime();
	}

	public static Date getLastDayOfYear(Date d) {
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		c.set(Calendar.DAY_OF_YEAR, c.getActualMaximum(Calendar.DAY_OF_YEAR));
		c.set(Calendar.HOUR_OF_DAY, 23);
		c.set(Calendar.MINUTE, 59);
		c.set(Calendar.SECOND, 59);
		c.set(Calendar.MILLISECOND, 999);
		return c.getTime();
	}

	public static Date getPreviousDay(Date d) {
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		c.set(Calendar.DATE, c.get(Calendar.DATE) - 1);
		return c.getTime();
	}

	public static Map<String, Date> getYesterday() {
		Map<String, Date> map = new HashMap<String, Date>();
		Date currentDate = new Date();
		map.put(START_TIME, parse(format(getPreviousDay(currentDate), "yyyyMMdd") + "000000", "yyyyMMddHHmmss"));
		map.put(END_TIME, parse(format(currentDate, "yyyyMMdd") + "000000", "yyyyMMddHHmmss"));
		return map;
	}

	public static Map<String, Date> getDay(Date currentDate) {
		Map<String, Date> map = new HashMap<String, Date>();
		map.put(START_TIME, parse(format(currentDate, "yyyyMMdd") + "000000", "yyyyMMddHHmmss"));
		map.put(END_TIME, parse(format(getNextDay(currentDate), "yyyyMMdd") + "000000", "yyyyMMddHHmmss"));
		return map;
	}

	public static Date getNextDay(Date d) {
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		c.set(Calendar.DATE, c.get(Calendar.DATE) + 1);
		return c.getTime();
	}

	public static Date getPreviousNDay(Date d, int n) {
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		c.set(Calendar.DATE, c.get(Calendar.DATE) - n);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		return c.getTime();
	}

	public static Date getNextNDay(Date d, int n) {
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		c.set(Calendar.DATE, c.get(Calendar.DATE) + n);
		c.set(Calendar.HOUR_OF_DAY, 23);
		c.set(Calendar.MINUTE, 59);
		c.set(Calendar.SECOND, 59);
		c.set(Calendar.MILLISECOND, 999);
		return c.getTime();
	}

	public static final Date truncateToMonth(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		calendar.clear();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month);
		return calendar.getTime();
	}

	public static final Date truncateToYear(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int year = calendar.get(Calendar.YEAR);
		calendar.clear();
		calendar.set(Calendar.YEAR, year);
		return calendar.getTime();
	}

	public static String getNextMonthDay(Date date, String pattern) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(getDayEndTime(date));
		int month = calendar.get(Calendar.MONTH);
		calendar.set(Calendar.MONTH, month + 1);
		return format(calendar.getTime(), pattern);
	}

	/**
	 * 得到每日结束时间
	 * 
	 * @param dateStr
	 *            yyyy-mm-dd
	 * @param duration
	 * @return
	 */
	public static Date getDayEndTime(Date date) {
		if (date == null)
			return null;
		else {
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			return cal.getTime();
		}
	}

	public static Date getNextMonthDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(getDayEndTime(date));
		int month = calendar.get(Calendar.MONTH);
		calendar.set(Calendar.MONTH, month + 1);
		return calendar.getTime();
	}

	public static String getNextSeasonDay(Date date, String pattern) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(getDayEndTime(date));
		int month = calendar.get(Calendar.MONTH);
		calendar.set(Calendar.MONTH, month + 3);
		return format(calendar.getTime(), pattern);
	}

	public static Date getNextSeasonDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(getDayEndTime(date));
		int month = calendar.get(Calendar.MONTH);
		calendar.set(Calendar.MONTH, month + 3);
		return calendar.getTime();
	}

	public static String getNextYearDay(Date date, String pattern) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(getDayEndTime(date));
		int month = calendar.get(Calendar.MONTH);
		int year = calendar.get(Calendar.YEAR);
		calendar.set(Calendar.MONTH, month + 1);
		calendar.set(Calendar.YEAR, year + 1);
		return format(calendar.getTime(), pattern);
	}

	public static Date getNextYearDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(getDayEndTime(date));
		int month = calendar.get(Calendar.MONTH);
		int year = calendar.get(Calendar.YEAR);
		calendar.set(Calendar.MONTH, month + 1);
		calendar.set(Calendar.YEAR, year + 1);
		return calendar.getTime();
	}

	public static String getWeekOfDate(Date dt) {
		String[] weekDays = { "01", "02", "03", "04", "05", "06", "07" };
		Calendar cal = Calendar.getInstance();
		cal.setTime(dt);
		int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if (w < 0)
			w = 0;
		return weekDays[w];
	}

	public static int getDayOfMonth(Date dt) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(dt);
		return cal.get(Calendar.DAY_OF_MONTH);
	}

	public static int getWeekOfDateNew(Date dt) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(dt);
		return cal.get(Calendar.DAY_OF_WEEK);
	}

	public static String getWeek(Date dt) {
		String[] weekDays = { "07", "01", "02", "03", "04", "05", "06" };
		Calendar cal = Calendar.getInstance();
		cal.setTime(dt);
		int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if (w < 0)
			w = 0;
		return weekDays[w];
	}

	public static long intervalSeconds(Date start, Date end) {
		long t1 = start == null ? 0 : start.getTime();
		long t2 = end.getTime();
		return (t2 - t1) / 1000;
	}

	public static boolean isSameDay(Date d1, Date d2) {
		String d1Str = format(d1, "yyyyMMdd");
		String d2Str = format(d2, "yyyyMMdd");
		return d1Str.equals(d2Str);
	}

	public static Date getToadyStart() {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		return c.getTime();
	}

	public static Date getToadyEnd() {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.HOUR_OF_DAY, 23);
		c.set(Calendar.MINUTE, 59);
		c.set(Calendar.SECOND, 59);
		c.set(Calendar.MILLISECOND, 999);
		return c.getTime();
	}

	public static Date getBeginDate(Date d) {
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		return c.getTime();
	}

	public static Date getEndDate(Date d) {
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		c.set(Calendar.HOUR_OF_DAY, 23);
		c.set(Calendar.MINUTE, 59);
		c.set(Calendar.SECOND, 59);
		c.set(Calendar.MILLISECOND, 999);
		return c.getTime();
	}

	public static Date getYesterdayStart() {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.DATE, c.get(Calendar.DATE) - 1);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		return c.getTime();
	}

	public static boolean checkInToday(Date d) {
		Date c = new Date();
		if (d != null) {
			return format(d, "yyyyMMdd").equals(format(c, "yyyyMMdd"));
		}
		return false;
	}

	public static Date getDateByS(Date date, int s) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.SECOND, s);
		return c.getTime();
	}

	/**
	 * 判断是否月末
	 * 
	 * @param d
	 * @return
	 */
	public static boolean isMonthEnd(Date d) {
		System.out.println(d);
		Date nd = org.apache.commons.lang.time.DateUtils.addDays(d, 1);
		System.out.println(format(nd, "yyyyMMdd"));
		Calendar cal = Calendar.getInstance();
		cal.setTime(nd);
		System.out.println(format(cal.getTime(), "yyyyMMdd"));
		if (cal.get(Calendar.DATE) == 1)
			return true;
		return false;
	}

	/**
	 * 判断是否季末
	 * 
	 * @param d
	 * @return
	 */
	public static boolean isSeasonEnd(Date d) {
		return false;
	}

	/**
	 * 判断是否年末
	 * 
	 * @param d
	 * @return
	 */
	public static boolean isYearEnd(Date d) {
		return false;
	}

	/**
	 * 指定日历的毫秒数
	 * 
	 * @param cal
	 *            指定日历
	 * @return 指定日历的毫秒数
	 */
	public static long getMillis(Calendar cal) {
		return cal.getTime().getTime();
	}

	/**
	 * 计算两个时间之间的差值，根据标志的不同而不同
	 * 
	 * @param flag
	 *            计算标志，表示按照年/月/日/时/分/秒等计算
	 * @param calSrc
	 *            减数
	 * @param calDes
	 *            被减数
	 * @return 两个日期之间的差值
	 */
	public static int dateDiff(char flag, Calendar calSrc, Calendar calDes) {

		long millisDiff = getMillis(calSrc) - getMillis(calDes);

		if (flag == 'y') {
			return (calSrc.get(Calendar.YEAR) - calDes.get(Calendar.YEAR));
		}

		if (flag == 'd') {
			return (int) (millisDiff / DAY_IN_MILLIS);
		}

		if (flag == 'h') {
			return (int) (millisDiff / HOUR_IN_MILLIS);
		}

		if (flag == 'm') {
			return (int) (millisDiff / MINUTE_IN_MILLIS);
		}

		if (flag == 's') {
			return (int) (millisDiff / SECOND_IN_MILLIS);
		}

		return 0;
	}

	public static Date getNextTime(Date date, int type, int n) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		if (type == Calendar.DATE) {
			c.set(Calendar.DATE, c.get(Calendar.DATE) + n);
		} else if (type == Calendar.HOUR_OF_DAY) {
			c.set(Calendar.HOUR_OF_DAY, c.get(Calendar.HOUR_OF_DAY) + n);
		} else if (type == Calendar.MINUTE) {
			c.set(Calendar.MINUTE, c.get(Calendar.MINUTE) + n);
		} else if (type == Calendar.SECOND) {
			c.set(Calendar.SECOND, c.get(Calendar.SECOND) + n);
		} else if (type == Calendar.MILLISECOND) {
			c.set(Calendar.MILLISECOND, c.get(Calendar.MILLISECOND) + n);
		}
		return c.getTime();
	}
}

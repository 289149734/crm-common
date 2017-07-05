package com.sjy.table.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import com.sjy.exception.CrmException;

/**
 * @author administrator
 * 
 */
public abstract class FormatUtil {

	final static String dateFormat = "yyyy-MM-dd";

	final static String timeFormat = "yyyy-MM-dd HH:mm:ss";

	final static String timeOnlyFormat = "HH:mm:ss";

	final static String minFormat = "yyyy-MM-dd HH:mm";

	final static String currencyFormat = "#0.00";
	private static Map<String, ThreadLocal<SimpleDateFormat>> dateFormats = new HashMap<String, ThreadLocal<SimpleDateFormat>>();
	private static Map<String, ThreadLocal<DecimalFormat>> numberFormats = new HashMap<String, ThreadLocal<DecimalFormat>>();

	// 统计时段
	public static final String DURATION_DAY = "day";
	public static final String DURATION_MONTH = "month";

	public static final String DURATION_YEAR = "year";
	static final long DAYMILISECONDS = 24 * 3600 * 1000;
	static final long HOURMILISECONDS = 3600 * 1000;
	static final long MINUTEMILISECONDS = 60 * 1000;
	public static final Class<?>[] INT = new Class<?>[] { int.class };

	/**
	 * 把Calendar清空到00:00:00 000
	 * 
	 * @param cal
	 */
	public static void clearHours(Calendar cal) {
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
	}

	/**
	 * 把日期清空到分钟
	 * 
	 * @param cal
	 */
	public static Date getMinuteOfTime(Date datetime) {
		return new Date(((datetime.getTime()) / MINUTEMILISECONDS)
				* MINUTEMILISECONDS);
	}

	/**
	 * 把日期清空到小时
	 * 
	 * @param cal
	 */
	public static Date getHourOfTime(Date datetime) {
		return new Date(((datetime.getTime()) / HOURMILISECONDS)
				* HOURMILISECONDS);
	}

	/**
	 * 把日期清空到天
	 * 
	 * @param cal
	 */
	public static Date getDayOfTime(Date datetime) {
		return new Date(
				((datetime.getTime() + DAYMILISECONDS / 3) / DAYMILISECONDS)
						* DAYMILISECONDS - DAYMILISECONDS / 3);
	}

	public static String format(String format, Object obj) {
		if (obj == null)
			return null;
		if (obj instanceof Date)
			return formatDate(format, (Date) obj);
		if (obj instanceof Number)
			return formatNumber(format, ((Number) obj).doubleValue());
		return obj.toString();
	}

	public static String formatDate(Date date) {

		return formatDate(dateFormat, date);
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

	/**
	 * 将longValue格式化为.00的字符串
	 * 
	 * @param doubleValue
	 * @return
	 */
	public static String formatDouble(double doubleValue) {
		return formatNumber(currencyFormat, doubleValue);
	}

	/**
	 * 将longValue格式化为.00的字符串
	 * 
	 * @param longValue
	 * @return
	 */
	public static String longToCurrency(long longValue) {
		if (longValue == 0)
			return "0";
		return formatDouble(longValue / 100.0);
	}

	public static String formatNumber(String pattern, double number) {
		DecimalFormat format = getNumberFormatter(pattern);
		return format.format(number);

	}

	/**
	 * 得到统计的开始时间
	 * 
	 * @param dateStr
	 *            yyyy-mm-dd
	 * @param duration
	 * @return
	 */
	public static String formatStatBeginDate(String dateStr, String duration) {

		if (DURATION_DAY.equals(duration)) {
			// 当天
			return dateStr;

		} else if (DURATION_MONTH.equals(duration)) {
			// 本月第一天

			Date date = parseDate(dateFormat, dateStr);
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			cal.set(Calendar.DAY_OF_MONTH, 1);

			return formatDate(dateFormat, cal.getTime());

		} else if (DURATION_YEAR.equals(duration)) {
			// 本年第一天

			Date date = parseDate(dateFormat, dateStr);
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			cal.set(Calendar.DAY_OF_YEAR, 1);

			return formatDate(dateFormat, cal.getTime());

		} else
			throw new CrmException("不能识别的时段" + duration);
	}

	/**
	 * 得到统计的结束时间
	 * 
	 * @param dateStr
	 * @param duration
	 * @return
	 */
	public static String formatStatEndDate(String dateStr, String duration) {
		if (DURATION_DAY.equals(duration)) {
			return dateStr;
		} else if (DURATION_MONTH.equals(duration)) {
			// 本月最后一天

			Date date = parseDate(dateFormat, dateStr);
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			cal.add(Calendar.MONTH, 1);
			cal.set(Calendar.DAY_OF_MONTH, 0);

			return formatDate(dateFormat, cal.getTime());

		} else if (DURATION_YEAR.equals(duration)) {
			// 本年第一天的前一天
			Date date = parseDate(dateFormat, dateStr);
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			cal.add(Calendar.YEAR, 1);
			cal.set(Calendar.DAY_OF_YEAR, 0);

			return formatDate(dateFormat, cal.getTime());

		} else
			throw new CrmException("不能识别的时段" + duration);
	}

	public static String formatTime(Date date) {
		return formatDate(timeFormat, date);
	}

	/**
	 * 格式化时间 1天2小时3分
	 * 
	 * @param time
	 * @return
	 */
	public static String formatTimeDuration(long time) {
		StringBuffer result = new StringBuffer();
		long days = time / (24 * 60 * 60 * 1000);
		if (days > 0) {
			result.append(days);
			result.append("天");
		}

		time = time % (24 * 60 * 60 * 1000);
		long hours = time / (60 * 60 * 1000);
		if (hours > 0) {
			result.append(hours);
			result.append("小时");
		}

		time = time % (60 * 60 * 1000);
		long minutes = time / (60 * 1000);
		if (minutes > 0) {
			result.append(minutes);
			result.append("分");
		}

		time = time % (60 * 1000);
		long seconds = time / (1000);
		if (seconds > 0) {
			result.append(seconds);
			result.append("秒");
		}

		if (result.length() == 0)
			result.append("0秒");

		return result.toString();
	}

	public static String formatTimeOnly(Date date) {
		return formatDate(timeOnlyFormat, date);
	}

	public static String getCurrentDate() {
		return formatDate(dateFormat, new Date());
	}

	/**
	 * 返回系统当前时间，时间格式是:yyyy-MM-dd HH:mm:ss
	 * 
	 * @return
	 */
	public static String getCurrentTime() {
		return formatDate(timeFormat, new Date());
	}

	/**
	 * 得到每日开始时间
	 * 
	 * @param dateStr
	 *            yyyy-mm-dd
	 * @param duration
	 * @return
	 */
	public static Date getDayBeginTime(Date date) {
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

	public static Date getDayDiff(int diff) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, diff);
		return cal.getTime();
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
			cal.add(Calendar.DATE, 1);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);

			return cal.getTime();

		}
	}

	/**
	 * 取得一个日期的字符串值,格式为2003-7-3
	 * 
	 * @param i
	 *            : i=-1,表示去昨天的值;i=0,表示取今天的值;i=1,表示取明天的值
	 * @return
	 */
	public static String getFormatDateStr(int i) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, i);
		return formatDate(dateFormat, calendar.getTime());
	}

	/**
	 * 查询条件使用，将一个字符串格式化成模糊查询所需的格式
	 * 
	 * @param s
	 * @return
	 */
	public static String getLikeString(String s) {
		if (s == null)
			return "%";
		else
			return "%" + s.trim() + "%";
	}

	/**
	 * 解析从OGNL返回值的boolean 1. 如果value是Boolean,返回Boolean.booleanValue 2. 如果value
	 * not null,返回true 3. 否则返回false
	 * 
	 * @param value
	 * @return
	 */
	public static boolean getOgnlBool(Object value) {
		if (value instanceof Boolean)
			return ((Boolean) value).booleanValue();
		else if (value != null)
			return true;
		else
			return false;

	}

	public static Date getYesterday() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);
		return cal.getTime();
	}

	/**
	 * 判断一个集合是否是有get(i)方法,velocity模板用
	 * 
	 * @param c
	 * @return
	 */
	public static boolean isIndexedCollection(Collection<?> c) {
		try {
			c.getClass().getMethod("get", INT);
			return true;
		} catch (NoSuchMethodException nome) {
			return false;
		}
	}

	public static Date parseDate(String s) {
		if (s == null || s.trim().equals(""))
			return null;
		s = s.trim();

		if (s.indexOf(" ") > 0) {
			return parseDate(timeFormat, s);
		} else
			return parseDate(dateFormat, s);

	}

	public static Date parseDate(String format, String s) {
		if (s == null || s.trim().equals(""))
			return null;
		s = s.trim();

		SimpleDateFormat formatter = getDateFormatter(format);

		try {
			return formatter.parse(s);
		} catch (ParseException e) {
			throw new CrmException("dateformat must be " + format);
		}
	}

	public static Date parseDateBegin(String s) {
		if (s == null || s.trim().equals(""))
			return null;
		try {
			s = s + " 00:00:00";
			return parseDate(timeFormat, s);
		} catch (Exception e) {
			throw new CrmException(e);
		}
	}

	public static Date parseDateEnd(String s) {
		if (s == null || s.trim().equals(""))
			return null;

		Date date = parseDate(dateFormat, s);
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, 1);

		return cal.getTime();

	}

	public static List<Integer> parseIntListFromString(String arrStr) {
		StringTokenizer st = new StringTokenizer(arrStr, ",");
		List<Integer> result = new ArrayList<Integer>(st.countTokens());
		while (st.hasMoreTokens()) {
			result.add(new Integer(Integer.parseInt(st.nextToken())));
		}
		return result;
	}

	/**
	 * 消除字符串中的空格
	 * 
	 * @param text
	 */
	public static String shrinkStr(String text) {
		if (text == null)
			return null;
		text = text.trim();

		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < text.length(); i++) {
			char c = text.charAt(i);
			if (!Character.isSpaceChar(c))
				sb.append(c);
		}

		return sb.toString();
	}

	/**
	 * 将金额由小写改成大写
	 * 
	 * @param smallCurrency
	 * @return
	 */
	public static String smallToBig(double smallCurrency) {
		String numChr = null;
		String lctChr = null;
		String bigStr = "";
		NumberFormat formatter = new DecimalFormat("#.00");
		String cvtCurrency = formatter.format(smallCurrency);
		int nendInner = cvtCurrency.indexOf(".");
		int len = 0;
		len = cvtCurrency.length();
		int dotLen = len - nendInner - 1;
		if (dotLen == 0) {
			cvtCurrency = String.valueOf(String.valueOf(cvtCurrency)).concat(
					".00");
			dotLen = 2;
			len += 3;
		}
		if (dotLen == 1) {
			cvtCurrency = String.valueOf(String.valueOf(cvtCurrency)).concat(
					"0");
			dotLen = 2;
			len++;
		}
		if (dotLen > 2) {
			cvtCurrency = String.valueOf((double) Math
					.round(smallCurrency * 100D) / 100D);
			dotLen = 2;
			len = cvtCurrency.length();
		}
		int nn = 0;
		if (len > 0) {
			int exNumber = 0;
			for (int i = len - 2 - dotLen; i > -dotLen - 1; i--) {
				if (i == -1)
					nn++;
				int num = Integer.parseInt(cvtCurrency.substring(nn, nn + 1));
				switch (num) {
				case 1: // '\001'
					numChr = "\u58F9";
					break;

				case 2: // '\002'
					numChr = "\u8D30";
					break;

				case 3: // '\003'
					numChr = "\u53C1";
					break;

				case 4: // '\004'
					numChr = "\u8086";
					break;

				case 5: // '\005'
					numChr = "\u4F0D";
					break;

				case 6: // '\006'
					numChr = "\u9646";
					break;

				case 7: // '\007'
					numChr = "\u67D2";
					break;

				case 8: // '\b'
					numChr = "\u634C";
					break;

				case 9: // '\t'
					numChr = "\u7396";
					break;

				case 0: // '\0'
					numChr = "\u96F6";
					break;
				}
				// if(num != 0)
				switch (i) {
				case -3:
					lctChr = "\u5398";
					break;

				case -2:
					lctChr = "\u5206";
					break;

				case -1:
					lctChr = "\u89D2";
					break;

				case 0: // '\0'
					lctChr = "\u5143";
					break;

				case 1: // '\001'
					lctChr = "\u62FE";
					break;

				case 2: // '\002'
					lctChr = "\u4F70";
					break;

				case 3: // '\003'
					lctChr = "\u5343";
					break;

				case 4: // '\004'
					lctChr = "\u4E07";
					break;

				case 5: // '\005'
					lctChr = "\u62FE";
					break;

				case 6: // '\006'
					lctChr = "\u4F70";
					break;

				case 7: // '\007'
					lctChr = "\u5343";
					break;

				case 8: // '\b'
					lctChr = "\u4EBF";
					break;

				case 9: // '\t'
					lctChr = "\u62FE";
					break;

				case 10: // '\n'
					lctChr = "\u4F70";
					break;

				case 11: // '\013'
					lctChr = "\u5343";
					break;
				}
				if (num != 0) {
					bigStr = String.valueOf(String.valueOf(bigStr))
							+ String.valueOf(String.valueOf(String.valueOf(
									String.valueOf(numChr)).concat(lctChr)));
				} else if (exNumber != 0) {
					bigStr = String.valueOf(String.valueOf(bigStr))
							+ String.valueOf(numChr);
				}
				numChr = "";
				lctChr = "";
				exNumber = num;
				nn++;
			}

		}
		for (int i = len; i < 8; i++)
			bigStr = String.valueOf(String.valueOf(bigStr));

		return bigStr;
	}

	public static String trimToMin(Date date) {
		if (date == null)
			return null;
		return formatDate(minFormat, date);
	}

	/**
	 * 替换${}
	 * 
	 * @param template
	 * @param params
	 * @return
	 */
	public static String replaceAll(String template, Map<String, Object> params) {
		// 生成匹配模式的正则表达式
		String patternString = "\\$\\{("
				+ StringUtils.join(params.keySet(), "|") + ")\\}";

		Pattern pattern = Pattern.compile(patternString);
		Matcher matcher = pattern.matcher(template);

		// 两个方法：appendReplacement, appendTail
		StringBuffer sb = new StringBuffer();
		while (matcher.find()) {
			matcher.appendReplacement(sb, params.get(matcher.group(1))
					.toString());
		}
		matcher.appendTail(sb);
		return sb.toString();
	}
}
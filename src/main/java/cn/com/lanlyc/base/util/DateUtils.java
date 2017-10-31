package cn.com.lanlyc.base.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * 对数据相关的工具包
 * 
 * @author
 */
public class DateUtils {
	

	public static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static final SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
	

	/**
	 * 变量：日期格式化类型 - 格式:yyyy/MM/dd
	 */
	public static final int DEFAULT = 0;
	public static final int YM = 1;

	/**
	 * 变量：日期格式化类型 - 格式:yyyy-MM-dd
	 * 
	 */
	public static final int YMR_SLASH = 11;

	/**
	 * 变量：日期格式化类型 - 格式:yyyyMMdd
	 * 
	 */
	public static final int NO_SLASH = 2;

	/**
	 * 变量：日期格式化类型 - 格式:yyyyMM
	 * 
	 */
	public static final int YM_NO_SLASH = 3;

	/**
	 * 变量：日期格式化类型 - 格式:yyyy/MM/dd HH:mm:ss
	 * 
	 */
	public static final int DATE_TIME = 4;

	/**
	 * 变量：日期格式化类型 - 格式:yyyyMMddHHmmss
	 * 
	 */
	public static final int DATE_TIME_NO_SLASH = 5;

	/**
	 * 变量：日期格式化类型 - 格式:yyyy/MM/dd HH:mm
	 * 
	 */
	public static final int DATE_HM = 6;

	/**
	 * 变量：日期格式化类型 - 格式:HH:mm:ss
	 * 
	 */
	public static final int TIME = 7;

	/**
	 * 变量：日期格式化类型 - 格式:HH:mm
	 * 
	 */
	public static final int HM = 8;

	/**
	 * 变量：日期格式化类型 - 格式:HHmmss
	 * 
	 */
	public static final int LONG_TIME = 9;
	/**
	 * 变量：日期格式化类型 - 格式:HHmm
	 * 
	 */

	public static final int SHORT_TIME = 10;

	/**
	 * 变量：日期格式化类型 - 格式:yyyy-MM-dd HH:mm:ss
	 */
	public static final int DATE_TIME_LINE = 12;

	public static String dateToStr(Date date, int type) {
		switch (type) {
		case DEFAULT:
			return dateToStr(date);
		case YM:
			return dateToStr(date, "yyyy/MM");
		case NO_SLASH:
			return dateToStr(date, "yyyyMMdd");
		case YMR_SLASH:
			return dateToStr(date, "yyyy-MM-dd");
		case YM_NO_SLASH:
			return dateToStr(date, "yyyyMM");
		case DATE_TIME:
			return dateToStr(date, "yyyy/MM/dd HH:mm:ss");
		case DATE_TIME_NO_SLASH:
			return dateToStr(date, "yyyyMMddHHmmss");
		case DATE_HM:
			return dateToStr(date, "yyyy/MM/dd HH:mm");
		case TIME:
			return dateToStr(date, "HH:mm:ss");
		case HM:
			return dateToStr(date, "HH:mm");
		case LONG_TIME:
			return dateToStr(date, "HHmmss");
		case SHORT_TIME:
			return dateToStr(date, "HHmm");
		case DATE_TIME_LINE:
			return dateToStr(date, "yyyy-MM-dd HH:mm:ss");
		default:
			throw new IllegalArgumentException("Type undefined : " + type);
		}
	}

	public static String dateToStr(Date date, String pattern) {
		if (date == null || date.equals(""))
			return null;
		SimpleDateFormat formatter = new SimpleDateFormat(pattern);
		return formatter.format(date);
	}

	public static String dateToStr(Date date) {
		return dateToStr(date, "yyyy/MM/dd");
	}

	public static Date strToDate(String dateStr) {
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		try {
			return formatter.parse(dateStr);
		} catch (ParseException e) {
			
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 将指定日期的时间单位清零后返回
	 * 
	 * @param date
	 *            日期对象
	 * @return 返回时间单位被清零后的date，即日期信息为 <b>yyyy-MM-dd 00:00:00</b>
	 * @throws IllegalArgumentException
	 *             date为空时抛出
	 * @author
	 */
	public static Date clearDateTime(Date date) {
		if (date == null)
			throw new IllegalArgumentException("date不允许为空");
		Calendar calendar = Calendar.getInstance(Locale.CHINA);
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	/**
	 * 将当前日期的时间清零后返回
	 * 
	 * @return 返回时间单位被清零后的当前日期，即日期信息为 <b>yyyy-MM-dd 00:00:00</b>
	 * @author
	 */
	public static Date clearDateTime() {
		return clearDateTime(new Date());
	}

	/**
	 * 计算日期天数差，该计算忽略时间差
	 * 
	 * @param cal1
	 *            第一个日期
	 * @param cal2
	 *            第二个日期
	 * @return 相差天数，若返回0，则表示同一天
	 * @throws IllegalArgumentException
	 *             cal1或cal2为空时抛出
	 * @author
	 */
	public static int dayDiff(Calendar cal1, Calendar cal2) {
		if (cal1 == null)
			throw new IllegalArgumentException("cal1不允许为空");
		if (cal2 == null)
			throw new IllegalArgumentException("cal2不允许为空");
		Calendar c = Calendar.getInstance(Locale.CHINA);
		c.set(cal1.get(Calendar.YEAR), cal1.get(Calendar.MONTH), cal1.get(Calendar.DAY_OF_MONTH));
		long millis1 = c.getTimeInMillis();
		c.set(cal2.get(Calendar.YEAR), cal2.get(Calendar.MONTH), cal2.get(Calendar.DAY_OF_MONTH));
		long millis2 = c.getTimeInMillis();
		return Long.valueOf((millis1 - millis2) / 86400000).intValue();
	}

	/**
	 * 计算日期天数差，该计算忽略时间差
	 * 
	 * @param date1
	 *            第一个日期
	 * @param date2
	 *            第二个日期
	 * @return 相差天数，若返回0，则表示同一天
	 * @throws IllegalArgumentException
	 *             date1或date2为空时抛出
	 * @author
	 */
	public static int dayDiff(Date date1, Date date2) {
		if (date1 == null)
			throw new IllegalArgumentException("date1不允许为空");
		if (date2 == null)
			throw new IllegalArgumentException("date2不允许为空");
		Calendar c1 = Calendar.getInstance(Locale.CHINA);
		c1.setTime(date1);
		Calendar c2 = Calendar.getInstance(Locale.CHINA);
		c2.setTime(date2);
		return dayDiff(c1, c2);
	}

	/**
	 * 计算日期天数差，该计算忽略时间差
	 * 
	 * @param cal
	 *            第一个日期
	 * @param date
	 *            第二个日期
	 * @return 相差天数，若返回0，则表示同一天
	 * @throws IllegalArgumentException
	 *             cal或date为空时抛出
	 * @author
	 */
	public static int dayDiff(Calendar cal, Date date) {
		if (cal == null)
			throw new IllegalArgumentException("cal不允许为空");
		if (date == null)
			throw new IllegalArgumentException("date不允许为空");
		Calendar c = Calendar.getInstance(Locale.CHINA);
		c.setTime(date);
		return dayDiff(cal, c);
	}
	
	/**
	 * 计算两个日期之间相差的天数
	 * 
	 * @param smdate
	 *            较小的时间
	 * @param bdate
	 *            较大的时间
	 * @return 相差天数
	 * @throws ParseException
	 */
	public static int daysBetween(Date smdate, Date bdate) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
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
		return 0;
	}

	/**
	 * 计算给出的日期相关sum天的日期
	 * 
	 * @param date
	 *            指定日期
	 * @param sum
	 *            相差天数，负数就是前几天
	 * @return 相差sum天的日期
	 * @throws IllegalArgumentException
	 *             date为空时抛出
	 * @author
	 */
	public static Date dayDiff(Date date, int sum) {
		if (date == null)
			throw new IllegalArgumentException("date不允许为空");
		return new Date(date.getTime() + (sum * 86400000l));
	}

	/**
	 * 计算给出的日期相关sum天的日期字符串
	 * 
	 * @param date
	 *            指定日期 <b>yyyy-MM-dd</b> 或 <b>yyyy/MM/dd</b>
	 * @param sum
	 *            相差天数，负数就是前几天
	 * @return 相差sum天的日期 <b>yyyy-MM-dd</b>
	 * @throws IllegalArgumentException
	 *             date格式错误
	 * @author
	 */
	public static String dayDiff(String date, int sum) {
		Date d = FormatUtils.toDate(date);
		if (d == null)
			throw new IllegalArgumentException("date格式错误");
		return FormatUtils.dateString(dayDiff(d, sum));
	}

	/**
	 * 根据给出星期规则从指定日期范围中获得所有符合规则的日期集合<br>
	 * 如指定周一，周二，则从指定日期范围中，挑出所有周一和周二的日期
	 * 
	 * @param week
	 *            日期规则 一个包含1-7的字符串，1-7分别对应周一至周日，如：1347或1,3,4,7
	 * @param beginDate
	 *            开始日期
	 * @param endDate
	 *            结束日期（含）
	 * @return 符合week指定的日期集合
	 * @author
	 */
	public static List<Date> weekDate(String week, Date beginDate, Date endDate) {
		List<Date> datelist = new ArrayList<Date>();
		Calendar begdate = Calendar.getInstance(Locale.CHINA);
		Calendar enddate = Calendar.getInstance(Locale.CHINA);
		begdate.setTime(beginDate);
		enddate.setTime(endDate);
		int weekday;
		while (begdate.before(enddate) || begdate.equals(enddate)) {
			weekday = begdate.get(Calendar.DAY_OF_WEEK) - 1;
			weekday = weekday == 0 ? 7 : weekday;
			if (week.contains(String.valueOf(weekday))) {
				datelist.add(begdate.getTime());
			}
			begdate.add(Calendar.DAY_OF_MONTH, 1);
		}
		return datelist;
	}

	/**
	 * 根据给出星期规则从指定日期范围中获得所有符合规则的日期集合<br>
	 * 如指定周一，周二，则从指定日期范围中，挑出所有周一和周二的日期
	 * 
	 * @param week
	 *            日期规则 一个包含1-7的字符串，1-7分别对应周一至周日，如：1347或1,3,4,7
	 * @param beginDate
	 *            开始日期 <b>yyyy-MM-dd</b>
	 * @param endDate
	 *            结束日期（含） <b>yyyy-MM-dd</b>
	 * @return 符合week指定的日期集合
	 * @throws IllegalArgumentException
	 *             beginDate或endDate为空或不符合指定格式时抛出
	 * @author
	 */
	public static List<Date> weekDate(String week, String beginDate, String endDate) {
		Date begdate = FormatUtils.toDate(beginDate);
		Date enddate = FormatUtils.toDate(endDate);
		if (begdate == null)
			throw new IllegalArgumentException("begdate格式错误");
		if (enddate == null)
			throw new IllegalArgumentException("enddate格式错误");
		return weekDate(week, begdate, enddate);
	}

	/**
	 * 获取字符串字节长度，针对数据库保存时准确的判断字符长度
	 * 
	 * @param value
	 *            要判断的字符(半角字母、数字、英文符号算1个字符，其他算2个字符)
	 * @return 字符串的字节总长度
	 * @author
	 */
	public static int bytesLength(String value) {
		return bytesLength(value, 2);
	}

	/**
	 * 获取字符串字节长度，针对数据库保存时准确的判断字符长度
	 * 
	 * @param value
	 *            要判断的字符(半角字母、数字、英文符号算1个字符，其他算repLen指定的长度字符)
	 * @param repLen
	 *            非半角字母数字等符号换算为多少长度
	 * @return 字符串的字节总长度
	 * @author
	 */
	public static int bytesLength(String value, int repLen) {
		if (value == null)
			return 0;
		StringBuilder len = new StringBuilder();
		for (int i = 0; i < repLen; i++) {
			len.append("*");
		}
		return value.replaceAll("[^\\x00-\\xff]", len.toString()).length();
	}

	/**
	 * 获取本月天数
	 * 
	 * @return
	 */
	public static int getCurrentMonthDay() {
		Calendar a = Calendar.getInstance();
		a.set(Calendar.DATE, 1);
		a.roll(Calendar.DATE, -1);
		int maxDate = a.get(Calendar.DATE);
		return maxDate;
	}

	/**
	 * 获取本月剩余天数
	 * 
	 * @return
	 */
	public static int getMonthRemainingDay() {
		Calendar c = Calendar.getInstance();
		// 得到当天是本月的第几天
		int currentDate = c.get(Calendar.DATE);
		// 本月总天数
		int maxDate = getCurrentMonthDay();
		return maxDate - currentDate;
	}

	/**
	 * 获取当前时间 加 minutes 分钟
	 * 
	 * @param minutes
	 * @return
	 */
	public static String getCurrentTimeAdd(Integer minutes) {
		long currentTime = System.currentTimeMillis() + minutes * 60 * 1000;
		Date date = new Date(currentTime);
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String nowTime = "";
		nowTime = df.format(date);
		return nowTime;
	}
	
	/**
	 * 获取当前时间 加 minutes 分钟
	 * 
	 * @param minutes
	 * @return
	 */
	public static Date getCurrentTimeAddMinutes(Integer minutes) {

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		try {
			return formatter.parse(getCurrentTimeAdd(minutes));
		} catch (ParseException e) {
			e.printStackTrace();
			
			return null;
		}
	}

	/**
	 * 获取当前时间 减 minutes 分钟
	 * 
	 * @param minutes
	 * @return
	 */
	public static String getCurrentTimeReduce(Integer minutes) {
		long currentTime = System.currentTimeMillis() - minutes * 60 * 1000;
		Date date = new Date(currentTime);
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String nowTime = "";
		nowTime = df.format(date);
		return nowTime;
	}

	/**
	 * 获得指定日期的前一天
	 * 
	 * @param specifiedDay
	 * @param pattern
	 *            时间格式 yy-MM-dd yyyy-MM-dd HH:mm:ss
	 * @return
	 * @throws Exception
	 */
	public static String getSpecifiedDayBefore(String specifiedDay, String pattern) {
		// SimpleDateFormat simpleDateFormat = new
		// SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		Date date = null;
		try {
			date = new SimpleDateFormat(pattern).parse(specifiedDay);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		c.setTime(date);
		int day = c.get(Calendar.DATE);
		c.set(Calendar.DATE, day - 1);

		String dayBefore = new SimpleDateFormat(pattern).format(c.getTime());
		return dayBefore;
	}

	/**
	 * 获得指定日期的后一天
	 * 
	 * @param specifiedDay
	 *            * @param pattern 时间格式 yy-MM-dd yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public static String getSpecifiedDayAfter(String specifiedDay, String pattern) {
		Calendar c = Calendar.getInstance();
		Date date = null;
		try {
			date = new SimpleDateFormat(pattern).parse(specifiedDay);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		c.setTime(date);
		int day = c.get(Calendar.DATE);
		c.set(Calendar.DATE, day + 1);

		String dayAfter = new SimpleDateFormat(pattern).format(c.getTime());
		return dayAfter;
	}

	/**
	 * 获取指定日期的后 d 天
	 * 
	 * @param specifiedDay
	 *            * @param pattern 时间格式 yy-MM-dd yyyy-MM-dd HH:mm:ss
	 * @param d
	 * @return
	 */
	public static String getSpecifiedDayAfter(String specifiedDay, Integer d, String pattern) {
		Calendar c = Calendar.getInstance();
		Date date = null;
		try {
			date = new SimpleDateFormat(pattern).parse(specifiedDay);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		c.setTime(date);
		int day = c.get(Calendar.DATE);
		c.set(Calendar.DATE, day + d);

		String dayAfter = new SimpleDateFormat(pattern).format(c.getTime());
		return dayAfter;
	}
	
	/**
	 * 获取时间  时间格式为：yyyy-MM-dd 00:00:00
	 * @param date
	 * @return
	 */
	public static Date getDateToDate(Date date) {

		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
		try {
			return sdf2.parse(sdf2.format(date) + " 00:00:00");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
	
	public static Date getBeforeDayAddForm(Date date, int day) {

		return getBeforeDayAddForm(date, day, " 00:00:00");
	}
	
	/**
	 * 获取 date之前的 day天数，并且追加格式
	 * @param date
	 * @param day
	 * @return
	 */
	public static Date getBeforeDayAddForm(Date date, int day, String form) {

		try {
			Calendar now = Calendar.getInstance();
			now.setTime(date);
			now.set(Calendar.DATE, now.get(Calendar.DATE) - day);
			date = now.getTime();

			String strDate = sdf2.format(date) + form;

			date = sdf.parse(strDate);

			return date;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * date追加 day天数，并且追加格式
	 * @param date
	 * @param day
	 * @return
	 */
	public static Date getAfterDayAddForm(Date date, int day) {

		try {
			Calendar now = Calendar.getInstance();
			now.setTime(date);
			now.set(Calendar.DATE, now.get(Calendar.DATE) + day);
			date = now.getTime();

			String strDate = sdf2.format(date) + " 00:00:00";

			date = sdf.parse(strDate);

			return date;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * date追加 num月
	 * 
	 * @param date
	 * @param num
	 * @return
	 */
	public static Date getAfterMonth(Date date, Integer num) {
		
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.MONTH, + num);
		
		return c.getTime();
	}

	/**
	 * date减少 num月
	 * 
	 * @param date
	 * @param num
	 * @return
	 */
	public static Date getBeforeMonth(Date date, Integer num) {
		
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.MONTH, - num);
		
		return c.getTime();
	}
	
	/**
	 * date减少 yaer年数
	 * 
	 * @param date
	 * @param yaer
	 * @return
	 */
	public static Date getBeforeYear(Date date, Integer year) {
		
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.YEAR, - year);
		
		return c.getTime();
	}
	
	/**
	 *  需要判断的字符串
	 *  
	 * @param keyArr
	 * @return JiangMin
	 */
	public static boolean isNullOrEmpty(String... keyArr) {
		boolean console = false;

		for (String key : keyArr) {
			if (isNullOrEmpty(key, true)) {
				console = true;
				break;
			}
		}

		return console;
	}
	
	/**
	 * 判断字符串是否为空或空字符串
	 * 
	 * @param str
	 *            需要判断的字符串
	 * @param trim
	 *            是否需要忽略两边空白后判断
	 * @return 当字符串为空或为空字符串或在trim=true时str.trim()后为空字符串时，返回true，否则返回false
	 * @author
	 */
	public static boolean isNullOrEmpty(String str, boolean trim) {
		if (str == null)
			return true;
		if (str.isEmpty() || (trim && str.trim().isEmpty()))
			return true;
		return false;
	}
	
	/**
	 * 根据用户生日计算年龄
	 */
	public static int getAgeByBirthday(Date birthday) {
		Calendar cal = Calendar.getInstance();

		if (cal.before(birthday)) {
			throw new IllegalArgumentException(
					"The birthDay is before Now.It's unbelievable!");
		}

		int yearNow = cal.get(Calendar.YEAR);
		int monthNow = cal.get(Calendar.MONTH) + 1;
		int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);

		cal.setTime(birthday);
		int yearBirth = cal.get(Calendar.YEAR);
		int monthBirth = cal.get(Calendar.MONTH) + 1;
		int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

		int age = yearNow - yearBirth;

		if (monthNow <= monthBirth) {
			if (monthNow == monthBirth) {
				// monthNow==monthBirth 
				if (dayOfMonthNow < dayOfMonthBirth) {
					age--;
				}
			} else {
				// monthNow>monthBirth 
				age--;
			}
		}
		return age;
	}
	/**
	 * 获取当前字符串类型的时间戳
	 * 
	 * @return
	 */
	public static String getCurrenTimeStamp() 
	{
		Date date=new Date();
		 String timestamp = String.valueOf(date.getTime()/1000);  
		    return timestamp;  
		    
//		long last_login_time=new Date().getTime();
//		String CurrenTimeStamp =(String) System.currentTimeMillis();
//		return CurrenTimeStamp;
	}
	/**
     * 时间戳转时间
     * @param time
     * @return
     */
    public static String timestampToDate(String time) {
        String dateTime;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long timeLong = Long.valueOf(time);
        dateTime = simpleDateFormat.format(new Date(timeLong * 1000L));
        return dateTime;
    }
    
    /**
     * 时间转换成时间戳
     * @param time
     * @return
     */
    public static String dateToTimestamp(String time){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = simpleDateFormat.parse(time);
            String ts = String.valueOf(date.getTime() / 1000);
            return ts;
        } catch (ParseException e) {
            return "";
        }
    }
	
	
}

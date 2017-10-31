package cn.com.lanlyc.base.util;

import static java.util.Calendar.DAY_OF_WEEK;
import static java.util.Calendar.DAY_OF_YEAR;
import static java.util.Calendar.HOUR_OF_DAY;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.WEEK_OF_MONTH;
import static java.util.Calendar.WEEK_OF_YEAR;
import static java.util.Calendar.YEAR;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.apache.log4j.Logger;

/**
 * 格式化工具包
 * @author 
 */
public class FormatUtils {
    
    protected static final Logger logger = Logger.getLogger("com.mustangclub.manager.util");
    /**
     * 将指定格式的日期时间字符串转换为日期
     * @param date 日期字符串
     * @param pattern date对应的格式
     * @return date为null或""，返回null，否则返回转换后的Date
     * @author 
     */
    public static Date toDate(String date, String pattern) {
        if (DataUtils.isNullOrEmpty(date)) return null;
        Date theDate;
        try {
            SimpleDateFormat theFormat = new SimpleDateFormat(pattern);
            theDate = theFormat.parse(date);
        } catch (Exception ex) {
            theDate = null;
        }
        return theDate;
    }
    
    /**
     * 将日期字符串转换为日期
     * @param date 日期字符串<br>
     *            格式须符合 <b>yyyy-MM-dd</b> 或 <b>yyyy/MM/dd</b>
     * @return 一个 Date，时间为00:00:00.0，如果无法分析date，则为 null
     * @author 
     */
    public static Date toDate(String date) {
        String separate = date.substring(4, 5);
        return toDate(date, "yyyy" + separate + "MM" + separate + "dd");
    }
    
    /**
     * 将日期时间字符串转换为日期
     * @param dateTime 日期时间字符串<br>
     *            格式须符合 <b>yyyy-MM-dd hh:mm:ss</b> 或 <b>yyyy/MM/dd hh:mm:ss</b>
     * @return 一个 Date，如果无法分析dateTime，则为 null
     */
    public static Date toDateTime(String dateTime) {
        String separate = dateTime.substring(4, 5);
        return toDate(dateTime, "yyyy" + separate + "MM" + separate + "dd HH:mm:ss");
    }
    
    /**
     * 将日期时间字符串转换为日期
     * @param dateTime 日期时间字符串<br>
     *            格式须符合 <b>yyyy-MM-dd hh:mm:ss</b> 或 <b>yyyy/MM/dd hh:mm:ss</b>
     * @param defaultDate 默认日期
     * @return 一个 Date，如果无法分析dateTime，则为 defaultDate
     * @author 
     */
    public static Date toDateTime(String dateTime, Date defaultDate) {
        String separate = dateTime.substring(4, 5);
        Date theDate = toDate(dateTime, "yyyy" + separate + "MM" + separate + "dd HH:mm:ss");
        return theDate == null ? defaultDate : theDate;
    }
    
    /**
     * 自动分析字符串并尝试转换为日期对象<br>
     * 可转换示例
     * 
     * <pre>
     * 2012-3-21, 2012-03-21
     * 2012/3/21, 2012/03/21
     * 2012.3.21, 2012.03.21
     * 2012-3-21 18:11:43, 2012-03-21 18:11:43
     * 2012-3-21 18:11:43.567, 2012-03-21 18:11:43.567
     * 2012/3/21 18:11:43, 2012/03/21 18:11:43
     * 2012/3/21 18:11:43.567, 2012/03/21 18:11:43.567
     * 2012.3.21 18:11:43, 2012.03.21 18:11:43
     * 2012.3.21 18:11:43.567, 2012.03.21 18:11:43.567
     * Wed Mar 21 18:11:43 CST 2012
     * 21/3/2012, 21/03/2012
     * 20120321
     * 1332324703000
     * </pre>
     * @param dateTime 日期字符串
     * @return 一个 Date，如果无法分析dateTime，则为 null
     * @author 
     */
    public static Date autoDate(String dateTime) {
        if (DataUtils.isNullOrEmpty(dateTime)) return null;
        String[][] patterns = {{"\\d{4}(-\\d{1,2}){2}", "yyyy-M-d", "zh"}, // 2012-03-21, 2012-3-21
                               {"\\d{4}(-\\d{1,2}){2} \\d{1,2}(:\\d{1,2}){2}", "yyyy-M-d H:m:s", "zh"}, // 2012-03-21 18:11:43
                               {"\\d{4}(-\\d{1,2}){2} \\d{1,2}(:\\d{1,2}){2}\\.\\d{1,3}", "yyyy-M-d H:m:s.S", "zh"}, // 2012-03-21 18:11:43.567
                               {"([A-Z][a-z]{2} ){2}\\d{1,2} \\d{1,2}(:\\d{1,2}){2} [A-Z]{3} \\d{4}", "EEE MMM d H:m:s z yyyy", "en"}, // Wed Mar 21 18:11:43 CST 2012
                               {"\\d{4}(/\\d{1,2}){2}", "yyyy/M/d", "zh"}, // 2012/03/21, 2012/3/21
                               {"\\d{4}(/\\d{1,2}){2} \\d{1,2}(:\\d{1,2}){2}", "yyyy/M/d H:m:s", "zh"}, // 2012/03/21 18:11:43
                               {"\\d{4}(/\\d{1,2}){2} \\d{1,2}(:\\d{1,2}){2}\\.\\d{1,3}", "yyyy/M/d H:m:s.S", "zh"}, // 2012/03/21 18:11:43.567
                               {"\\d{4}(\\.\\d{1,2}){2}", "yyyy.M.d", "zh"}, // 2012.03.21, 2012.3.21
                               {"\\d{4}(\\.\\d{1,2}){2} \\d{1,2}(:\\d{1,2}){2}", "yyyy.M.d H:m:s", "zh"}, // 2012.03.21 18:11:43
                               {"\\d{4}(\\.\\d{1,2}){2} \\d{1,2}(:\\d{1,2}){2}\\.\\d{1,3}", "yyyy.M.d H:m:s.S", "zh"}, // 2012.03.21 18:11:43.567
                               {"(\\d{1,2}/){2}\\d{4}", "d/M/yyyy", "zh"}, // 21/03/2012, 21/3/2012
                               {"\\d{8}", "yyyyMMdd", "zh"}, // 20120321
        };
        for (int i = 0; i < patterns.length; i++) {
            if (dateTime.matches(patterns[i][0])) {
                SimpleDateFormat format = new SimpleDateFormat(patterns[i][1], new Locale(patterns[i][2]));
                try {
                    return format.parse(dateTime);
                } catch (ParseException e) {
                    logger.warn("日期格式[" + dateTime + "]自动转换错误");
                    return null;
                }
            }
        }
        if (DataUtils.isInteger(dateTime)) return new Date(Long.valueOf(dateTime)); // 1332324703000
        return null;
    }
    
    /**
     * 日期格式化方法
     * @param date 需要格式化的日期
     * @param pattern 格式化格式
     * @return 如果date为null，则返回空字符串，否则返回pattern格式指定的字符串日期格式
     * @throws IllegalArgumentException pattern为空时抛出
     * @author 
     */
    public static String formatDate(Date date, String pattern) {
        if (pattern == null) throw new IllegalArgumentException("pattern不允许为空");
        if (date == null) return "";
        SimpleDateFormat theFormat = new SimpleDateFormat(pattern, Locale.CHINA);
        return theFormat.format(date);
    }
    
    /**
     * 将日期转换为 <b>yyyy-MM-dd HH:mm:ss</b> 格式的日期时间字符串
     * @param dateTime 需要转换的日期对象
     * @return 如果date为null，则返回""，否则返回格式为 <b>yyyy-MM-dd HH:mm:ss</b> 的日期字符串
     * @author 
     */
    public static String dateTimeString(Date dateTime) {
        return formatDate(dateTime, "yyyy-MM-dd HH:mm:ss");
    }
    
    /**
     * 将日期转换为 <b>yyyy-MM-dd</b> 格式的日期字符串
     * @param date 需要转换的日期对象
     * @return 如果date为null，则返回""，否则返回格式为 <b>yyyy-MM-dd</b> 的日期字符串
     * @author 
     */
    public static String dateString(Date date) {
        return formatDate(date, "yyyy-MM-dd");
    }
    
    /**
     * 将日期转换为距离当前多久的时间描述<br>
     * 如：刚刚，昨天，2小时之前等
     * @param date
     * @return 距离当前多久
     * @author 
     */
    public static String prettyDate(Date date) {
        if (date == null) return null;
        Calendar now = Calendar.getInstance(Locale.CHINA);
        Calendar foretime = Calendar.getInstance(Locale.CHINA);
        foretime.setTime(date);
        long diff = now.getTimeInMillis() - foretime.getTimeInMillis();
        if (diff < 0) {
            return "未来";
        } else if (diff < 60000) { // 一分钟之内
            return "刚刚";
        } else if (diff < 3600000) { // 一小时之内
            return diff / 60000 + "分钟前";
        } else if (diff < 86400000) { // 一天之内
            if (now.get(DAY_OF_YEAR) == foretime.get(DAY_OF_YEAR) // 同一天
                || (foretime.get(HOUR_OF_DAY) > 12 && now.get(HOUR_OF_DAY) < 6)) { // 不同天且在昨天12点后，今天6点前
                return diff / 3600000 + "小时前";
            } else {
                return "昨天";
            }
        } else if (diff < 604800000) { // 一周之内
            if (now.get(DAY_OF_WEEK) > foretime.get(DAY_OF_WEEK) // 同一周（含跨年）
                || (foretime.get(DAY_OF_WEEK) > 5 && now.get(DAY_OF_WEEK) < 3)) { // 不同周且在上周后两天，本周前两天内
                int dayOfWeek = now.get(DAY_OF_WEEK) - foretime.get(DAY_OF_WEEK);
                if (dayOfWeek < 0) dayOfWeek += 7; // 不同周时，差额负数补正
                if (dayOfWeek == 1) {
                    return "昨天";
                } else {
                    return (dayOfWeek - 1) + "天前";
                }
            } else {
                return "上周";
            }
        } else if (diff < 2592000000l) { // 一个月之内
            if (now.get(MONTH) == foretime.get(MONTH) // 同一月
                || now.get(WEEK_OF_MONTH) < 3) { // 不同月且在本月前2周内
                int weekOfYear = now.get(WEEK_OF_YEAR) - foretime.get(WEEK_OF_YEAR);
                if (weekOfYear < 0) { // 是否跨年
                    weekOfYear += foretime.getMaximum(WEEK_OF_YEAR); // 跨年时，差额负数补正
                    if (now.get(DAY_OF_YEAR) - now.get(DAY_OF_WEEK) - now.get(WEEK_OF_YEAR) * 7 - 7 < 0) { // 去年最后一周是否跨年，合并跨年造成的多余数
                        weekOfYear -= 1;
                    }
                }
                if (weekOfYear == 1) {
                    return "上周";
                } else {
                    return (weekOfYear - 1) + "周前";
                }
            } else {
                return "上月";
            }
        } else if (diff < 31536000000l) { // 一年之内
            if (now.get(YEAR) == foretime.get(YEAR) // 同一年
                || (foretime.get(MONTH) > 4 && now.get(MONTH) < 3)) { // 不同年且在去年6月后，今年前3个月内
                int monthOfYear = now.get(MONTH) - foretime.get(MONTH);
                if (monthOfYear < 0) { // 是否跨年
                    monthOfYear += foretime.getMaximum(MONTH); // 跨年时，差额负数补正
                }
                if (monthOfYear == 1) {
                    return "上月";
                } else {
                    return (monthOfYear - 1) + "个月前";
                }
            } else {
                return "去年";
            }
        } else { // 一年以前
            int yearOfAge = now.get(YEAR) - foretime.get(YEAR);
            if (yearOfAge == 1) {
                return "去年";
            } else {
                return (yearOfAge - 1) + "年前";
            }
        }
    }
    /**
     * 根据秒数转换为时间字符串
     * @param second 秒数
     * @return 字符串时间格式 HHH:mm:ss
     * @author 
     */
    public static String timeString(long second) {
        StringBuilder time = new StringBuilder("");
        int h = (int) second / 60 / 60;
        int m = (int) second / 60 % 60;
        int s = (int) second % 60;
        DecimalFormat d = new DecimalFormat("00");
        if (h > 10) {
            time.append(h + ":");
        } else {
            time.append("0" + h + ":");
        }
        time.append(d.format(m) + ":");
        time.append(d.format(s));
        return time.toString();
    }
    
    /**
     * 将字符串转换成unicode编码，同native2ascii转换效果
     * @param value 原始字符串
     * @return 返回Unicode编码格式的字符串，如：中国 -> \u4e2d\u56fd
     * @author 
     */
    public static String toUnicode(String value) {
        StringBuffer buffer = new StringBuffer();
        char[] chars = value.toCharArray();
        for (char ch : chars) {
            if (ch > 128) {
                buffer.append("\\u");
                buffer.append(Integer.toHexString(ch));
            } else {
                buffer.append(ch);
            }
        }
        return buffer.toString();
    }
    
}

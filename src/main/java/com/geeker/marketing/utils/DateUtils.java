package com.geeker.marketing.utils;

import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * @version V1.0
 * @Description: TODO(时间操作工具类)
 */
public class DateUtils {

    public static String YYMMDD = "yyyy-MM-dd";
    public static String YYMMDD_HHmm = "yyyy-MM-dd HH:mm";
    public static String YYMMDD_HHmmSS = "yyyy-MM-dd HH:mm:ss";
    public static String HHmmSS = "HH:mm:ss";
    public static String YYMMDDHHmmssSSS = "yyyyMMddHHmmssSSS";
    public static String HHmmssSSS = "HHmmssSSS";
    public static String YYMM = "yyyy-MM";
    public static String YYMMDDHHmm = "yyyyMMddHHmm";
    public static String YYMMDD_SIMPLE = "yyyyMMdd";
    public static String MMDD_SIMPLE = "MMdd";
    public static String YYMMDDHHmmss = "yyyyMMddHHmmss";


    /**
     * 不可实例化
     */
    private DateUtils() {
    }

    /**
     * 日期参数类型
     */
    public enum ParamType {
        day, month, yeah
    }

    /**
     * 获取每月第一天
     *
     * @param date  月份时间,为空时 取当前时间
     * @param month 要减去的月份数, 1表示往前一个月, -1表示往后一个月
     * @return 时间格式:2015-06-01 00:00:01
     */
    public static Date getMonthFirstDay(Date date, int month) {
        if (date == null) {
            date = new Date();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH)
                - month, 1, 0, 0, 1);
        return calendar.getTime();
    }

    /**
     * 格式化时间
     *
     * @param date 时间,为空时 取当前时间
     *             格式
     * @return
     */
    public static String format(Date date, boolean defail) {
        if (date == null) {
            if (defail) {
                date = new Date();
            } else {
                return "";
            }
        }
        String format = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.format(date);
    }

    public static String format(Date date) {
        if (date == null) {
            date = new Date();
        }
        String format = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.format(date);
    }

    public static String formatNew(Date date) {
        if (date == null) {
            return "";
        }
        String format = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.format(date);
    }

    /**
     * 格式化时间
     *
     * @param date   时间,为空时 取当前时间
     * @param format 格式
     * @return
     */
    public static String format(Date date, String format) {
        if (date == null) {
            date = new Date();
        }
        if (format == null) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.format(date);
    }

    /**
     * String 字符串转时间类型
     *
     * @param dateStr 时间字符串
     * @param format  格式化
     * @return
     */
    public static Date StringToDate(String dateStr, String format) {

        if (dateStr == null) {
            return null;
        }


        if (format == null) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        Date date = null;
        try {
            date = simpleDateFormat.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * @param datestr 日期字符串
     * @return 指定日期字符串n天之前或者之后的日期
     */
    public static Date getBeforeAfterDate(String datestr, int num, ParamType paramType, String formatter) {
        SimpleDateFormat df = new SimpleDateFormat(formatter);
        java.sql.Date olddate = null;
        try {
            df.setLenient(false);
            olddate = new java.sql.Date(df.parse(datestr).getTime());
        } catch (ParseException e) {
            throw new RuntimeException("日期转换错误");
        }
        Calendar cal = new GregorianCalendar();
        cal.setTime(olddate);

        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        if (paramType == ParamType.yeah) {
            int newYeah = year + num;
            cal.set(Calendar.YEAR, newYeah);
            cal.set(Calendar.MONTH, month);
            cal.set(Calendar.DAY_OF_MONTH, day);
        }
        if (paramType == ParamType.month) {
            int newMonth = month + num;
            cal.set(Calendar.YEAR, year);
            cal.set(Calendar.MONTH, newMonth);
            cal.set(Calendar.DAY_OF_MONTH, day);
        }
        if (paramType == ParamType.day) {
            int newDay = day + num;
            cal.set(Calendar.YEAR, year);
            cal.set(Calendar.MONTH, month);
            cal.set(Calendar.DAY_OF_MONTH, newDay);
        }
        return new Date(cal.getTimeInMillis());
    }

    /**
     * 获取指定时间N月前的日期
     *
     * @param date
     * @param monthNum
     * @return
     */
    @SuppressWarnings("static-access")
    public static Date prevNMonth(Date date, Integer monthNum) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(cal.MONTH, -monthNum);
        return cal.getTime();
    }

    /**
     * 获取指定时间N月后的日期
     *
     * @param date
     * @param monthNum
     * @return
     */
    public static Date nextDay4NMonth(Date date, Integer monthNum) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, monthNum);
        return calendar.getTime();
    }

    /**
     * 获得距当前时间多少天前的日期
     *
     * @return
     */
    public static Date getBeforeDate(int days) {

        Calendar now = Calendar.getInstance();
        now.setTime(new Date());
        now.set(Calendar.DATE, now.get(Calendar.DATE) - days);
        return now.getTime();
    }

    /**
     * 获得距当前时间多少天之后的日期
     *
     * @return
     */
    public static Date getAfterDate(int days) {

        Calendar now = Calendar.getInstance();
        now.setTime(new Date());
        now.set(Calendar.DATE, now.get(Calendar.DATE) + days);
        return now.getTime();
    }

    /**
     * 获得距指定时间多少天之后的日期
     *
     * @return
     */
    public static Date getAfterDate(Date date, int days) {
        Calendar now = Calendar.getInstance();
        now.setTime(date);
        now.set(Calendar.DATE, now.get(Calendar.DATE) + days);
        return now.getTime();
    }

    /**
     * 获得距指定时间多少天前的日期
     *
     * @return
     */
    public static Date getBeforeDate(Date date, int days) {
        Calendar now = Calendar.getInstance();
        now.setTime(date);
        now.set(Calendar.DATE, now.get(Calendar.DATE) - days);
        return now.getTime();
    }

    /**
     * 获得距指定时间多少天前的日期
     *
     * @return
     */
    public static Date getBeforeDate2(Date date, int days) {
        Calendar now = Calendar.getInstance();
        now.setTime(date);
        now.set(Calendar.DATE, now.get(Calendar.DATE) - days);
        Date nowdate=now.getTime();
        return DateUtils.parse( DateUtils.format(nowdate,"yyyy-MM-dd"),"yyyy-MM-dd");
    }



    public static String dateStr2(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String str = format.format(date);
        return str;
    }

    /**
     * @param date
     * @param format
     * @return
     */
    public static Date dateFormat(Date date, String format) {
        String dateStr = format(date, format);
        Date rsltDate = parse(dateStr, format);
        return rsltDate;
    }

    /**
     * 获取指定时间的day
     *
     * @param date
     * @return
     */
    public static int getDay(Date date) {
        if (date == null) {
            return -1;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 将字符串日期解析成日期对象
     *
     * @param strDate
     * @param format  取值如：DateUtil.YYMMDD
     * @return
     */
    public static Date parse(String strDate, String format) {
        if (StringUtils.isEmpty(strDate)) {
            return null;
        }

        SimpleDateFormat formater = new SimpleDateFormat(format);
        try {
            return formater.parse(strDate);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 将字符串日期解析成日期对象用于查询的结束时间
     *
     * @param strDate
     * @return
     */
    public static Date parseLastTime(String strDate) {
        return new Date(DateUtils.parse(strDate, DateUtils.YYMMDD).getTime()
                + 24 * 60 * 60 * 1000 - 1);

    }

    /**
     * @param strDate
     * @return
     */
    public static Date parseDelayTime(String strDate) {
        return new Date(DateUtils.parse(strDate, DateUtils.YYMMDD).getTime()
                - 2 * 24 * 60 * 60 * 1000);

    }

    /**
     * 解析日期
     *
     * @param strDate
     * @return
     */
    public static Date parse(String strDate) {
        if (StringUtils.isEmpty(strDate)) {
            return null;
        }
        SimpleDateFormat formater = new SimpleDateFormat(YYMMDD_HHmmSS);
        try {
            return formater.parse(strDate);
        } catch (ParseException e) {
        }
        formater = new SimpleDateFormat(YYMMDD);
        try {
            return formater.parse(strDate);
        } catch (ParseException e) {
        }
        return null;

    }

    /*
     * yyyy年MM月dd日 yyyy-MM-dd
     */
    public static Date parseShort(String strDate) {
        return parse(strDate, YYMMDD);
    }

    public static Date parseShortNow() {
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
                cal.get(Calendar.DATE), 0, 0, 0);
        return cal.getTime();
    }

    /*
     * yyyy年MM月dd日 HH时mm分 yyyy-MM-dd HH:mm
     */
    public static Date parseSimple(String strDate) {
        return parse(strDate, YYMMDD_HHmm);
    }

    /*
     * yyyy年MM月dd日 HH时mm分ss秒 yyyy-MM-dd HH:mm:ss
     */
    public static Date parseFully(String strDate) {
        return parse(strDate, YYMMDD_HHmmSS);
    }

    /**
     * 将字符串日期解析成日期对象
     *
     * @param strDate
     * @return
     */
    public static Date parseYMD(String strDate) {
        if (StringUtils.isEmpty(strDate)) {
            return null;
        }

        SimpleDateFormat formater = new SimpleDateFormat(YYMMDD);
        try {
            return formater.parse(strDate);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 将字符串日期解析成日期对象
     *
     * @param strDate
     * @param endDate
     * @return
     */
    public static Date parseYMDHMS(String strDate, String endDate) {
        if (StringUtils.isEmpty(strDate)) {
            return null;
        }

        SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return formater.parse(strDate + " " + endDate);
        } catch (ParseException e) {
            return null;
        }
    }


    /*
     * yyyy年MM月dd日 yyyy-MM-dd
     */
    public static String formatShort(Date date) {
        return format(date, YYMMDD);
    }

    /*
     * yyyy年MM月dd日 HH时mm分 yyyy-MM-dd HH:mm
     */
    public static String formatSimple(Date date) {
        return format(date, YYMMDD_HHmm);
    }

    /*
     * yyyy年MM月dd日 HH时mm分ss秒 yyyy-MM-dd HH:mm:ss
     */
    public static String formatFully(Date date) {
        return format(date, YYMMDD_HHmmSS);
    }

    public static String formatTimestamp() {
        return format(new Date(), YYMMDDHHmmssSSS);
    }

    public static String formatTimestamp4oss() {
        return format(new Date(), HHmmssSSS);
    }

    public static String formatTimes(Date date) {
        return format(date, HHmmSS);
    }

    public static Date addSecond(Date date) {
        GregorianCalendar calendar = new GregorianCalendar();
        // 最后时间加1秒（防止导入号码过少，时间相同，取不到数据）
        calendar.setTime(date);
        calendar.add(Calendar.MILLISECOND, 60000);
        return calendar.getTime();
    }

    /**
     * 将日期格式化成年月日 yyyy-MM-dd
     *
     * @param date
     * @return
     */
    public static String formatYM(Date date) {
        String strDate = "";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
        try {
            strDate = format.format(date);
        } catch (Exception e) {
            return null;
        }
        return strDate;

    }

    public static String formatYMD(Date date) {
        String strDate = "";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            strDate = format.format(date);
        } catch (Exception e) {
            return null;
        }
        return strDate;

    }

    /**
     * 获得指定月份的周数
     *
     * @param year
     * @param month
     * @return
     */
    public static int getTotalWeeksOfMonth(int year, int month) {
        int days = getDaysOfMonth(year, month);
        Calendar c = Calendar.getInstance();
        c.set(year, month - 1, days);
        return c.get(Calendar.WEEK_OF_MONTH);
    }

    /**
     * 获得指定月份的天数
     *
     * @param year
     * @param month
     * @return
     */
    public static int getDaysOfMonth(int year, int month) {
        int days = 0;
        if (month < 8) {
            if (month % 2 == 0) {
                if (month == 2)
                    days = getDaysOfFeb(year);
                else
                    days = 30;
            } else {
                days = 31;
            }
        } else {
            if (month % 2 == 0)
                days = 31;
            else
                days = 30;
        }
        return days;
    }

    /**
     * 获得指定年份的2月份天数
     *
     * @param year
     * @return
     */
    public static int getDaysOfFeb(int year) {
        if (year % 400 == 0 || (year % 4 == 0 && year % 100 != 0))
            return 29;
        else
            return 28;
    }

    /**
     * 获得2个时间相减的天数
     *
     * @return
     */
    public static int getTwoDaySub(Date start, Date end) {

        long mint = (end.getTime() - start.getTime()) / (1000);
        long hor = mint / 3600;
        int day = (int) (hor / 24);
        return day;
    }

    /**
     * 获得2个时间相减的分钟数
     *
     * @return
     */
    public static int getTwoDaySubByMinute(Date start, Date end) {
        long second = (end.getTime() - start.getTime()) / (1000);
        long min = second / 60;
        int day = (int) min;
        return day;
    }

    /**
     * 获得2个时间相减的秒数
     *
     * @return
     */
    public static int getTwoDaySubBySeconds(Date start, Date end) {
        Long second = (end.getTime() - start.getTime()) / (1000);
        return second.intValue();
    }

    /**
     * 获得距当前时间多少秒前的日期
     *
     * @return
     */
    public static Date getBeforeSecondsDate(int seconds) {

        Calendar now = Calendar.getInstance();
        now.setTime(new Date());
        now.set(Calendar.SECOND, now.get(Calendar.SECOND) - seconds);
        return now.getTime();
    }

    /**
     * 获得距指定时间多少秒后的日期
     *
     * @return
     */
    public static Date getNextSecondsDate(Date date, int seconds) {
        Calendar now = Calendar.getInstance();
        now.setTime(date);
        now.set(Calendar.SECOND, now.get(Calendar.SECOND) + seconds);
        return now.getTime();
    }



    /**
     * 获得距当前时间多少分钟前的日期
     *
     * @return
     */
    public static Date getBeforeMinutesDate(Date data,int minutes) {

        Calendar now = Calendar.getInstance();
        now.setTime(data);
        now.set(Calendar.MINUTE, now.get(Calendar.MINUTE) - minutes);
        return now.getTime();
    }

    /**
     * 获得距指定时间多少分钟后的日期
     *
     * @return
     */
    public static Date getNextNMinutesDate(Date date, int minutes) {
        Calendar now = Calendar.getInstance();
        now.setTime(date);
        now.set(Calendar.MINUTE, now.get(Calendar.MINUTE) + minutes);
        return now.getTime();
    }

    /**
     * 获得2个时间相减的天数
     *
     * @return
     */
    public static int getTwoDaySub(Date start, Date end, int plus) {

        long mint = (end.getTime() - start.getTime()) / (1000);
        int hor = (int) mint / 3600;
        int day = (int) hor / 24;
        if (start.after(end))
            return day;

        return day + plus;
    }

    /**
     * 获取当前年份
     *
     * @returns
     */
    public static int getNowYear() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.YEAR);
    }

    /**
     * 获取当前月份
     *
     * @param date 日期
     * @return
     */
    public static int getNowMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.MONTH) + 1;
    }

    /**
     * 上月最后一天
     * <p/>
     * 例如：上上月 offset -2 例如：上月 offset -1 Date date =
     * getSpecifyMonthDate(0,0);当月第一天 Date date =
     * getSpecifyMonthDate(-1,0);上月第一天 Date date =
     * getSpecifyMonthDate(0,-1);上月最后一天
     *
     * @param monthOffset
     * @param dayOfMonthOffset
     * @return
     */
    public static Date getSpecifyMonthDate(int monthOffset, int dayOfMonthOffset) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), 1, 0, 0, 0);
        cal.add(Calendar.MONTH, monthOffset);
        cal.add(Calendar.DAY_OF_MONTH, dayOfMonthOffset);
        return cal.getTime();
    }

    /**
     * 上月最后一天
     * <p/>
     * 例如：上上月 offset -1 例如：上月 offset 0
     *
     * @param monthOffset
     * @param dayOfMonthOffset
     * @return
     */
    public static Date getSpecifyMonthDate(Date time, int monthOffset,
                                           int dayOfMonthOffset) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(time);
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), 1, 0, 0, 0);
        cal.add(Calendar.MONTH, monthOffset);
        cal.add(Calendar.DAY_OF_MONTH, dayOfMonthOffset);
        return cal.getTime();
    }

    /**
     * 获取当前月份
     *
     * @param date 日期
     * @return
     */
    public static int getNowDay(Date date) {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.DATE);
    }

    /**
     * 获取当前月份
     *
     * @return
     */
    public static int getYesterday() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.DATE) - 1;
    }

    /**
     * 获取当前月份
     *
     * @return
     */
    public static Date getYesterdayDate() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        return cal.getTime();
    }

    /**
     * 获取星期名称
     *
     * @param date 日期
     * @return
     */
    public static String getDayweek(Date date) {
        String[] weeks = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int dayinweek = cal.get(Calendar.DAY_OF_WEEK) - 1;
        return weeks[dayinweek];
    }

    public static int getDayWeek(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int dayinweek = cal.get(Calendar.DAY_OF_WEEK) - 1;
        return dayinweek;
    }

    /**
     * 获取当前日期起始周(星期日)
     *
     * @param date 日期
     * @return
     */
    public static Date getWeekStart(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int day = cal.get(Calendar.DAY_OF_WEEK);
        cal.add(Calendar.DATE, -day + 1);
        return cal.getTime();
    }

    /**
     * 获取当前日期周末(星期六)
     *
     * @param date 日期
     * @return
     */
    public static Date getWeekEnd(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int day = cal.get(Calendar.DAY_OF_WEEK);
        cal.add(Calendar.DATE, 6 - day + 1);
        return cal.getTime();
    }

    /**
     * 获取当月第一天时间
     *
     * @return
     */
    public static String getMonthFirstDate() {
        Calendar cal = Calendar.getInstance(Locale.CHINA);
        int month = cal.get(Calendar.MONTH) + 1;
        int year = cal.get(Calendar.YEAR);
        int day = cal.get(Calendar.DATE);
        String startMonthDay = "";
        String mop = "-";
        String dop = "-";
        if (month < 10) {
            mop = mop + "0";
        }
        if (day < 10) {
            dop = dop + "0";
        }

        startMonthDay = year + mop + month + "-01";

        return startMonthDay;
    }

    /**
     * 获取当前日期
     *
     * @return
     */
    public static String getMonthNowDate() {
        Calendar cal = Calendar.getInstance(Locale.CHINA);
        int month = cal.get(Calendar.MONTH) + 1;
        int year = cal.get(Calendar.YEAR);
        int day = cal.get(Calendar.DATE);
        String now = "";
        String mop = "-";
        String dop = "-";
        if (month < 10) {
            mop = mop + "0";
        }
        if (day < 10) {
            dop = dop + "0";
        }

        now = year + mop + month + dop + day;
        return now;
    }

    /**
     * 获取当前月份
     *
     * @return
     */
    public static String getNowMonth() {
        Calendar cal = Calendar.getInstance(Locale.CHINA);
        int month = cal.get(Calendar.MONTH) + 1;
        int year = cal.get(Calendar.YEAR);
        String now = "";
        String mop = "-";
        if (month < 10) {
            mop = mop + "0";
        }

        now = year + mop + month;
        return now;
    }

    /**
     * 获取当前月份
     *
     * @return
     */
    public static String getNowMonthDay(int day) {
        Calendar cal = Calendar.getInstance(Locale.CHINA);
        int month = cal.get(Calendar.MONTH) + 1;
        int year = cal.get(Calendar.YEAR);
        String now = "";
        String mop = "-";
        if (month < 10) {
            mop = mop + "0";
        }
        if (day > 9) {
            now = year + mop + month + "-" + day;
        } else {
            now = year + mop + month + "-0" + day;
        }

        return now;
    }

    public static List<Integer> getYearRange(int year) {
        List<Integer> years = new ArrayList<Integer>();
        Calendar cal = GregorianCalendar.getInstance(Locale.CHINESE);
        int now = cal.get(Calendar.YEAR);
        if (year < now) {
            years = new ArrayList<Integer>();
            for (int i = year; i <= now; i++) {
                years.add(i);
            }
        } else {
            years.add(now);
        }
        return years;
    }

    public static Map<Integer, String> getMonthRange() {
        Map<Integer, String> month = new HashMap<Integer, String>();
        month.put(1, "一月");
        month.put(2, "二月");
        month.put(3, "三月");
        month.put(4, "四月");
        month.put(5, "五月");
        month.put(6, "六月");
        month.put(7, "七月");
        month.put(8, "八月");
        month.put(9, "九月");
        month.put(10, "十月");
        month.put(11, "十一月");
        month.put(12, "十二月");
        return month;
    }

    // 取时间范围，24时
    public static LinkedHashMap<Integer, Integer> getTimeRange(int start,
                                                               int end) {
        LinkedHashMap<Integer, Integer> map = new LinkedHashMap<Integer, Integer>();
        int result = end - start;
        if (result > 0) {
            for (int i = 1; i < result + 2; i++) {
                map.put(i, start++);
            }
        } else if (result == 0) {
            map.put(1, start);
        }
        return map;
    }

    public static Date getBelongToDateTimeRange(Date date) {
        String shortDate = format(date, "yyyy-MM-dd HH");
        parseSimple(shortDate + ":00").getTime();
        long second = parseSimple(shortDate + ":15").getTime();
        long third = parseSimple(shortDate + ":30").getTime();
        long fourth = parseSimple(shortDate + ":45").getTime();
        long mDate = date.getTime();
        if (mDate < second)

        {
            return parseSimple(shortDate + ":00");
        } else if (mDate >= second && mDate < third)

        {
            return parseSimple(shortDate + ":15");
        } else if (mDate >= third && mDate < fourth)

        {
            return parseSimple(shortDate + ":30");
        } else if (mDate >= fourth)

        {
            return parseSimple(shortDate + ":45");
        }

        return null;
    }

    /**
     * 获取指定日期的年月
     *
     * @param p_date
     * @return
     */
    public static String getYearMonthOfDate(Date p_date) {
        Calendar c = GregorianCalendar.getInstance();
        c.setTime(p_date);
        int month = c.get(GregorianCalendar.MONTH) + 1;
        return c.get(GregorianCalendar.YEAR) + "-"
                + (month < 9 ? ("0" + month) : month);
    }

    /**
     * 获取指定日期的年份
     *
     * @param p_date
     * @return
     */
    public static int getYearOfDate(Date p_date) {
        Calendar c = GregorianCalendar.getInstance();
        c.setTime(p_date);
        return c.get(GregorianCalendar.YEAR);
    }

    /**
     * 获取指定日期的月份
     *
     * @param p_date
     * @return
     */
    public static int getMonthOfDate(Date p_date) {
        Calendar c = GregorianCalendar.getInstance();
        c.setTime(p_date);
        return c.get(GregorianCalendar.MONTH) + 1;
    }

    /**
     * 获取指定日期的天数
     *
     * @param p_date
     * @return
     */
    public static int getDayOfMonth(Date p_date) {
        Calendar c = GregorianCalendar.getInstance();
        c.setTime(p_date);
        return c.get(GregorianCalendar.DAY_OF_MONTH);
    }

    /**
     * 获取指定日期的点数
     *
     * @param p_date
     * @return
     */
    public static int getTimeOfDate(Date p_date) {
        Calendar c = GregorianCalendar.getInstance();
        c.setTime(p_date);
        return c.get(GregorianCalendar.HOUR_OF_DAY);
    }

    /**
     * 获取指定日期的分数
     *
     * @param p_date
     * @return
     */
    public static int getMinOfDate(Date p_date) {
        Calendar c = GregorianCalendar.getInstance();
        c.setTime(p_date);
        return c.get(GregorianCalendar.MINUTE);
    }

    /**
     * 获取据当前时间的任何时间
     *
     * @param type 类型
     * @param i    数值
     * @return
     */
    public static Date getAnyDate(int type, int i) {
        Calendar cal = GregorianCalendar.getInstance(Locale.CHINESE);
        cal.add(type, i);
        return cal.getTime();
    }

    public static Date getAnyDateMonthStart(int type, int i) {
        Calendar cal = GregorianCalendar.getInstance(Locale.CHINESE);
        cal.add(type, i);
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
                cal.get(Calendar.DATE), 0, 0, 0);
        return cal.getTime();
    }

    public static String formatFullyByYMDStr(String startTime) {
        if (!StringUtils.isEmpty(startTime))
            return formatFully(parse(startTime));

        return null;
    }

    public static String formatFullyByYMDLastStr(String startTime) {
        if (!StringUtils.isEmpty(startTime))
            return formatFully(parseLastTime(startTime));
        return null;
    }

    public static Date getMonthStart(String claimMonth) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(parse(claimMonth, YYMM));
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), 1, 0, 0, 0);
        return cal.getTime();
    }

    public static Date getMonthEnd(String claimMonth) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(parse(claimMonth, YYMM));
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), 1, 0, 0, 0);
        cal.add(Calendar.MONTH, 1);
        cal.add(Calendar.SECOND, -1);
        return cal.getTime();
    }

    /**
     * 获取当前月份下个月
     *
     * @return
     */
    public static String getNextMonth() {
        Calendar cal = Calendar.getInstance(Locale.CHINA);
        int month = cal.get(Calendar.MONTH) + 1;
        int year = cal.get(Calendar.YEAR);
        String now = "";
        String mop = "-";
        if (month < 10) {
            mop = mop + "0";
            month = month + 1;
        } else if (month == 10 || month == 11) {
            month = month + 1;
        } else if (month == 12) {
            year = year + 1;
            month = 1;
            mop = mop + "0";
        }

        now = year + mop + month;
        return now;
    }

    /**
     * 获取当前月份上个月
     *
     * @return
     */
    public static String getPreMonth() {
        Calendar cal = Calendar.getInstance(Locale.CHINA);
        int month = cal.get(Calendar.MONTH) + 1;
        int year = cal.get(Calendar.YEAR);
        String now = "";
        String mop = "-";
        if (month == 1) {
            year = year - 1;
            month = 12;
        } else if (month < 11) {
            mop = mop + "0";
            month = month - 1;
        } else {
            month = month - 1;
        }
        now = year + mop + month;
        return now;
    }

    // 获取指定时间N天后的日期-YY-MM-DD
    public static String nextNDayStr(Date date, Integer num) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(YYMMDD);
        cal.setTime(date);
        cal.add(Calendar.DATE, num);
        // System.out.println("过N天后日期是：" + sdf.format(cal.getTime()));
        return sdf.format(cal.getTime());
    }

    // 获取指定时间N天前的日期-YY-MM-DD
    public static String prevNDayStr(Date date, Integer num) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(YYMMDD);
        cal.setTime(date);
        cal.add(Calendar.DATE, -num);
        // System.out.println("N天前日期是：" + sdf.format(cal.getTime()));
        return sdf.format(cal.getTime());
    }

    // 获取指定时间N天前的日期-YY-MM-DD
    public static String prevNDayStr(Date date, Integer num, String format) {
        if (StringUtils.isBlank(format)) {
            format = YYMMDD;
        }
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        cal.setTime(date);
        cal.add(Calendar.DATE, -num);
        // System.out.println("N天前日期是：" + sdf.format(cal.getTime()));
        return sdf.format(cal.getTime());
    }

    // 获取指定时间N天后的日期-YYMMDD_HHmmSS
    public static Date nextNDay(Date date, Integer num) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, num);
        // System.out.println("过N天后日期是：" + cal.getTime());
        return cal.getTime();
    }

    // 获取指定时间N天前的日期-YYMMDD_HHmmSS
    public static Date prevNDay(Date date, Integer num) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, -num);
        // System.out.println("过N天前日期是：" + cal.getTime());
        return cal.getTime();
    }

    /**
     * 获取指定时间N年后的日期
     *
     * @param date
     * @param yearNum
     * @return
     */
    public static Date nextDay4NYear(Date date, Integer yearNum) {
        Calendar calendar = Calendar.getInstance();// 创建实例
        calendar.setTime(date);
        calendar.add(Calendar.YEAR, yearNum);// N年之后的时间
        return calendar.getTime();
    }
    /**
     * 获取星期 0表示周日 1表示周一
     *
     * @param date 日期
     * @return
     */
    public static int getDayweekInteger(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_WEEK) - 1;
    }

    public static int getTwoMonthSub(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        int c = (cal1.get(Calendar.YEAR) - cal2.get(Calendar.YEAR)) * 12
                + cal1.get(Calendar.MONTH) - cal2.get(Calendar.MONTH);
        return c;
    }

    /**
     * 判断两个时间是否超过num小时
     *
     * @param num
     * @return
     * @throws Exception
     */
    public static boolean judgmentDate(Date start, Date end, int num) throws Exception {
        long cha = end.getTime() - start.getTime();
        if (cha < 0) {
            return false;
        }
        double result = cha * 1.0 / (1000 * 60 * 60);
        if (result <= num) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 计算过期
     *
     * @param date 起始时间
     * @param fmt  格式
     * @return
     */
    @SuppressWarnings("deprecation")
    public static Date getAfterManyDays(Date date, Integer months, String fmt) {
        if (null == fmt) {
            fmt = "yyyy/MM/dd";
        }
        if (null == date) {
            date = new Date();
        }
        SimpleDateFormat df = new SimpleDateFormat(fmt);
        date = new Date(df.format(date));
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DAY_OF_YEAR, 1);//今天不算过期日
        if (null == months) {
            return date;
        }
        c.add(Calendar.MONTH, months);
        return c.getTime();
    }

    /**
     * 获取年月日时分秒
     */
    public static String getYYMMDDHHmm(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(DateUtils.YYMMDDHHmm);
        return sdf.format(date);
    }

    /**
     * 获取年月日时分秒
     */
    public static String getYYMMDDHHmmss(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(DateUtils.YYMMDDHHmmss);
        return sdf.format(date);
    }


    /**
     * 时间戳转日期
     */
    public static Date timeStampToDate(Object obj) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(DateUtils.YYMMDD_HHmmSS);
        String date=sdf.format(obj);
        return sdf.parse(date);
    }

    /**
     * 时间戳转日期
     */
    public static Date timeStampToDateYYMMDD(Object obj) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(DateUtils.YYMMDD_HHmmSS);
        String date=sdf.format(obj);
        return sdf.parse(date);
    }

    public static void main(String[] args) {
        try {
            System.out.print(DateUtils.timeStampToDateYYMMDD(1507687810000L));
        }catch (Exception e){
            e.printStackTrace();
        }
    }


}

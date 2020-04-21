package com.example.commonlibs.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 时间工具类
 *
 * @author ZJ
 * created at 2019/11/25 10:58
 */
public class DateUtils {
    // 日期格式

    private static final String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private static final long ONE_DAY = 1000 * 60 * 60 * 24;

    /**
     * 获取年
     *
     * @return
     */
    public static int getYear() {
        Calendar cd = Calendar.getInstance();
        return cd.get(Calendar.YEAR);
    }

    /**
     * 获取月
     *
     * @return
     */
    public static int getMonth() {
        Calendar cd = Calendar.getInstance();
        return cd.get(Calendar.MONTH) + 1;
    }

    public static String getLastMonth() {

        if (getMonth() == 1) {
            return (getYear() - 1) + "/12/01 00:00:00";
        }
        if (getMonth()<10){

            return getYear() + "/0" + (getMonth() - 1) + "/01 00:00:00";
        }else {
            return getYear() + "/" + (getMonth() - 1) + "/01 00:00:00";
        }
    }

    /**
     * 获取日
     *
     * @return
     */
    public static int getDay() {
        Calendar cd = Calendar.getInstance();
        return cd.get(Calendar.DATE);
    }

    /**
     * 获取时
     *
     * @return
     */
    public static int getHour() {
        Calendar cd = Calendar.getInstance();
        return cd.get(Calendar.HOUR);
    }

    /**
     * 获取分
     *
     * @return
     */
    public static int getMinute() {
        Calendar cd = Calendar.getInstance();
        return cd.get(Calendar.MINUTE);
    }

    /**
     * 获取当前时间年月日
     *
     * @return
     */
    public static String getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        Date now = calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault());
        return sdf.format(now);
    }

    public static String getStartTime(String time) {
        return time += " 00:00:00";
    }

    public static String getEndTime(String time) {
        return time += "23:59:59";
    }

    /**
     * 获取当前时间  年月日 时分秒
     *
     * @return
     */
    public static String getCurrentTime() {
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss", Locale.getDefault());
        return sdf.format(date);
    }

    /**
     * 转换时间
     *
     * @param time
     * @return
     */
    public static String dateToFormat(long time) {
        Date date = new Date(time);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss", Locale.getDefault());
        return simpleDateFormat.format(date);
    }

    /**
     * 时间转换
     *
     * @param date
     * @return
     */
    public static String dateToFormat(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
        return simpleDateFormat.format(date);
    }

    /**
     * 时间转换
     *
     * @param date
     * @return
     */
    public static String dateToFormatActiveDeviceOrCollect(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
        return simpleDateFormat.format(date);
    }

    /**
     * 将日期格式的字符串转换为长整型
     *
     * @param date
     * @param format
     * @return
     */
    public static long convertToLong(String date, String format) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.getDefault());
            return formatter.parse(date).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 将长整型数字转换为日期格式的字符串
     *
     * @param time
     * @return
     */
    public static String convertToString(long time) {
        if (time > 0) {
            SimpleDateFormat formatter = new SimpleDateFormat(TIME_FORMAT, Locale.getDefault());
            Date date = new Date(time);
            return formatter.format(date);
        }
        return "";
    }

    /**
     * 获取当前时间是星期几
     *
     * @param dt
     * @return
     */
    public static String getWeekOfDate(Date dt) {
        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar cal = Calendar.getInstance();

        cal.setTime(dt);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;

        return weekDays[w];
    }

    /**
     * 获取某年某月有多少天
     */

    public static int getDayOfMonth(int year, int month) {
        Calendar c = Calendar.getInstance();
        c.set(year, month, 0); //输入类型为int类型
        return c.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 获取两个时间相差的天数
     *
     * @return time1 - time2相差的天数
     */
    public static int getDayOffset(long time1, long time2) {
        // 将小的时间置为当天的0点
        long offsetTime;
        if (time1 > time2) {
            offsetTime = time1 - getDayStartTime(getCalendar(time2)).getTimeInMillis();
        } else {
            offsetTime = getDayStartTime(getCalendar(time1)).getTimeInMillis() - time2;
        }
        return (int) (offsetTime / ONE_DAY);
    }

    private static Calendar getCalendar(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        return calendar;
    }

    private static Calendar getDayStartTime(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar;
    }

    /**
     * 将时间转化为时分秒
     *
     * @param ms
     * @return
     */
    public static String formatTime(Long ms) {
        int ss = 1000;
        int mi = ss * 60;
        int hh = mi * 60;
        int dd = hh * 24;

        long day = ms / dd;
        long hour = (ms - day * dd) / hh;
        long minute = (ms - day * dd - hour * hh) / mi;
        long second = (ms - day * dd - hour * hh - minute * mi) / ss;
        long milliSecond = ms - day * dd - hour * hh - minute * mi - second * ss;

        StringBuilder sb = new StringBuilder();
        if (day > 0) {
            sb.append(day).append("天");
        }
        if (hour > 0) {
            sb.append(hour).append("小时");
        }
        if (minute > 0) {
            sb.append(minute).append("分钟");
        }
        if (second > 0) {
            sb.append(second).append("秒");
        }
        if (milliSecond > 0) {
            sb.append(milliSecond).append("毫秒");
        }
        return sb.toString();
    }
}

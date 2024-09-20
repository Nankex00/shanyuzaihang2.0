package com.fushuhealth.recovery.common.util;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DateUtil {

    public static final String FORMAT_Y_M_D_H_M = "yyyy-MM-dd HH:mm";


    public static final String FORMAT_Y_M_D = "yyyy-MM-dd";

    public static final String FORMAT_Y_M = "yyyy-MM";

    public static final String FORMAT_Y_M_D_H_M_S = "yyyy-MM-dd HH:mm:ss";

    public static final String FORMAT_YMDHMS = "yyyyMMddHHmmss";

    public static final String FORMAT_H_M = "HH:mm";

    public static long getCurrentTimeStamp() {
        return System.currentTimeMillis() / 1000;
    }

    public static long getDateTimeStamp(Date date) {
        return date.getTime() / 1000;
    }

    public static long getDateTimeStamp(String date) {

        if (null == date) {
            return 0;
        }

        Date date1 = LocalDate.parse(date, DateTimeFormat.forPattern(FORMAT_Y_M_D_H_M)).toDate();

        return date1.getTime() / 1000;
    }


    public static long getDateTimeHN(String date) {

        Date date1 = LocalDate.parse(date, DateTimeFormat.forPattern(FORMAT_H_M)).toDate();

        return date1.getTime() / 1000;
    }

    public static String getYMDHMSDate(Long date) {

        return getFormatDate(new Date(date * 1000), FORMAT_Y_M_D_H_M_S);
    }

    public static String getYMDHMDate(Long date) {

        return getFormatDate(new Date(date * 1000), FORMAT_Y_M_D_H_M);
    }

    public static String getYMD(Long date) {

        return getFormatDate(new Date(date * 1000), FORMAT_Y_M_D);
    }

    public static String getYM(Long date) {

        return getFormatDate(new Date(date * 1000), FORMAT_Y_M);
    }

    public static final Long DEFAULT_SECOND_DATETIME = -1893484800L;

    private static String getFormatDate(Date date, String format) {

        if (date == null || date.getTime() / 1000 == DEFAULT_SECOND_DATETIME) {
            return "";
        }

        return new SimpleDateFormat(format).format(date);
    }

    public static String getNowYMDHMSM() {

        Long cuurentTime = System.currentTimeMillis();
        String YMDHMS = getFormatDate(new Date(cuurentTime), FORMAT_YMDHMS);

        String millis = String.valueOf(cuurentTime);
        millis = millis.substring(millis.length() - 3, millis.length());
        return YMDHMS + millis;
    }

    public static String getNowYMDHMS() {

        return getFormatDate(new Date(System.currentTimeMillis()), FORMAT_YMDHMS);
    }

    public static long getFirstDayOfNowMonth(Long time) {

        if (null == time || time == 0) {
            return 0l;
        }
        Date date = new Date(time * 1000);
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.MONTH, 0);
        //每月第一天
        c.set(Calendar.DAY_OF_MONTH, 1);
        //将小时至0
        c.set(Calendar.HOUR_OF_DAY, 0);
        //将分钟至0
        c.set(Calendar.MINUTE, 0);
        //将秒至0
        c.set(Calendar.SECOND, 0);
        //将毫秒至0
        c.set(Calendar.MILLISECOND, 0);
        return c.getTimeInMillis() / 1000;
    }

    public static long getLastDayOfNowMonth(Long time) {
        if (null == time || time == 0) {
            return 0l;
        }
        Date date = new Date(time * 1000);
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
        //将小时至0
        c.set(Calendar.HOUR_OF_DAY, 23);
        //将分钟至0
        c.set(Calendar.MINUTE, 59);
        //将秒至0
        c.set(Calendar.SECOND, 59);
        //将毫秒至0
        c.set(Calendar.MILLISECOND, 999);
        return c.getTimeInMillis() / 1000;
    }

    public static long getStartTimeOfDay(Long time) {

        if (null == time || time == 0) {
            return 0l;
        }
        Date date = new Date(time * 1000);
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        //将小时至0
        c.set(Calendar.HOUR_OF_DAY, 0);
        //将分钟至0
        c.set(Calendar.MINUTE, 0);
        //将秒至0
        c.set(Calendar.SECOND, 0);
        //将毫秒至0
        c.set(Calendar.MILLISECOND, 0);
        return c.getTimeInMillis() / 1000;
    }

    public static long getEndTimeOfDay(Long time) {

        if (null == time || time == 0) {
            return 0l;
        }
        Date date = new Date(time * 1000);
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        //将小时至0
        //将小时至0
        c.set(Calendar.HOUR_OF_DAY, 23);
        //将分钟至0
        c.set(Calendar.MINUTE, 59);
        //将秒至0
        c.set(Calendar.SECOND, 59);
        //将毫秒至0
        c.set(Calendar.MILLISECOND, 999);
        return c.getTimeInMillis() / 1000;
    }

    public static long getTimeAfterYear(Long time, int delta) {
        if (null == time || time == 0) {
            return 0l;
        }
        Date date = new Date(time * 1000);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.YEAR, delta);
        return calendar.getTimeInMillis() / 1000;
    }

    public static long getTimeAfterMonth(Long time, int delta) {
        if (null == time || time == 0) {
            return 0l;
        }
        Date date = new Date(time * 1000);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, delta);
        return calendar.getTimeInMillis() / 1000;
    }

    public static long getDaysBetweenTime(Long end, Long start) {
        Calendar endDate = Calendar.getInstance();
        endDate.setTime(new Date(end * 1000));
        java.time.LocalDateTime endLocalDate = java.time.LocalDateTime.
                of(endDate.get(Calendar.YEAR), endDate.get(Calendar.MONTH) + 1, endDate.get(Calendar.DAY_OF_MONTH), 0, 0);

        Calendar startDate = Calendar.getInstance();
        startDate.setTime(new Date(start * 1000));
        java.time.LocalDateTime startLocalDate = java.time.LocalDateTime.
                of(startDate.get(Calendar.YEAR), startDate.get(Calendar.MONTH) + 1, startDate.get(Calendar.DAY_OF_MONTH),0 ,0);

        Duration duration = Duration.between(startLocalDate, endLocalDate);

        long days = duration.toDays();


        return duration.toDays();
    }

    public static List<Long> getMonthBetween(Long minDate, Long maxDate) {
        List<Long> result = new ArrayList<Long>();

        Calendar min = Calendar.getInstance();
        Calendar max = Calendar.getInstance();

        min.setTime(new Date(minDate * 1000));
        min.set(min.get(Calendar.YEAR), min.get(Calendar.MONTH), 1);

        max.setTime(new Date(maxDate * 1000));
        max.set(max.get(Calendar.YEAR), max.get(Calendar.MONTH), 2);

        Calendar curr = min;
        while (curr.before(max)) {
            result.add(curr.getTime().getTime() / 1000);
            curr.add(Calendar.MONTH, 1);
        }

        return result;
    }

    public static boolean iSameYear(Long date1, Long date2) {
        Calendar min = Calendar.getInstance();
        Calendar max = Calendar.getInstance();

        min.setTime(new Date(date1 * 1000));

        max.setTime(new Date(date2 * 1000));

        return (min.get(Calendar.YEAR) == max.get(Calendar.YEAR));
    }


    public static void main(String[] args) {
        System.out.println(iSameYear(DateUtil.getCurrentTimeStamp(), 1546275661l));
    }
}

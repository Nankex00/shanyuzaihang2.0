package com.fushuhealth.recovery.common.util;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.Months;
import org.joda.time.format.DateTimeFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class OldDateUtil {

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
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_Y_M_D_H_M_S);
        try {
            Date date1 = sdf.parse(date);
            return date1.getTime() / 1000;
        } catch (ParseException e) {

        }
        return 0;
    }

    public static long getDateTimeHN(String date) {

        if (StringUtils.isBlank(date)) {
            return 0;
        }
        String[] split = date.split(":");
        int hour = Integer.parseInt(split[0]);
        int minute = Integer.parseInt(split[1]);
        return hour * 60 * 60 + minute * 60;
    }

    public static long getDateTimeYMD(String date) {

        Date date1 = LocalDate.parse(date, DateTimeFormat.forPattern(FORMAT_Y_M_D)).toDate();

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

    public static String getHM(Long date) {

        return getFormatDate(new Date(date * 1000), FORMAT_H_M);
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

    public static long getTimeAfterDay(Date time, int delta) {
        if (null == time) {
            return 0l;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(time);
        calendar.add(Calendar.DAY_OF_YEAR, delta - 1);
        return calendar.getTimeInMillis() / 1000;
    }

    public static long getTimeAfterDay(Long time, int delta) {
        if (null == time || time == 0) {
            return 0l;
        }
        Date date = new Date(time * 1000);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_YEAR, delta);
        return calendar.getTimeInMillis() / 1000;
    }

    public static long getTimeAfterWeek(Date time, int delta) {
        if (null == time) {
            return 0l;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(time);
        calendar.add(Calendar.WEEK_OF_YEAR, delta);
        calendar.add(Calendar.DAY_OF_YEAR, -1);
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

    public static int getMonthBetween(Long minDate, Long maxDate) {
//        List<Long> result = new ArrayList<Long>();
//
//
//        Calendar min = Calendar.getInstance();
//        min.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
//        Calendar max = Calendar.getInstance();
//        max.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
//
//        min.setTime(new Date(minDate * 1000));
//        min.set(min.get(Calendar.YEAR), min.get(Calendar.MONTH), 1);
//
//        max.setTime(new Date(maxDate * 1000));
//        max.set(max.get(Calendar.YEAR), max.get(Calendar.MONTH), 2);
//
//        Calendar curr = min;
//        while (curr.before(max)) {
//            Date time = curr.getTime();
//            result.add(time.getTime() / 1000);
//            curr.add(Calendar.MONTH, 1);
//        }

        DateTime start = new DateTime(minDate*1000);
        DateTime end = new DateTime(maxDate*1000);
        int months = Months.monthsBetween(start, end).getMonths();

        return months;
    }


    public static boolean iSameYear(Long date1, Long date2) {
        Calendar min = Calendar.getInstance();
        Calendar max = Calendar.getInstance();

        min.setTime(new Date(date1 * 1000));

        max.setTime(new Date(date2 * 1000));

        return (min.get(Calendar.YEAR) == max.get(Calendar.YEAR));
    }

    public static java.time.LocalDate dateToLocalDate(Long source) {
        String ymd = DateUtil.getYMD(source);
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        java.time.LocalDate localDate = java.time.LocalDate.parse(ymd, format);
        return localDate;
    }

    public static String secondToHMS(Long time) {
        long hour = 0;
        long minute = 0;
        long second = 0;
        if (time <= 0) {
            return "00:00";
        } else {
            if (time >= 3600) {
                hour = time / 3600;
                time = time % 3600;
            }

            if (time >= 60) {
                minute = time / 60;
                second = time % 60;
            } else {
                second = time;
            }
            return timeFormat(hour) + ":" + timeFormat(minute) + ":" + timeFormat(second);
        }
    }

    public static String timeFormat(long num) {
        String retStr = null;
        if (num >= 0 && num < 10) {
            retStr = "0" + Long.toString(num);
        } else {
            retStr = "" + num;
        }
        return retStr;
    }

    public static List<Long> getDaysBetweenTwoDayInWeek(long startTime, long endTime, List<Integer> week) {
        Calendar startCal = Calendar.getInstance();
        startCal.setTime(new Date(startTime * 1000));
        Calendar endCal = Calendar.getInstance();
        endCal.setTime(new Date(endTime * 1000));

        List<Integer> collect = week.stream().map(i -> {
            if(i == 6) {
                return 1;
            } else {
                return i + 2;
            }
        }).collect(Collectors.toList());

        ArrayList<Long> days = new ArrayList<>();
        while (startCal.before(endCal)) {
            int i = startCal.get(Calendar.DAY_OF_WEEK);
            if (collect.contains(i)) {
                days.add(startCal.getTime().getTime()/1000);
            }
            startCal.add(Calendar.DATE, 1);
        }
        if (collect.contains(endCal.get(Calendar.DAY_OF_WEEK))) {
            days.add(endTime);
        }
        return days;
    }

//    public int getDayOfWeek(long time) {
//        Calendar c=Calendar.getInstance();
//        c.setTime(new Date(time));
//        int weekday=c.get(Calendar.DAY_OF_WEEK);
//
//        //转为 js 的周几
//
//    }

    public static void main(String[] args) {

        System.out.println(getDateTimeHN("09:00"));
    }

    public static int getAgeByBirthday(Long birthday) {
        int age = 0;
        try {
            Calendar now = Calendar.getInstance();
            now.setTime(new Date());

            Calendar birth = Calendar.getInstance();
            birth.setTime(new Date(birthday * 1000));

            if (birth.after(now)) {
                return age;
            }
            age = now.get(Calendar.YEAR) - birth.get(Calendar.YEAR);
            if (now.get(Calendar.DAY_OF_YEAR) > birth.get(Calendar.DAY_OF_YEAR)) {
                age += 1;
            }
            return age;
        } catch (Exception e) {
            return 0;
        }
    }

    public static String getAge(Long birthday) {

        long month = (getCurrentTimeStamp() - birthday) / 60 / 60 / 24 / 30;
        if (month / 12 > 6) {
            return "6岁";
        } else {
            return (month / 12) + "岁" + (month % 12) + "月";
        }
    }

    //计算年龄，根据出生日期 10 位时间戳计算年龄，输出 x岁x月
    public static String calculateAge(long birthTimestamp, long nowTimestamp) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        java.time.LocalDate birthDate = java.time.LocalDate.parse(getYMD(birthTimestamp), formatter);
        java.time.LocalDate currentDate = java.time.LocalDate.parse(getYMD(nowTimestamp), formatter);

        long totalMonths = ChronoUnit.MONTHS.between(birthDate, currentDate);
        int years = (int) (totalMonths / 12);
        int months = (int) (totalMonths % 12);

        return String.format("%d岁%d月", years, months);
    }


    //判断日期是不是周末

    public static boolean isWeekend(String time) {
        java.time.LocalDate date = java.time.LocalDate.parse(time);
        return date.getDayOfWeek() == DayOfWeek.MONDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY;
    }
}

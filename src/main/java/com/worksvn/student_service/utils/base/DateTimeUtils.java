package com.worksvn.student_service.utils.base;

import java.util.Calendar;
import java.util.Date;

public class DateTimeUtils {
    private static final String DATE_FORMAT = "dd/MM/YYYY";

    private static final String MONTH = "tháng";
    private static final String DAY = "ngày";
    private static final String HOUR = "giờ";
    private static final String LEFT = "Còn";
    private static final String AGO = "trước";

    private static Integer HOUR_PER_DAY = 24;
    private static Integer MINUTE_PER_HOUR = 60;

    private static final long A_MONTH_IN_MILLIS = 2592000000L;
    private static final int A_DAY_IN_MILLIS = 86400000;
    private static final int AN_HOUR_IN_MILLIS = 3600000;

    public static boolean isExpired(Date expirationDate) {
        return expirationDate == null || expirationDate.getTime() - new Date().getTime() < 0;
    }

    public static String calculateTimeLeft(Date endTime) {
        return calculateTimeLeft(endTime, 2);
    }

    public static String calculateTimeLeft(Date endTime, int level) {
        String result = "";
        if (endTime == null) {
            return result;
        }
        int currentLevel = 0;

        long timeDiff = endTime.getTime() - new Date().getTime();
        String headPrefix = "";
        String tailPrefix = "";
        if (timeDiff > 0) {
            headPrefix = LEFT + " ";
        } else {
            tailPrefix = AGO;
        }
        long absTimeDiff = Math.abs(timeDiff);

        long month = absTimeDiff / A_MONTH_IN_MILLIS;
        if (month > 0) {
            result += month + " " + MONTH + " ";
            currentLevel++;
            if (currentLevel == level) {
                return headPrefix + result + tailPrefix;
            }
            absTimeDiff = absTimeDiff % A_MONTH_IN_MILLIS;
        }

        long day = absTimeDiff / A_DAY_IN_MILLIS;
        if (day > 0) {
            result += day + " " + DAY + " ";
            currentLevel++;
            if (currentLevel == level) {
                return headPrefix + result + tailPrefix;
            }
            absTimeDiff = absTimeDiff % A_DAY_IN_MILLIS;
        }

        long hour = absTimeDiff / AN_HOUR_IN_MILLIS;
        if (hour > 0) {
            result += hour + " " + HOUR + " ";
        }

        return (headPrefix + result + tailPrefix).trim();
    }

    public static int getTimeInMinute(int hour, int minute) throws InvalidTimeException {
        if (minute >= MINUTE_PER_HOUR) {
            hour += minute / MINUTE_PER_HOUR;
            minute = minute % MINUTE_PER_HOUR;
        }
        if (0 > hour || hour > HOUR_PER_DAY) {
            throw new InvalidTimeException("Hour must be from 0 to " + HOUR_PER_DAY);
        }
        return hour * MINUTE_PER_HOUR + minute;
    }

    public static class InvalidTimeException extends Exception {
        InvalidTimeException(String msg) {
            super(msg);
        }
    }

    public static Date addDayToDate(Date date, int numberOfDay) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, numberOfDay);
        return cal.getTime();
    }
}

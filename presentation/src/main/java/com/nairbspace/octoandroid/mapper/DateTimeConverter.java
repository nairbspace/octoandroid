package com.nairbspace.octoandroid.mapper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateTimeConverter {

    public static String msTimeToDateTimeString(long msTime) {
        Date date = new Date(msTime);
        DateFormat df = SimpleDateFormat.getDateTimeInstance();
        return df.format(date);
    }

    public static String msTimeToDateTimeString(Double msTime) {
        return msTimeToDateTimeString(msTime.longValue());
    }

    public static String unixTimeToDateTimeString(long unixTime) {
        return msTimeToDateTimeString(unixTime * 1000);
    }

    public static String unixTimeToDateTimeString(Double unixTime) {
        return unixTimeToDateTimeString(unixTime.longValue());
    }

    public static String msTimeToShortDateString(long msTime) {
        Date date = new Date(msTime);
        DateFormat df = SimpleDateFormat.getDateInstance(DateFormat.SHORT);
        return df.format(date);
    }

    public static String unixTimeToShortDateString(long unixTime) {
        return msTimeToShortDateString(unixTime * 1000);
    }

    public static String msTimeToShortTimeString(long msTime) {
        Date date = new Date(msTime);
        DateFormat df = SimpleDateFormat.getTimeInstance(DateFormat.SHORT);
        return df.format(date);
    }

    public static String unixTimeToShortTimeString(long unixTime) {
        return msTimeToShortTimeString(unixTime * 1000);
    }

    public static String unixTimeToHHmmss(long unixTime) {
        Date date = new Date(unixTime * 1000);
        DateFormat df = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        return df.format(date);
    }

    public static String secondsToHHmmss(long inputSeconds) {
        long absSeconds = Math.abs(inputSeconds);
        long hours = absSeconds / 3600;
        long secondsLeft = absSeconds - hours * 3600;
        long minutes = secondsLeft / 60;
        long seconds = secondsLeft - minutes * 60;

        String format = String.format(Locale.US, "%02d:%02d:%02d", hours, minutes, seconds);
        if (inputSeconds < 0) {
            return "-" + format;
        } else {
            return format;
        }
    }

    public static String secondsToHHmmss(Double inputSeconds) {
        return secondsToHHmmss(inputSeconds.longValue());
    }
}

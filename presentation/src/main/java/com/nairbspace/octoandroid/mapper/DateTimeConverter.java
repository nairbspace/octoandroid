package com.nairbspace.octoandroid.mapper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateTimeConverter {

    public static String millisecondsToDateTimeString(long milliseconds) {
        Date date = new Date(milliseconds);
        DateFormat df = SimpleDateFormat.getDateTimeInstance();
        return df.format(date);
    }

    public static String millisecondsToDateTimeString(Double milliseconds) {
        return millisecondsToDateTimeString(milliseconds.longValue());
    }

    public static String secondsToDateTimeString(long seconds) {
        return millisecondsToDateTimeString(seconds * 1000);
    }

    public static String secondsToDateTimeString(Double seconds) {
        return secondsToDateTimeString(seconds.longValue());
    }

    public static String millisecondsToShortDateString(long milliseconds) {
        Date date = new Date(milliseconds);
        DateFormat df = SimpleDateFormat.getDateInstance(DateFormat.SHORT);
        return df.format(date);
    }

    public static String secondsToShortDateString(long seconds) {
        return millisecondsToShortDateString(seconds * 1000);
    }

    public static String millisecondsToShortTimeString(long milliseconds) {
        Date date = new Date(milliseconds);
        DateFormat df = SimpleDateFormat.getTimeInstance(DateFormat.SHORT);
        return df.format(date);
    }

    public static String secondsToShortTimeString(long seconds) {
        return millisecondsToShortTimeString(seconds * 1000);
    }

    public static String unixSecondsToHHmmss(long seconds) {
        Date date = new Date(seconds * 1000);
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

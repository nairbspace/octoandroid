package com.nairbspace.octoandroid.mapper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.inject.Inject;

public class DateTimeConverter {

    @Inject
    public DateTimeConverter() {

    }

    public String millisecondsToDateTimeString(long milliseconds) {
        Date date = new Date(milliseconds);
        DateFormat df = SimpleDateFormat.getDateTimeInstance();
        return df.format(date);
    }

    public String millisecondsToDateTimeString(Double milliseconds) {
        return millisecondsToDateTimeString(milliseconds.longValue());
    }

    public String secondsToDateTimeString(long seconds) {
        return millisecondsToDateTimeString(seconds * 1000);
    }

    public String secondsToDateTimeString(Double seconds) {
        return secondsToDateTimeString(seconds.longValue());
    }

    public String secondsToHHmmss(long inputSeconds) {
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

    public String secondsToHHmmss(Double inputSeconds) {
        return secondsToHHmmss(inputSeconds.longValue());
    }
}

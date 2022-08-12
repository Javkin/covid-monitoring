package com.covid.monitoring.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtil {

    public static String getPreviousDayInFormat(LocalDateTime offsetDateTime) {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd").format(offsetDateTime.minusDays(1));
    }

    public static LocalDateTime parseUpdatedField(String updated) {
        return LocalDateTime.parse(updated, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}

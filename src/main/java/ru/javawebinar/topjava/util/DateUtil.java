package ru.javawebinar.topjava.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtil {
    private final static DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd h:mm");

    public static String dateFormat(LocalDateTime localDateTime) {
        return localDateTime.format(FORMATTER);
    }
}

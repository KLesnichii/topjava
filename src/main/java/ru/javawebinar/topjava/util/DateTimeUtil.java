package ru.javawebinar.topjava.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static <T extends Comparable<T>> boolean isBetweenInclusive(T lt, T startTime, T endTime) {
        return lt.compareTo(startTime) >= 0 && lt.compareTo(endTime) <= 0;
    }

    public static String toString(LocalDateTime ldt) {
        return ldt == null ? "" : ldt.format(DATE_TIME_FORMATTER);
    }

    public static LocalDate checkStart(LocalDate ldt) {
        return ldt == null ? LocalDate.MIN : ldt;
    }

    public static LocalDate checkEnd(LocalDate ldt) {
        return ldt == null ? LocalDate.MAX : ldt;
    }

    public static LocalTime checkStart(LocalTime ldt) {
        return ldt == null ? LocalTime.MIN : ldt;
    }

    public static LocalTime checkEnd(LocalTime ldt) {
        return ldt == null ? LocalTime.MAX : ldt;
    }
}


package ru.javawebinar.topjava.web;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalTime;

@Component
public final class TimeConverter implements Converter<String, LocalTime> {
    @Override
    public LocalTime convert(String source) {
        return parseLocalTime(source);
    }
}


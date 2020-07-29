package ru.javawebinar.topjava.web;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalDate;

@Component
public final class DataConverter implements Converter<String, LocalDate> {
    @Override
    public LocalDate convert(String source) {
        return parseLocalDate(source);
    }
}


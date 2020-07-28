package ru.javawebinar.topjava.web;

import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalTime;

@Component
public class TimeConverterFactory implements ConverterFactory<String, LocalTime> {
    @Override
    public <T extends LocalTime> Converter<String, T> getConverter(Class<T> targetType) {
        return new TimeConverter<>();
    }

    private static final class TimeConverter<T extends LocalTime> implements Converter<String, T> {
        @Override
        public T convert(String source) {
            return (T) parseLocalTime(source);
        }
    }
}

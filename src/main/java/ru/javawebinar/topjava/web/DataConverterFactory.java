package ru.javawebinar.topjava.web;

import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;
import org.springframework.stereotype.Component;


import java.time.LocalDate;

import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalDate;

@Component
public class DataConverterFactory implements ConverterFactory<String, LocalDate> {
    @Override
    public <T extends LocalDate> Converter<String, T> getConverter(Class<T> targetType) {
        return new DataConverter<>();
    }

    private static final class DataConverter<T extends LocalDate> implements Converter<String, T> {
        @Override
        public T convert(String source) {
            return (T) parseLocalDate(source);
        }
    }
}

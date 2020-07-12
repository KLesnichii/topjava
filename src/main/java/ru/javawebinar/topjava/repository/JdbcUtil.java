package ru.javawebinar.topjava.repository;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.Set;

public class JdbcUtil {
    public static <T> void validate(T t, Validator validator) {
        Set<ConstraintViolation<T>> constraintViolations = validator
                .validate(t);

        if (!constraintViolations.isEmpty()) {
            throw new ConstraintViolationException(constraintViolations);
        }
    }
}

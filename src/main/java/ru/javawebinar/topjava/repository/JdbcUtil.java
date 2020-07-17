package ru.javawebinar.topjava.repository;

import javax.validation.*;
import java.util.Set;

public class JdbcUtil {
    public static <T> void validate(T t) {
        Set<ConstraintViolation<T>> constraintViolations = Validation.
                buildDefaultValidatorFactory().
                getValidator().
                validate(t);

        if (!constraintViolations.isEmpty()) {
            throw new ConstraintViolationException(constraintViolations);
        }
    }
}

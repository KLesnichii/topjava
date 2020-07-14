package ru.javawebinar.topjava.repository;

import javax.validation.*;
import java.util.Set;

public class JdbcUtil {
    public final static Validator VALIDATOR = Validation.buildDefaultValidatorFactory().getValidator();


    public static <T> void validate(T t, Validator validator) {
        Set<ConstraintViolation<T>> constraintViolations = validator
                .validate(t);

        if (!constraintViolations.isEmpty()) {
            throw new ConstraintViolationException(constraintViolations);
        }
    }
}

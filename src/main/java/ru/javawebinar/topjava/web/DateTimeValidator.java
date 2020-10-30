package ru.javawebinar.topjava.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.util.Util;

import java.util.List;
import java.util.Optional;

@Component
public class DateTimeValidator implements Validator {

    @Autowired
    MealService mealService;

    @Override
    public boolean supports(Class<?> clazz) {
        return Meal.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Meal meal = (Meal) target;
        if (meal.getDateTime() != null) {
            List<Meal> meals = mealService.getBetweenInclusive(meal.getDate(), meal.getDate(), SecurityUtil.authUserId());
            if (!meals.isEmpty()) {
                Optional<Meal> mealForThisDate = meals.stream().filter(m -> Util.isBetweenHalfOpen(m.getTime(), meal.getTime(), meal.getTime().plusMinutes(1))).findFirst();
                if (mealForThisDate.isPresent() && !mealForThisDate.get().getId().equals(meal.getId())) {
                    errors.rejectValue("dateTime", "meal.dateTimeAlreadyExists", "meal.dateTimeAlreadyExists");
                }
            }
        }
    }
}

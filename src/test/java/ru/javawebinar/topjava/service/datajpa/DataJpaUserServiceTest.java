package ru.javawebinar.topjava.service.datajpa;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;

import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.UserServiceTest;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.util.List;

import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.*;
import static ru.javawebinar.topjava.UserTestData.getNew;

@ActiveProfiles(Profiles.DATAJPA)
public class DataJpaUserServiceTest extends UserServiceTest {

    @Test
    public void getWithMeals() {
        User actual = service.getWithMeal(USER_ID);
        USER_MATCHER.assertMatch(actual, USER);
        MEAL_MATCHER.assertMatch(actual.getMeals(), MEALS);
    }

    @Test
    public void getWithMealsNotFound() throws Exception {
        Assert.assertThrows(NotFoundException.class,
                () -> service.getWithMeal(1));
    }

    @Test
    public void getWithMealsForUserWithoutMeals() {
        User created = service.create(getNew());
        User actual = service.getWithMeal(created.getId());
        USER_MATCHER.assertMatch(actual, created);
        MEAL_MATCHER.assertMatch(actual.getMeals(), List.of());
    }
}

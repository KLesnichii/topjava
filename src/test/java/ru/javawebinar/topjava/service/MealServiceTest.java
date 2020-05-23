package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static ru.javawebinar.topjava.MealTestData.*;


@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    static {
        // Only for postgres driver logging
        // It uses java.util.logging and logged via jul-to-slf4j bridge
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService service;

    @Test
    public void get() {
        Meal meal = service.get(MEAL4_USER.getId(), USER_ID);
        assertMatch(meal, MEAL4_USER);
    }

    @Test(expected = NotFoundException.class)
    public void getNotFound() throws Exception {
        service.get(1, USER_ID);
    }

    @Test(expected = NotFoundException.class)
    public void getWrongOwner() throws Exception {
        service.get(MEAL2_ADMIN.getId(), USER_ID);
    }

    @Test(expected = NotFoundException.class)
    public void delete() {
        service.delete(MEAL4_USER.getId(), USER_ID);
        service.get(MEAL4_USER.getId(), USER_ID);
    }


    @Test(expected = NotFoundException.class)
    public void deletedNotFound() throws Exception {
        service.delete(1, USER_ID);
    }

    @Test(expected = NotFoundException.class)
    public void deletedWrongOwner() throws Exception {
        service.delete(MEAL1_ADMIN.getId(), USER_ID);
    }

    @Test
    public void getBetweenHalfOpen() {
        List<Meal> all = service.getBetweenHalfOpen(LocalDate.of(2020, Month.JANUARY, 30), LocalDate.of(2020, Month.JANUARY, 30), USER_ID);
        assertMatch(all, MEAL3_USER, MEAL2_USER, MEAL1_USER);
    }

    @Test
    public void getAll() {
        List<Meal> all = service.getAll(USER_ID);
        assertMatch(all, MEAL7_USER, MEAL6_USER, MEAL5_USER, MEAL4_USER, MEAL3_USER, MEAL2_USER, MEAL1_USER);
    }

    @Test
    public void update() {
        Meal updated = getUpdated();
        service.update(updated, USER_ID);
        assertMatch(service.get(updated.getId(), USER_ID), updated);
    }

    @Test(expected = NotFoundException.class)
    public void updateNotFound() {
        Meal updated = getUpdated();
        updated.setId(1);
        service.update(updated, USER_ID);
    }

    @Test(expected = NotFoundException.class)
    public void updateWrongOwner() {
        Meal updated = getUpdated();
        service.update(updated, ADMIN_ID);
    }

    @Test(expected = DataAccessException.class)
    public void duplicateDateTimeUpdate() {
        Meal updated = getUpdated();
        updated.setDateTime(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0));
        service.update(updated, USER_ID);
    }

    @Test
    public void create() {
        Meal newMeal = getNew();
        Meal created = service.create(newMeal, USER_ID);
        Integer newId = created.getId();
        newMeal.setId(newId);
        assertMatch(created, newMeal);
        assertMatch(service.get(newId, USER_ID), newMeal);
    }

    @Test(expected = DataAccessException.class)
    public void duplicateDateTimeCreate() {
        Meal newMeal = getNew();
        newMeal.setDateTime(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0));
        service.create(newMeal, USER_ID);
    }
}
package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class DataJpaMealRepository implements MealRepository {
    private static final Sort SORT_GET_ALL = Sort.by(Sort.Direction.DESC, "dateTime");

    @Autowired
    private CrudMealRepository mealCrudRepository;

    @Autowired
    private CrudUserRepository userCrudRepository;

    @Override
    public Meal save(Meal meal, int userId) {
        meal.setUser(userCrudRepository.getOne(userId));
        if (meal.isNew()) {
            return mealCrudRepository.save(meal);
        } else {
            return mealCrudRepository.update(meal.getId(), userId,
                    meal.getDateTime(), meal.getDescription(), meal.getCalories()) != 0 ? meal : null;
        }
    }

    @Override
    public boolean delete(int id, int userId) {
        return mealCrudRepository.delete(id, userId) != 0;
    }

    @Override
    public Meal get(int id, int userId) {
        return mealCrudRepository.findByIdAndUserId(id, userId);
    }

    @Override
    public List<Meal> getAll(int userId) {
        return mealCrudRepository.getAll(userId, SORT_GET_ALL);
    }

    @Override
    public List<Meal> getBetweenHalfOpen(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
        return mealCrudRepository.getBetweenHalfOpen(
                userId, startDateTime, endDateTime, SORT_GET_ALL);
    }

    @Override
    public Meal getWithUser(int id, int userId) {
        return mealCrudRepository.findWithUser(id, userId);
    }
}
package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private Map<Integer, ConcurrentHashMap<Integer, Meal>> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.MEALS.forEach(meal -> save(meal, 1));
    }

    @Override
    public Meal save(Meal meal, int userId) {
        ConcurrentHashMap<Integer, Meal> mealMap = repository.get(userId);
        if (meal.isNew()) {
            if (mealMap == null) {
                mealMap = new ConcurrentHashMap<>();
            }
            meal.setId(counter.incrementAndGet());
            mealMap.put(meal.getId(), meal);
            repository.put(userId, mealMap);
            return meal;
        }
        return mealMap != null ? mealMap.computeIfPresent(meal.getId(), (id, oldMeal) -> meal) : null;
    }

    @Override
    public boolean delete(int id, int userId) {
        ConcurrentHashMap<Integer, Meal> mealMap = repository.get(userId);
        return mealMap != null && mealMap.remove(id) != null;
    }

    @Override
    public Meal get(int id, int userId) {
        ConcurrentHashMap<Integer, Meal> mealMap = repository.get(userId);
        return (mealMap != null) ? mealMap.get(id) : null;
    }

    @Override
    public Collection<Meal> getAll(int userId) {
        return getFiltered(userId, meal -> true);
    }


    public Collection<Meal> getAll(int userId, LocalDate startDate, LocalDate endDate) {
        return getFiltered(userId, meal -> DateTimeUtil.isBetweenInclusive(meal.getDate(), startDate, endDate));
    }

    private Collection<Meal> getFiltered(int userId, Predicate<Meal> filter) {
        ConcurrentHashMap<Integer, Meal> mealMap = repository.get(userId);
        if (mealMap != null) {
            return mealMap.values().stream()
                    .filter(filter)
                    .sorted(Comparator
                            .comparing(Meal::getDateTime)
                            .reversed())
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }
}


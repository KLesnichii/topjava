package ru.javawebinar.topjava.storage;

import ru.javawebinar.topjava.model.Meal;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MapMealStorage implements Storage {

    private final Map<String, Meal> storage;

    public MapMealStorage() {
        storage = new ConcurrentHashMap<>();
    }

    public MapMealStorage(Map<String, Meal> map) {
        storage = new ConcurrentHashMap<>(map);
    }

    @Override
    public void clear() {
        storage.clear();
    }

    @Override
    public void save(Meal meal) {
        storage.put(meal.getId(), meal);
    }

    @Override
    public void update(Meal meal) {
        storage.put(meal.getId(), meal);
    }

    @Override
    public Meal get(String id) {
        return storage.get(id);
    }

    @Override
    public void delete(String id) {
        storage.remove(id);
    }

    @Override
    public int size() {
        return storage.size();
    }

    @Override
    public List<Meal> getAll() {
        return new ArrayList<>(storage.values());
    }
}

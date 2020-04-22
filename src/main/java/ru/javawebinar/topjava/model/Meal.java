package ru.javawebinar.topjava.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

public class Meal {
    private final String id;

    private final LocalDateTime dateTime;

    private final String description;

    private final int calories;

    public Meal() {
        id = "";
        dateTime = null;
        description = null;
        calories = 0;
    }

    public Meal(LocalDateTime dateTime, String description, int calories) {
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
        this.id = UUID.randomUUID().toString();
    }

    public Meal(String id, LocalDateTime dateTime, String description, int calories) {
        this.id = id;
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
    }

    public synchronized String getId() {
        return id;
    }

    public synchronized LocalDateTime getDateTime() {
        return dateTime;
    }

    public synchronized String getDescription() {
        return description;
    }

    public synchronized int getCalories() {
        return calories;
    }

    public LocalDate getDate() {
        return dateTime.toLocalDate();
    }

    public LocalTime getTime() {
        return dateTime.toLocalTime();
    }
}

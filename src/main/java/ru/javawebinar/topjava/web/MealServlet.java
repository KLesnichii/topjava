package ru.javawebinar.topjava.web;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.storage.MapMealStorage;
import ru.javawebinar.topjava.storage.Storage;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static ru.javawebinar.topjava.model.Meal.EMPTY_MEAL;

public class MealServlet extends HttpServlet {
    private Storage storage;

    @Override
    public void init() throws ServletException {
        storage = new MapMealStorage();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        int id = Integer.parseInt(req.getParameter("id"));
        LocalDateTime dateTime = LocalDateTime.parse(req.getParameter("dateTime"));
        String description = req.getParameter("description");
        int calories = Integer.parseInt(req.getParameter("calories"));
        Meal meal;
        if (id == 0) {
            meal = new Meal(dateTime, description, calories);
            storage.save(meal);
        } else {
            meal = new Meal(id, dateTime, description, calories);
            storage.update(meal);
        }
        resp.sendRedirect("meals");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");
        String action = req.getParameter("action");

        switch (action == null ? "" : action) {
            case "delete":
                storage.delete(Integer.parseInt(id));
                resp.sendRedirect("meals");
                return;
            case "edit":
                req.setAttribute("meal", storage.get(Integer.parseInt(id)));
                req.getRequestDispatcher("/WEB-INF/jsp/edit.jsp")
                        .forward(req, resp);
                return;
            case "add":
                req.setAttribute("meal", EMPTY_MEAL);
                req.getRequestDispatcher("/WEB-INF/jsp/edit.jsp").forward(req, resp);
                return;
            default:
                req.setAttribute("meals", MealsUtil.filteredByStreams(storage.getAll(), LocalTime.MIN, LocalTime.MAX, 2000));
                req.getRequestDispatcher("/WEB-INF/jsp/meals.jsp").forward(req, resp);
        }
    }
}

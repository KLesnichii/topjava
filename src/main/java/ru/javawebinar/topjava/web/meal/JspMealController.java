package ru.javawebinar.topjava.web.meal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.javawebinar.topjava.service.MealService;

@Controller
public class JspMealController {
//    @Autowired
//    private MealService service;
//
//    @GetMapping("/meal")
//    public String getMeals(Model model) {
//        model.addAttribute("users", service.getAll());
//        return "users";
//    }
}

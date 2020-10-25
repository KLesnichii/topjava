package ru.javawebinar.topjava.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.UserService;
import ru.javawebinar.topjava.util.UserUtil;
import ru.javawebinar.topjava.util.ValidationUtil;

@Component
public class UserEmailValidator implements Validator {

    @Autowired
    UserService userService;

    @Autowired
    Validator validator;

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;
        validator.validate(user, ValidationUtil.emailExistsValidate(UserUtil.asTo(user), errors, userService));
    }
}

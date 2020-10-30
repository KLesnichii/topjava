package ru.javawebinar.topjava.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.UserService;
import ru.javawebinar.topjava.to.UserTo;
import ru.javawebinar.topjava.util.UserUtil;
import ru.javawebinar.topjava.util.exception.NotFoundException;

@Component
public class EmailValidator implements Validator {

    @Autowired
    UserService userService;

    @Override
    public boolean supports(Class<?> clazz) {
        return UserTo.class.equals(clazz) || User.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserTo userTo;
        if (target instanceof User) {
            userTo = UserUtil.asTo((User) target);
        } else {
            userTo = (UserTo) target;
        }
        if (userTo.getEmail() != null && !userTo.getEmail().equals("")) {
            try {
                User user = userService.getByEmail(userTo.getEmail());
                if (!user.getId().equals(userTo.getId())) {
                    errors.rejectValue("email", "user.emailAlreadyExists", "user.emailAlreadyExists");
                }
            } catch (NotFoundException ignored) {
            }
        }
    }
}

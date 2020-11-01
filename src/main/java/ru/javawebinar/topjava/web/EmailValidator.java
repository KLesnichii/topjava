package ru.javawebinar.topjava.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;
import ru.javawebinar.topjava.to.UserTo;
import ru.javawebinar.topjava.util.UserUtil;

@Component
public class EmailValidator implements Validator {

    @Autowired
    UserRepository repository;

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
            User user = repository.getByEmail(userTo.getEmail());
            if (user != null && !user.getId().equals(userTo.getId())) {
                errors.rejectValue("email", "user.emailAlreadyExists");
            }
        }
    }
}

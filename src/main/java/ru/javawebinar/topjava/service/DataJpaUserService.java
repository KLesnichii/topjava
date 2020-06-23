package ru.javawebinar.topjava.service;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFoundWithId;

@Service
@Profile({Profiles.DATAJPA})
public class DataJpaUserService extends UserService {
    public DataJpaUserService(UserRepository repository) {
        super(repository);
    }

    public User getWithMeal(int id) {
        return checkNotFoundWithId(repository.getWithMeals(id), id);
    }
}

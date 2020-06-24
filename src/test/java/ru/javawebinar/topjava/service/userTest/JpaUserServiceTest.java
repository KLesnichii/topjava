package ru.javawebinar.topjava.service.userTest;

import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;

@ActiveProfiles(Profiles.JPA)
public class JpaUserServiceTest extends UserServiceTest {
}

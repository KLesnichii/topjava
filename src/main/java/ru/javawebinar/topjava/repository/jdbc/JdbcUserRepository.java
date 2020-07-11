package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

@Repository
@Transactional(readOnly = true)
public class JdbcUserRepository implements UserRepository {

    private static final BeanPropertyRowMapper<User> ROW_MAPPER = BeanPropertyRowMapper.newInstance(User.class);

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;

    @Autowired
    public JdbcUserRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.insertUser = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    @Transactional
    public User save(User user) {
        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);
        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            user.setId(newKey.intValue());
            System.out.println(user.getId());
        } else if (namedParameterJdbcTemplate.update(
                "UPDATE users SET name=:name, email=:email, password=:password, " +
                        "registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id", parameterSource) == 0) {
            return null;
        }
        return user;
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }

    @Override
    public User get(int id) {
        List<User> users = jdbcTemplate.query("SELECT * FROM users INNER JOIN user_roles ON users.id = user_roles.user_id WHERE id=?", new SetExtractor(), id);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public User getByEmail(String email) {
//        return jdbcTemplate.queryForObject("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        List<User> users = jdbcTemplate.query("SELECT * FROM users INNER JOIN user_roles ON users.id = user_roles.user_id WHERE email=?", new SetExtractor(), email);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public List<User> getAll() {
        return jdbcTemplate.query("SELECT * " +
                        "FROM users INNER JOIN user_roles ON users.id = user_roles.user_id " +
                        "ORDER BY name, email",
                new SetExtractor());
    }

    private static class SetExtractor implements ResultSetExtractor<List<User>> {
        @Override
        public List<User> extractData(ResultSet rs) throws SQLException, DataAccessException {
            Map<Integer, User> map = new HashMap<>();
            AtomicInteger i = new AtomicInteger(0);
            while (rs.next()) {
                Role role = Role.valueOf(rs.getString("role"));
                User user = map.computeIfAbsent(rs.getInt("id"), id -> getMapRow(rs, i));
                if (Objects.nonNull(user)) {
                    if (Objects.isNull(user.getRoles())) {
                        user.setRoles(Set.of());
                    }
                    user.getRoles().addAll(Set.of(role));
                }
                i.incrementAndGet();
            }
            return new ArrayList<>(map.values());
        }
    }

    private static User getMapRow(ResultSet rs, AtomicInteger i) {
        try {
            return ROW_MAPPER.mapRow(rs, i.get());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}

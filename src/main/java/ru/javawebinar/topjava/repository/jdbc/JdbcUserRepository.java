package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
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

import javax.validation.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static ru.javawebinar.topjava.repository.JdbcUtil.validate;

@Repository
@Transactional(readOnly = true)
public class JdbcUserRepository implements UserRepository {

    private static final BeanPropertyRowMapper<User> ROW_MAPPER = BeanPropertyRowMapper.newInstance(User.class);

    private final SetExtractor SET_EXTRACTOR = new SetExtractor();

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;

    private final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = validatorFactory.getValidator();

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
        validate(user, validator);
        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);
        List<Role> roles = new ArrayList<>(user.getRoles());
        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            user.setId(newKey.intValue());
        } else if (namedParameterJdbcTemplate.update(
                "UPDATE users SET name=:name, email=:email, password=:password, " +
                        "registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay " +
                        "WHERE id=:id", parameterSource) != 0) {
            jdbcTemplate.update("DELETE FROM user_roles WHERE user_id=?", user.getId());
        } else {
            return null;
        }
        batchUpdate(roles, user.getId());
        return user;
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }

    @Override
    public User get(int id) {
        List<User> users = jdbcTemplate.query("SELECT * " +
                "FROM users LEFT JOIN  user_roles ON users.id = user_roles.user_id " +
                "WHERE id=?", SET_EXTRACTOR, id);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public User getByEmail(String email) {
//        return jdbcTemplate.queryForObject("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        List<User> users = jdbcTemplate.query("SELECT * " +
                "FROM users LEFT JOIN user_roles ON users.id = user_roles.user_id" +
                " WHERE email=?", SET_EXTRACTOR, email);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public List<User> getAll() {
        return jdbcTemplate.query("SELECT * " +
                        "FROM users LEFT JOIN user_roles ON users.id = user_roles.user_id " +
                        "ORDER BY name, email",
                SET_EXTRACTOR);
    }

    private class SetExtractor implements ResultSetExtractor<List<User>> {
        @Override
        public List<User> extractData(ResultSet rs) throws SQLException, DataAccessException {
            Map<Integer, User> map = new HashMap<>();
            AtomicInteger rowCount = new AtomicInteger(0);
            while (rs.next()) {
                Role role = Role.valueOf(rs.getString("role"));
                User user = map.computeIfAbsent(rs.getInt("id"), id -> {
                    User newUser = getMapRow(rs, rowCount.get());
                    newUser.setRoles(Set.of());
                    return newUser;
                });
                user.getRoles().addAll(Set.of(role));
                rowCount.incrementAndGet();
            }
            return new ArrayList<>(map.values());
        }
    }

    private static User getMapRow(ResultSet rs, Integer rowCount) {
        try {
            return ROW_MAPPER.mapRow(rs, rowCount);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void batchUpdate(List<Role> roles, Integer user_id) {
        jdbcTemplate.batchUpdate("INSERT INTO user_roles (user_id, role) VALUES (?,?)", new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setInt(1, user_id);
                ps.setString(2, roles.get(i).toString());
            }

            @Override
            public int getBatchSize() {
                return roles.size();
            }
        });
    }
}

package com.example.seabattleserver.dao;

import java.util.Optional;

import com.example.seabattleserver.model.User;
import jakarta.security.auth.message.AuthException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import static java.util.Optional.ofNullable;

@Repository
@RequiredArgsConstructor
public class UserDao {

    private final JdbcTemplate jdbcTemplate;

    private static final String INSERT_USER = """
            insert into users (username, password) values (?, ?);
            """;

    private static final String SELECT_USER_BY_USERNAME = """
            select username, password from users where username = ?;
            """;

    private static final RowMapper<User> USER_ROW_MAPPER = (rs, rn) ->
            User.builder()
                    .username(rs.getString("username"))
                    .password(rs.getString("password"))
                    .build();

    public int registerUser(User user) {
        Optional<User> userInDb = getUserByLogin(user.getUsername());
        if (userInDb.isPresent()) {
            throw new IllegalArgumentException("User with that login already exist");
        }
        return jdbcTemplate.update(INSERT_USER,
                user.getUsername(),
                user.getPassword());
    }

    private Optional<User> getUserByLogin(String login) {
        return ofNullable(DataAccessUtils.singleResult(jdbcTemplate.query(SELECT_USER_BY_USERNAME,
                USER_ROW_MAPPER,
                login)));
    }

    public boolean login(User user) throws AuthException {
        Optional<User> userInDb = getUserByLogin(user.getUsername());
        if (userInDb.isEmpty()) {
            throw new AuthException("No user registered with login " + user.getUsername());
        }
        return userInDb.get().getPassword().equals(user.getPassword());
    }
}

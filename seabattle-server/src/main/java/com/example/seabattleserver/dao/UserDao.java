package com.example.seabattleserver.dao;

import com.example.seabattleserver.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserDao {

    private final JdbcTemplate jdbcTemplate;

    private static final String INSERT_USER = """
            insert into user (username, password) values (?, ?) returning id;
            """;

    public Long registerUser(User user) {
        return DataAccessUtils.singleResult(jdbcTemplate.query(INSERT_USER,
                (rs, rn) -> rs.getLong("id"),
                user.getId(),
                user.getUsername(),
                user.getPassword()));
    }

    public boolean
}

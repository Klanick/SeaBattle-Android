package com.example.seabattleserver.service;

import java.util.Optional;

import com.example.seabattleserver.dao.UserDao;
import com.example.seabattleserver.model.User;
import jakarta.security.auth.message.AuthException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Service;

import static java.util.Optional.ofNullable;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserDao userDao;

    public boolean registerUser(User user) {
        int result = userDao.registerUser(user);
        if (result != 1) {
            throw new IllegalStateException("Can't register user " + user.getUsername());
        }
        return true;
    }

    public boolean login(User user) throws AuthException {
        return userDao.login(user);
    }
}

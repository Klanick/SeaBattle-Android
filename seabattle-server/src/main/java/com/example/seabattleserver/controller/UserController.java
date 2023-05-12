package com.example.seabattleserver.controller;

import com.example.seabattleserver.model.BooleanResponse;
import com.example.seabattleserver.model.User;
import com.example.seabattleserver.service.UserService;
import jakarta.security.auth.message.AuthException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<BooleanResponse> registerUser(@RequestBody User user) {
        try {
            if (userService.registerUser(user)) {
                return ResponseEntity.ok(BooleanResponse.of(true));
            }
            return ResponseEntity.ok(BooleanResponse.error("Unexpected error"));
        } catch (IllegalStateException | IllegalArgumentException e) {
            return ResponseEntity.ok(BooleanResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<BooleanResponse> login(@RequestBody User user) {
        try {
            if (userService.login(user)) {
                return ResponseEntity.ok(BooleanResponse.of(true));
            }
            return ResponseEntity.ok(BooleanResponse.error("Unexpected error"));
        } catch (AuthException e) {
            return ResponseEntity.ok(BooleanResponse.error(e.getMessage()));
        }
    }
}

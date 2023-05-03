package com.example.seabattleserver.model;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.lang.NonNull;

@Getter
@Setter
@Builder
@RequiredArgsConstructor
public class User {

    @NonNull
    Long id;

    @NonNull
    String username;

    @NonNull
    String password;
}

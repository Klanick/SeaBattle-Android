package com.example.seabattleserver.model;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty
    String username;

    @NonNull
    @JsonProperty
    String password;
}

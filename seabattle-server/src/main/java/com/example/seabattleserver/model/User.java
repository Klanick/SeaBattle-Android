package com.example.seabattleserver.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.lang.NonNull;

@Getter
@Setter
@Builder
@RequiredArgsConstructor
public class User {

    @NonNull
    @JsonProperty
    @Length(min = 2, max = 15)
    String username;

    @NonNull
    @JsonProperty
    @NotEmpty
    @Pattern(regexp = "[A-Za-z0-9_\\-!@#$%]+")
    String password;
}

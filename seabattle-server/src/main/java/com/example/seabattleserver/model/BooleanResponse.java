package com.example.seabattleserver.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record BooleanResponse(@JsonProperty boolean response, @JsonProperty String errorMessage) {

    public static BooleanResponse of(boolean value) {
        return new BooleanResponse(value, null);
    }

    public static BooleanResponse error(String message) {
        return new BooleanResponse(false, message);
    }
}

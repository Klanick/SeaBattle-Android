package com.example.seabattleserver.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record BooleanResponse(@JsonProperty boolean response) {

    public static BooleanResponse of(boolean value) {
        return new BooleanResponse(value);
    }
}

package com.example.seabattleserver.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.lang.NonNull;

@RequiredArgsConstructor
@Builder
@Getter
@Setter
public class Statistic {

    @NonNull
    @JsonProperty
    private final Long userId;

    @NonNull
    @Builder.Default
    @JsonProperty
    private final Long totalGames = 0L;

    @NonNull
    @Builder.Default
    @JsonProperty
    private final Long totalWins = 0L;

    @NonNull
    @Builder.Default
    @JsonProperty
    private final Long totalLoses = 0L;

    @NonNull
    @Builder.Default
    @JsonProperty
    private final Long totalShipsDestroyed = 0L;

    @NonNull
    @Builder.Default
    @JsonProperty
    private final Long totalShipsLost = 0L;
}

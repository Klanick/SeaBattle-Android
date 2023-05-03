package com.example.seabattleserver.dao;

import java.util.Optional;

import com.example.seabattleserver.model.Statistic;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import static java.util.Optional.ofNullable;

@Repository
@RequiredArgsConstructor
public class StatisticDao {

    private final JdbcTemplate jdbcTemplate;

    private static final String CREATE_STATISTIC = """
            insert into statistic (
                user_id,
                total_games,
                total_wins,
                total_loses,
                total_ships_destroyed,
                total_ships_lost
                ) values (
                    ?,
                    0,
                    0,
                    0,
                    0,
                    0
                )
            """;

    private static final String SELECT_STATISTIC = """
            select
                user_id,
                total_games,
                total_wins,
                total_loses,
                total_ships_destroyed,
                total_ships_lost
            from statistic
            where user_id = ?
            """;

    private static final String UPDATE_STATISTIC = """
            update statistic
            set
                total_games = ?,
                total_wins = ?,
                total_loses = ?,
                total_ships_destroyed = ?,
                total_ships_lost = ?
            where user_id = ?
            """;

    private static final RowMapper<Statistic> STATISTIC_ROW_MAPPER = (rs, rn) ->
            Statistic.builder()
                    .userId(rs.getLong("user_id"))
                    .totalGames(rs.getLong("total_games"))
                    .totalWins(rs.getLong("total_wins"))
                    .totalLoses(rs.getLong("total_loses"))
                    .totalShipsDestroyed(rs.getLong("total_ships_destroyed"))
                    .totalShipsLost(rs.getLong("total_ships_lost"))
                    .build();

    private void createStatistic(@NonNull Long userId) {
        jdbcTemplate.update(CREATE_STATISTIC, userId);
    }

    public Optional<Statistic> getStatisticForUserId(@NonNull Long userId) {
        return ofNullable(DataAccessUtils.singleResult(
                jdbcTemplate.query(SELECT_STATISTIC, STATISTIC_ROW_MAPPER, userId)));
    }

    public int addStatistic(@NonNull Statistic statistic) {
        Optional<Statistic> oldStatOpt = getStatisticForUserId(statistic.getUserId());

        if (oldStatOpt.isEmpty()) {
            createStatistic(statistic.getUserId());
            oldStatOpt = getStatisticForUserId(statistic.getUserId());
        }

        if (oldStatOpt.isEmpty()) {
            throw new IllegalStateException("Can't update settings for user with id " + statistic.getUserId());
        }

        Statistic oldStat = oldStatOpt.get();

        return jdbcTemplate.update(UPDATE_STATISTIC,
                oldStat.getTotalGames() + statistic.getTotalGames(),
                oldStat.getTotalWins() + statistic.getTotalWins(),
                oldStat.getTotalLoses() + statistic.getTotalLoses(),
                oldStat.getTotalShipsDestroyed() + statistic.getTotalShipsDestroyed(),
                oldStat.getTotalShipsLost() + statistic.getTotalShipsLost(),
                statistic.getUserId()
                );
    }
}

package com.example.seabattleserver.service;

import com.example.seabattleserver.dao.StatisticDao;
import com.example.seabattleserver.model.Statistic;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StatisticService {

    private final StatisticDao statisticDao;

    public Statistic getStatisticForUser(String userName) {
        return statisticDao.getStatisticForUserName(userName)
                .orElse(Statistic.builder().username(userName).build());
    }

    public boolean addStatistic(Statistic statistic) {
        int result = statisticDao.addStatistic(statistic);
        if (result != 1) {
            throw new IllegalStateException("Can't update statistic for " + statistic.getUsername());
        }
        return true;
    }
}

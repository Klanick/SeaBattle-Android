package com.example.seabattleserver.service;

import com.example.seabattleserver.dao.StatisticDao;
import com.example.seabattleserver.model.Statistic;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StatisticService {

    private final StatisticDao statisticDao;

    public Statistic getStatisticForUserId(Long userId) {
        return statisticDao.getStatisticForUserId(userId)
                .orElse(Statistic.builder().userId(userId).build());
    }

    public int addStatistic(Statistic statistic) {
        return statisticDao.addStatistic(statistic);
    }
}

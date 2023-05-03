package com.example.seabattleserver.controller;

import com.example.seabattleserver.model.Statistic;
import com.example.seabattleserver.service.StatisticService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/statistic")
public class StatisticController {

    private final StatisticService statisticService;

    @GetMapping("/get/{userId}")
    public ResponseEntity<Statistic> getStatistic(@PathVariable Long userId) {
        return ResponseEntity.ok(statisticService.getStatisticForUserId(userId));
    }

    @PostMapping("/addStatistic")
    public ResponseEntity<String> addStatistic(@RequestBody Statistic statistic) {
        int updatedRows = statisticService.addStatistic(statistic);
        if (updatedRows != 1) {
            return ResponseEntity.ok("Failed while trying to add statistic for user with id " +
                    statistic.getUserId());
        }
        return ResponseEntity.ok("Successful update statistic for user with id " + statistic.getUserId());
    }
}

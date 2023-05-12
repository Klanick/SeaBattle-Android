package com.example.seabattleserver.controller;

import com.example.seabattleserver.model.BooleanResponse;
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

    @GetMapping("/get/{username}")
    public ResponseEntity<Statistic> getStatistic(@PathVariable String username) {
        return ResponseEntity.ok(statisticService.getStatisticForUser(username));
    }

    @PostMapping("/addStatistic")
    public ResponseEntity<BooleanResponse> addStatistic(@RequestBody Statistic statistic) {
        return ResponseEntity.ok(BooleanResponse.of(statisticService.addStatistic(statistic)));
    }
}

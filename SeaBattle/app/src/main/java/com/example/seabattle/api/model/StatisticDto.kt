package com.example.seabattle.api.model

data class StatisticDto(
    private var username: String? = null,
    private var totalGames: Long? = null,
    private var totalWins: Long? = null,
    private var totalLoses: Long? = null,
    private var totalShipsDestroyed: Long? = null,
    private var totalShipsLost: Long? = null
)
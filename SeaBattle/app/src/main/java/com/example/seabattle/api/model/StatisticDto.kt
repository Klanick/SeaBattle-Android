package com.example.seabattle.api.model

data class StatisticDto(
    private var username: String? = null,
    private var totalGames: Long? = null,
    private var totalWins: Long? = null,
    private var totalLoses: Long? = null,
    private var totalShipsDestroyed: Long? = null,
    private var totalShipsLost: Long? = null
) {

    public fun getUsername(): String {
        return username.orEmpty()
    }

    public fun getTotalGames(): Long {
        return getOrZero(totalGames)
    }

    public fun getTotalWins(): Long {
        return getOrZero(totalWins)
    }

    public fun getTotalLoses(): Long {
        return getOrZero(totalLoses)
    }

    public fun getTotalShipsDestroyed(): Long {
        return getOrZero(totalShipsDestroyed)
    }

    public fun getTotalShipsLost(): Long {
        return getOrZero(totalShipsLost)
    }

    private fun getOrZero(value: Long?): Long {
        if (value == null) {
            return 0
        }
        return value!!
    }
}
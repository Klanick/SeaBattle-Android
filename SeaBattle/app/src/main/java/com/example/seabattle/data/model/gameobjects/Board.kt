package com.example.seabattle.data.model.gameobjects

interface Board {
    fun include(cell : Cell) : Boolean
    fun include(figure: Figure) : Boolean {
        return figure.getCells().stream().allMatch(this::include)
    }
}
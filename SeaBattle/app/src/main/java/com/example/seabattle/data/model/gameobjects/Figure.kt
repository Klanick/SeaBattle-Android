package com.example.seabattle.data.model.gameobjects

interface Figure {
    fun getCells() : Collection<Cell>

    override fun equals(other: Any?): Boolean
    override fun hashCode(): Int
}
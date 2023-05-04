package com.example.seabattle.data.model.gameobjects

interface Figure {
    fun getCells() : Set<Cell>
    fun include(cell : Cell) : Boolean {
        return getCells().contains(cell)
    }
    fun includeWhole(figure: Figure) : Boolean {
        return figure.getCells().stream().allMatch(this::include)
    }
    fun intersect(figure: Figure) : Boolean {
        return figure.getCells().stream().anyMatch(this::include)
    }
    fun size() : Int {
        return getCells().size
    }
    override fun equals(other: Any?): Boolean
    override fun hashCode(): Int
}
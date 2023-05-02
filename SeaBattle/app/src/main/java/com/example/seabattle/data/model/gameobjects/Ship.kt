package com.example.seabattle.data.model.gameobjects

import android.os.Parcelable

interface Ship : Figure, Parcelable {
    fun getNeighbourCells() : Collection<Cell> {
        val res = HashSet<Cell>()
        getCells().stream().forEach { cell : Cell ->
            for (x_delta in -1 until 1 + 1) {
                for (y_delta in -1 until 1 + 1) {
                    res.add(Cell(cell.posX + x_delta, cell.posY + y_delta))
                }
            }
        }
        return res
    }

    fun size() : Int {
        return getCells().size
    }
}
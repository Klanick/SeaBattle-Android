package com.example.seabattle.data.model.gameobjects

import android.os.Parcelable

interface Ship : Figure, Parcelable {
    fun getContainerFigure() : Figure
    fun getContainerCells() : Set<Cell> {
        return getContainerFigure().getCells()
    }
}
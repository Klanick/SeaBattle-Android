package com.example.seabattle.data.model.gameobjects
import kotlinx.parcelize.Parcelize

@Parcelize
open class RectangleShip(private val posX1: Int, private val posX2: Int, private val posY1: Int, private val posY2: Int) :
    RectangleFigure(posX1, posX2, posY1, posY2), Ship {
        override fun size(): Int {
            return (posToX - posFromX) * (posToY - posFromY)
        }
    }
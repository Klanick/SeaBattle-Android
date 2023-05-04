package com.example.seabattle.data.model.gameobjects
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
open class RectangleShip(private val posX1: Int, private val posX2: Int, private val posY1: Int, private val posY2: Int) :
    RectangleFigure(posX1, posX2, posY1, posY2), Ship {
    @IgnoredOnParcel
    private val container : Figure = RectangleFigure(posFromX - 1, posToX + 1, posFromY - 1, posToY + 1)
    override fun getContainerFigure() : Figure = container

    override fun toString(): String {
        return "[(x:$posFromX~$posToX, y:$posFromY~$posToY)]"
    }
}
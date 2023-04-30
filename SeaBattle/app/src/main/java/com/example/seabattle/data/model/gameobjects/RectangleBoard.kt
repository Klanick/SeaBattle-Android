package com.example.seabattle.data.model.gameobjects

class RectangleBoard(posX1: Int, posX2: Int, posY1: Int, posY2: Int) :
    RectangleFigure(posX1, posX2, posY1, posY2),
    Board {
    override fun include(cell: Cell): Boolean {
        return cell.posX in posFromX..posToX &&
                cell.posY in posFromY..posToY
    }
}
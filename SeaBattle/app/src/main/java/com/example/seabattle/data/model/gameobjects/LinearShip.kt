package com.example.seabattle.data.model.gameobjects

class LinearShip(val startPosX : Int, val startPosY: Int, val length : Int, val isHorizontal: Boolean = true)
    : RectangleShip(
    startPosX,
    startPosX + if (isHorizontal) length - 1 else 0,
    startPosY,
    startPosY + if (!isHorizontal) length - 1 else 0) {
        constructor(cell: Cell, length: Int, isHorizontal: Boolean = true) : this(cell.posX, cell.posY, length, isHorizontal)

        override fun toString(): String {
            return "[($startPosX, $startPosY) $length-len horizontal=$isHorizontal]"
        }
    }
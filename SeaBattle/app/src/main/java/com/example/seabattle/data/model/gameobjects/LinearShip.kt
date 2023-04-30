package com.example.seabattle.data.model.gameobjects

class LinearShip(startPosX : Int, startPosY: Int, length : Int, isHorizontal : Boolean)
    : RectangleShip(
    startPosX,
    startPosY,
    startPosX + if (isHorizontal) length - 1 else 0,
    startPosY + if (!isHorizontal) length - 1 else 0)
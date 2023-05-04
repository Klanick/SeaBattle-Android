package com.example.seabattle.data.model.gameobjects

class Cell (val posX: Int, val posY: Int) {
    override fun equals(other: Any?)
            = (other is Cell)
            && posX == other.posX
            && posY == other.posY

    override fun hashCode(): Int {
        var result = posX
        result = 31 * result + posY
        return result
    }

    override fun toString() : String {
        return "($posX, $posY)"
    }
}

package com.example.seabattle.data.model.gameobjects

import kotlin.math.max
import kotlin.math.min

open class RectangleFigure (posX1: Int, posX2: Int,
                            posY1: Int, posY2: Int) : Figure {
    protected val posFromX = min(posX1, posX2)
    protected val posToX = max(posX1, posX2)
    protected val posFromY = min(posY1, posY2)
    protected val posToY = max(posY1, posY2)

    private var cells : Set<Cell> = generateCells()

    private fun generateCells() : Set<Cell> {
        val res = HashSet<Cell>()
        for (x in posFromX until posToX + 1) {
            for (y in posFromY until posToY + 1) {
                res.add(Cell(x, y))
            }
        }
        return res
    }

    override fun getCells(): Collection<Cell> {
        return cells
    }

    override fun equals(other: Any?)
            = (other is RectangleFigure)
            && posFromX == other.posFromX
            && posToX == other.posToX
            && posFromY == other.posFromY
            && posToY == other.posToY

    override fun hashCode(): Int {
        var result = posFromX
        result = 31 * result + posToX
        result = 31 * result + posFromY
        result = 31 * result + posToY
        return result
    }
}
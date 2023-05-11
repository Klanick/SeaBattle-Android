package com.example.seabattle.data.model.gameobjects

import kotlin.math.max
import kotlin.math.min

open class RectangleFigure (posX1: Int, posX2: Int,
                            posY1: Int, posY2: Int) : Figure {
    protected val posFromX = min(posX1, posX2)
    protected val posToX = max(posX1, posX2)
    protected val posFromY = min(posY1, posY2)
    protected val posToY = max(posY1, posY2)

    private val cells : Set<Cell> = generateCells()

    private fun generateCells() : Set<Cell> {
        val res = HashSet<Cell>()
        for (x in posFromX .. posToX) {
            for (y in posFromY .. posToY) {
                res.add(Cell(x, y))
            }
        }
        return res
    }

    override fun getCells(): Set<Cell> {
        return cells
    }

    override fun include(cell: Cell): Boolean {
        return cell.posX in posFromX..posToX &&
                cell.posY in posFromY..posToY
    }

    override fun includeWhole(figure: Figure): Boolean {
        if (figure is RectangleFigure) {
            return posFromX <= figure.posFromX &&  figure.posToX <= posToX &&
                    posFromY <= figure.posFromY && figure.posToY <= posToY
        }
        return super.includeWhole(figure)
    }

    override fun intersect(figure: Figure): Boolean {
        if (figure is RectangleFigure) {
            return (figure.posFromX in posFromX..posToX || figure.posToX in posFromX..posToX) &&
                    (figure.posFromY in posFromY..posToY || figure.posToY in posFromY..posToY)
        }
        return super.intersect(figure)
    }

    override fun size(): Int {
        return (posToX - posFromX + 1) * (posToY - posFromY + 1)
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

    override fun toString(): String {
        return "[RecFigure:(x:$posFromX~$posToX, y:$posFromY~$posToY)]"
    }
}
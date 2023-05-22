package com.example.seabattle.bot

import com.example.seabattle.data.model.gameobjects.Cell
import java.util.*
import kotlin.collections.ArrayList

class SeaBattleBot {

    private val random: Random = Random()
    private var list: ArrayList<Cell> = ArrayList()

    fun getCell() : Cell {
        val x: Int
        val y: Int
        if (list.isEmpty()) {
            x = random.nextInt(10)
            y = random.nextInt(10)
        } else {
            if ((list.size != 1 && getXs().all { list[0].posX == it }) || (list.size == 1 && random.nextBoolean())) {
                x = list[0].posX
                y = if (getYs().min() == 0 || (random.nextBoolean() && getYs().max() != 9))
                    getYs().max() + 1
                    else
                    getYs().min() - 1
            } else {
                x = if (getXs().min() == 0 || (random.nextBoolean() && getXs().max() != 9))
                    getXs().max() + 1
                    else
                    getXs().min() - 1
                y = list[0].posY
            }
        }

        return Cell(x, y)
    }

    fun foundCell(cell: Cell) {
        list.add(cell)
    }

    fun foundShip() {
        list = ArrayList()
    }

    private fun getXs(): List<Int> {
        return list.map{ it.posX }
    }

    private fun getYs(): List<Int> {
        return list.map{ it.posY }
    }
}
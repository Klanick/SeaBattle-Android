package com.example.seabattle

import android.os.Parcelable
import androidx.lifecycle.ViewModel
import com.example.seabattle.data.model.gameobjects.RectangleFigure
import com.example.seabattle.data.model.gameobjects.Ship
import com.example.seabattle.data.model.gameobjects.ShipPackBuilder
import com.example.seabattle.data.model.gameobjects.StandardShipPackBuilder
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.parcelize.Parcelize

class PreGameViewModel : ViewModel() {
    private val _state = MutableStateFlow(State(board = RectangleFigure(0, 9, 0, 9)))
    val state = _state.asStateFlow()
    data class State(val board: RectangleFigure, val opponentBoard: RectangleFigure = board) : ShipPackBuilder<GameStartPack> {
        private val shipPackBuilder = StandardShipPackBuilder(board)
        private val opponentShipPackBuilder = StandardShipPackBuilder(opponentBoard)
        override fun tryAdd(ship: Ship) {
            shipPackBuilder.tryAdd(ship)
        }

        override fun tryComplete() {
            shipPackBuilder.tryComplete()
        }

        override fun tryDelete(ship: Ship) {
            shipPackBuilder.tryDelete(ship)
        }

        override fun clear() {
            shipPackBuilder.clear()
        }

        override fun tryBuild(): GameStartPack {
            val ships = shipPackBuilder.tryBuild()
            opponentShipPackBuilder.tryComplete()
            val opponentShips = opponentShipPackBuilder.tryBuild()
            return GameStartPack(ships, opponentShips)
        }
    }

    @Parcelize
    class GameStartPack(val ships : List<Ship>, val opponentShips : List<Ship>) : Parcelable
}
package com.example.seabattle

import android.os.Parcelable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.seabattle.data.model.gameobjects.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.parcelize.Parcelize
import kotlin.math.abs

class PreGameViewModel : ViewModel() {
    private val _state = MutableStateFlow(State(board = RectangleFigure(0, 9, 0, 9)))
    val state = _state.asStateFlow()
    data class State(val board: RectangleFigure, val opponentBoard: RectangleFigure = board) : ShipPackBuilder<GameStartPack> {
        private val shipPackBuilder = StandardShipPackBuilder(board)
        private val opponentShipPackBuilder = StandardShipPackBuilder(opponentBoard)
        private var chosenCell : Cell? = null
        val chosenShip : MutableLiveData<Ship?> = MutableLiveData(null)
        val chosenShipType : MutableLiveData<ChosenShipType?> = MutableLiveData(null)
        val packWasChanged = MutableLiveData<Boolean>()
        val errorPosition = MutableLiveData<ErrorPosition?>(null)

        enum class ErrorPosition {
            localError, globalError
        }

        private fun packUpdate() {
            fixChosenShip()
            packWasChanged.value = true
        }
        private fun fixChosenShip() {
            val ship = chosenShip.value
            if (ship != null && !shipPackBuilder.shipSet().contains(ship) && shipPackBuilder.shipSet().stream().anyMatch(ship::intersect)) {
                cancelChoose()
            }
            fixChosenShipType()
        }

        private fun fixChosenShipType() {
            if (updateChosenShipType(calcChosenShipType())) {
                cancelChoose()
            }
        }
        override fun tryAdd(ship: Ship) {
            shipPackBuilder.tryAdd(ship)
            packUpdate()
        }

        fun tryAddChosen() {
            val ship = chosenShip.value
            if (ship != null) {
                shipPackBuilder.tryAdd(ship)
                cancelChoose()
                packUpdate()
            }
        }
        override fun tryComplete() {
            try {
                shipPackBuilder.tryComplete()
            } finally {
                packUpdate()
            }
        }

        override fun tryDelete(ship: Ship) {
            shipPackBuilder.tryDelete(ship)
            packUpdate()
        }

        fun tryDeleteChosen() {
            val ship = chosenShip.value
            if (ship != null) {
                shipPackBuilder.tryDelete(ship)
                cancelChoose()
                packUpdate()
            }
        }

        override fun clear() {
            shipPackBuilder.clear()
            packUpdate()
        }

        override fun tryBuild(): GameStartPack {
            val ships = shipPackBuilder.tryBuild()
            opponentShipPackBuilder.tryComplete()
            val opponentShips = opponentShipPackBuilder.tryBuild()
            return GameStartPack(ships, opponentShips)
        }
        fun clickCell(cell : Cell) {
            val oldCell = chosenCell
            if (oldCell != null) {
                if (oldCell.posX == cell.posX || oldCell.posY == cell.posY) {
                    val newShip = linearShip(oldCell, cell)
                    if (!shipPackBuilder.shipSet().stream().anyMatch(newShip::intersect)) {
                        updateChosenShip(newShip, ChosenShipType.New)
                        chosenCell = cell
                        return
                    }
                }
            }
            firstClickCell(cell)
        }

        private fun firstClickCell(cell : Cell) {
            val includeShip = shipPackBuilder.shipSet().stream().filter{ ship -> ship.include(cell)}.findFirst()
            return if (!includeShip.isPresent) {
                chosenCell = cell
                updateChosenShip(LinearShip(cell, 1), ChosenShipType.New)
            } else {
                chosenCell = null
                updateChosenShip(includeShip.get(), ChosenShipType.Exist)
            }
        }

        enum class ChosenShipType {
            Exist, New
        }
        fun shipCells() : Collection<Cell> {
            return shipPackBuilder.shipCells()
        }

        fun getExistShipCells() : Collection<Cell> {
            val res = chosenShip.value
            if (res != null && shipPackBuilder.shipSet().contains(res)) {
                return res.getCells()
            }
            return HashSet()
        }

        fun getNewShipCells() : Collection<Cell> {
            val res = chosenShip.value
            if (res != null && !shipPackBuilder.shipSet().contains(res)) {
                return res.getCells()
            }
            return HashSet()
        }

        private fun updateChosenShip(ship : Ship?, shipType : ChosenShipType?) {
            updateChosenShipType(shipType)
            chosenShip.value = ship
        }

        private fun updateChosenShipType(shipType : ChosenShipType?) : Boolean {
            if (chosenShipType.value != shipType) {
                chosenShipType.value = shipType
                return true
            }
            return false
        }

        private fun calcChosenShipType() : ChosenShipType? {
            val ship = chosenShip.value
            return if (ship == null) {
                null
            } else if (shipPackBuilder.shipSet().contains(chosenShip.value)) {
                ChosenShipType.Exist
            } else {
                ChosenShipType.New
            }
        }

        fun cancelChoose() {
            chosenCell = null
            updateChosenShip(null, null)
        }

        private fun linearShip(oldCell: Cell, newCell: Cell) : Ship {
            return RectangleShip(oldCell.posX, newCell.posX, oldCell.posY, newCell.posY)
        }
    }

    @Parcelize
    class GameStartPack(val ships : List<Ship>, val opponentShips : List<Ship>) : Parcelable
}
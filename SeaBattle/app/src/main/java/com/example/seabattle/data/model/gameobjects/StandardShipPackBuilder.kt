package com.example.seabattle.data.model.gameobjects

import com.example.seabattle.R
import com.example.seabattle.data.model.gameobjects.ShipPackBuilder.ShipSetBuilderException
import java.util.stream.Collectors
import kotlin.random.Random

open class StandardShipPackBuilder(private val board: Figure) : ShipPackBuilder<List<Ship>> {
    private val shipSet : MutableSet<Ship> = HashSet()
    fun shipSet() : Set<Ship> = shipSet
    fun shipCells() : Set<Cell> = shipSet.stream().flatMap { ship -> ship.getCells().stream() }.collect(
        Collectors.toSet())
    override fun tryAdd(ship: Ship) {
        if (shipSet.contains(ship)) {
            throw ShipSetBuilderException("This ship already exists", R.string.shipAlreadyExistMessage)
        }
        if (!board.includeWhole(ship)) {
            throw ShipSetBuilderException("This ship has cells out of the Board", R.string.shipOutOfBoardMessage)
        }
        if (shipSet.stream().anyMatch(ship.getContainerFigure()::intersect)) {
            throw ShipSetBuilderException("This ship too close to another ships", R.string.shipTooCloseMessage)
        }
        tryCheckRulesForAdd(ship)

        shipSet.add(ship)
        println("Successfully Add : $ship")
    }

    override fun tryDelete(ship: Ship) {
        if (!shipSet.contains(ship)) {
            throw ShipSetBuilderException("This ship doesn't exist", R.string.shipNotExistMessage)
        }
        shipSet.remove(ship)
        println("Successfully Delete : $ship")
    }

    override fun clear() {
        shipSet.clear()
    }

    override fun tryBuild(): List<Ship> {
        tryCheckRulesForBuild()
        return build()
    }

    private fun build(): List<Ship> {
        println("Successfully build : {\n" +  shipSet.joinToString("\n") + "\n}")
        return shipSet.toList()
    }

    private val shipTypes : List<ShipType> = listOf(LinearType(4), LinearType(3), LinearType(2), LinearType(1))
    private val shipTypesExpectCounts : List<Int> = listOf(1, 2, 3, 4)

    private fun tryCheckRulesForBuild() {
        val expectSize = shipTypesExpectCounts.sum()
        val curSize = shipSet.size
        if (curSize != expectSize) {
            throw ShipSetBuilderException("Expected $expectSize ships, found $curSize", R.string.wrongShipsCountMessage)
        }

        for(typeI in shipTypes.indices) {
            val type = shipTypes[typeI]
            val expectTypeCount = shipTypesExpectCounts[typeI]
            val curTypeCount = type.countIn(shipSet)
            if (curTypeCount != expectTypeCount) {
                throw ShipSetBuilderException("Expected $expectTypeCount count $type ships, found $curTypeCount", R.string.wrongShipsCountForTypeMessage)
            }
        }
    }

    private fun tryCheckRulesForAdd(ship: Ship) {
        val expectSize = shipTypesExpectCounts.sum()
        val curSize = shipSet.size
        if (curSize >= expectSize) {
            throw ShipSetBuilderException("Can't add ship to full collection", R.string.fullShipCollectionMessage)
        }

        var hasType = false
        for(typeI in shipTypes.withIndex()
            .filter { (_, shipType) -> shipType.isAccept(ship) }
            .map { indexedValue -> indexedValue.index }) {
            hasType = true
            val type = shipTypes[typeI]
            val expectTypeCount = shipTypesExpectCounts[typeI]
            val curTypeCount = type.countIn(shipSet)
            if (curTypeCount >= expectTypeCount) {
                throw ShipSetBuilderException(
                    "Can't add new $type ship, $curTypeCount these ships already have been placed", R.string.fullShipTypeMessage)
            }
        }
        if (!hasType) {
            throw ShipSetBuilderException(
                "Can't add new ship. This ship has unexpected type", R.string.unexpectedShipTypeMessage)
        }
    }

    private fun getFreeCells() : ArrayList<Cell> {
        return ArrayList(board.getCells().filter {
                cell: Cell -> shipSet.all {
                ship: Ship -> !ship.getContainerFigure().include(cell)
                }})
    }

    class RandomUnrepeated<E>(private val list : ArrayList<E>) {
        private val numbers = (0 until list.size).toMutableList()
        fun nextElement() : E {
            val index = Random.nextInt(numbers.size)
            val number = numbers[index]
            numbers.removeAt(index)
            return list[number]
        }

        fun hasNext() : Boolean {
            return numbers.size > 0
        }
    }

    private fun tryAddRandom() {
        for (typeI in shipTypes.indices) {
            val type = shipTypes[typeI]
            val expectTypeCount = shipTypesExpectCounts[typeI]
            val curTypeCount = type.countIn(shipSet)
            if (curTypeCount < expectTypeCount) {
                val randomCells = RandomUnrepeated(getFreeCells())
                while(randomCells.hasNext()) {
                    val cell = randomCells.nextElement()
                    try {
                        tryAdd(type.generateFirst(cell))
                        return
                    } catch (_ : ShipSetBuilderException) {}
                    while (type.hasNext()) {
                        try {
                            tryAdd(type.generateNext())
                            return
                        } catch (_ : ShipSetBuilderException) {}
                    }
                }
                throw ShipSetBuilderException("There isn't enough place for $type ship", R.string.noEnoughSpaceMessage)
            }
        }
        throw ShipSetBuilderException("Can't add ship to full collection", R.string.fullShipCollectionMessage)
    }

    override fun tryComplete() {
        while (shipSet.size < shipTypesExpectCounts.sum()) {
            tryAddRandom()
        }
        println("Successfully Complete.")
    }

    interface AcceptAble<T> {
        fun isAccept(obj: T) : Boolean

        fun countIn(collection: Collection<T>) : Int {
            return collection.stream().filter(this::isAccept).count().toInt()
        }
    }
    interface GenerateAble<T, F> {
        fun generateFirst(from: F) : T
        fun generateNext() : T
        fun hasNext() : Boolean
    }

    interface ShipType : AcceptAble<Ship>, GenerateAble<Ship, Cell> {
        override fun toString(): String
    }


    class LinearType(private val length : Int) : ShipType {
        override fun isAccept(obj: Ship): Boolean {
            if (obj is LinearShip) {
                return (obj.size() == length)
            } else if (obj is RectangleShip) {
                return (obj.height() == 0 || obj.width() == 0) && (obj.size() == length)
            }
            return false
        }
        private var prevGen : LinearShip? = null
        private var hasNext = false

        override fun generateFirst(from: Cell): Ship {
            prevGen = LinearShip(from, length, Random.nextBoolean())
            hasNext = true
            return prevGen as LinearShip
        }

        override fun generateNext(): Ship {
            if (hasNext()) {
                prevGen = LinearShip(prevGen!!.startPosX, prevGen!!.startPosY, prevGen!!.length, !prevGen!!.isHorizontal)
                hasNext = false
                return prevGen as LinearShip
            }
            throw IllegalArgumentException()
        }

        override fun hasNext(): Boolean = hasNext

        override fun toString(): String {
            return "$length-length linear-type"
        }
    }
}
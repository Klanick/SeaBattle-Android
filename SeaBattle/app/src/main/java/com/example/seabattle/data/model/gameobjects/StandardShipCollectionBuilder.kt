package com.example.seabattle.data.model.gameobjects

import com.example.seabattle.data.model.gameobjects.ShipCollectionBuilder.ShipSetBuilderException
import kotlin.random.Random

class StandardShipCollectionBuilder(private val board: Figure) : ShipCollectionBuilder {
    private val shipSet : MutableSet<Ship> = HashSet()
    override fun tryAdd(ship: Ship) {
        if (shipSet.contains(ship)) {
            throw ShipSetBuilderException("This ship already exists")
        }
        if (!board.includeWhole(ship)) {
            throw ShipSetBuilderException("This ship can't be placed on the Board")
        }
        if (shipSet.stream().anyMatch(ship.getContainerFigure()::intersect)) {
            throw ShipSetBuilderException("This ship too close to another ships")
        }
        tryCheckRulesForAdd(ship)

        shipSet.add(ship)
        println("Successfully Add : $ship")
    }

    override fun tryDelete(ship: Ship) {
        if (!shipSet.contains(ship)) {
            throw ShipSetBuilderException("This ship doesn't exist")
        }
        shipSet.remove(ship)
        println("Successfully Delete : $ship")
    }

    override fun clear() {
        shipSet.clear()
    }

    override fun tryBuild(): Collection<Ship> {
        tryCheckRulesForBuild()
        return build()
    }

    override fun build(): Collection<Ship> {
        println("Successfully build : {\n" +  shipSet.joinToString("\n") + "\n}")
        return shipSet
    }

    private val shipTypes : List<ShipType> = listOf(LinearType(4), LinearType(3), LinearType(2), LinearType(1))
    private val shipTypesExpectCounts : List<Int> = listOf(1, 2, 3, 4)

    private fun tryCheckRulesForBuild() {
        val expectSize = shipTypesExpectCounts.sum()
        val curSize = shipSet.size
        if (curSize != expectSize) {
            throw ShipSetBuilderException("Expected $expectSize count ships, found $curSize")
        }

        for(typeI in shipTypes.indices) {
            val type = shipTypes[typeI]
            val expectTypeCount = shipTypesExpectCounts[typeI]
            val curTypeCount = type.countIn(shipSet)
            if (curTypeCount != expectTypeCount) {
                throw ShipSetBuilderException("Expected $expectTypeCount count $type ships, found $curTypeCount")
            }
        }
    }

    private fun tryCheckRulesForAdd(ship: Ship) {
        val expectSize = shipTypesExpectCounts.sum()
        val curSize = shipSet.size
        if (curSize >= expectSize) {
            throw ShipSetBuilderException("Can't add ship to full collection")
        }

        for(typeI in shipTypes.withIndex()
            .filter { (_, shipType) -> shipType.isAccept(ship) }
            .map { indexedValue -> indexedValue.index }) {
            val type = shipTypes[typeI]
            val expectTypeCount = shipTypesExpectCounts[typeI]
            val curTypeCount = type.countIn(shipSet)
            if (curTypeCount >= expectTypeCount) {
                throw ShipSetBuilderException(
                    "Can't add new $type ship, $curTypeCount these ships already have been placed")
            }
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

    override fun tryAddRandom() {
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
                throw ShipSetBuilderException("There isn't enough place for $type ship")
            }
        }
        throw ShipSetBuilderException("Can't add ship to full collection")
    }

    override fun tryCompleteRandom() {
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
            return (obj is LinearShip) && (obj.size() == length)
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
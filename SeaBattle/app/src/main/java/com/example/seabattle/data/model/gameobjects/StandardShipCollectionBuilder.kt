package com.example.seabattle.data.model.gameobjects

import com.example.seabattle.data.model.gameobjects.ShipCollectionBuilder.ShipSetBuilderException

class StandardShipCollectionBuilder(private val board: Board) : ShipCollectionBuilder {
    private val shipSet : MutableSet<Ship> = HashSet()
    override fun tryAdd(ship: Ship) {
        if (shipSet.contains(ship)) {
            throw ShipSetBuilderException("This ship already exist")
        }
        if (!board.include(ship)) {
            throw ShipSetBuilderException("This ship can't be placed on the Board")
        }

        val neighbourCells : Set<Cell> = ship.getNeighbourCells().toSet()
        if (shipSet.stream().anyMatch {
                s : Ship -> s.getCells().stream().anyMatch(neighbourCells::contains)
        }) {
            throw ShipSetBuilderException("This ship too close to another ships")
        }

        shipSet.add(ship)
    }

    override fun tryDelete(ship: Ship) {
        if (!shipSet.contains(ship)) {
            throw ShipSetBuilderException("This ship don't exist")
        }
        shipSet.remove(ship)
    }

    override fun tryBuild(): Collection<Ship> {
        if (!checkShipTypes()) {
            throw ShipSetBuilderException("Failed checking Ship Types")
        }
        return build()
    }

    override fun build(): Collection<Ship> {
        return shipSet
    }

    private val shipTypes: Map<ShipType, Int> = mapOf(
        LinearType(1) to 4,
        LinearType(2) to 3,
        LinearType(3) to 2,
        LinearType(4) to 1
    )

    private fun checkShipTypes() : Boolean {
        val checkingShipSet : MutableSet<Ship> = shipSet.toMutableSet()

        for(entry in shipTypes.entries) {
            for (i in 0 until entry.value) {
                val findShip = entry.key.getShip(checkingShipSet) ?: return false
                checkingShipSet.remove(findShip)
            }
        }
        return checkingShipSet.isEmpty()
    }

    interface ShipType {
        fun isAccept(ship: Ship) : Boolean

        fun getShip(ships: Collection<Ship>): Ship? {
            return ships.stream().filter(this::isAccept).findFirst().orElse(null)
        }
    }

    class LinearType(private val length : Int) : ShipType {
        override fun isAccept(ship: Ship): Boolean {
            return (ship is LinearShip) && (ship.size() == length)
        }
    }

}
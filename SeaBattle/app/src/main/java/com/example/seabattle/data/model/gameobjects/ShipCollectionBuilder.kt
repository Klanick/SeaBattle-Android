package com.example.seabattle.data.model.gameobjects

interface ShipCollectionBuilder {
    @Throws(ShipSetBuilderException::class)
    fun tryAdd(ship: Ship)

    @Throws(ShipSetBuilderException::class)
    fun tryAddRandom()

    @Throws(ShipSetBuilderException::class)
    fun tryCompleteRandom()

    @Throws(ShipSetBuilderException::class)
    fun tryDelete(ship: Ship)
    fun clear()

    @Throws(ShipSetBuilderException::class)
    fun tryBuild() : Collection<Ship>

    fun build() : Collection<Ship>

    class ShipSetBuilderException(message:String): Exception(message)
}


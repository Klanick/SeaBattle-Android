package com.example.seabattle.data.model.gameobjects

interface ShipCollectionBuilder {
    @Throws(ShipSetBuilderException::class)
    fun tryAdd(ship: Ship)

    @Throws(ShipSetBuilderException::class)
    fun tryDelete(ship: Ship)

    @Throws(ShipSetBuilderException::class)
    fun tryBuild() : Collection<Ship>

    //TODO Delete
    fun build() : Collection<Ship>

    class ShipSetBuilderException(message:String): Exception(message)
}


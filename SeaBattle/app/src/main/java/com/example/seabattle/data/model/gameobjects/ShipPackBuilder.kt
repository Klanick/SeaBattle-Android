package com.example.seabattle.data.model.gameobjects

interface ShipPackBuilder <ShipPack> {
    @Throws(ShipSetBuilderException::class)
    fun tryAdd(ship: Ship)

    @Throws(ShipSetBuilderException::class)
    fun tryComplete()

    @Throws(ShipSetBuilderException::class)
    fun tryDelete(ship: Ship)
    fun clear()

    @Throws(ShipSetBuilderException::class)
    fun tryBuild() : ShipPack

    class ShipSetBuilderException(message:String): Exception(message)
}


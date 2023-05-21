package com.example.seabattle.data.model.gameobjects

import android.content.Context

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

    class ShipSetBuilderException(message:String, private val resMessageId : Int? = null): Exception(message) {
        fun getMessageByContext(context: Context): String? {
            if (resMessageId == null) {
                return super.getLocalizedMessage()
            }
            return context.getString(resMessageId)
        }
    }
}


package net.ruvios.bgbgui.api.event

import org.bukkit.entity.Player
import org.bukkit.event.HandlerList
import org.bukkit.event.player.PlayerEvent

class BgbSimpleFormEvent(
    who: Player,
    val id: String,
    val title: String,
    val index: Int,
    val button: String,
) : PlayerEvent(who) {

    override fun getHandlers(): HandlerList = HANDLERS

    companion object {
        @JvmStatic
        private val HANDLERS = HandlerList()

        @JvmStatic
        fun getHandlerList(): HandlerList = HANDLERS
    }
}

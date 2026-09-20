package net.ruvios.bgbgui.api.event

import net.ruvios.bgbgui.api.FormValues
import org.bukkit.entity.Player
import org.bukkit.event.HandlerList
import org.bukkit.event.player.PlayerEvent

class BgbCustomFormEvent(
    who: Player,
    val id: String,
    val title: String,
    val values: FormValues,
) : PlayerEvent(who) {

    override fun getHandlers(): HandlerList = HANDLERS

    companion object {
        @JvmStatic
        private val HANDLERS = HandlerList()

        @JvmStatic
        fun getHandlerList(): HandlerList = HANDLERS
    }
}

package net.ruvios.bgbgui.skript

import ch.njol.skript.Skript
import org.bukkit.plugin.java.JavaPlugin

object SkriptAddon {

    @JvmStatic
    fun register(plugin: JavaPlugin) {
        Skript.registerAddon(plugin)
        EffOpenSimpleForm.register()
        EffOpenModalForm.register()
        EffOpenCustomForm.register()
        SecOpenSimpleForm.register()
        SecOpenModalForm.register()
        SecOpenCustomForm.register()
        SkriptEvents.register()
        SkriptExpressions.register()
        plugin.logger.info("BGB_GUI Skript-Addon geladen.")
    }
}

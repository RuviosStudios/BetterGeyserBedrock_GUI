package net.ruvios.bgbgui.skript

import org.bukkit.plugin.java.JavaPlugin

internal object SkriptSupport {

    fun tryRegister(plugin: JavaPlugin) {
        if (plugin.server.pluginManager.getPlugin("Skript") == null) {
            return
        }
        try {
            Class.forName("ch.njol.skript.Skript")
            Class.forName("net.ruvios.bgbgui.skript.SkriptAddon")
                .getMethod("register", JavaPlugin::class.java)
                .invoke(null, plugin)
        } catch (error: Throwable) {
            plugin.logger.info("Skript ist da, BGB_GUI-Addon wurde nicht geladen: ${error.message}")
        }
    }
}

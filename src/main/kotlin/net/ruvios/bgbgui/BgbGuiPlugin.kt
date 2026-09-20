package net.ruvios.bgbgui

import net.ruvios.bgbgui.api.BgbGuiApi
import net.ruvios.bgbgui.api.BgbGuiApiImpl
import net.ruvios.bgbgui.commands.TestCmd
import net.ruvios.bgbgui.skript.SkriptSupport
import org.bukkit.plugin.ServicePriority
import org.bukkit.plugin.java.JavaPlugin

class BgbGuiPlugin : JavaPlugin() {

    override fun onEnable() {
        instance = this
        server.servicesManager.register(BgbGuiApi::class.java, BgbGuiApiImpl, this, ServicePriority.Normal)
        TestCmd.register(this)
        SkriptSupport.tryRegister(this)
        logger.info("BGB_GUI aktiviert.")
    }

    override fun onDisable() {
        server.servicesManager.unregisterAll(this)
        logger.info("BGB_GUI deaktiviert.")
    }

    companion object {
        lateinit var instance: BgbGuiPlugin
            private set

        fun loaded(): Boolean {
            return ::instance.isInitialized && instance.isEnabled
        }
    }
}

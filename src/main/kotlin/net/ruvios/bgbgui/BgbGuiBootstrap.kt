package net.ruvios.bgbgui

import io.papermc.paper.plugin.bootstrap.BootstrapContext
import io.papermc.paper.plugin.bootstrap.PluginBootstrap
import io.papermc.paper.plugin.bootstrap.PluginProviderContext
import org.bukkit.plugin.java.JavaPlugin

@Suppress("UnstableApiUsage")
class BgbGuiBootstrap : PluginBootstrap {

    override fun bootstrap(context: BootstrapContext) {
        context.logger.info("BGB_GUI Bootstrap ({})", context.pluginMeta.name)
    }

    override fun createPlugin(context: PluginProviderContext): JavaPlugin {
        return BgbGuiPlugin()
    }
}

package net.ruvios.bgbgui.utils

import net.ruvios.bgbgui.BgbGuiPlugin
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import org.geysermc.cumulus.form.util.FormBuilder
import org.geysermc.floodgate.api.FloodgateApi

internal fun runOnPlayer(plugin: JavaPlugin, player: Player, action: () -> Unit) {
    if (player.isOnline.not()) {
        return
    }
    player.scheduler.run(plugin, { _ ->
        if (player.isOnline) {
            action()
        }
    }, null)
}

internal fun sendForm(
    player: Player,
    builder: FormBuilder<*, *, *>,
): Boolean {
    val api = try {
        FloodgateApi.getInstance()
    } catch (t: Throwable) {
        if (BgbGuiPlugin.loaded()) {
            BgbGuiPlugin.instance.logger.warning("FloodgateApi nicht verfügbar: ${t.message}")
        }
        return false
    }
    if (player.isOnline.not()) {
        return false
    }
    return try {
        val ok = api.sendForm(player.uniqueId, builder)
        if (!ok && BgbGuiPlugin.loaded()) {
            BgbGuiPlugin.instance.logger.warning(
                "Floodgate sendForm=false für ${player.name} (${player.uniqueId})",
            )
        }
        ok
    } catch (t: Throwable) {
        if (BgbGuiPlugin.loaded()) {
            BgbGuiPlugin.instance.logger.warning(
                "Floodgate sendForm Exception für ${player.name}: ${t.message}",
            )
        }
        false
    }
}

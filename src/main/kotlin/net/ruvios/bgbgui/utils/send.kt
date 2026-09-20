package net.ruvios.bgbgui.utils

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
    } catch (_: Throwable) {
        return false
    }
    if (player.isOnline.not()) {
        return false
    }
    return try {
        api.sendForm(player.uniqueId, builder)
    } catch (_: Throwable) {
        false
    }
}

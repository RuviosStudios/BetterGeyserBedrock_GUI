package net.ruvios.bgbgui.forms

import net.ruvios.bgbgui.BgbGuiPlugin
import net.ruvios.bgbgui.utils.runOnPlayer
import net.ruvios.bgbgui.utils.sendForm
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import org.geysermc.cumulus.form.SimpleForm

internal fun createSimple(
    player: Player,
    title: String,
    content: String,
    buttons: List<String>,
    onSelect: (index: Int, label: String) -> Unit = { _, _ -> },
    onClose: () -> Unit = {},
    plugin: JavaPlugin = BgbGuiPlugin.instance,
): Boolean {
    val builder = SimpleForm.builder()
        .title(title)
        .content(content)
    buttons.forEach { label ->
        builder.button(label)
    }
    builder.validResultHandler { response ->
        val index = response.clickedButtonId()
        val label = buttons.getOrElse(index) { "Button $index" }
        runOnPlayer(plugin, player) {
            onSelect(index, label)
        }
    }
    builder.closedOrInvalidResultHandler(Runnable {
        runOnPlayer(plugin, player, onClose)
    })
    return sendForm(player, builder)
}

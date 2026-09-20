package net.ruvios.bgbgui.forms

import net.ruvios.bgbgui.BgbGuiPlugin
import net.ruvios.bgbgui.utils.runOnPlayer
import net.ruvios.bgbgui.utils.sendForm
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import org.geysermc.cumulus.form.ModalForm

internal fun createModal(
    player: Player,
    title: String,
    content: String,
    button1: String,
    button2: String,
    onButton1: () -> Unit = {},
    onButton2: () -> Unit = {},
    onClose: () -> Unit = {},
    plugin: JavaPlugin = BgbGuiPlugin.instance,
): Boolean {
    val builder = ModalForm.builder()
        .title(title)
        .content(content)
        .button1(button1)
        .button2(button2)
        .validResultHandler { response ->
            runOnPlayer(plugin, player) {
                if (response.clickedButtonId() == 0) {
                    onButton1()
                } else {
                    onButton2()
                }
            }
        }
        .closedOrInvalidResultHandler(Runnable {
            runOnPlayer(plugin, player, onClose)
        })
    return sendForm(player, builder)
}

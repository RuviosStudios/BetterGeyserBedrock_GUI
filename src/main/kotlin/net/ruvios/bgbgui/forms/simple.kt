package net.ruvios.bgbgui.forms

import net.ruvios.bgbgui.BgbGuiPlugin
import net.ruvios.bgbgui.api.FormButton
import net.ruvios.bgbgui.api.FormImageType
import net.ruvios.bgbgui.utils.runOnPlayer
import net.ruvios.bgbgui.utils.sendForm
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import org.geysermc.cumulus.form.SimpleForm
import org.geysermc.cumulus.util.FormImage

internal fun createSimple(
    player: Player,
    title: String,
    content: String,
    buttons: List<FormButton>,
    onSelect: (index: Int, label: String) -> Unit = { _, _ -> },
    onClose: () -> Unit = {},
    plugin: JavaPlugin = BgbGuiPlugin.instance,
): Boolean {
    val builder = SimpleForm.builder()
        .title(title)
        .content(content)
    buttons.forEach { button ->
        val image = button.image
        if (image.isNullOrBlank()) {
            builder.button(button.label)
        } else {
            builder.button(button.label, button.imageType.toCumulus(), image)
        }
    }
    builder.validResultHandler { response ->
        val index = response.clickedButtonId()
        val label = buttons.getOrNull(index)?.label ?: "Button $index"
        runOnPlayer(plugin, player) {
            onSelect(index, label)
        }
    }
    builder.closedOrInvalidResultHandler(Runnable {
        runOnPlayer(plugin, player, onClose)
    })
    return sendForm(player, builder)
}

private fun FormImageType.toCumulus(): FormImage.Type {
    return when (this) {
        FormImageType.URL -> FormImage.Type.URL
        FormImageType.PATH -> FormImage.Type.PATH
    }
}

package net.ruvios.bgbgui.forms

import net.ruvios.bgbgui.BgbGuiPlugin
import net.ruvios.bgbgui.api.FormField
import net.ruvios.bgbgui.api.FormValues
import net.ruvios.bgbgui.utils.runOnPlayer
import net.ruvios.bgbgui.utils.sendForm
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import org.geysermc.cumulus.form.CustomForm

internal fun createCustom(
    player: Player,
    title: String,
    fields: List<FormField>,
    onSubmit: (FormValues) -> Unit = {},
    onClose: () -> Unit = {},
    plugin: JavaPlugin = BgbGuiPlugin.instance,
): Boolean {
    val builder = CustomForm.builder().title(title)
    fields.forEach { field ->
        when (field) {
            is FormField.Label -> builder.label(field.text)
            is FormField.Input -> builder.input(field.label, field.placeholder, field.default)
            is FormField.Dropdown -> builder.dropdown(field.label, field.options, field.defaultIndex)
            is FormField.Toggle -> builder.toggle(field.label, field.default)
            is FormField.Slider -> builder.slider(field.label, field.min, field.max, field.step, field.default)
        }
    }
    builder.validResultHandler { response ->
        val values = linkedMapOf<String, Any?>()
        fields.forEachIndexed { index, field ->
            when (field) {
                is FormField.Label -> Unit
                is FormField.Input -> values[field.id] = response.asInput(index) ?: ""
                is FormField.Dropdown -> values[field.id] = response.asDropdown(index)
                is FormField.Toggle -> values[field.id] = response.asToggle(index)
                is FormField.Slider -> values[field.id] = response.asSlider(index)
            }
        }
        runOnPlayer(plugin, player) {
            onSubmit(FormValues(values))
        }
    }
    builder.closedOrInvalidResultHandler(Runnable {
        runOnPlayer(plugin, player, onClose)
    })
    return sendForm(player, builder)
}

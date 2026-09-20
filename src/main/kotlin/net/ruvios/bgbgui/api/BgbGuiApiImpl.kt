package net.ruvios.bgbgui.api

import net.ruvios.bgbgui.forms.createCustom
import net.ruvios.bgbgui.forms.createModal
import net.ruvios.bgbgui.forms.createSimple
import org.bukkit.entity.Player

internal object BgbGuiApiImpl : BgbGuiApi {

    override fun simple(
        player: Player,
        title: String,
        content: String,
        buttons: List<FormButton>,
        onSelect: (Int, String) -> Unit,
        onClose: () -> Unit,
    ): Boolean {
        return createSimple(player, title, content, buttons, onSelect, onClose)
    }

    override fun modal(
        player: Player,
        title: String,
        content: String,
        button1: String,
        button2: String,
        onButton1: () -> Unit,
        onButton2: () -> Unit,
        onClose: () -> Unit,
    ): Boolean {
        return createModal(player, title, content, button1, button2, onButton1, onButton2, onClose)
    }

    override fun custom(
        player: Player,
        title: String,
        fields: List<FormField>,
        onSubmit: (FormValues) -> Unit,
        onClose: () -> Unit,
    ): Boolean {
        return createCustom(player, title, fields, onSubmit, onClose)
    }
}

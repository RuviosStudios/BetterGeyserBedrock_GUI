package net.ruvios.bgbgui.api

import org.bukkit.entity.Player

internal interface BgbGuiApi {

    fun simple(
        player: Player,
        title: String,
        content: String,
        buttons: List<FormButton>,
        onSelect: (Int, String) -> Unit = { _, _ -> },
        onClose: () -> Unit = {},
    ): Boolean

    fun modal(
        player: Player,
        title: String,
        content: String,
        button1: String,
        button2: String,
        onButton1: () -> Unit = {},
        onButton2: () -> Unit = {},
        onClose: () -> Unit = {},
    ): Boolean

    fun custom(
        player: Player,
        title: String,
        fields: List<FormField>,
        onSubmit: (FormValues) -> Unit = {},
        onClose: () -> Unit = {},
    ): Boolean
}

package net.ruvios.bgbgui.api

import net.ruvios.bgbgui.BgbGuiPlugin
import net.ruvios.bgbgui.api.event.BgbCustomFormEvent
import net.ruvios.bgbgui.api.event.BgbModalFormEvent
import net.ruvios.bgbgui.api.event.BgbSimpleFormEvent
import org.bukkit.entity.Player
import org.bukkit.event.Event
import java.util.function.BiConsumer
import java.util.function.Consumer

object BgbGui {

    @JvmSynthetic
    fun simple(
        player: Player,
        title: String,
        content: String,
        vararg buttons: String,
        id: String? = null,
        onClose: () -> Unit = {},
        onSelect: (Int, String) -> Unit = { _, _ -> },
    ): Boolean {
        return openSimple(player, title, content, buttons.toList(), id, onSelect, onClose)
    }

    @JvmSynthetic
    fun simple(
        player: Player,
        title: String,
        content: String,
        buttons: List<String>,
        id: String? = null,
        onClose: () -> Unit = {},
        onSelect: (Int, String) -> Unit = { _, _ -> },
    ): Boolean {
        return openSimple(player, title, content, buttons, id, onSelect, onClose)
    }

    @JvmStatic
    @JvmOverloads
    @JvmName("simple")
    fun simpleJava(
        player: Player,
        title: String,
        content: String,
        buttons: List<String>,
        onSelect: BiConsumer<Int, String>? = null,
        onClose: Runnable? = null,
        id: String? = null,
    ): Boolean {
        return openSimple(
            player,
            title,
            content,
            buttons,
            id,
            { index, label -> onSelect?.accept(index, label) },
            { onClose?.run() },
        )
    }

    @JvmSynthetic
    fun modal(
        player: Player,
        title: String,
        content: String,
        button1: String,
        button2: String,
        id: String? = null,
        onYes: () -> Unit = {},
        onNo: () -> Unit = {},
        onClose: () -> Unit = {},
        onButton1: () -> Unit = onYes,
        onButton2: () -> Unit = onNo,
    ): Boolean {
        return openModal(player, title, content, button1, button2, id, onButton1, onButton2, onClose)
    }

    @JvmStatic
    @JvmOverloads
    @JvmName("modal")
    fun modalJava(
        player: Player,
        title: String,
        content: String,
        button1: String,
        button2: String,
        onYes: Runnable? = null,
        onNo: Runnable? = null,
        onClose: Runnable? = null,
        id: String? = null,
    ): Boolean {
        return openModal(
            player,
            title,
            content,
            button1,
            button2,
            id,
            { onYes?.run() },
            { onNo?.run() },
            { onClose?.run() },
        )
    }

    @JvmSynthetic
    fun custom(
        player: Player,
        title: String,
        id: String? = null,
        build: CustomFormBuilder.() -> Unit,
    ): Boolean {
        val spec = CustomFormBuilder().apply(build)
        return openCustom(player, title, spec.fields, id, spec.submit, spec.close)
    }

    @JvmSynthetic
    fun custom(
        player: Player,
        title: String,
        fields: List<FormField>,
        id: String? = null,
        onSubmit: (FormValues) -> Unit = {},
        onClose: () -> Unit = {},
    ): Boolean {
        return openCustom(player, title, fields, id, onSubmit, onClose)
    }

    @JvmStatic
    @JvmOverloads
    @JvmName("custom")
    fun customJava(
        player: Player,
        title: String,
        fields: List<FormField>,
        onSubmit: Consumer<FormValues>? = null,
        onClose: Runnable? = null,
        id: String? = null,
    ): Boolean {
        return openCustom(
            player,
            title,
            fields,
            id,
            { values -> onSubmit?.accept(values) },
            { onClose?.run() },
        )
    }

    private fun openSimple(
        player: Player,
        title: String,
        content: String,
        buttons: List<String>,
        id: String?,
        onSelect: (Int, String) -> Unit,
        onClose: () -> Unit,
    ): Boolean {
        val api = currentApi() ?: return false
        val formId = resolveFormId(id, title)
        return api.simple(player, title, content, buttons, { index, label ->
            onSelect(index, label)
            fire(BgbSimpleFormEvent(player, formId, title, index, label))
        }, onClose)
    }

    private fun openModal(
        player: Player,
        title: String,
        content: String,
        button1: String,
        button2: String,
        id: String?,
        onYes: () -> Unit,
        onNo: () -> Unit,
        onClose: () -> Unit,
    ): Boolean {
        val api = currentApi() ?: return false
        val formId = resolveFormId(id, title)
        return api.modal(player, title, content, button1, button2, {
            onYes()
            fire(BgbModalFormEvent(player, formId, title, true, button1))
        }, {
            onNo()
            fire(BgbModalFormEvent(player, formId, title, false, button2))
        }, onClose)
    }

    private fun openCustom(
        player: Player,
        title: String,
        fields: List<FormField>,
        id: String?,
        onSubmit: (FormValues) -> Unit,
        onClose: () -> Unit,
    ): Boolean {
        val api = currentApi() ?: return false
        val formId = resolveFormId(id, title)
        return api.custom(player, title, fields, { values ->
            onSubmit(values)
            fire(BgbCustomFormEvent(player, formId, title, values))
        }, onClose)
    }

    private fun currentApi(): BgbGuiApi? {
        if (BgbGuiPlugin.loaded().not()) {
            return null
        }
        return BgbGuiApiImpl
    }

    private fun fire(event: Event) {
        try {
            BgbGuiPlugin.instance.server.pluginManager.callEvent(event)
        } catch (_: Throwable) {
        }
    }
}

class CustomFormBuilder {
    internal val fields = mutableListOf<FormField>()
    internal var submit: (FormValues) -> Unit = {}
    internal var close: () -> Unit = {}

    fun label(text: String) {
        fields += FormField.Label(text)
    }

    fun input(
        id: String,
        label: String,
        placeholder: String = "",
        default: String = "",
    ) {
        fields += FormField.Input(id, label, placeholder, default)
    }

    fun dropdown(
        id: String,
        label: String,
        vararg options: String,
        defaultIndex: Int = 0,
    ) {
        fields += FormField.Dropdown(id, label, options.toList(), defaultIndex)
    }

    fun dropdown(
        id: String,
        label: String,
        options: List<String>,
        defaultIndex: Int = 0,
    ) {
        fields += FormField.Dropdown(id, label, options, defaultIndex)
    }

    fun toggle(
        id: String,
        label: String,
        default: Boolean = false,
    ) {
        fields += FormField.Toggle(id, label, default)
    }

    fun slider(
        id: String,
        label: String,
        min: Float,
        max: Float,
        step: Float = 1f,
        default: Float = min,
    ) {
        fields += FormField.Slider(id, label, min, max, step, default)
    }

    fun onSubmit(handler: (FormValues) -> Unit) {
        submit = handler
    }

    fun onClose(handler: () -> Unit) {
        close = handler
    }
}

package net.ruvios.bgbgui.skript

import ch.njol.skript.Skript
import ch.njol.skript.config.SectionNode
import ch.njol.skript.lang.Expression
import ch.njol.skript.lang.Section
import ch.njol.skript.lang.SkriptParser
import ch.njol.skript.lang.TriggerItem
import ch.njol.util.Kleenean
import net.ruvios.bgbgui.api.BgbGui
import net.ruvios.bgbgui.api.FormField
import org.bukkit.entity.Player
import org.bukkit.event.Event

class SecOpenCustomForm : Section() {

    private lateinit var player: Expression<Player>
    private var id: Expression<String>? = null
    private var title: Expression<String>? = null
    private val fields = mutableListOf<FieldSpec>()

    override fun init(
        exprs: Array<Expression<*>>,
        matchedPattern: Int,
        isDelayed: Kleenean,
        parseResult: SkriptParser.ParseResult,
        sectionNode: SectionNode,
        triggerItems: List<TriggerItem>,
    ): Boolean {
        @Suppress("UNCHECKED_CAST")
        player = exprs[0] as Expression<Player>

        val ok = SecFormHelpers.forEachEntry(sectionNode) { key, value, node ->
            when (key) {
                "id" -> {
                    id = SecFormHelpers.parseStringExpression(value)
                    if (id == null) {
                        Skript.error("Ungültige id in Zeile ${node.line}: $value")
                        return@forEachEntry false
                    }
                    true
                }
                "title", "titel" -> {
                    title = SecFormHelpers.parseStringExpression(value)
                    if (title == null) {
                        Skript.error("Ungültiger title in Zeile ${node.line}: $value")
                        return@forEachEntry false
                    }
                    true
                }
                "label", "text", "content", "beschreibung" -> {
                    val text = SecFormHelpers.parseStringExpression(value)
                    if (text == null) {
                        Skript.error("Ungültiges label in Zeile ${node.line}: $value")
                        return@forEachEntry false
                    }
                    fields += FieldSpec.Label(text)
                    true
                }
                "input", "inputs" -> {
                    val spec = parseInput(value, node.line) ?: return@forEachEntry false
                    fields += spec
                    true
                }
                "dropdown", "dropdowns" -> {
                    val spec = parseDropdown(value, node.line) ?: return@forEachEntry false
                    fields += spec
                    true
                }
                "toggle", "toggles" -> {
                    val spec = parseToggle(value, node.line) ?: return@forEachEntry false
                    fields += spec
                    true
                }
                "slider", "sliders" -> {
                    val spec = parseSlider(value, node.line) ?: return@forEachEntry false
                    fields += spec
                    true
                }
                else -> {
                    Skript.error("Unbekannter Eintrag '$key' in custom form (Zeile ${node.line})")
                    false
                }
            }
        }
        if (!ok) {
            return false
        }
        if (title == null) {
            Skript.error("custom form braucht 'title: \"...\"'")
            return false
        }
        return true
    }

    override fun walk(event: Event): TriggerItem? {
        val target = player.getSingle(event)
        val heading = title?.getSingle(event)
        if (target != null && heading != null) {
            val resolved = fields.mapNotNull { it.resolve(event) }
            val formId = id?.getSingle(event)
            BgbGui.custom(target, heading, resolved, id = formId)
        }
        return walk(event, false)
    }

    override fun toString(event: Event?, debug: Boolean): String {
        return "open custom form to ${player.toString(event, debug)}"
    }

    companion object {
        fun register() {
            Skript.registerSection(
                SecOpenCustomForm::class.java,
                "open [bgb] custom form to %player%",
            )
        }

        private fun parseInput(raw: String, line: Int): FieldSpec.Input? {
            val full = SecFormHelpers.parseResult(
                raw,
                "%string% [labeled %-string%] [with placeholder %-string%] [with default %-string%]",
            )
            if (full != null) {
                @Suppress("UNCHECKED_CAST")
                val id = full.exprs?.getOrNull(0) as? Expression<String> ?: return null
                @Suppress("UNCHECKED_CAST")
                val label = full.exprs?.getOrNull(1) as? Expression<String>
                @Suppress("UNCHECKED_CAST")
                val placeholder = full.exprs?.getOrNull(2) as? Expression<String>
                @Suppress("UNCHECKED_CAST")
                val default = full.exprs?.getOrNull(3) as? Expression<String>
                return FieldSpec.Input(id, label, placeholder, default)
            }
            Skript.error(
                "Ungültiges input in Zeile $line. " +
                    "Erwartet: \"id\" [labeled \"Label\"] [with placeholder \"...\"] [with default \"...\"]",
            )
            return null
        }

        private fun parseDropdown(raw: String, line: Int): FieldSpec.Dropdown? {
            val withLabel = SecFormHelpers.parseResult(
                raw,
                "%string% labeled %string% with options %strings% [with default %-number%]",
            )
            if (withLabel != null) {
                @Suppress("UNCHECKED_CAST")
                val id = withLabel.exprs?.getOrNull(0) as? Expression<String> ?: return null
                @Suppress("UNCHECKED_CAST")
                val label = withLabel.exprs?.getOrNull(1) as? Expression<String> ?: return null
                @Suppress("UNCHECKED_CAST")
                val options = withLabel.exprs?.getOrNull(2) as? Expression<String> ?: return null
                @Suppress("UNCHECKED_CAST")
                val defaultIndex = withLabel.exprs?.getOrNull(3) as? Expression<Number>
                return FieldSpec.Dropdown(id, label, options, defaultIndex)
            }
            val short = SecFormHelpers.parseResult(
                raw,
                "%string% with options %strings% [with default %-number%]",
            )
            if (short != null) {
                @Suppress("UNCHECKED_CAST")
                val id = short.exprs?.getOrNull(0) as? Expression<String> ?: return null
                @Suppress("UNCHECKED_CAST")
                val options = short.exprs?.getOrNull(1) as? Expression<String> ?: return null
                @Suppress("UNCHECKED_CAST")
                val defaultIndex = short.exprs?.getOrNull(2) as? Expression<Number>
                return FieldSpec.Dropdown(id, null, options, defaultIndex)
            }
            Skript.error(
                "Ungültiges dropdown in Zeile $line. " +
                    "Erwartet: \"id\" [labeled \"Label\"] with options \"A\", \"B\" [with default 0]",
            )
            return null
        }

        private fun parseToggle(raw: String, line: Int): FieldSpec.Toggle? {
            val withLabel = SecFormHelpers.parseResult(
                raw,
                "%string% labeled %string% [with default %-boolean%]",
            )
            if (withLabel != null) {
                @Suppress("UNCHECKED_CAST")
                val id = withLabel.exprs?.getOrNull(0) as? Expression<String> ?: return null
                @Suppress("UNCHECKED_CAST")
                val label = withLabel.exprs?.getOrNull(1) as? Expression<String> ?: return null
                @Suppress("UNCHECKED_CAST")
                val default = withLabel.exprs?.getOrNull(2) as? Expression<Boolean>
                return FieldSpec.Toggle(id, label, default)
            }
            val short = SecFormHelpers.parseResult(
                raw,
                "%string% [with default %-boolean%]",
            )
            if (short != null) {
                @Suppress("UNCHECKED_CAST")
                val id = short.exprs?.getOrNull(0) as? Expression<String> ?: return null
                @Suppress("UNCHECKED_CAST")
                val default = short.exprs?.getOrNull(1) as? Expression<Boolean>
                return FieldSpec.Toggle(id, null, default)
            }
            Skript.error(
                "Ungültiges toggle in Zeile $line. " +
                    "Erwartet: \"id\" [labeled \"Label\"] [with default true/false]",
            )
            return null
        }

        private fun parseSlider(raw: String, line: Int): FieldSpec.Slider? {
            val withLabel = SecFormHelpers.parseResult(
                raw,
                "%string% labeled %string% from %number% to %number% [with step %-number%] [with default %-number%]",
            )
            if (withLabel != null) {
                @Suppress("UNCHECKED_CAST")
                val id = withLabel.exprs?.getOrNull(0) as? Expression<String> ?: return null
                @Suppress("UNCHECKED_CAST")
                val label = withLabel.exprs?.getOrNull(1) as? Expression<String> ?: return null
                @Suppress("UNCHECKED_CAST")
                val min = withLabel.exprs?.getOrNull(2) as? Expression<Number> ?: return null
                @Suppress("UNCHECKED_CAST")
                val max = withLabel.exprs?.getOrNull(3) as? Expression<Number> ?: return null
                @Suppress("UNCHECKED_CAST")
                val step = withLabel.exprs?.getOrNull(4) as? Expression<Number>
                @Suppress("UNCHECKED_CAST")
                val default = withLabel.exprs?.getOrNull(5) as? Expression<Number>
                return FieldSpec.Slider(id, label, min, max, step, default)
            }
            val short = SecFormHelpers.parseResult(
                raw,
                "%string% from %number% to %number% [with step %-number%] [with default %-number%]",
            )
            if (short != null) {
                @Suppress("UNCHECKED_CAST")
                val id = short.exprs?.getOrNull(0) as? Expression<String> ?: return null
                @Suppress("UNCHECKED_CAST")
                val min = short.exprs?.getOrNull(1) as? Expression<Number> ?: return null
                @Suppress("UNCHECKED_CAST")
                val max = short.exprs?.getOrNull(2) as? Expression<Number> ?: return null
                @Suppress("UNCHECKED_CAST")
                val step = short.exprs?.getOrNull(3) as? Expression<Number>
                @Suppress("UNCHECKED_CAST")
                val default = short.exprs?.getOrNull(4) as? Expression<Number>
                return FieldSpec.Slider(id, null, min, max, step, default)
            }
            Skript.error(
                "Ungültiges slider in Zeile $line. " +
                    "Erwartet: \"id\" [labeled \"Label\"] from 0 to 10 [with step 1] [with default 5]",
            )
            return null
        }
    }

    private sealed interface FieldSpec {
        fun resolve(event: Event): FormField?

        class Label(private val text: Expression<String>) : FieldSpec {
            override fun resolve(event: Event): FormField? {
                val value = text.getSingle(event) ?: return null
                return FormField.label(value)
            }
        }

        class Input(
            private val id: Expression<String>,
            private val label: Expression<String>?,
            private val placeholder: Expression<String>?,
            private val default: Expression<String>?,
        ) : FieldSpec {
            override fun resolve(event: Event): FormField? {
                val fieldId = id.getSingle(event) ?: return null
                val fieldLabel = label?.getSingle(event) ?: fieldId
                return FormField.input(
                    fieldId,
                    fieldLabel,
                    placeholder?.getSingle(event) ?: "",
                    default?.getSingle(event) ?: "",
                )
            }
        }

        class Dropdown(
            private val id: Expression<String>,
            private val label: Expression<String>?,
            private val options: Expression<String>,
            private val defaultIndex: Expression<Number>?,
        ) : FieldSpec {
            override fun resolve(event: Event): FormField? {
                val fieldId = id.getSingle(event) ?: return null
                val fieldLabel = label?.getSingle(event) ?: fieldId
                val opts = options.getArray(event).map { it.toString() }
                if (opts.isEmpty()) {
                    return null
                }
                val index = defaultIndex?.getSingle(event)?.toInt() ?: 0
                return FormField.dropdown(fieldId, fieldLabel, opts, index.coerceIn(0, opts.lastIndex))
            }
        }

        class Toggle(
            private val id: Expression<String>,
            private val label: Expression<String>?,
            private val default: Expression<Boolean>?,
        ) : FieldSpec {
            override fun resolve(event: Event): FormField? {
                val fieldId = id.getSingle(event) ?: return null
                val fieldLabel = label?.getSingle(event) ?: fieldId
                return FormField.toggle(fieldId, fieldLabel, default?.getSingle(event) ?: false)
            }
        }

        class Slider(
            private val id: Expression<String>,
            private val label: Expression<String>?,
            private val min: Expression<Number>,
            private val max: Expression<Number>,
            private val step: Expression<Number>?,
            private val default: Expression<Number>?,
        ) : FieldSpec {
            override fun resolve(event: Event): FormField? {
                val fieldId = id.getSingle(event) ?: return null
                val fieldLabel = label?.getSingle(event) ?: fieldId
                val minV = min.getSingle(event)?.toFloat() ?: return null
                val maxV = max.getSingle(event)?.toFloat() ?: return null
                val stepV = step?.getSingle(event)?.toFloat() ?: 1f
                val defaultV = default?.getSingle(event)?.toFloat() ?: minV
                return FormField.slider(fieldId, fieldLabel, minV, maxV, stepV, defaultV)
            }
        }
    }
}

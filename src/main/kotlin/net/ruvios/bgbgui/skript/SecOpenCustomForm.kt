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
    private val inputs = mutableListOf<Expression<String>>()

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
                "input", "inputs" -> {
                    val expr = SecFormHelpers.parseStringExpression(value)
                    if (expr == null) {
                        Skript.error("Ungültiges input in Zeile ${node.line}: $value")
                        return@forEachEntry false
                    }
                    inputs += expr
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
            val fields = inputs.mapNotNull { expr ->
                val label = expr.getSingle(event) ?: return@mapNotNull null
                FormField.input(id = label, label = label)
            }
            val formId = id?.getSingle(event)
            BgbGui.custom(target, heading, fields, id = formId)
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
    }
}

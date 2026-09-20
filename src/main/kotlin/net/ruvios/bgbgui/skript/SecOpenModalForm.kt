package net.ruvios.bgbgui.skript

import ch.njol.skript.Skript
import ch.njol.skript.config.SectionNode
import ch.njol.skript.lang.Expression
import ch.njol.skript.lang.Section
import ch.njol.skript.lang.SkriptParser
import ch.njol.skript.lang.TriggerItem
import ch.njol.util.Kleenean
import net.ruvios.bgbgui.api.BgbGui
import org.bukkit.entity.Player
import org.bukkit.event.Event

class SecOpenModalForm : Section() {

    private lateinit var player: Expression<Player>
    private var id: Expression<String>? = null
    private var title: Expression<String>? = null
    private var description: Expression<String>? = null
    private var yesButton: Expression<String>? = null
    private var noButton: Expression<String>? = null

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
                "description", "desc", "content", "beschreibung" -> {
                    description = SecFormHelpers.parseStringExpression(value)
                    if (description == null) {
                        Skript.error("Ungültige description in Zeile ${node.line}: $value")
                        return@forEachEntry false
                    }
                    true
                }
                "yes", "ja", "button1" -> {
                    yesButton = SecFormHelpers.parseStringExpression(value)
                    if (yesButton == null) {
                        Skript.error("Ungültiges yes in Zeile ${node.line}: $value")
                        return@forEachEntry false
                    }
                    true
                }
                "no", "nein", "button2" -> {
                    noButton = SecFormHelpers.parseStringExpression(value)
                    if (noButton == null) {
                        Skript.error("Ungültiges no in Zeile ${node.line}: $value")
                        return@forEachEntry false
                    }
                    true
                }
                else -> {
                    Skript.error("Unbekannter Eintrag '$key' in modal form (Zeile ${node.line})")
                    false
                }
            }
        }
        if (!ok) {
            return false
        }
        if (title == null) {
            Skript.error("modal form braucht 'title: \"...\"'")
            return false
        }
        return true
    }

    override fun walk(event: Event): TriggerItem? {
        val target = player.getSingle(event)
        val heading = title?.getSingle(event)
        if (target != null && heading != null) {
            val text = description?.getSingle(event) ?: ""
            val yes = yesButton?.getSingle(event) ?: "Yes"
            val no = noButton?.getSingle(event) ?: "No"
            val formId = id?.getSingle(event)
            BgbGui.modal(target, heading, text, yes, no, id = formId)
        }
        return walk(event, false)
    }

    override fun toString(event: Event?, debug: Boolean): String {
        return "open modal form to ${player.toString(event, debug)}"
    }

    companion object {
        fun register() {
            Skript.registerSection(
                SecOpenModalForm::class.java,
                "open [bgb] modal form to %player%",
            )
        }
    }
}

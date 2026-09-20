package net.ruvios.bgbgui.skript

import ch.njol.skript.Skript
import ch.njol.skript.lang.Effect
import ch.njol.skript.lang.Expression
import ch.njol.skript.lang.SkriptParser
import ch.njol.util.Kleenean
import net.ruvios.bgbgui.api.BgbGui
import net.ruvios.bgbgui.api.FormField
import org.bukkit.entity.Player
import org.bukkit.event.Event

class EffOpenCustomForm : Effect() {

    private var id: Expression<String>? = null
    private lateinit var title: Expression<String>
    private var inputs: Expression<String>? = null
    private lateinit var player: Expression<Player>

    override fun init(
        exprs: Array<Expression<*>>,
        matchedPattern: Int,
        isDelayed: Kleenean,
        parseResult: SkriptParser.ParseResult,
    ): Boolean {
        when (matchedPattern) {
            0 -> {
                title = exprs[0] as Expression<String>
                inputs = exprs[1] as Expression<String>
                player = exprs[2] as Expression<Player>
            }
            1 -> {
                title = exprs[0] as Expression<String>
                player = exprs[1] as Expression<Player>
            }
            2 -> {
                id = exprs[0] as Expression<String>
                title = exprs[1] as Expression<String>
                inputs = exprs[2] as Expression<String>
                player = exprs[3] as Expression<Player>
            }
            else -> {
                id = exprs[0] as Expression<String>
                title = exprs[1] as Expression<String>
                player = exprs[2] as Expression<Player>
            }
        }
        return true
    }

    override fun execute(event: Event) {
        val target = player.getSingle(event) ?: return
        val heading = title.getSingle(event) ?: return
        val labels = inputs?.getArray(event) ?: emptyArray()
        val fields = labels.map { label ->
            FormField.input(id = label, label = label)
        }
        val formId = id?.getSingle(event)
        BgbGui.custom(target, heading, fields, id = formId)
    }

    override fun toString(event: Event?, debug: Boolean): String {
        return "open custom form titled ${title.toString(event, debug)} to ${player.toString(event, debug)}"
    }

    companion object {
        fun register() {
            Skript.registerEffect(
                EffOpenCustomForm::class.java,
                "open custom form titled %string% with inputs %strings% to %player%",
                "open custom form titled %string% to %player%",
                "open custom form with id %string% titled %string% with inputs %strings% to %player%",
                "open custom form with id %string% titled %string% to %player%",
            )
        }
    }
}

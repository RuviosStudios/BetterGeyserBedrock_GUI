package net.ruvios.bgbgui.skript

import ch.njol.skript.Skript
import ch.njol.skript.lang.Effect
import ch.njol.skript.lang.Expression
import ch.njol.skript.lang.SkriptParser
import ch.njol.util.Kleenean
import net.ruvios.bgbgui.api.BgbGui
import org.bukkit.entity.Player
import org.bukkit.event.Event

class EffOpenSimpleForm : Effect() {

    private var id: Expression<String>? = null
    private lateinit var title: Expression<String>
    private lateinit var description: Expression<String>
    private var buttons: Expression<String>? = null
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
                description = exprs[1] as Expression<String>
                buttons = exprs[2] as Expression<String>
                player = exprs[3] as Expression<Player>
            }
            1 -> {
                title = exprs[0] as Expression<String>
                description = exprs[1] as Expression<String>
                player = exprs[2] as Expression<Player>
            }
            2 -> {
                id = exprs[0] as Expression<String>
                title = exprs[1] as Expression<String>
                description = exprs[2] as Expression<String>
                buttons = exprs[3] as Expression<String>
                player = exprs[4] as Expression<Player>
            }
            else -> {
                id = exprs[0] as Expression<String>
                title = exprs[1] as Expression<String>
                description = exprs[2] as Expression<String>
                player = exprs[3] as Expression<Player>
            }
        }
        return true
    }

    override fun execute(event: Event) {
        val target = player.getSingle(event) ?: return
        val heading = title.getSingle(event) ?: return
        val text = description.getSingle(event) ?: ""
        val labels = buttons?.getArray(event)?.map { it.toString() } ?: emptyList()
        val formId = id?.getSingle(event)
        BgbGui.simple(target, heading, text, labels, id = formId)
    }

    override fun toString(event: Event?, debug: Boolean): String {
        return "open simple form titled ${title.toString(event, debug)} to ${player.toString(event, debug)}"
    }

    companion object {
        fun register() {
            Skript.registerEffect(
                EffOpenSimpleForm::class.java,
                "open simple form titled %string% with description %string% with buttons %strings% to %player%",
                "open simple form titled %string% with description %string% to %player%",
                "open simple form with id %string% titled %string% with description %string% with buttons %strings% to %player%",
                "open simple form with id %string% titled %string% with description %string% to %player%",
            )
        }
    }
}

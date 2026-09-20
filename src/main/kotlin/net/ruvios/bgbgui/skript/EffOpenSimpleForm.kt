package net.ruvios.bgbgui.skript

import ch.njol.skript.Skript
import ch.njol.skript.lang.Effect
import ch.njol.skript.lang.Expression
import ch.njol.skript.lang.SkriptParser
import ch.njol.util.Kleenean
import net.ruvios.bgbgui.api.BgbGui
import net.ruvios.bgbgui.api.FormButton
import org.bukkit.entity.Player
import org.bukkit.event.Event

class EffOpenSimpleForm : Effect() {

    private var id: Expression<String>? = null
    private lateinit var title: Expression<String>
    private lateinit var description: Expression<String>
    private var buttons: Expression<String>? = null
    private var images: Expression<String>? = null
    private lateinit var player: Expression<Player>

    override fun init(
        exprs: Array<Expression<*>>,
        matchedPattern: Int,
        isDelayed: Kleenean,
        parseResult: SkriptParser.ParseResult,
    ): Boolean {
        val withId = matchedPattern >= 3
        var index = 0
        if (withId) {
            id = exprs[index++] as Expression<String>
        }
        title = exprs[index++] as Expression<String>
        description = exprs[index++] as Expression<String>
        when (matchedPattern) {
            0, 3 -> {
                buttons = exprs[index++] as Expression<String>
                images = exprs[index++] as Expression<String>
            }
            1, 4 -> {
                buttons = exprs[index++] as Expression<String>
            }
        }
        player = exprs[index] as Expression<Player>
        return true
    }

    override fun execute(event: Event) {
        val target = player.getSingle(event) ?: return
        val heading = title.getSingle(event) ?: return
        val text = description.getSingle(event) ?: ""
        val labels = buttons?.getArray(event)?.map { it.toString() } ?: emptyList()
        val icons = images?.getArray(event)?.map { it.toString() } ?: emptyList()
        val entries = labels.mapIndexed { i, label -> FormButton.auto(label, icons.getOrNull(i)) }
        val formId = id?.getSingle(event)
        BgbGui.simple(target, heading, text, entries, id = formId)
    }

    override fun toString(event: Event?, debug: Boolean): String {
        return "open simple form titled ${title.toString(event, debug)} to ${player.toString(event, debug)}"
    }

    companion object {
        fun register() {
            Skript.registerEffect(
                EffOpenSimpleForm::class.java,
                "open simple form titled %string% with description %string% with buttons %strings% with images %strings% to %player%",
                "open simple form titled %string% with description %string% with buttons %strings% to %player%",
                "open simple form titled %string% with description %string% to %player%",
                "open simple form with id %string% titled %string% with description %string% with buttons %strings% with images %strings% to %player%",
                "open simple form with id %string% titled %string% with description %string% with buttons %strings% to %player%",
                "open simple form with id %string% titled %string% with description %string% to %player%",
            )
        }
    }
}

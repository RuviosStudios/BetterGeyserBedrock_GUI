package net.ruvios.bgbgui.skript

import ch.njol.skript.Skript
import ch.njol.skript.lang.Literal
import ch.njol.skript.lang.SkriptEvent
import ch.njol.skript.lang.SkriptParser
import net.ruvios.bgbgui.api.event.BgbCustomFormEvent
import net.ruvios.bgbgui.api.event.BgbModalFormEvent
import net.ruvios.bgbgui.api.event.BgbSimpleFormEvent
import org.bukkit.entity.Player
import org.bukkit.event.Event

class EvtBgbSimpleForm : SkriptEvent() {

    private var id: Literal<String>? = null

    override fun init(
        args: Array<Literal<*>>,
        matchedPattern: Int,
        parseResult: SkriptParser.ParseResult,
    ): Boolean {
        if (matchedPattern == 1 && args.isNotEmpty()) {
            @Suppress("UNCHECKED_CAST")
            id = args[0] as Literal<String>
        }
        return true
    }

    override fun check(event: Event): Boolean {
        val form = event as? BgbSimpleFormEvent ?: return false
        val wanted = id?.getSingle(event) ?: return true
        return form.id.equals(wanted, ignoreCase = true)
    }

    override fun toString(event: Event?, debug: Boolean): String {
        val wanted = id?.toString(event, debug) ?: return "bgb simple form"
        return "bgb simple form with id $wanted"
    }
}

class EvtBgbModalForm : SkriptEvent() {

    private var id: Literal<String>? = null

    override fun init(
        args: Array<Literal<*>>,
        matchedPattern: Int,
        parseResult: SkriptParser.ParseResult,
    ): Boolean {
        if (matchedPattern == 1 && args.isNotEmpty()) {
            @Suppress("UNCHECKED_CAST")
            id = args[0] as Literal<String>
        }
        return true
    }

    override fun check(event: Event): Boolean {
        val form = event as? BgbModalFormEvent ?: return false
        val wanted = id?.getSingle(event) ?: return true
        return form.id.equals(wanted, ignoreCase = true)
    }

    override fun toString(event: Event?, debug: Boolean): String {
        val wanted = id?.toString(event, debug) ?: return "bgb modal form"
        return "bgb modal form with id $wanted"
    }
}

class EvtBgbCustomForm : SkriptEvent() {

    private var id: Literal<String>? = null

    override fun init(
        args: Array<Literal<*>>,
        matchedPattern: Int,
        parseResult: SkriptParser.ParseResult,
    ): Boolean {
        if (matchedPattern == 1 && args.isNotEmpty()) {
            @Suppress("UNCHECKED_CAST")
            id = args[0] as Literal<String>
        }
        return true
    }

    override fun check(event: Event): Boolean {
        val form = event as? BgbCustomFormEvent ?: return false
        val wanted = id?.getSingle(event) ?: return true
        return form.id.equals(wanted, ignoreCase = true)
    }

    override fun toString(event: Event?, debug: Boolean): String {
        val wanted = id?.toString(event, debug) ?: return "bgb custom form"
        return "bgb custom form with id $wanted"
    }
}

internal object SkriptEvents {

    fun register() {
        Skript.registerEvent(
            "BGB Simple Form",
            EvtBgbSimpleForm::class.java,
            BgbSimpleFormEvent::class.java,
            "bgb simple form",
            "bgb simple form with id %string%",
        )
        Skript.registerEvent(
            "BGB Modal Form",
            EvtBgbModalForm::class.java,
            BgbModalFormEvent::class.java,
            "bgb modal form",
            "bgb modal form with id %string%",
        )
        Skript.registerEvent(
            "BGB Custom Form",
            EvtBgbCustomForm::class.java,
            BgbCustomFormEvent::class.java,
            "bgb custom form",
            "bgb custom form with id %string%",
        )

        ch.njol.skript.registrations.EventValues.registerEventValue(
            BgbSimpleFormEvent::class.java,
            Player::class.java,
        ) { event -> event.player }
        ch.njol.skript.registrations.EventValues.registerEventValue(
            BgbSimpleFormEvent::class.java,
            Number::class.java,
        ) { event -> event.index }
        ch.njol.skript.registrations.EventValues.registerEventValue(
            BgbSimpleFormEvent::class.java,
            String::class.java,
        ) { event -> event.button }

        ch.njol.skript.registrations.EventValues.registerEventValue(
            BgbModalFormEvent::class.java,
            Player::class.java,
        ) { event -> event.player }
        ch.njol.skript.registrations.EventValues.registerEventValue(
            BgbModalFormEvent::class.java,
            String::class.java,
        ) { event -> event.button }
        ch.njol.skript.registrations.EventValues.registerEventValue(
            BgbModalFormEvent::class.java,
            Boolean::class.javaObjectType,
        ) { event -> event.yes }

        ch.njol.skript.registrations.EventValues.registerEventValue(
            BgbCustomFormEvent::class.java,
            Player::class.java,
        ) { event -> event.player }
    }
}

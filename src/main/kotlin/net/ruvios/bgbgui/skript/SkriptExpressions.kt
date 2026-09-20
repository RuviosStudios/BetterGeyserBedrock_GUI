package net.ruvios.bgbgui.skript

import ch.njol.skript.Skript
import ch.njol.skript.lang.Expression
import ch.njol.skript.lang.ExpressionType
import ch.njol.skript.lang.SkriptParser
import ch.njol.skript.lang.util.SimpleExpression
import ch.njol.util.Kleenean
import net.ruvios.bgbgui.api.event.BgbCustomFormEvent
import net.ruvios.bgbgui.api.event.BgbModalFormEvent
import net.ruvios.bgbgui.api.event.BgbSimpleFormEvent
import org.bukkit.event.Event

internal object SkriptExpressions {

    fun register() {
        Skript.registerExpression(
            ExprFormButton::class.java,
            String::class.java,
            ExpressionType.SIMPLE,
            "[the] [bgb] form button",
        )
        Skript.registerExpression(
            ExprFormIndex::class.java,
            Long::class.javaObjectType,
            ExpressionType.SIMPLE,
            "[the] [bgb] form index",
        )
        Skript.registerExpression(
            ExprFormYes::class.java,
            Boolean::class.javaObjectType,
            ExpressionType.SIMPLE,
            "[the] [bgb] form yes",
        )
        Skript.registerExpression(
            ExprFormValue::class.java,
            String::class.java,
            ExpressionType.SIMPLE,
            "[the] [bgb] form value [of] %string%",
        )
        Skript.registerExpression(
            ExprFormId::class.java,
            String::class.java,
            ExpressionType.SIMPLE,
            "[the] [bgb] form id",
        )
        Skript.registerExpression(
            ExprFormTitle::class.java,
            String::class.java,
            ExpressionType.SIMPLE,
            "[the] [bgb] form title",
        )
    }
}

class ExprFormButton : SimpleExpression<String>() {

    override fun init(
        exprs: Array<Expression<*>>,
        matchedPattern: Int,
        isDelayed: Kleenean,
        parseResult: SkriptParser.ParseResult,
    ): Boolean {
        return true
    }

    override fun get(event: Event): Array<String>? {
        val button = when (event) {
            is BgbSimpleFormEvent -> event.button
            is BgbModalFormEvent -> event.button
            else -> return null
        }
        return arrayOf(button)
    }

    override fun isSingle(): Boolean = true

    override fun getReturnType(): Class<out String> = String::class.java

    override fun toString(event: Event?, debug: Boolean): String = "form button"
}

class ExprFormIndex : SimpleExpression<Long>() {

    override fun init(
        exprs: Array<Expression<*>>,
        matchedPattern: Int,
        isDelayed: Kleenean,
        parseResult: SkriptParser.ParseResult,
    ): Boolean {
        return true
    }

    override fun get(event: Event): Array<Long>? {
        val formEvent = event as? BgbSimpleFormEvent ?: return null
        return arrayOf(formEvent.index.toLong())
    }

    override fun isSingle(): Boolean = true

    override fun getReturnType(): Class<out Long> = Long::class.javaObjectType

    override fun toString(event: Event?, debug: Boolean): String = "form index"
}

class ExprFormYes : SimpleExpression<Boolean>() {

    override fun init(
        exprs: Array<Expression<*>>,
        matchedPattern: Int,
        isDelayed: Kleenean,
        parseResult: SkriptParser.ParseResult,
    ): Boolean {
        return true
    }

    override fun get(event: Event): Array<Boolean>? {
        val formEvent = event as? BgbModalFormEvent ?: return null
        return arrayOf(formEvent.yes)
    }

    override fun isSingle(): Boolean = true

    override fun getReturnType(): Class<out Boolean> = Boolean::class.javaObjectType

    override fun toString(event: Event?, debug: Boolean): String = "form yes"
}

class ExprFormValue : SimpleExpression<String>() {

    private lateinit var id: Expression<String>

    override fun init(
        exprs: Array<Expression<*>>,
        matchedPattern: Int,
        isDelayed: Kleenean,
        parseResult: SkriptParser.ParseResult,
    ): Boolean {
        id = exprs[0] as Expression<String>
        return true
    }

    override fun get(event: Event): Array<String>? {
        val formEvent = event as? BgbCustomFormEvent ?: return null
        val key = id.getSingle(event) ?: return null
        return arrayOf(formEvent.values.getString(key))
    }

    override fun isSingle(): Boolean = true

    override fun getReturnType(): Class<out String> = String::class.java

    override fun toString(event: Event?, debug: Boolean): String = "form value of ${id.toString(event, debug)}"
}

class ExprFormId : SimpleExpression<String>() {

    override fun init(
        exprs: Array<Expression<*>>,
        matchedPattern: Int,
        isDelayed: Kleenean,
        parseResult: SkriptParser.ParseResult,
    ): Boolean {
        return true
    }

    override fun get(event: Event): Array<String>? {
        val value = when (event) {
            is BgbSimpleFormEvent -> event.id
            is BgbModalFormEvent -> event.id
            is BgbCustomFormEvent -> event.id
            else -> return null
        }
        return arrayOf(value)
    }

    override fun isSingle(): Boolean = true

    override fun getReturnType(): Class<out String> = String::class.java

    override fun toString(event: Event?, debug: Boolean): String = "form id"
}

class ExprFormTitle : SimpleExpression<String>() {

    override fun init(
        exprs: Array<Expression<*>>,
        matchedPattern: Int,
        isDelayed: Kleenean,
        parseResult: SkriptParser.ParseResult,
    ): Boolean {
        return true
    }

    override fun get(event: Event): Array<String>? {
        val value = when (event) {
            is BgbSimpleFormEvent -> event.title
            is BgbModalFormEvent -> event.title
            is BgbCustomFormEvent -> event.title
            else -> return null
        }
        return arrayOf(value)
    }

    override fun isSingle(): Boolean = true

    override fun getReturnType(): Class<out String> = String::class.java

    override fun toString(event: Event?, debug: Boolean): String = "form title"
}

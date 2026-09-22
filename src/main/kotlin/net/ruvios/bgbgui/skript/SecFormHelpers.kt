package net.ruvios.bgbgui.skript

import ch.njol.skript.Skript
import ch.njol.skript.config.EntryNode
import ch.njol.skript.config.Node
import ch.njol.skript.config.SectionNode
import ch.njol.skript.config.VoidNode
import ch.njol.skript.lang.Expression
import ch.njol.skript.lang.ParseContext
import ch.njol.skript.lang.SkriptParser

internal object SecFormHelpers {

    private val parseFlags: Int = SkriptParser.PARSE_EXPRESSIONS or SkriptParser.PARSE_LITERALS

    fun parseStringExpression(raw: String): Expression<String>? {
        val parser = SkriptParser(raw.trim(), parseFlags, ParseContext.DEFAULT)
        @Suppress("UNCHECKED_CAST")
        return parser.parseExpression(String::class.java) as Expression<String>?
    }

    fun parseButtonValue(raw: String): Pair<Expression<String>, Expression<String>?>? {
        val result = parseResult(raw, "%string% [with [the] image %string%]") ?: return null
        val exprs = result.exprs ?: return null
        @Suppress("UNCHECKED_CAST")
        val label = exprs.getOrNull(0) as? Expression<String> ?: return null
        @Suppress("UNCHECKED_CAST")
        val image = exprs.getOrNull(1) as? Expression<String>
        return label to image
    }

    fun parseResult(raw: String, pattern: String): SkriptParser.ParseResult? {
        return SkriptParser.parse(
            raw.trim(),
            pattern,
            parseFlags,
            ParseContext.DEFAULT,
        )
    }

    fun forEachEntry(
        sectionNode: SectionNode,
        handler: (key: String, value: String, node: EntryNode) -> Boolean,
    ): Boolean {
        sectionNode.convertToEntries(0)
        for (node: Node in sectionNode) {
            if (node is VoidNode) {
                continue
            }
            if (node !is EntryNode) {
                Skript.error("Erwartet 'schlüssel: wert', gefunden: ${node.key}")
                return false
            }
            val key = node.key?.trim()?.lowercase()
            if (key.isNullOrEmpty()) {
                Skript.error("Leerer Schlüssel in Form-Section (Zeile ${node.line})")
                return false
            }
            if (!handler(key, node.value ?: "", node)) {
                return false
            }
        }
        return true
    }
}

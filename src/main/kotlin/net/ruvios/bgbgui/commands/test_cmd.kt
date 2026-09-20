package net.ruvios.bgbgui.commands

import com.mojang.brigadier.Command
import io.papermc.paper.command.brigadier.Commands
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents
import net.ruvios.bgbgui.api.BgbGui
import net.ruvios.bgbgui.api.FormButton
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

object TestCmd {

    fun register(plugin: JavaPlugin) {
        plugin.lifecycleManager.registerEventHandler(LifecycleEvents.COMMANDS) { event ->
            val bgbgui = Commands.literal("bgbgui")
                .executes { ctx ->
                    ctx.source.sender.sendRichMessage("<yellow>Nutze /bgbgui <simple|modal|custom>")
                    Command.SINGLE_SUCCESS
                }
                .then(
                    Commands.literal("simple")
                        .executes { ctx ->
                            val sender = ctx.source.sender
                            if (sender !is Player) return@executes 0
                            openSimple(sender)
                            Command.SINGLE_SUCCESS
                        },
                )
                .then(
                    Commands.literal("modal")
                        .executes { ctx ->
                            val sender = ctx.source.sender
                            if (sender !is Player) return@executes 0
                            openModal(sender)
                            Command.SINGLE_SUCCESS
                        },
                )
                .then(
                    Commands.literal("custom")
                        .executes { ctx ->
                            val sender = ctx.source.sender
                            if (sender !is Player) return@executes 0
                            openCustom(sender)
                            Command.SINGLE_SUCCESS
                        },
                )

            event.registrar().dispatcher.register(bgbgui)
        }
    }

    private fun openSimple(player: Player) {
        BgbGui.simple(
            player,
            "BGB_GUI Simple",
            "Wähle einen Button.",
            FormButton.url("Erster", "https://avatars.githubusercontent.com/u/52673035?s=200&v=4"),
            FormButton.path("Zweiter", "textures/i/glyph_world_template.png"),
            FormButton.of("Dritter"),
            onClose = {
                player.sendRichMessage("<gray>Simple-Form geschlossen.")
            },
        ) { index, label ->
            player.sendRichMessage("<green>Simple: $label (Index $index)")
        }
    }

    private fun openModal(player: Player) {
        BgbGui.modal(
            player,
            "BGB_GUI Modal",
            "Zwei Buttons, ja oder nein?",
            "Ja",
            "Nein",
            onYes = {
                player.sendRichMessage("<green>Modal: Ja")
            },
            onNo = {
                player.sendRichMessage("<red>Modal: Nein")
            },
            onClose = {
                player.sendRichMessage("<gray>Modal-Form geschlossen.")
            },
        )
    }

    private fun openCustom(player: Player) {
        BgbGui.custom(player, "BGB_GUI Custom") {
            label("Bitte ausfüllen.")
            dropdown("color", "Farbe", "Rot", "Grün", "Blau")
            input("name", "Name", placeholder = "Spielername")
            toggle("notify", "Benachrichtigungen")
            slider("volume", "Lautstärke", 0f, 10f, default = 5f)
            onSubmit { values ->
                player.sendRichMessage(
                    "<green>Custom: Farbe=${values.int("color")}, " +
                        "Name=${values.string("name")}, " +
                        "Toggle=${values.bool("notify")}, " +
                        "Slider=${values.float("volume")}",
                )
            }
            onClose {
                player.sendRichMessage("<gray>Custom-Form geschlossen.")
            }
        }
    }
}

package net.ruvios.bgbgui.commands

import com.mojang.brigadier.Command
import io.papermc.paper.command.brigadier.Commands
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents
import net.ruvios.bgbgui.BgbGuiPlugin
import net.ruvios.bgbgui.api.BgbGui
import net.ruvios.bgbgui.api.FormButton
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

object TestCmd {

    fun register(plugin: JavaPlugin) {
        plugin.lifecycleManager.registerEventHandler(LifecycleEvents.COMMANDS) { event ->
            val bgbgui = Commands.literal("bgbgui")
                .executes { ctx ->
                    ctx.source.sender.sendRichMessage("<yellow>Nutze /bgbgui <status|simple|modal|custom>")
                    Command.SINGLE_SUCCESS
                }
                .then(
                    Commands.literal("status")
                        .executes { ctx ->
                            val sender = ctx.source.sender
                            if (sender !is Player) {
                                sender.sendMessage("Nur als Spieler nutzbar.")
                                return@executes 0
                            }
                            openStatus(sender)
                            Command.SINGLE_SUCCESS
                        },
                )
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

    private fun openStatus(player: Player) {
        val plugin = BgbGuiPlugin.instance
        val floodgatePlugin = plugin.server.pluginManager.getPlugin("floodgate")
        val floodgateOk = try {
            org.geysermc.floodgate.api.FloodgateApi.getInstance()
            true
        } catch (_: Throwable) {
            false
        }
        val isFg = isFloodgatePlayer(player)
        val lines = listOf(
            "BGB_GUI v${plugin.pluginMeta.version} aktiv",
            "Server: ${plugin.server.version}",
            "Floodgate-Plugin: ${if (floodgatePlugin?.isEnabled == true) "geladen" else "FEHLT/disabled"}",
            "Floodgate-API: ${if (floodgateOk) "ok" else "nicht erreichbar"}",
            "Du bist Floodgate-Player: $isFg",
            "UUID: ${player.uniqueId}",
            "Name: ${player.name}",
        )
        lines.forEach { player.sendMessage("[BGB_GUI] $it") }
        lines.forEach { plugin.logger.info("[status] $it") }
    }

    private fun openSimple(player: Player) {
        player.sendMessage("[BGB_GUI] /bgbgui simple gestartet…")
        BgbGuiPlugin.instance.logger.info("simple von ${player.name} (${player.uniqueId})")
        if (!isFloodgatePlayer(player)) {
            player.sendMessage("[BGB_GUI] FEHLER: Du bist kein Floodgate-Player (kein Bedrock via Floodgate).")
            return
        }
        val ok = BgbGui.simple(
            player,
            "BGB_GUI Simple",
            "Wähle einen Button.",
            FormButton.url("Erster", "https://avatars.githubusercontent.com/u/52673035?s=200&v=4"),
            FormButton.path("Zweiter", "textures/i/glyph_world_template.png"),
            FormButton.of("Dritter"),
            onClose = {
                player.sendMessage("[BGB_GUI] Simple-Form geschlossen.")
            },
        ) { index, label ->
            player.sendMessage("[BGB_GUI] Simple: $label (Index $index)")
        }
        if (!ok) {
            player.sendMessage("[BGB_GUI] FEHLER: Form konnte nicht gesendet werden.")
            BgbGuiPlugin.instance.logger.warning("sendForm simple fehlgeschlagen für ${player.name}")
        } else {
            player.sendMessage("[BGB_GUI] Form an Floodgate übergeben.")
        }
    }

    private fun openModal(player: Player) {
        player.sendMessage("[BGB_GUI] /bgbgui modal gestartet…")
        if (!isFloodgatePlayer(player)) {
            player.sendMessage("[BGB_GUI] FEHLER: Du bist kein Floodgate-Player (kein Bedrock via Floodgate).")
            return
        }
        val ok = BgbGui.modal(
            player,
            "BGB_GUI Modal",
            "Zwei Buttons, ja oder nein?",
            "Ja",
            "Nein",
            onYes = {
                player.sendMessage("[BGB_GUI] Modal: Ja")
            },
            onNo = {
                player.sendMessage("[BGB_GUI] Modal: Nein")
            },
            onClose = {
                player.sendMessage("[BGB_GUI] Modal-Form geschlossen.")
            },
        )
        if (!ok) {
            player.sendMessage("[BGB_GUI] FEHLER: Form konnte nicht gesendet werden.")
        } else {
            player.sendMessage("[BGB_GUI] Form an Floodgate übergeben.")
        }
    }

    private fun openCustom(player: Player) {
        player.sendMessage("[BGB_GUI] /bgbgui custom gestartet…")
        if (!isFloodgatePlayer(player)) {
            player.sendMessage("[BGB_GUI] FEHLER: Du bist kein Floodgate-Player (kein Bedrock via Floodgate).")
            return
        }
        val ok = BgbGui.custom(player, "BGB_GUI Custom") {
            label("Bitte ausfüllen.")
            dropdown("color", "Farbe", "Rot", "Grün", "Blau")
            input("name", "Name", placeholder = "Spielername")
            toggle("notify", "Benachrichtigungen")
            slider("volume", "Lautstärke", 0f, 10f, default = 5f)
            onSubmit { values ->
                player.sendMessage(
                    "[BGB_GUI] Custom: Farbe=${values.int("color")}, " +
                        "Name=${values.string("name")}, " +
                        "Toggle=${values.bool("notify")}, " +
                        "Slider=${values.float("volume")}",
                )
            }
            onClose {
                player.sendMessage("[BGB_GUI] Custom-Form geschlossen.")
            }
        }
        if (!ok) {
            player.sendMessage("[BGB_GUI] FEHLER: Form konnte nicht gesendet werden.")
        } else {
            player.sendMessage("[BGB_GUI] Form an Floodgate übergeben.")
        }
    }

    private fun isFloodgatePlayer(player: Player): Boolean {
        return try {
            org.geysermc.floodgate.api.FloodgateApi.getInstance().isFloodgatePlayer(player.uniqueId)
        } catch (t: Throwable) {
            BgbGuiPlugin.instance.logger.warning("isFloodgatePlayer fehlgeschlagen: ${t.message}")
            false
        }
    }
}

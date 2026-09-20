// BGB_GUI Kotlin-Beispiel (Snippet zum Kopieren, kein Plugin-Entry)
// Braucht: BGB_GUI + Floodgate. Java/Bedrock prüft BGB_GUI nicht.
// Import: net.ruvios.bgbgui.api.BgbGui

import net.ruvios.bgbgui.api.BgbGui
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.geysermc.floodgate.api.FloodgateApi

private fun isBedrock(player: Player): Boolean {
    return try {
        FloodgateApi.getInstance().isFloodgatePlayer(player.uniqueId)
    } catch (_: Throwable) {
        false
    }
}

fun openTextExample(player: Player) {
    if (!isBedrock(player)) return
    BgbGui.simple(player, "TextExample", "Test Text", emptyList(), id = "textexample")
}

fun openShop(player: Player) {
    if (!isBedrock(player)) return
    BgbGui.simple(
        player,
        "Shop",
        "Was möchtest du?",
        "Diamant kaufen",
        "Diamant verkaufen",
        "Abbruch",
        id = "shop",
    ) { _, label ->
        when (label) {
            "Diamant kaufen" -> {
                player.inventory.addItem(ItemStack(Material.DIAMOND))
                player.sendMessage("§8[§6BGB_GUI§8]§r §aDu hast einen Diamanten bekommen.")
            }
            "Diamant verkaufen" -> {
                if (player.inventory.contains(Material.DIAMOND)) {
                    player.inventory.removeItem(ItemStack(Material.DIAMOND))
                    player.inventory.addItem(ItemStack(Material.GOLD_INGOT, 5))
                    player.sendMessage("§8[§6BGB_GUI§8]§r §e1 Diamant verkauft, 5 Goldbarren erhalten.")
                } else {
                    player.sendMessage("§8[§6BGB_GUI§8]§r §cDu hast keinen Diamanten.")
                }
            }
            else -> player.sendMessage("§8[§6BGB_GUI§8]§r §7Shop geschlossen.")
        }
    }
}

fun openHeal(player: Player) {
    if (!isBedrock(player)) return
    BgbGui.modal(
        player,
        "Heilen?",
        "HP und Hunger voll machen?",
        "Ja, heilen",
        "Nein",
        id = "heal",
        onYes = {
            player.health = player.maxHealth
            player.foodLevel = 20
            player.sendMessage("§8[§6BGB_GUI§8]§r §aDu wurdest geheilt.")
        },
        onNo = {
            player.sendMessage("§8[§6BGB_GUI§8]§r §7Nicht geheilt.")
        },
    )
}

fun openProfil(player: Player) {
    if (!isBedrock(player)) return
    BgbGui.custom(player, "Profil", id = "profil") {
        input("Name", "Name")
        input("Stadt", "Stadt")
        onSubmit { values ->
            val name = values.string("Name")
            val stadt = values.string("Stadt")
            if (name.isBlank() || stadt.isBlank()) {
                player.sendMessage("§8[§6BGB_GUI§8]§r §cBitte Namen und Stadt eintragen.")
                return@onSubmit
            }
            player.sendMessage("§8[§6BGB_GUI§8]§r §aProfil gesetzt: §e$name §7aus §e$stadt§a.")
        }
    }
}

fun openSettings(player: Player) {
    if (!isBedrock(player)) return
    BgbGui.custom(player, "Einstellungen", id = "settings") {
        label("Bitte ausfüllen.")
        dropdown("color", "Farbe", "Rot", "Grün")
        input("name", "Name", placeholder = "Spielername")
        toggle("notify", "Benachrichtigungen")
        slider("volume", "Lautstärke", 0f, 10f)
        onSubmit { v ->
            v.int("color")
            v.string("name")
            v.bool("notify")
            v.float("volume")
        }
        onClose { }
    }
}

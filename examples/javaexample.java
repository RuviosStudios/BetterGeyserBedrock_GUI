// BGB_GUI Java-Beispiel (Snippet zum Kopieren, kein Plugin-Entry)
// Braucht: BGB_GUI + Floodgate. Java/Bedrock prüft BGB_GUI nicht.
// Import: net.ruvios.bgbgui.api.BgbGui und FormField

import net.ruvios.bgbgui.api.BgbGui;
import net.ruvios.bgbgui.api.FormButton;
import net.ruvios.bgbgui.api.FormField;
import net.ruvios.bgbgui.api.FormValues;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.geysermc.floodgate.api.FloodgateApi;

import java.util.List;

public final class JavaExample {

    private JavaExample() {
    }

    private static boolean isBedrock(Player player) {
        try {
            return FloodgateApi.getInstance().isFloodgatePlayer(player.getUniqueId());
        } catch (Throwable ignored) {
            return false;
        }
    }

    public static void openTextExample(Player player) {
        if (!isBedrock(player)) {
            return;
        }
        BgbGui.simple(player, "TextExample", "Test Text", List.of(), null, null, "textexample");
    }

    public static void openShop(Player player) {
        if (!isBedrock(player)) {
            return;
        }
        BgbGui.simpleButtons(
            player,
            "Shop",
            "Was möchtest du?",
            List.of(
                FormButton.path("Diamant kaufen", "textures/items/diamond"),
                FormButton.path("Diamant verkaufen", "textures/items/gold_ingot"),
                FormButton.url("Hilfe", "https://avatars.githubusercontent.com/u/52673035?s=200&v=4"),
                FormButton.of("Abbruch")
            ),
            (index, label) -> {
                switch (label) {
                    case "Diamant kaufen" -> {
                        player.getInventory().addItem(new ItemStack(Material.DIAMOND));
                        player.sendMessage("§8[§6BGB_GUI§8]§r §aDu hast einen Diamanten bekommen.");
                    }
                    case "Diamant verkaufen" -> {
                        if (player.getInventory().contains(Material.DIAMOND)) {
                            player.getInventory().removeItem(new ItemStack(Material.DIAMOND));
                            player.getInventory().addItem(new ItemStack(Material.GOLD_INGOT, 5));
                            player.sendMessage("§8[§6BGB_GUI§8]§r §e1 Diamant verkauft, 5 Goldbarren erhalten.");
                        } else {
                            player.sendMessage("§8[§6BGB_GUI§8]§r §cDu hast keinen Diamanten.");
                        }
                    }
                    case "Hilfe" -> player.sendMessage("§8[§6BGB_GUI§8]§r §7Kaufen kostet nichts, verkaufen bringt Gold.");
                    default -> player.sendMessage("§8[§6BGB_GUI§8]§r §7Shop geschlossen.");
                }
            },
            null,
            "shop"
        );
    }

    public static void openHeal(Player player) {
        if (!isBedrock(player)) {
            return;
        }
        BgbGui.modal(
            player,
            "Heilen?",
            "HP und Hunger voll machen?",
            "Ja, heilen",
            "Nein",
            () -> {
                player.setHealth(player.getMaxHealth());
                player.setFoodLevel(20);
                player.sendMessage("§8[§6BGB_GUI§8]§r §aDu wurdest geheilt.");
            },
            () -> player.sendMessage("§8[§6BGB_GUI§8]§r §7Nicht geheilt."),
            null,
            "heal"
        );
    }

    public static void openProfil(Player player) {
        if (!isBedrock(player)) {
            return;
        }
        List<FormField> fields = List.of(
            FormField.input("Name", "Name"),
            FormField.input("Stadt", "Stadt")
        );
        BgbGui.custom(player, "Profil", fields, (FormValues values) -> {
            String name = values.getString("Name");
            String stadt = values.getString("Stadt");
            if (name.isBlank() || stadt.isBlank()) {
                player.sendMessage("§8[§6BGB_GUI§8]§r §cBitte Namen und Stadt eintragen.");
                return;
            }
            player.sendMessage("§8[§6BGB_GUI§8]§r §aProfil gesetzt: §e" + name + " §7aus §e" + stadt + "§a.");
        }, null, "profil");
    }

    public static void openSettings(Player player) {
        if (!isBedrock(player)) {
            return;
        }
        List<FormField> fields = List.of(
            FormField.label("Bitte ausfüllen."),
            FormField.dropdown("color", "Farbe", "Rot", "Grün"),
            FormField.input("name", "Name", "Spielername"),
            FormField.toggle("notify", "Benachrichtigungen"),
            FormField.slider("volume", "Lautstärke", 0f, 10f)
        );
        BgbGui.custom(player, "Einstellungen", fields, (FormValues v) -> {
            v.getInt("color");
            v.getString("name");
            v.getBoolean("notify");
            v.getFloat("volume");
        }, () -> {
        }, "settings");
    }
}

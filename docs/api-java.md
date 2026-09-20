# Java-API

Package: `net.ruvios.bgbgui.api`  
Einstieg: `BgbGui` (statische Methoden)

Alle Methoden geben `boolean` zurück: `true` = Form gesendet, `false` = Floodgate fehlt / Spieler offline / Send fehlgeschlagen. Keine Chat-Nachricht von BGB_GUI.

Optionale Parameter (`onClose`, `id`, …) dürfen weggelassen werden (`@JvmOverloads`). Ohne `id` gilt der **Titel** als Form-ID.

Vollständiges Beispiel: [examples/javaexample.java](../examples/javaexample.java)

## Simple Form

```java
import net.ruvios.bgbgui.api.BgbGui;
import java.util.List;

BgbGui.simple(player, "Shop", "Wähle.", List.of("Kaufen", "Abbruch"), (index, label) -> {
    // geklickter Button
}, () -> {
    // geschlossen
});

// mit Form-ID
BgbGui.simple(player, "Shop", "Wähle.", List.of("Kaufen", "Abbruch"),
    (index, label) -> { },
    null,
    "shop"
);
```

Callback: `BiConsumer<Integer, String>` (index, label), optional `Runnable` onClose.

Leere Liste `List.of()` = nur Titel + Text.

### Buttons mit Bild (`simpleButtons`)

In Java heißt die Variante mit `FormButton` bewusst **`simpleButtons`**, weil `List<String>` und `List<FormButton>` nach Type-Erasure dieselbe JVM-Signatur hätten. Die alte Methode `simple(...)` bleibt unverändert.

```java
import net.ruvios.bgbgui.api.FormButton;

BgbGui.simpleButtons(player, "Shop", "Wähle.", List.of(
    FormButton.path("Kaufen", "textures/items/diamond"),
    FormButton.url("Hilfe", "https://avatars.githubusercontent.com/u/52673035?s=200&v=4"),
    FormButton.of("Abbruch")
), (index, label) -> {
    // ...
}, null, "shop");
```

Factories: `FormButton.of`, `FormButton.url`, `FormButton.path`, `FormButton.auto` (siehe [Kotlin-API](api-kotlin.md#buttons-mit-bild-formbutton)).

`URL` = direkter Bild-Link (Redirects oft problematisch). `PATH` = Resource-Pack / Vanilla, z. B. `textures/items/diamond`. Nur Simple-Forms.

## Modal Form

```java
BgbGui.modal(player, "Sicher?", "Wirklich?", "Ja", "Nein",
    () -> { /* Ja */ },
    () -> { /* Nein */ },
    () -> { /* geschlossen */ }
);

BgbGui.modal(player, "Sicher?", "Wirklich?", "Ja", "Nein",
    () -> { },
    () -> { },
    null,
    "ask"
);
```

Callbacks: drei optionale `Runnable` (yes, no, onClose).

## Custom Form

```java
import net.ruvios.bgbgui.api.FormField;
import net.ruvios.bgbgui.api.FormValues;
import java.util.List;

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
}, () -> {}, "settings");
```

`Consumer<FormValues>` + optional `Runnable` onClose + optional `id`.

## Bedrock-Check (eure Seite)

```java
import org.geysermc.floodgate.api.FloodgateApi;

private static boolean isBedrock(Player player) {
    try {
        return FloodgateApi.getInstance().isFloodgatePlayer(player.getUniqueId());
    } catch (Throwable ignored) {
        return false;
    }
}

if (!isBedrock(player)) return;
BgbGui.simple(...);
```

## Weiter

- [Installation](installation.md)
- [Kotlin-API](api-kotlin.md)
- [Skript](skript.md)

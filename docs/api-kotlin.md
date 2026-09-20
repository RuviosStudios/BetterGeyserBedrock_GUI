# Kotlin-API

Package: `net.ruvios.bgbgui.api`  
Einstieg: `BgbGui`

Alle `BgbGui.*`-Methoden geben `Boolean` zurück: `true` = Form gesendet, `false` = Floodgate fehlt / Spieler offline / Send fehlgeschlagen. Keine Chat-Nachricht von BGB_GUI.

Form-IDs (`id = "..."`) sind optional; ohne Angabe gilt der **Titel** als ID. IDs braucht ihr vor allem für Skript-Events — in Kotlin reichen oft die Callbacks.

Vollständiges Beispiel: [examples/kotlinexample.kt](../examples/kotlinexample.kt)

## Simple Form

Text-Buttons:

```kotlin
import net.ruvios.bgbgui.api.BgbGui

BgbGui.simple(player, "Shop", "Wähle.", "Kaufen", "Abbruch") { index, label ->
    // geklickter Button
}

BgbGui.simple(
    player, "Shop", "Wähle.",
    listOf("Kaufen", "Abbruch"),
    id = "shop",
    onClose = { /* geschlossen / abgebrochen */ },
) { index, label -> }
```

Leere Button-Liste = nur Titel + Text (schließen über X). Untypisiertes `emptyList()` als Buttons: `emptyList<String>()` schreiben.

### Buttons mit Bild (`FormButton`)

Nur bei Simple-Forms. `URL` = Link (ohne Redirect, direktes PNG/JPEG). `PATH` = Resource-Pack / Vanilla-Pfad, z. B. `textures/items/diamond`.

```kotlin
import net.ruvios.bgbgui.api.FormButton

BgbGui.simple(
    player, "Shop", "Wähle.",
    FormButton.path("Kaufen", "textures/items/diamond"),
    FormButton.url("Hilfe", "https://avatars.githubusercontent.com/u/52673035?s=200&v=4"),
    FormButton.of("Abbruch"),
    id = "shop",
) { index, label -> }
```

Factories:

| Methode | Bedeutung |
|---|---|
| `FormButton.of(label)` | ohne Bild |
| `FormButton.url(label, url)` | Netz-Bild |
| `FormButton.path(label, path)` | Resource-Pack |
| `FormButton.auto(label, image?)` | `http(s)://` → URL, sonst PATH; `null`/leer → ohne Bild |

## Modal Form

Immer zwei Buttons.

```kotlin
BgbGui.modal(
    player, "Sicher?", "Wirklich?",
    "Ja", "Nein",
    id = "ask",
    onYes = { },
    onNo = { },
    onClose = { },
)
```

`onButton1` / `onButton2` sind Aliase für `onYes` / `onNo`.

## Custom Form

DSL oder `List<FormField>`:

```kotlin
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
```

`FormValues`: `string` / `int` / `bool` / `float` (Aliases: `getString`, `getInt`, `getBoolean`, `getFloat`). Die IDs sind die, die ihr beim Feld übergebt (`"name"`, `"color"`, …).

## Bedrock-Check (eure Seite)

```kotlin
import org.geysermc.floodgate.api.FloodgateApi

fun isBedrock(player: Player): Boolean =
    try {
        FloodgateApi.getInstance().isFloodgatePlayer(player.uniqueId)
    } catch (_: Throwable) {
        false
    }

if (!isBedrock(player)) return
BgbGui.simple(...)
```

## Events (optional)

Bukkit-Events: `BgbSimpleFormEvent`, `BgbModalFormEvent`, `BgbCustomFormEvent` (mit `id`, `title`, …). In Kotlin reichen meist die Lambdas; Events sind vor allem für Skript gedacht.

## Weiter

- [Installation](installation.md)
- [Java-API](api-java.md)
- [Skript](skript.md)

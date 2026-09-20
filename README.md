# BGB_GUI

**RuviosStudios** · [ruvios.net](https://ruvios.net) · Ersteller: Mangur7090

Paper/Folia-Plugin: andere Plugins und Skripte öffnen Geyser/Floodgate-Forms (Simple, Modal, Custom) über eine gemeinsame API. **Java/Bedrock-Check bleibt im Verbraucher**, nicht in BGB_GUI. Fehlt Floodgate oder schlägt der Send fehl, gibt `BgbGui` still `false` zurück — keine Chat-Nachricht.

JAR: `build/libs/bgb_gui-0.1.0.jar`

## Examples

Vollständige Copy-Paste-Beispiele (Shop, Heilen, Profil, Floodgate-Check):

| Sprache | Datei |
|---|---|
| Skript | [`examples/skriptexample.sk`](examples/skriptexample.sk) |
| Kotlin | [`examples/kotlinexample.kt`](examples/kotlinexample.kt) |
| Java | [`examples/javaexample.java`](examples/javaexample.java) |

Skript: Datei nach `plugins/Skript/scripts/` (Skript + BGB_GUI + Floodgate; für `import:` zusätzlich [skript-reflect](https://github.com/SkriptLang/skript-reflect)). Kotlin/Java: Snippets in euer Plugin kopieren, nicht als eigene Main-Klasse kompilieren.

## Einbinden

**Kotlin / Java** — Plugin hängt von BGB_GUI ab und kompiliert gegen die JAR.

`plugin.yml`:

```yaml
softdepend:
  - BGB_GUI
```

`paper-plugin.yml` (Paper-Plugin):

```yaml
dependencies:
  server:
    BGB_GUI:
      load: BEFORE
      required: false
      join-classpath: true
```

`build.gradle.kts`:

```kotlin
dependencies {
    compileOnly(files("../GeyserBedrockGUI/build/libs/bgb_gui-0.1.0.jar"))
}
```

Import: `net.ruvios.bgbgui.api.BgbGui` (plus `FormField` in Java).

**Skript** — BGB_GUI und Skript auf dem Server, dann eine `.sk`-Datei. Kein compileOnly.

## Kotlin

```kotlin
import net.ruvios.bgbgui.api.BgbGui

BgbGui.simple(player, "Shop", "Wähle.", "Kaufen", "Abbruch") { index, label ->
    // index, label
}

BgbGui.simple(player, "Shop", "Wähle.", listOf("Kaufen", "Abbruch"), onClose = {
    // geschlossen
}) { index, label -> }

BgbGui.modal(player, "Sicher?", "Wirklich?", "Ja", "Nein",
    onYes = { },
    onNo = { },
    onClose = { },
)

BgbGui.custom(player, "Einstellungen") {
    label("Bitte ausfüllen.")
    dropdown("color", "Farbe", "Rot", "Grün")
    input("name", "Name", placeholder = "Spielername")
    toggle("notify", "Benachrichtigungen")
    slider("volume", "Lautstärke", 0f, 10f)
    onSubmit { v ->
        v.int("color"); v.string("name"); v.bool("notify"); v.float("volume")
    }
    onClose { }
}
```

`onButton1` / `onButton2` sind Aliase für `onYes` / `onNo`. `FormValues`: `string`/`int`/`bool`/`float` oder `getString`/`getInt`/`getBoolean`/`getFloat`.

## Java

```java
import net.ruvios.bgbgui.api.BgbGui;
import net.ruvios.bgbgui.api.FormField;
import net.ruvios.bgbgui.api.FormValues;

import java.util.List;

BgbGui.simple(player, "Shop", "Wähle.", List.of("Kaufen", "Abbruch"), (index, label) -> {
    // index, label
}, () -> {
    // geschlossen
});

BgbGui.modal(player, "Sicher?", "Wirklich?", "Ja", "Nein",
    () -> { /* Ja */ },
    () -> { /* Nein */ },
    () -> { /* geschlossen */ }
);

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
}, () -> {});
```

Callbacks: `BiConsumer<Integer, String>` + `Runnable` (simple), `Runnable` (modal), `Consumer<FormValues>` + `Runnable` (custom). OnClose-Parameter dürfen weggelassen werden.

## Skript

Simple und Modal sind vollständig. Custom ist absichtlich schmal: nur Text-Inputs, IDs = Label-Text.
Jedes Formular sollte `with id "..."` nutzen, sonst kollidieren gleiche Button-Labels. Ohne ID gilt der Titel als ID. Zusätzlich: `form id`, `form title`.

Optional: `with buttons …` (Simple, sonst keine Buttons), `with yes … and no …` (Modal, sonst `Yes`/`No`), `with inputs …` (Custom, sonst leer).

```skript
command /info:
    trigger:
        open simple form with id "info" titled "Info" with description "Nur Text, ohne Buttons" to player

command /shop:
    trigger:
        open simple form with id "shop" titled "Shop" with description "Wähle" with buttons "Kaufen" and "Abbruch" to player

on bgb simple form with id "shop":
    send "Button %form button% (index %form index%)" to player

command /ask:
    trigger:
        open modal form with id "ask" titled "Sicher?" with description "Wirklich?" with yes "Ja" and no "Nein" to player

on bgb modal form with id "ask":
    if form yes is true:
        send "Ja: %form button%" to player
    else:
        send "Nein: %form button%" to player

command /settings:
    trigger:
        open custom form with id "settings" titled "Einstellungen" with inputs "Name" and "Stadt" to player

on bgb custom form with id "settings":
    set {_name} to form value of "Name"
    send "Name: %{_name}%" to player
```

Expressions: `form button`, `form index` (simple), `form yes` (modal), `form value of "Label"` (custom).

Bedrock-Prüfung (Floodgate/Geyser) vor dem Öffnen selbst machen — BGB_GUI prüft das nicht.

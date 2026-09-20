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
import net.ruvios.bgbgui.api.FormButton

BgbGui.simple(player, "Shop", "Wähle.", "Kaufen", "Abbruch") { index, label ->
    // index, label
}

BgbGui.simple(player, "Shop", "Wähle.", listOf("Kaufen", "Abbruch"), onClose = {
    // geschlossen
}) { index, label -> }

// Buttons mit Bild
BgbGui.simple(
    player, "Shop", "Wähle.",
    FormButton.path("Kaufen", "textures/items/diamond"),
    FormButton.url("Hilfe", "https://avatars.githubusercontent.com/u/52673035?s=200&v=4"),
    FormButton.of("Abbruch"),
) { index, label -> }

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

### Button-Bilder

`FormButton` gibt es als `of(label)` (ohne Bild), `url(label, url)`, `path(label, pfad)` und `auto(label, bild)` (erkennt `http://` / `https://` selbst). `URL` lädt ein Bild aus dem Netz, `PATH` verweist auf eine Textur im Resource-Pack bzw. im Spiel (z. B. `textures/items/diamond`). Button-Bilder gibt es nur bei Simple-Forms — Modal und Custom haben keine.

Statt `FormButton.url(...)` geht auch direkt `FormButton("Kaufen", "textures/items/diamond", FormImageType.PATH)`. Die alten String-Buttons bleiben unverändert nutzbar.

## Java

```java
import net.ruvios.bgbgui.api.BgbGui;
import net.ruvios.bgbgui.api.FormButton;
import net.ruvios.bgbgui.api.FormField;
import net.ruvios.bgbgui.api.FormValues;

import java.util.List;

BgbGui.simple(player, "Shop", "Wähle.", List.of("Kaufen", "Abbruch"), (index, label) -> {
    // index, label
}, () -> {
    // geschlossen
});

// Buttons mit Bild: simpleButtons statt simple
BgbGui.simpleButtons(player, "Shop", "Wähle.", List.of(
    FormButton.path("Kaufen", "textures/items/diamond"),
    FormButton.url("Hilfe", "https://avatars.githubusercontent.com/u/52673035?s=200&v=4"),
    FormButton.of("Abbruch")
), (index, label) -> {
    // index, label
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

`simpleButtons` heißt in Java bewusst anders als `simple`: beide nehmen eine `List`, und nach der Type-Erasure der JVM wären `List<String>` und `List<FormButton>` dieselbe Signatur.

## Skript

Simple und Modal sind vollständig. Custom ist absichtlich schmal: nur Text-Inputs, IDs = Label-Text.
Jedes Formular sollte eine eigene ID nutzen (`id: "..."` bzw. `with id "..."`), sonst kollidieren gleiche Button-Labels. Ohne ID gilt der Titel als ID. Zusätzlich: `form id`, `form title`.

### Multiline (empfohlen)

Einrückungsblöcke über Sections. Pro Zeile ein Eintrag (`schlüssel: wert`). Mehrere `button:` / `input:`-Zeilen sind erlaubt.

```skript
open simple form to player:
    id: "shop"
    title: "Shop"
    description: "Was möchtest du?"
    button: "Diamant kaufen" with image "textures/items/diamond"
    button: "Hilfe" with image "https://avatars.githubusercontent.com/u/52673035?s=200&v=4"
    button: "Abbruch"

open modal form to player:
    id: "heal"
    title: "Heilen?"
    description: "HP und Hunger voll machen?"
    yes: "Ja, heilen"
    no: "Nein"

open custom form to player:
    id: "profil"
    title: "Profil"
    input: "Name"
    input: "Stadt"
```

Simple-Einträge: `id`, `title` (Pflicht), `description`/`desc`/`content`, `button` (optional `with image "..."`).
Modal-Einträge: `id`, `title` (Pflicht), `description`, `yes`/`ja`, `no`/`nein` (sonst `Yes`/`No`).
Custom-Einträge: `id`, `title` (Pflicht), `input`.

Bilder: `http://` oder `https://` = URL, sonst Resource-Pack-Pfad. Modal/Custom haben keine Button-Bilder.

### Einzeiler (weiterhin gültig)

Optional: `with buttons …`, `with images …` (index-basiert zu den Buttons), `with yes … and no …`, `with inputs …`.

```skript
command /info:
    trigger:
        open simple form with id "info" titled "Info" with description "Nur Text, ohne Buttons" to player

command /shop:
    trigger:
        open simple form with id "shop" titled "Shop" with description "Wähle" with buttons "Kaufen" and "Abbruch" to player

command /shopmitbildern:
    trigger:
        open simple form with id "shop" titled "Shop" with description "Wähle" with buttons "Kaufen", "Hilfe" and "Abbruch" with images "textures/items/diamond" and "https://avatars.githubusercontent.com/u/52673035?s=200&v=4" to player

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

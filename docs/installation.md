# Installation

## Voraussetzungen

| Software | Von | Bis / getestet | Pflicht |
|---|---|---|---|
| Java | 25 | 25 | ja (Server) |
| Paper oder Folia | 1.21.11 | 26.3 | ja |
| Floodgate | 2.2.x | 2.2.4-SNAPSHOT | ja (Forms) |
| Geyser-Spigot | passend zu Floodgate | aktuell | empfohlen |
| BGB_GUI | 0.1.2 | 0.1.2 | ja |
| Skript | 2.12.x | 2.12.2 | nur für `.sk` |
| skript-reflect | zur Skript-Version | aktuell | nur für Floodgate-`import:` in Skript |

BGB_GUI ist **folia-supported**. Ohne Floodgate startet das Plugin, Forms schlagen aber still fehl (`false`).

## Server einrichten

1. Paper/Folia **1.21.11–26.3** mit **Java 25** (entwickelt/getestet gegen Paper 26.2).
2. [Geyser](https://geysermc.org/) und [Floodgate](https://geysermc.org/download) in `plugins/` legen (Key von Floodgate mit Geyser teilen).
3. [BGB_GUI-Release](https://github.com/RuviosStudios/BetterGeyserBedrock_GUI/releases) herunterladen → `plugins/bgb_gui-0.1.2.jar`.
4. Optional: Skript 2.12.x (+ skript-reflect), dann `.sk` nach `plugins/Skript/scripts/`.
5. Server starten. In der Konsole sollte `BGB_GUI` laden.

Plugin-Name in `plugin.yml` / Dependencies: **`BGB_GUI`** (nicht der GitHub-Repo-Name).

## Eigenes Plugin einbinden

### `plugin.yml`

```yaml
softdepend:
  - BGB_GUI
```

### `paper-plugin.yml`

```yaml
dependencies:
  server:
    BGB_GUI:
      load: BEFORE
      required: false
      join-classpath: true
```

`required: true`, wenn euer Plugin ohne BGB_GUI nicht laufen soll.

### Gradle (`build.gradle.kts`)

JAR vom Release oder lokal gebaut:

```kotlin
dependencies {
    compileOnly(files("libs/bgb_gui-0.1.2.jar"))
    // oder relativ zum BGB_GUI-Repo:
    // compileOnly(files("../GeyserBedrockGUI/build/libs/bgb_gui-0.1.2.jar"))
}
```

Import: `net.ruvios.bgbgui.api.BgbGui`  
Zusätzlich: `FormButton`, `FormField`, `FormValues` je nach Bedarf.

Floodgate nur, wenn ihr selbst Bedrock prüft:

```kotlin
compileOnly("org.geysermc.floodgate:api:2.2.4-SNAPSHOT")
```

Repository: `https://repo.opencollab.dev/maven-snapshots/` bzw. `main/`.

### Skript

Kein compileOnly. Auf dem Server: BGB_GUI + Skript (+ optional skript-reflect). Script-Datei nach `plugins/Skript/scripts/`, dann `/sk reload <datei>` oder Server-Neustart.

## Bedrock-Check

BGB_GUI prüft **nicht**, ob der Spieler Bedrock ist. Das macht euer Plugin / Skript (Floodgate-API). Forms an Java-Spieler schicken bringt nichts Sinnvolles.

Siehe Examples: [skriptexample.sk](../examples/skriptexample.sk), [kotlinexample.kt](../examples/kotlinexample.kt), [javaexample.java](../examples/javaexample.java).

## Update

1. Neue JAR aus dem Release laden.
2. Alte `bgb_gui-*.jar` in `plugins/` ersetzen.
3. Server neu starten (Hot-Reload von Paper-Plugins ist unzuverlässig).

## Weiter

- [Kotlin-API](api-kotlin.md)
- [Java-API](api-java.md)
- [Skript](skript.md)

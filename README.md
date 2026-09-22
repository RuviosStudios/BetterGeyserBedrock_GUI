# BGB_GUI

[![Release](https://img.shields.io/github/v/release/RuviosStudios/BetterGeyserBedrock_GUI?include_prereleases&label=release)](https://github.com/RuviosStudios/BetterGeyserBedrock_GUI/releases)
[![Java](https://img.shields.io/badge/Java-25-orange?logo=openjdk)](https://openjdk.org/)
[![Paper](https://img.shields.io/badge/Paper-1.21.11%20–%2026.3-00A8E8?logo=minecraft)](https://papermc.io/)
[![Folia](https://img.shields.io/badge/Folia-supported-brightgreen)](https://papermc.io/software/folia)
[![Floodgate](https://img.shields.io/badge/Floodgate-2.2.x-purple)](https://geysermc.org/)
[![Skript](https://img.shields.io/badge/Skript-2.12.x-yellow)](https://skriptlang.org/)

**RuviosStudios** · [ruvios.net](https://ruvios.net) · Ersteller: [Mangur7090](https://github.com/Mangur7090)

Paper/Folia-API für Geyser/Floodgate-Forms (Simple, Modal, Custom). Andere Plugins und Skripte übergeben nur Daten und Callbacks. **Java/Bedrock-Check bleibt im Verbraucher** — BGB_GUI prüft das nicht. Fehlt Floodgate oder schlägt der Send fehl, gibt `BgbGui` still `false` zurück.

## Download

[![Download JAR](https://img.shields.io/badge/Download-JAR-success?style=for-the-badge&logo=github)](https://github.com/RuviosStudios/BetterGeyserBedrock_GUI/releases)
[![Docs](https://img.shields.io/badge/Docs-Installation-blue?style=for-the-badge)](docs/installation.md)
[![Examples](https://img.shields.io/badge/Examples-Kotlin%20·%20Java%20·%20Skript-orange?style=for-the-badge)](examples/)

## Kompatibilität

| Abhängigkeit | Von | Bis / getestet |
|---|---|---|
| Java | 25 | 25 |
| Paper / Folia | 1.21.11 | 26.3 |
| Bukkit `api-version` | 1.21.11 | 1.21.11 |
| Floodgate | 2.2.x | 2.2.4-SNAPSHOT |
| Geyser-Spigot | passend zu Floodgate | aktuell |
| Skript (optional) | 2.12.x | 2.12.2 |
| skript-reflect (optional) | zur Skript-Version | für Floodgate-`import:` |

Plugin-Name auf dem Server: **`BGB_GUI`**. JAR: `bgb_gui-0.1.2.jar`.

## Dokumentation

| Thema | Link |
|---|---|
| Installation & Einbinden | [docs/installation.md](docs/installation.md) |
| Kotlin-API | [docs/api-kotlin.md](docs/api-kotlin.md) |
| Java-API | [docs/api-java.md](docs/api-java.md) |
| Skript | [docs/skript.md](docs/skript.md) |

## Examples

| Sprache | Datei |
|---|---|
| Skript | [examples/skriptexample.sk](examples/skriptexample.sk) |
| Kotlin | [examples/kotlinexample.kt](examples/kotlinexample.kt) |
| Java | [examples/javaexample.java](examples/javaexample.java) |

## Kurzüberblick

```kotlin
BgbGui.simple(player, "Shop", "Wähle.", "Kaufen", "Abbruch") { index, label -> }
```

```java
BgbGui.simple(player, "Shop", "Wähle.", List.of("Kaufen", "Abbruch"), (i, label) -> {});
```

```skript
open simple form to player:
    id: "shop"
    title: "Shop"
    description: "Wähle"
    button: "Kaufen"
```

Mehr Details in den Docs oben.

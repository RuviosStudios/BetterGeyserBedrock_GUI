# Skript

Voraussetzungen: **BGB_GUI** + **Skript 2.12.x** auf dem Server. Für Floodgate-`import:` zusätzlich [skript-reflect](https://github.com/SkriptLang/skript-reflect).

BGB_GUI prüft nicht Java/Bedrock — das macht ihr selbst. Jedes Formular sollte eine eigene **ID** haben, sonst kollidieren Events bei gleichen Button-Labels.

Vollständiges Beispiel: [examples/skriptexample.sk](../examples/skriptexample.sk)

## Bedrock-Check

```skript
import:
    org.geysermc.floodgate.api.FloodgateApi

function isBedrock(p: player) :: boolean:
    return FloodgateApi.getInstance().isFloodgatePlayer(uuid of {_p})
```

Braucht skript-reflect. Ohne Addon: Prefix-Check (nur zuverlässig, wenn Floodgate-Linking aus ist und der Username-Prefix gesetzt ist).

## Multiline (empfohlen)

Einrückungsblöcke. Pro Zeile `schlüssel: wert`. Mehrere `button:` / `input:`-Zeilen sind erlaubt.

### Simple

```skript
open simple form to player:
    id: "shop"
    title: "Shop"
    description: "Was möchtest du?"
    button: "Diamant kaufen" with image "textures/items/diamond"
    button: "Hilfe" with image "https://avatars.githubusercontent.com/u/52673035?s=200&v=4"
    button: "Abbruch"
```

| Eintrag | Pflicht | Hinweis |
|---|---|---|
| `title` / `titel` | ja | Fenstertitel |
| `id` | nein | sonst = Titel |
| `description` / `desc` / `content` / `beschreibung` | nein | Text über den Buttons |
| `button` | nein | optional `with image "..."` |

Bilder: `http://` / `https://` → URL, sonst Resource-Pack-Pfad. Ohne `button:` nur Titel + Text.

### Modal

```skript
open modal form to player:
    id: "heal"
    title: "Heilen?"
    description: "HP und Hunger voll machen?"
    yes: "Ja, heilen"
    no: "Nein"
```

| Eintrag | Pflicht | Hinweis |
|---|---|---|
| `title` | ja | |
| `id` | nein | |
| `description` | nein | |
| `yes` / `ja` / `button1` | nein | Default `Yes` |
| `no` / `nein` / `button2` | nein | Default `No` |

Keine Button-Bilder (Cumulus/Modal).

### Custom

```skript
open custom form to player:
    id: "profil"
    title: "Profil"
    input: "Name"
    input: "Stadt"
```

Nur Text-Inputs. Feld-ID = Label-Text. Keine Dropdowns/Slider in Skript (dafür Kotlin/Java).

## Einzeiler

Weiterhin gültig:

```skript
open simple form with id "info" titled "Info" with description "Nur Text" to player

open simple form with id "shop" titled "Shop" with description "Wähle" with buttons "Kaufen" and "Abbruch" to player

open simple form with id "shop" titled "Shop" with description "Wähle" with buttons "Kaufen", "Hilfe" and "Abbruch" with images "textures/items/diamond" and "https://avatars.githubusercontent.com/u/52673035?s=200&v=4" to player

open modal form with id "ask" titled "Sicher?" with description "Wirklich?" with yes "Ja" and no "Nein" to player

open custom form with id "settings" titled "Einstellungen" with inputs "Name" and "Stadt" to player
```

`with images …` ist index-basiert zu `with buttons …` (weniger Bilder = Rest ohne Icon).

## Events & Expressions

```skript
on bgb simple form with id "shop":
    send "Button %form button% (index %form index%)" to player

on bgb modal form with id "ask":
    if form yes is true:
        send "Ja" to player

on bgb custom form with id "profil":
    set {_name} to form value of "Name"
```

| Expression | Form |
|---|---|
| `form button` | Simple, Modal |
| `form index` | Simple |
| `form yes` | Modal |
| `form value of "Label"` | Custom |
| `form id` | alle |
| `form title` | alle |

Ohne `with id "..."` am Event: alle Forms dieses Typs (meist unerwünscht).

## Weiter

- [Installation](installation.md)
- [Kotlin-API](api-kotlin.md)
- [Java-API](api-java.md)

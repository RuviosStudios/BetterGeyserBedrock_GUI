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
| `button` | nein | optional `with image "..."`; mehrere Zeilen |
| `buttons` | nein | Liste, z. B. `{_buttons::*}` (nicht zusammen mit `button:`) |
| `images` / `image` | nein | parallel zu `buttons:` / `button:` (Index) |

Bilder: `http://` / `https://` → URL, sonst Resource-Pack-Pfad. Ohne `button:`/`buttons:` nur Titel + Text.

Dynamische Menüs (Navigator):

```skript
open simple form to {_player}:
    id: "navigator"
    title: "Navigator"
    description: "Wähle eine Option"
    buttons: {_buttons::*}
    images: {_images::*}
```

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
    id: "settings"
    title: "Einstellungen"
    label: "Bitte ausfüllen"
    input: "name" labeled "Name" with placeholder "Spieler"
    dropdown: "color" labeled "Farbe" with options "Rot", "Grün" and "Blau"
    toggle: "notify" labeled "Benachrichtigungen" with default true
    slider: "volume" labeled "Lautstärke" from 0 to 10 with default 5
```

Kurzform für Inputs: `input: "Name"` → Feld-ID = Label = `"Name"`.

| Eintrag | Pflicht | Hinweis |
|---|---|---|
| `title` / `titel` | ja | Fenstertitel |
| `id` | nein | sonst = Titel |
| `label` / `text` / `content` / `beschreibung` | nein | nur Anzeige, kein Wert |
| `input` / `inputs` | nein | `"id"` oder `"id" labeled "Label" [with placeholder "…"] [with default "…"]` |
| `dropdown` / `dropdowns` | nein | `"id" [labeled "Label"] with options "A", "B" [with default 0]` (Default = Index) |
| `toggle` / `toggles` | nein | `"id" [labeled "Label"] [with default true/false]` |
| `slider` / `sliders` | nein | `"id" [labeled "Label"] from 0 to 10 [with step 1] [with default 5]` |

`form value of "id"` liefert immer einen String: Input-Text, Dropdown-**Index** (`"0"`, `"1"`, …), Toggle (`"true"`/`"false"`), Slider (Zahl als Text, z. B. `"5"`).

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

on bgb custom form with id "settings":
    set {_name} to form value of "name"
    set {_color} to form value of "color"   # Index als Text, z. B. "0"
    set {_notify} to form value of "notify" # "true" / "false"
    set {_volume} to form value of "volume" # z. B. "5"
```

| Expression | Form |
|---|---|
| `form button` | Simple, Modal |
| `form index` | Simple |
| `form yes` | Modal |
| `form value of "id"` | Custom (String; Dropdown = Index) |
| `form id` | alle |
| `form title` | alle |

Ohne `with id "..."` am Event: alle Forms dieses Typs (meist unerwünscht).

## Weiter

- [Installation](installation.md)
- [Kotlin-API](api-kotlin.md)
- [Java-API](api-java.md)

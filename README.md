<p align="center">
  <img src="https://raw.githubusercontent.com/JJdevjoHan/Blood-Of-The-Rift_ProjectOOP/main/Resources/images/backgroundpic/intro.png" alt="Blood Of The Rift" width="760" />
</p>

<h1 align="center">Blood Of The Rift</h1>

<p align="center">
  A story-driven Java Swing RPG about cursed heroes, fractured realms, and the truth buried inside a world-ending rift.
</p>

<p align="center">
  <img alt="Java" src="https://img.shields.io/badge/Java-17%2B-ED8B00?logo=openjdk&logoColor=white" />
  <img alt="UI" src="https://img.shields.io/badge/UI-Java%20Swing-5382A1" />
  <img alt="Architecture" src="https://img.shields.io/badge/architecture-object--oriented-7C3AED" />
  <img alt="Status" src="https://img.shields.io/badge/status-playable%20prototype-16A34A" />
</p>

## The premise

One thousand years ago, a champion was chosen to save the Kingdom of Sethra from the wrath of the gods. His own people betrayed him, sacrificed him, and blamed him when reality began to tear apart.

Eight hundred years later, new heroes cross the divided worlds believing the fallen champion is the cause of the chaos. Their journey leads through hostile realms, forgotten memories, and a final confrontation where the official history begins to collapse.

> **The world was saved by a lie.**

## What the game offers

### A complete RPG loop

- Open with an optional cinematic intro and an unfolding narrative sequence
- Name your hero and choose a class before leaving the safehouse
- Search the chest for equipment and permanent adventure buffs
- Travel through five distinct worlds connected by story-driven portals
- Explore by choosing a direction, trigger encounters, and read atmospheric scene text
- Fight enemies through turn-based skill buttons with HP and mana management
- Defeat each realm’s guardian to unlock the next chapter
- Survive the final two-boss sequence and uncover the truth behind the Rift

### Five realms, one escalating journey

| Chapter | Realm | Regular enemies | Guardian / finale |
| --- | --- | --- | --- |
| I | Grassy Plains | Slime, Wild Bull, Dire Wolf | Minotaur |
| II | Desert World | Spider, Snake, Mummy | Giant Worm |
| III | Snowy Island | Snow Golem, Witch Gnome, Yeti | Giant Frost Wolves |
| IV | Lava World | Lava Imp, Magma Beast, Skeleton Head | Golem |
| V | Final World | Story encounter | General Kyros, then the Demon Lord |

The first four realms randomly select regular enemies. After three victories in a realm, its guardian appears. The final realm replaces regular exploration with a cinematic boss sequence.

## Choose your class

Each class changes your starting survivability, resource pool, and three-button combat kit.

| Class | Starting HP | Starting Mana | Skill identity |
| --- | ---: | ---: | --- |
| Warrior | 180 | 80 | Reliable weapon damage and heavy strikes |
| Mage | 120 | 150 | High mana capacity and ranged spell damage |
| Paladin | 220 | 120 | Durable frontline play with defense and healing tools |

### Skill kits

| Class | Skill 1 | Skill 2 | Skill 3 |
| --- | --- | --- | --- |
| Warrior | Stone Slash | Flame Strike | Earthquake Blade |
| Mage | Frost Bolt | Rune Burst | Lightstorm |
| Paladin | Shield Bash | Radiant Guard | Holy Renewal |

Basic skills restore mana, while stronger skills consume mana. Attacks can critically hit, and the combat log reports damage, critical hits, healing, and resource feedback after every action.

## Boss encounters

The game builds tension through escalating guardian battles:

- **Minotaur** — the first realm’s Earth Shatter guardian
- **Giant Worm** — a desert guardian capable of sand-storm damage
- **Giant Frost Wolves** — the frozen realm’s high-health guardian
- **Golem** — the lava realm’s ultimate guardian
- **General Kyros** — the final realm’s betrayed champion
- **Demon Lord** — the last battle, with Hellfire Blast and critical-hit pressure

Bosses receive dedicated versus screens, portrait art, stronger attacks, special skills, timed story beats, victory transitions, and game-over handling.

## A visual identity built in Swing

Blood Of The Rift is intentionally more than a collection of default Swing forms. The interface uses a consistent neon-retro language across every screen:

- Dark cinematic backdrops for menus, story panels, safehouse scenes, and realms
- Neon cyan, red, yellow, and purple accents to distinguish player, enemy, and Rift states
- Custom slanted `RiftButton` controls with hover feedback
- Custom polygonal `RiftBar` meters for HP and mana
- Borderless modal `RiftDialog` windows for choices, rewards, and transitions
- Large versus compositions that place the hero and enemy portraits on opposite sides of the screen
- Timed dialogue, typewriter-style loading text, story pacing, and transition delays

## Controls

The current build is a click-driven desktop experience:

| Screen | Action |
| --- | --- |
| Intro | Choose whether to skip the opening story |
| Character setup | Enter a name of at least three characters and choose Warrior, Mage, or Paladin |
| Safehouse | Walk to the chest, open it, then choose whether to begin the journey |
| Exploration | Click `NORTH`, `SOUTH`, `EAST`, or `WEST` to travel |
| Battle | Click one of the three class skills |
| Dialogues | Choose the displayed confirmation or reward option |

## Tech stack

| Area | Technology |
| --- | --- |
| Language | Java |
| Desktop UI | Java Swing and AWT |
| Layout and navigation | `CardLayout`, `BorderLayout`, `GridBagLayout`, `GridLayout` |
| Architecture | Object-oriented inheritance and polymorphism |
| Rendering | Java 2D, `Graphics2D`, custom painting, `ImageIcon` |
| State and flow | Swing panels, timers, listeners, and shared player objects |
| Assets | PNG backgrounds, character portraits, mob art, boss art, and item sprites |
| IDE metadata | Eclipse project configuration |

## Getting started

### Requirements

- JDK 17 or newer recommended
- Git
- A desktop environment capable of opening a Swing window

The project uses modern switch expressions, so use a recent JDK rather than an older Java runtime.

### Clone the repository

```bash
git clone https://github.com/JJdevjoHan/Blood-Of-The-Rift_ProjectOOP.git
cd Blood-Of-The-Rift_ProjectOOP
```

### Run with Eclipse

1. Import the repository as an **Existing Eclipse Project**.
2. Keep `src` as a Java source folder and `Resources` as a second source/resource folder.
3. Use a recent JDK for the project runtime.
4. Run `src/ui/MainFrame.java` as a Java application.

The checked-in `.classpath` already points Eclipse to `src`, `Resources`, and the `bin` output folder.

### Compile and run from a terminal

From the repository root:

```bash
mkdir -p bin
javac -d bin $(find src -name '*.java')
java -cp "bin:Resources" ui.MainFrame
```

On Windows, replace the runtime classpath separator `:` with `;`:

```bat
java -cp "bin;Resources" ui.MainFrame
```

Run from the repository root so classpath resources such as `/images/backgroundpic/intro.png` resolve correctly.

## Project structure

| Path | Purpose |
| --- | --- |
| `src/ui/MainFrame.java` | Application entry point, panel registration, and world transitions |
| `src/ui/IntroPanel.java` | Opening screen and intro skip choice |
| `src/ui/DialoguePanel.java` | Timed narrative sequence |
| `src/ui/HomePanel.java` | Player name and class selection |
| `src/ui/LoadingPanel.java` | Typewriter-style loading story before the safehouse |
| `src/ui/Worlds/Home.java` | Safehouse, chest interaction, and journey confirmation |
| `src/ui/Worlds/` | Grassy Plains, Desert, Snowy Island, Lava, and Final World panels |
| `src/ui/Characters/` | Abstract character model plus Warrior, Mage, and Paladin classes |
| `src/ui/Mobs/` | Realm enemies, guardians, and final bosses |
| `src/ui/Designs/` | Reusable Rift buttons, bars, and dialogs |
| `Resources/images/` | Backgrounds, playable characters, enemies, bosses, and item art |
| `out/` | Checked-in compiled output from the project workspace |

## Implementation notes

- `MainFrame` coordinates the full application with a `CardLayout`, switching between story, menu, safehouse, exploration, versus, and battle states.
- Each realm owns its encounter pool, guardian threshold, narrative lines, battle UI, and transition to the next realm.
- The abstract `Character` class centralizes shared health, mana, damage buffs, and life-state behavior; concrete classes provide their own skill logic.
- `World1Mob` supplies common enemy behavior, while later mob families and `World5Boss` specialize damage and special attacks through inheritance.
- Swing `Timer` and `java.util.Timer` sequences control dialogue pacing, versus reveals, enemy turns, victory screens, and ending scenes.
- Image loading uses classpath resources under `Resources/images`, so keeping the project root and resource folder intact is important when running locally.

## Current scope

This repository is a playable object-oriented Java Swing prototype with a strong presentation layer and a complete beginning-to-ending game flow. There is currently no Maven or Gradle build file and no automated test suite included; Eclipse or direct `javac` compilation is the intended workflow.

## License

No license file is currently included. Add a license before distributing the project or accepting external contributions.

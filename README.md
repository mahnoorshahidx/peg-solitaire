# Peg Solitaire

A Java Swing implementation of Peg Solitaire, supporting multiple board shapes and both manual and automated play, with game recording and replay.

## Features

- **Board types:** English, Hexagon, Diamond (`model.BoardType`)
- **Board sizes:** 5x5, 7x7, 9x9
- **Game modes:**
  - **Manual** — click a peg, then click an empty space two spots away to jump
  - **Automated** — the computer makes random valid moves until no moves remain
- **Recording & replay** — check "Record Game" to save every move to `game_record.txt`, then hit "Replay" to watch it play back

## Project structure

```
src/main/java/com/pegsolitaire/
├── Main.java              # entry point
├── model/                 # game state and rules
│   ├── SolitaireGame.java
│   ├── BoardType.java
│   └── GameMode.java
├── controller/             # how moves get made
│   ├── Game.java
│   ├── ManualGame.java
│   └── AutomatedGame.java
├── io/                     # recording/replaying to disk
│   ├── GameRecorder.java
│   └── GameReplayer.java
└── gui/                     # Swing UI
    └── SolitaireGUI.java

src/test/java/com/pegsolitaire/
└── SolitaireGameTest.java
```

## Running it

This project uses Maven.

```bash
# run the GUI directly
mvn compile exec:java

# run the tests
mvn test

# build a runnable jar (output in target/)
mvn package
java -jar target/peg-solitaire-1.0.0.jar
```

## Notes

- `game_record.txt` is created in whatever directory you run the app from — it's git-ignored, so it won't show up as a change in your repo.
- Requires Java 17+ and Maven.

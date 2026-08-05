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
## Screenshots

### English board (9x9)
A manual game in progress on the classic English cross-shaped board, showing pegs (●) and empty spaces after several jumps.

![English board gameplay]<img width="709" height="705" alt="board" src="https://github.com/user-attachments/assets/1b8b0f79-4b5c-492d-a33a-8c19511676e7" />

### Game controls
Board size, board type, and game mode (Manual or Automated) are all configurable from dropdowns at the top of the window.

![Board size, type, and mode controls]<img width="704" height="74" alt="controls" src="https://github.com/user-attachments/assets/6b59d291-6192-4ba8-b419-3759991afe8c" />

### Diamond board (7x7)
An alternate board layout — the Diamond shape trims the board into a rotated square instead of the standard cross.

![Diamond board layout]<img width="708" height="708" alt="diamond" src="https://github.com/user-attachments/assets/4c9c3e55-97d4-4cc0-9457-1df503fb9acc" />

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

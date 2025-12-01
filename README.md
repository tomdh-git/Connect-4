# Connect 4 - Extended Edition

## DESCRIPTION

This Connect 4 game has been extended with multiple gameplay modes, AI opponents, and save/load functionality.

### Features:
- **Two-Player Mode**: Two humans compete with color selection
- **Vs Computer Mode**: Play against AI with 3 difficulty levels
- **Variable Board Sizes**: 7x6, 14x12, 21x18 (rectangular) or 7x7, 12x12, 18x18 (square)
- **Four Corners Win**: Capture all 4 corners to win on square boards!
- **Lucky Coin System**: Random coin offers that you can accept or reject
- **Save/Load**: Save and resume games anytime
- **Statistics**: Track wins for each player
- **Undo/Restart**: Full undo and game restart support

## WIN CONDITIONS

1. **Four in a Row**: Connect 4 pieces horizontally, vertically, or diagonally
2. **Four Corners** (Square boards only): Capture all 4 corner positions

## DIFFICULTY LEVELS

### Rectangular Boards (Standard)
| Level | Board Size | Max Lucky Coins | AI Depth |
|-------|------------|-----------------|----------|
| Beginner | 7 x 6 | 3 | 2 |
| Intermediate | 14 x 12 | 7 | 4 |
| Expert | 21 x 18 | 11 | 6 |

### Square Boards (Four Corners Mode)
| Level | Board Size | Max Lucky Coins | AI Depth |
|-------|------------|-----------------|----------|
| Beginner | 7 x 7 | 3 | 2 |
| Intermediate | 12 x 12 | 7 | 4 |
| Expert | 18 x 18 | 11 | 6 |

## LUCKY COIN SYSTEM

Lucky coins appear randomly (15% chance after each move). When one appears:

1. A turquoise "lucky coin" is placed at a valid position
2. The current player can **ACCEPT** (claim it as their piece) or **REJECT** (remove it)
3. Accepting uses your turn; rejecting does not
4. Lucky coins count toward winning conditions
5. Maximum lucky coins per game depends on difficulty level

## HOW TO RUN

### Compile:
```bash
cd Connect-4 && javac *.java
```

### Run with configuration:
```bash
java Connect4Game          # Interactive setup
java Connect4Game gui      # GUI mode
java Connect4Game text     # Text mode
```

## TEXT MODE COMMANDS

| Command | Action |
|---------|--------|
| 1-N | Drop piece in column N |
| A/ACCEPT | Accept lucky coin offer |
| X/REJECT | Reject lucky coin offer |
| U/UNDO | Undo last move |
| R/RESTART | Restart game |
| S/SAVE | Save current game |
| L/LOAD | Load saved game |
| T/STATS | Show statistics |
| Q/QUIT | Exit game |

## GUI MODE FEATURES

- Click column numbers above board to place pieces
- Lucky coin dialog prompts for accept/reject
- Game menu: New Game, Save, Load, Exit
- Stats menu: View/Reset statistics
- Buttons: Undo, Restart, New Game
- Board type selection: Rectangular or Square

## AI IMPLEMENTATION

The computer opponent uses **Min/Max algorithm with alpha-beta pruning**:

### Algorithm Overview:
1. Generate all possible moves
2. For each move, recursively evaluate resulting positions
3. AI (maximizing) picks moves that maximize score
4. Opponent (minimizing) picks moves that minimize score
5. Alpha-beta pruning eliminates unnecessary branches

### Lucky Coin Decision:
AI evaluates both accept and reject outcomes and chooses the better option.
On square boards, AI always accepts corner positions.

### Evaluation Heuristics:
- **Center Control**: Center columns score higher
- **Line Potential**: 2-in-a-row +10, 3-in-a-row +100
- **Threat Detection**: Immediate wins/blocks +/-10000
- **Corner Value**: Corners valuable on square boards

## FILE STRUCTURE

| File | Purpose |
|------|---------|
| `Cell.java` | Cell state (empty, red, yellow, lucky) |
| `Player.java` | Player info (name, type, color, stats) |
| `DifficultyLevel.java` | Board sizes and AI parameters |
| `GameSettings.java` | Game configuration container |
| `GameState.java` | Game logic and state management |
| `GameView.java` | Interface for view implementations |
| `GUIView.java` | Swing-based graphical interface |
| `TextView.java` | Console-based text interface |
| `BoardDrawing.java` | GUI board rendering |
| `AIPlayer.java` | Computer opponent with min/max |
| `SaveLoadManager.java` | Save/load functionality |
| `Connect4Game.java` | Main launcher |

## SAVE FILE LOCATION

Saved games are stored in `saves/` directory with `.c4save` extension.

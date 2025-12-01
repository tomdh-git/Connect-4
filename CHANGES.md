# Connect Four - Extended Edition: Complete Change Log

This document provides a comprehensive overview of all modifications and implementations made to extend the base Connect Four game into the Extended Edition.

---

## Table of Contents

1. [How to Run the Game](#how-to-run-the-game)
   - [Prerequisites](#prerequisites)
   - [Running in Terminal](#running-in-terminal--command-line)
   - [Running in IDEs](#running-in-ides)
   - [Troubleshooting](#troubleshooting)
2. [Modified Classes](#modified-classes)
   - [Cell.java](#celljava)
   - [Player.java](#playerjava)
   - [DifficultyLevel.java](#difficultylevelsjava)
   - [GameSettings.java](#gamesettingsjava)
   - [GameState.java](#gamestatejava)
   - [BoardDrawing.java](#boarddrawingjava)
   - [GUIView.java](#guiviewjava)
   - [Connect4Game.java](#connect4gamejava)
3. [New Classes](#new-classes)
   - [GameView.java](#gameviewjava-interface)
   - [TextView.java](#textviewjava)
   - [AIPlayer.java](#aiplayerjava)
   - [SaveLoadManager.java](#saveloadmanagerjava)
   - [MainMenu.java](#mainmenujava)
4. [Summary](#summary-of-extended-features)

---

## How to Run the Game

### Prerequisites

Before running the game, ensure you have:

- **Java Development Kit (JDK) 8 or higher** installed
- Verify installation by running these commands in your terminal:
  ```bash
  java -version
  javac -version
  ```
- Both commands should display version information (e.g., `java version "17.0.1"`)

**Download JDK:** If not installed, download from [Oracle JDK](https://www.oracle.com/java/technologies/downloads/) or [OpenJDK](https://adoptium.net/)

---

### Running in Terminal / Command Line

#### Windows (Command Prompt or PowerShell)

```cmd
cd path\to\Connect-4
javac *.java
java MainMenu
```

#### macOS / Linux (Terminal)

```bash
cd /path/to/Connect-4
javac *.java
java MainMenu
```

#### Step-by-Step Explanation

1. **`cd Connect-4`** - Navigate to the project directory containing all `.java` files
2. **`javac *.java`** - Compile all 13 Java source files (creates `.class` files)
3. **`java MainMenu`** - Launch the game starting with the graphical main menu

#### Quick One-Liner

```bash
cd Connect-4 && javac *.java && java MainMenu
```

---

### Running in IDEs

#### IntelliJ IDEA

**Method 1: Open as Folder**

1. Launch IntelliJ IDEA
2. Click **File → Open**
3. Navigate to and select the `Connect-4` folder
4. Click **OK**
5. Wait for IntelliJ to index the Java files (progress bar at bottom)
6. In the **Project** panel (left side), expand the folder structure
7. Right-click on **`MainMenu.java`**
8. Select **Run 'MainMenu.main()'**

**Method 2: Quick Run**

1. Open the `Connect-4` folder in IntelliJ
2. Open `MainMenu.java` in the editor
3. Look for the green **▶** play button in the gutter (left margin) next to:
   ```java
   public static void main(String[] args)
   ```
4. Click the play button → Select **Run 'MainMenu.main()'**

**Method 3: Create Run Configuration**

1. Go to **Run → Edit Configurations**
2. Click the **+** button → Select **Application**
3. Configure:
   - **Name:** Connect Four
   - **Main class:** MainMenu
   - **Working directory:** `/path/to/Connect-4`
4. Click **Apply** then **OK**
5. Press **Shift+F10** or click **Run** to start

---

#### Eclipse

**Method 1: Import Existing Project**

1. Launch Eclipse
2. Go to **File → Import**
3. Expand **General** → Select **Existing Projects into Workspace**
4. Click **Next**
5. Click **Browse** next to "Select root directory"
6. Navigate to the `Connect-4` folder and select it
7. Ensure the project is checked in the projects list
8. Click **Finish**
9. In **Package Explorer**, find and right-click **`MainMenu.java`**
10. Select **Run As → Java Application**

**Method 2: Create New Project with Existing Source**

1. Go to **File → New → Java Project**
2. Enter project name: `Connect4`
3. **Uncheck** "Use default location"
4. Click **Browse** and select the `Connect-4` folder
5. Click **Finish**
6. Right-click **`MainMenu.java`** → **Run As → Java Application**

---

#### Visual Studio Code

**Step 1: Install Java Extension Pack**

1. Open VS Code
2. Press **Ctrl+Shift+X** (or Cmd+Shift+X on Mac) to open Extensions
3. Search for **"Extension Pack for Java"** by Microsoft
4. Click **Install** (this installs multiple Java-related extensions)
5. Restart VS Code if prompted

**Step 2: Open the Project**

1. Go to **File → Open Folder**
2. Select the `Connect-4` folder
3. Click **Select Folder**

**Step 3: Run the Game**

**Option A: Click to Run**
1. Open `MainMenu.java` in the editor
2. Look for **Run | Debug** text appearing above the `main` method
3. Click **Run**

**Option B: Use F5**
1. Open `MainMenu.java`
2. Press **F5**
3. If prompted, select **Java** as the debug configuration

**Option C: Run and Debug Panel**
1. Press **Ctrl+Shift+D** (or Cmd+Shift+D on Mac)
2. Click **create a launch.json file** if no configuration exists
3. Select **Java**
4. Click the green play button

**Option D: Integrated Terminal**
1. Press **Ctrl+`** to open terminal
2. Run: `javac *.java && java MainMenu`

---

#### NetBeans

**Method 1: Open as Project**

1. Launch NetBeans
2. Go to **File → New Project**
3. Select **Java with Ant → Java Application**
4. Click **Next**
5. Configure:
   - **Project Name:** Connect4
   - **Uncheck** "Create Main Class"
   - **Project Location:** Parent folder of `Connect-4`
6. Click **Finish**
7. In **Projects** panel, right-click **Source Packages**
8. Select **Properties** → Add `Connect-4` as source folder
9. Right-click **`MainMenu.java`** → **Run File**

**Method 2: Open Single Files**

1. Go to **File → Open File**
2. Select all `.java` files in the `Connect-4` folder
3. Right-click on `MainMenu.java` tab
4. Select **Run File** (Shift+F6)

---

### Troubleshooting

| Problem | Cause | Solution |
|---------|-------|----------|
| `'javac' is not recognized` | JDK not installed or not in PATH | Install JDK and add `JAVA_HOME/bin` to system PATH |
| `'java' is not recognized` | JRE/JDK not in PATH | Add Java bin directory to system PATH |
| `Error: Could not find or load main class MainMenu` | Wrong directory or not compiled | Ensure you're in `Connect-4` folder and ran `javac *.java` first |
| `Error: Could not find or load main class MainMenu` (in IDE) | Working directory incorrect | Set working directory to `Connect-4` folder in run configuration |
| Compilation errors | Missing source files | Ensure all 13 `.java` files are present |
| `No main class found` | IDE can't find entry point | Manually specify `MainMenu` as the main class |
| GUI window doesn't appear | Headless environment | Run on a system with graphical display capability |
| Text mode colors not working | Terminal doesn't support ANSI | Use a modern terminal (Windows Terminal, iTerm2, etc.) |
| `java.awt.HeadlessException` | No display available | Set `DISPLAY` environment variable (Linux) or use GUI-capable environment |

**Verify Java Installation:**
```bash
# Check Java version
java -version

# Check Java compiler version  
javac -version

# Find Java installation path
# Windows:
where java

# macOS/Linux:
which java
```

---

## Modified Classes

### Cell.java

**Purpose**: Represents a single cell on the Connect Four game board.

#### Changes Made:

1. **Added CellState Enum**
   - `EMPTY` - Cell is available for placement
   - `RED` - Cell contains a red coin
   - `YELLOW` - Cell contains a yellow coin
   - `LUCKY` - Cell contains a lucky coin (turquoise with 4-leaf clover)

2. **State Management**
   - Changed from simple boolean flags to enum-based state
   - Added `private CellState state` instance variable

3. **New Methods Added**
   - `isAvailable()` - Returns true if cell is empty
   - `isRed()` - Returns true if cell contains red coin
   - `isYellow()` - Returns true if cell contains yellow coin
   - `isLucky()` - Returns true if cell contains lucky coin
   - `setEmpty()` - Resets cell to empty state
   - `setRed()` - Sets cell to red coin
   - `setYellow()` - Sets cell to yellow coin
   - `setLucky()` - Sets cell to lucky coin state
   - `getState()` - Returns current CellState
   - `setState(CellState)` - Sets cell to specified state

4. **Visual Representation**
   - Added methods for GUI color mapping
   - Support for turquoise lucky coin display

---

### Player.java

**Purpose**: Defines a player with comprehensive attributes and statistics tracking.

#### Changes Made:

1. **Added PlayerType Enum**
   ```java
   public enum PlayerType {
       HUMAN,      // Human player controlled by user input
       COMPUTER    // AI player controlled by computer algorithm
   }
   ```

2. **Added CoinColor Enum**
   ```java
   public enum CoinColor {
       RED("Red", "\u001B[31m"),
       YELLOW("Yellow", "\u001B[33m"),
       BLUE("Blue", "\u001B[34m"),
       GREEN("Green", "\u001B[32m"),
       PURPLE("Purple", "\u001B[35m"),
       ORANGE("Orange", "\u001B[38;5;208m")
   }
   ```
   - Each color includes display name and ANSI code for terminal coloring

3. **New Instance Variables**
   - `int id` - Unique player identifier (1 or 2)
   - `String name` - Player's display name
   - `PlayerType type` - Human or Computer
   - `CoinColor coinColor` - Selected coin color
   - `int gamesWon` - Total games won (statistics)
   - `int gamesPlayed` - Total games played (statistics)

4. **New Methods**
   - `isHuman()` / `isComputer()` - Type checking
   - `recordWin()` / `recordLoss()` / `recordTie()` - Statistics tracking
   - `getWinRate()` - Calculate win percentage
   - `getStatsString()` - Formatted statistics display
   - `resetStats()` - Clear statistics
   - `createDefaultPlayer1()` / `createDefaultPlayer2()` - Factory methods
   - `createComputerPlayer(CoinColor)` - Factory for AI players

5. **Serialization Support**
   - Implements `Serializable` for save/load functionality
   - Added `serialVersionUID`

---

### DifficultyLevel.java

**Purpose**: Enum defining difficulty levels with board sizes and AI parameters.

#### Changes Made:

1. **Expanded from 3 to 6 Difficulty Levels**

   **Rectangular Boards (Standard Mode):**
   | Level | Columns | Rows | Lucky Coins | AI Depth |
   |-------|---------|------|-------------|----------|
   | BEGINNER | 7 | 6 | 3 | 2 |
   | INTERMEDIATE | 14 | 12 | 7 | 4 |
   | EXPERT | 21 | 18 | 11 | 4 |

   **Square Boards (Four Corners Mode):**
   | Level | Columns | Rows | Lucky Coins | AI Depth |
   |-------|---------|------|-------------|----------|
   | BEGINNER_SQUARE | 7 | 7 | 3 | 2 |
   | INTERMEDIATE_SQUARE | 12 | 12 | 7 | 4 |
   | EXPERT_SQUARE | 18 | 18 | 11 | 4 |

2. **New Properties Added**
   - `columns` - Board width
   - `rows` - Board height
   - `maxLuckyCoins` - Maximum lucky coins per game
   - `aiSearchDepth` - Min/max algorithm search depth
   - `winCondition` - Number in a row to win (always 4)
   - `isSquare` - Boolean for Four Corners mode eligibility

3. **New Methods**
   - `getColumns()` / `getRows()` - Board dimensions
   - `getMaxLuckyCoins()` - Lucky coin limit
   - `getAiSearchDepth()` - AI search depth
   - `isFourCornersEnabled()` - Check if Four Corners win available
   - `fromLevel(int)` - Convert 1-3 to rectangular difficulty
   - `fromSquareLevel(int)` - Convert 1-3 to square difficulty

4. **Performance Optimization**
   - Reduced EXPERT/EXPERT_SQUARE AI depth from 6 to 4
   - Prevents excessive computation time on large boards

---

### GameSettings.java

**Purpose**: Encapsulates all game configuration options.

#### Changes Made:

1. **Added GameMode Enum**
   ```java
   public enum GameMode {
       TWO_PLAYER("Two Players", "Two human players compete"),
       VS_COMPUTER("Vs Computer", "Single player vs AI opponent")
   }
   ```

2. **New Instance Variables**
   - `GameMode gameMode` - Current game mode
   - `DifficultyLevel difficultyLevel` - Selected difficulty
   - `Player player1` / `Player player2` - Player objects
   - `int columns` / `int rows` - Board dimensions
   - `int maxLuckyCoins` - Lucky coin limit
   - `int currentLuckyCoins` - Coins used this game

3. **Multiple Constructors**
   - `GameSettings()` - Default two-player mode
   - `GameSettings(DifficultyLevel, Player, boolean)` - Vs Computer mode
   - `GameSettings(Player, Player)` - Two-player with custom players

4. **New Methods**
   - `isVsComputer()` / `isTwoPlayer()` - Mode checking
   - `getComputerPlayer()` - Returns AI player if applicable
   - `canOfferLuckyCoin()` / `useLuckyCoin()` - Lucky coin management
   - `resetLuckyCoins()` - Reset for new game
   - `applyDifficultySettings()` - Apply level-specific settings
   - `setDifficultyLevel(DifficultyLevel)` - Change difficulty

---

### GameState.java

**Purpose**: Core game logic managing board state, moves, and win conditions.

#### Changes Made:

1. **Lucky Coin Offer System**
   - Added `luckyOfferPending` boolean flag
   - Added `luckyOfferColumn` / `luckyOfferRow` for offer position
   - 15% chance (`LUCKY_COIN_OFFER_CHANCE = 0.15`) after each move
   - Methods: `isLuckyOfferPending()`, `acceptLuckyOffer()`, `rejectLuckyOffer()`

2. **Four Corners Win Condition**
   - New win condition for square boards
   - `checkFourCornersWin()` - Check if player owns all 4 corners
   - Corners: (0,0), (0,rows-1), (cols-1,0), (cols-1,rows-1)
   - Only enabled when `difficultyLevel.isFourCornersEnabled()` is true

3. **Move System Enhancements**
   - `move(int column)` - Public move with validation and lucky coin check
   - `moveInternal(int column)` - Internal move for AI simulation (no lucky coins)
   - `wasLuckyCoin` stack for undo tracking of lucky coin moves

4. **Undo System**
   - Stack-based move history with `moves` Stack
   - Proper restoration of lucky coin states
   - Turn switching on undo

5. **New Accessor Methods**
   - `getColumns()` / `getRows()` - Board dimensions
   - `getCells()` - Access to cell array
   - `getCurrentPlayer()` - Current player object
   - `getSettings()` - Game settings access
   - `getStatusMessage()` - Formatted status for display
   - `getLuckyOfferColumn()` / `getLuckyOfferRow()` - Offer position

6. **Game State Serialization Support**
   - Setters for all state variables for save/load
   - `setLuckyOfferState(boolean, int, int)` - Restore offer state

---

### BoardDrawing.java

**Purpose**: Swing component for rendering the game board graphically.

#### Changes Made:

1. **Variable Board Size Support**
   - Dynamic `boardCols` and `boardRows` from GameState
   - `cellSize` calculated based on available space
   - Minimum cell size of 20 pixels ensured

2. **Dynamic Dimension Calculation**
   - `updateDimensions()` method recalculates on resize/state change
   - Responsive `columnZones` array for click detection
   - Centered board positioning

3. **Preferred Size Implementation**
   - Added `getPreferredSize()` method for proper layout
   - Returns dimensions based on board size

4. **Enhanced Rendering**
   - Support for all CoinColor values (not just red/yellow)
   - Lucky coin rendering in turquoise/cyan
   - Corner highlighting for Four Corners mode
   - Anti-aliasing for smooth graphics

5. **GUIView Integration**
   - `setGUIView(GUIView)` for callback connection
   - `onColumnClicked(int)` callback for move input
   - `setGameState(GameState)` for game updates

6. **Visual Enhancements**
   - Column number buttons at top
   - Current player indicator
   - Game status display area
   - Win highlighting

---

### GUIView.java

**Purpose**: Graphical user interface implementing the GameView interface.

#### Changes Made:

1. **Complete Rewrite as GameView Implementation**
   - Implements `GameView` interface
   - Full separation from game logic

2. **Menu System**
   - **Game Menu**: New Game, Save, Load, Exit
   - **View Menu**: Switch to Text Mode, Back to Main Menu
   - **Stats Menu**: View Statistics, Reset Statistics

3. **AI Integration**
   - `AIPlayer aiPlayer` instance variable
   - `setupAIIfNeeded()` - Initialize AI for vs computer mode
   - `makeAIMove()` - Execute AI turn with SwingWorker
   - `triggerAIMove()` - Timer-based AI triggering (300ms delay)

4. **View Switching**
   - `switchToTextView()` - Transition to console mode
   - `backToMainMenu()` - Return to main menu
   - Preserves game state during switch

5. **Lucky Coin Dialog**
   - `showLuckyOfferDialog()` - Accept/reject prompt
   - Visual indication of coin position

6. **Save/Load Integration**
   - `handleSave()` - Save game dialog
   - `handleLoad()` - Load game with file selection

7. **Configuration Dialog**
   - `showConfigDialog()` - In-game new game setup
   - Mode, difficulty, player selection

8. **Threading Fixes**
   - EDT-safe initialization with `SwingUtilities.invokeAndWait()`
   - `SwingWorker` for AI moves (prevents UI freeze)
   - Timer-based AI triggering for smooth gameplay

---

### Connect4Game.java

**Purpose**: Alternative launcher with command-line configuration.

#### Changes Made:

1. **Command-Line Mode Selection**
   - Parses arguments for view type selection
   - Supports launching directly into GUI or Text mode

2. **GameView Integration**
   - Creates appropriate view based on selection
   - Passes GameState to chosen view

---

## New Classes

### GameView.java (Interface)

**Purpose**: Defines contract for all game view implementations, enabling GUI/Text mode switching.

#### Interface Methods:

```java
public interface GameView {
    void initialize();           // Set up the view
    void update();               // Refresh display
    void displayBoard();         // Show game board
    void displayMessage(String); // Show status message
    void displayError(String);   // Show error message
    int getPlayerInput();        // Get player's move choice
    void startGameLoop();        // Main game loop
    void cleanup();              // Clean up resources
}
```

#### Design Benefits:
- Decouples view from game logic
- Enables seamless view switching
- Supports future view implementations

---

### TextView.java

**Purpose**: Console-based game interface with colored ASCII board display.

#### Features Implemented:

1. **ANSI Color Support**
   - Red, Yellow, Cyan, Blue, Green, Purple colors
   - Bold text for emphasis
   - Color-coded player pieces

2. **ASCII Board Rendering**
   - Unicode box-drawing characters for board grid
   - Dynamic sizing based on board dimensions
   - Legend showing piece symbols

3. **Command System**
   | Command | Action |
   |---------|--------|
   | 1-N | Drop piece in column |
   | A/ACCEPT | Accept lucky coin |
   | X/REJECT | Reject lucky coin |
   | U/UNDO | Undo last move |
   | R/RESTART | Restart game |
   | S/SAVE | Save game |
   | L/LOAD | Load game |
   | T/STATS | Show statistics |
   | G/GUI | Switch to GUI mode |
   | M/MENU | Back to main menu |
   | Q/QUIT | Exit game |

4. **AI Integration**
   - `AIPlayer` instance for vs computer mode
   - Displays "Computer is thinking..." message
   - Handles AI lucky coin decisions

5. **Save/Load Support**
   - File listing with summaries
   - Numbered selection for loading
   - Auto-naming for quick saves

6. **View Switching**
   - `handleSwitchToGUI()` - Transition to graphical mode
   - `handleBackToMenu()` - Return to main menu
   - Confirmation prompts before switching

---

### AIPlayer.java

**Purpose**: Computer opponent using min/max algorithm with alpha-beta pruning.

#### Implementation Details:

1. **Algorithm: Min/Max with Alpha-Beta Pruning**
   - Recursive evaluation of game positions
   - Alpha-beta pruning eliminates unnecessary branches
   - Configurable search depth by difficulty

2. **Search Depths**
   | Difficulty | Depth | Notes |
   |------------|-------|-------|
   | Beginner | 2 | 30% random moves for unpredictability |
   | Intermediate | 4 | Full evaluation |
   | Expert | 4 | Comprehensive analysis (optimized from 6) |

3. **Move Ordering**
   - Center columns evaluated first
   - Improves alpha-beta pruning efficiency
   - `getValidMoves()` returns ordered column list

4. **Position Evaluation Heuristics**
   - `WIN_SCORE = 1,000,000` / `LOSE_SCORE = -1,000,000`
   - Center column bonus (columns closer to center score higher)
   - Window scoring (evaluate all 4-cell windows)
   - Threat detection (3-in-a-row with open end)
   - Blocking detection (prevent opponent wins)

5. **Four Corners Strategy**
   - Corner positions heavily weighted on square boards
   - AI always accepts corner lucky coins
   - `evaluateFourCorners()` for position scoring

6. **Lucky Coin Decision Making**
   - `shouldAcceptLuckyOffer(GameState)` - Evaluate accept vs reject
   - Simulates both outcomes and chooses better score
   - Special handling for corner positions

7. **Game State Cloning**
   - `cloneGameState(GameState)` for move simulation
   - Deep copy of board and game state
   - Enables lookahead without modifying actual game

---

### SaveLoadManager.java

**Purpose**: Handles game persistence using Java serialization.

#### Implementation Details:

1. **GameSnapshot Inner Class**
   ```java
   public static class GameSnapshot implements Serializable {
       Cell[][] cells;
       boolean player1Turn;
       boolean gameOver;
       boolean player1Wins;
       boolean player2Wins;
       GameSettings settings;
       Stack<Point> moves;
       boolean luckyOfferPending;
       int luckyOfferColumn;
       int luckyOfferRow;
       String description;
       long timestamp;
   }
   ```

2. **Save Directory Management**
   - Creates `saves/` directory if not exists
   - Files stored as `.c4save` extension

3. **Save Methods**
   - `saveGame(GameState, String, String)` - Save with custom name
   - `saveGameAuto(GameState, String)` - Auto-generated timestamp name
   - Returns filename on success, null on failure

4. **Load Methods**
   - `loadGame(String)` - Load snapshot from file
   - `applySnapshot(GameSnapshot)` - Restore GameState from snapshot
   - Handles file not found and deserialization errors

5. **Save Management**
   - `listSaves()` - Get array of save file names
   - `getSaveSummaries()` - Get formatted descriptions
   - `deleteSave(String)` - Remove save file

---

### MainMenu.java

**Purpose**: Graphical main menu and game configuration wizard.

#### Implementation Details:

1. **CardLayout-Based Navigation**
   - Multiple screens in single frame
   - Smooth transitions between configuration steps

2. **Menu Screens (in order)**

   **Screen 1: Title Screen**
   - "CONNECT FOUR - Extended Edition" title
   - Buttons: New Game, Load Game, Statistics, Exit

   **Screen 2: Game Mode Selection**
   - Two Players button
   - Vs Computer button
   - Back button

   **Screen 3: View Type Selection**
   - Graphical Interface (GUI) button
   - Text Mode (Console) button
   - Back button

   **Screen 4: Board Type Selection**
   - Rectangular (Standard) button
   - Square (Four Corners Mode) button
   - Back button

   **Screen 5: Difficulty Selection**
   - Beginner (7x6 or 7x7)
   - Intermediate (14x12 or 12x12)
   - Expert (21x18 or 18x18)
   - Back button

   **Screen 6: Player Setup**
   - For Two Players: Both player names and colors
   - For Vs Computer: Player name, color, who goes first

3. **Styled Components**
   - `createMenuButton(String)` - Consistent button styling
   - Golden background with dark blue text
   - Hover effects
   - `setOpaque(true)` and `setContentAreaFilled(true)` for proper rendering

4. **Game Launching**
   - `startGame(GameSettings)` - Create GameState and launch view
   - Supports both GUI and Text mode launch
   - Proper window disposal before launching

5. **Load Game Integration**
   - Lists available saves
   - View type selection for loaded games
   - Immediate game launch after load

---

## Summary of Extended Features

### Feature Categories:

1. **Multiple Game Modes**
   - Two-Player Mode
   - Vs Computer Mode (with 3 difficulty levels)

2. **Variable Board Sizes**
   - Rectangular: 7x6, 14x12, 21x18
   - Square: 7x7, 12x12, 18x18

3. **Dual View System**
   - Graphical User Interface (GUI)
   - Text-Based Console Interface
   - Seamless switching between views during gameplay

4. **Lucky Coin System**
   - 15% chance after each move
   - Accept/Reject decision
   - Limits: 3/7/11 based on difficulty

5. **Four Corners Win Condition**
   - Available only on square boards
   - First to capture all 4 corners wins
   - Alternative to 4-in-a-row

6. **AI Opponent**
   - Min/Max with Alpha-Beta Pruning
   - 3 difficulty levels with varying search depths
   - Smart lucky coin evaluation

7. **Save/Load System**
   - Full game state persistence
   - Multiple save slots
   - Auto-save naming

8. **Statistics Tracking**
   - Games won/played per player
   - Win rate calculation
   - Reset capability

9. **Undo Functionality**
   - Stack-based move history
   - Lucky coin state restoration

---

## File Statistics

| File | Lines of Code | Status |
|------|---------------|--------|
| Cell.java | ~200 | Modified |
| Player.java | ~290 | Modified |
| DifficultyLevel.java | ~200 | Modified |
| GameSettings.java | ~350 | Modified |
| GameState.java | ~535 | Modified |
| BoardDrawing.java | ~230 | Modified |
| GUIView.java | ~658 | Modified |
| Connect4Game.java | ~40 | Modified |
| GameView.java | ~90 | **New** |
| TextView.java | ~560 | **New** |
| AIPlayer.java | ~530 | **New** |
| SaveLoadManager.java | ~360 | **New** |
| MainMenu.java | ~575 | **New** |

**Total Approximate Lines: ~4,600+**

---

*Document generated for Connect Four - Extended Edition*

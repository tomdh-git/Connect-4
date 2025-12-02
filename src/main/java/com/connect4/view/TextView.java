package src.main.java.com.connect4.view;

import java.util.Scanner;

import src.main.java.com.connect4.player.AIPlayer;
import src.main.java.com.connect4.player.Player;
import src.main.java.com.connect4.settings.GameSettings;
import src.main.java.com.connect4.settings.SaveLoadManager;

/**
 * TextView.java - MODIFIED CLASS
 * This class implements the GameView interface for text-based console gameplay.
 * EXTENDED FEATURES:
 * - Game configuration menu (mode, difficulty, players)
 * - Save/Load game functionality
 * - Player statistics display
 * - AI opponent support
 * - Variable board size support
 * - Lucky coin offer accept/reject handling
 * - Four corners win mode for square boards
 * 
 * @author Created for multi-view support + Extended features
 */
public class TextView implements GameView {

    private GameState state;
    private final Scanner scanner;
    private boolean running;
    private AIPlayer aiPlayer;

    private static final String RESET = "\u001B[0m";
    private static final String RED = "\u001B[31m";
    private static final String YELLOW = "\u001B[33m";
    private static final String CYAN = "\u001B[36m";
    private static final String BLUE = "\u001B[34m";
    private static final String GREEN = "\u001B[32m";
    private static final String BOLD = "\u001B[1m";
    private static final String PURPLE = "\u001B[35m";

    public TextView(GameState gameState) {
        this.state = gameState;
        this.scanner = new Scanner(System.in);
        this.running = true;
        this.aiPlayer = null;
    }

    @Override
    public void initialize() {
        clearScreen();
        printWelcome();
    }

    private void printWelcome() {
        System.out.println(BOLD + BLUE + "╔═══════════════════════════════════════════╗" + RESET);
        System.out.println(BOLD + BLUE + "║" + RESET + "         " + BOLD + "CONNECT FOUR - TEXT MODE" + RESET
                + "        " + BLUE + "║" + RESET);
        System.out.println(BOLD + BLUE + "╚═══════════════════════════════════════════╝" + RESET);
        System.out.println();
    }

    private void printInstructions() {
        GameSettings settings = state.getSettings();
        int cols = state.getColumns();

        System.out.println(BOLD + "Game Settings:" + RESET);
        System.out.println("  Board: " + cols + " x " + state.getRows());
        System.out.println("  Mode: " + settings.getGameMode().getDisplayName());
        if (settings.isVsComputer()) {
            System.out.println("  Difficulty: " + settings.getDifficultyLevel().getDisplayName());
        }
        if (settings.getDifficultyLevel().isFourCornersEnabled()) {
            System.out.println("  " + CYAN + "Four Corners Win: ENABLED" + RESET);
        }
        System.out.println();
        System.out.println(BOLD + "Commands:" + RESET);
        System.out.println("  1-" + cols + "       - Drop piece in column");
        System.out.println("  A/ACCEPT  - Accept lucky coin offer");
        System.out.println("  X/REJECT  - Reject lucky coin offer");
        System.out.println("  U/UNDO    - Undo last move");
        System.out.println("  R/RESTART - Restart game");
        System.out.println("  S/SAVE    - Save game");
        System.out.println("  L/LOAD    - Load game");
        System.out.println("  T/STATS   - Show statistics");
        System.out.println("  G/GUI     - Switch to GUI mode");
        System.out.println("  M/MENU    - Back to main menu");
        System.out.println("  Q/QUIT    - Exit game");
        System.out.println();
        System.out.println(BOLD + "Legend:" + RESET);
        System.out.println("  " + getColoredSymbol(state.getSettings().getPlayer1()) +
                " = " + state.getSettings().getPlayer1().getName());
        System.out.println("  " + getColoredSymbol(state.getSettings().getPlayer2()) +
                " = " + state.getSettings().getPlayer2().getName());
        System.out.println("  " + CYAN + "L" + RESET + " = Lucky coin offer");
        System.out.println("  . = Empty cell");
        if (settings.getDifficultyLevel().isFourCornersEnabled()) {
            System.out.println("  " + CYAN + "* = Corner (capture all 4 to win!)" + RESET);
        }
        System.out.println();
    }

    private String getColoredSymbol(Player player) {
        String color = getColorCode(player.getCoinColor());
        return color + BOLD + player.getCoinColor().getDisplayName().charAt(0) + RESET;
    }

    private String getColorCode(Player.CoinColor coinColor) {
        return switch (coinColor) {
            case RED -> RED;
            case YELLOW -> YELLOW;
            case BLUE -> BLUE;
            case GREEN -> GREEN;
            case PURPLE -> PURPLE;
            default -> RESET;
        };
    }

    private void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    @Override
    public void update() {
        displayBoard();
        displayStatus();
    }

    @Override
    public void displayBoard() {
        System.out.println();
        int cols = state.getColumns();
        int rows = state.getRows();
        Cell[][] cells = state.getCells();
        boolean isSquare = state.getSettings().getDifficultyLevel().isFourCornersEnabled();

        System.out.print(BLUE + "  ╔");
        for (int i = 0; i < cols; i++) {
            System.out.print("═══");
            if (i < cols - 1)
                System.out.print("╦");
        }
        System.out.println("╗" + RESET);

        for (int row = rows - 1; row >= 0; row--) {
            System.out.print(BLUE + "  ║" + RESET);
            for (int col = 0; col < cols; col++) {
                Cell cell = cells[col][row];
                String cellDisplay = getCellDisplay(cell, col, row, rows, cols, isSquare);
                System.out.print(" " + cellDisplay + " " + BLUE + "║" + RESET);
            }
            System.out.println();

            if (row > 0) {
                System.out.print(BLUE + "  ╠");
                for (int i = 0; i < cols; i++) {
                    System.out.print("═══");
                    if (i < cols - 1)
                        System.out.print("╬");
                }
                System.out.println("╣" + RESET);
            }
        }

        System.out.print(BLUE + "  ╚");
        for (int i = 0; i < cols; i++) {
            System.out.print("═══");
            if (i < cols - 1)
                System.out.print("╩");
        }
        System.out.println("╝" + RESET);

        System.out.print("  ");
        for (int i = 1; i <= cols; i++) {
            System.out.printf(" %2d", i);
            if (i < cols)
                System.out.print(" ");
        }
        System.out.println();
        System.out.println();
    }

    private boolean isCorner(int col, int row, int rows, int cols) {
        return (col == 0 && row == 0) ||
                (col == 0 && row == rows - 1) ||
                (col == cols - 1 && row == 0) ||
                (col == cols - 1 && row == rows - 1);
    }

    private String getCellDisplay(Cell cell, int col, int row, int rows, int cols, boolean isSquare) {
        GameSettings settings = state.getSettings();
        boolean isCornerCell = isSquare && isCorner(col, row, rows, cols);

        if (cell.isLucky()) {
            return CYAN + BOLD + "L" + RESET;
        } else if (cell.isRed()) {
            String color = settings.getPlayer1().getCoinColor() == Player.CoinColor.RED
                    ? getColorCode(settings.getPlayer1().getCoinColor())
                    : getColorCode(settings.getPlayer2().getCoinColor());
            return color + BOLD + "R" + RESET;
        } else if (cell.isYellow()) {
            String color = settings.getPlayer1().getCoinColor() == Player.CoinColor.YELLOW
                    ? getColorCode(settings.getPlayer1().getCoinColor())
                    : getColorCode(settings.getPlayer2().getCoinColor());
            return color + BOLD + "Y" + RESET;
        } else {
            if (isCornerCell) {
                return CYAN + "*" + RESET;
            }
            return ".";
        }
    }

    private void displayStatus() {
        String status = state.getStatusMessage();
        Player current = state.getCurrentPlayer();
        String color = getColorCode(current.getCoinColor());

        if (state.getPlayer1Wins() || state.getPlayer2Wins()) {
            System.out.println(color + BOLD + "★★★ " + status + " ★★★" + RESET);
        } else if (state.getGameOver()) {
            System.out.println(BOLD + "☆☆☆ " + status + " ☆☆☆" + RESET);
        } else if (state.isLuckyOfferPending()) {
            System.out.println(CYAN + BOLD + "★ " + status + RESET);
            System.out.println(CYAN + "  Enter A to Accept or X to Reject the lucky coin" + RESET);
        } else {
            System.out.println(color + "► " + status + RESET);
        }
        System.out.println();
    }

    @Override
    public void displayMessage(String message) {
        System.out.println(GREEN + message + RESET);
    }

    @Override
    public void displayError(String error) {
        System.out.println(RED + "✗ Error: " + error + RESET);
    }

    @Override
    public int getPlayerInput() {
        int cols = state.getColumns();
        while (true) {
            if (state.isLuckyOfferPending()) {
                System.out.print("Lucky coin offer! Enter A (Accept) or X (Reject): ");
            } else {
                System.out.print("Enter move (1-" + cols + "), commands: U/R/S/L/T/Q: ");
            }
            String input = scanner.nextLine().trim().toUpperCase();

            switch (input) {
                case "Q", "QUIT" -> {
                    return -2;
                }
                case "R", "RESTART" -> {
                    return -1;
                }
                case "U", "UNDO" -> {
                    return 0;
                }
                case "S", "SAVE" -> {
                    return -3;
                }
                case "L", "LOAD" -> {
                    return -4;
                }
                case "T", "STATS" -> {
                    return -5;
                }
                case "A", "ACCEPT" -> {
                    return -6;
                }
                case "X", "REJECT" -> {
                    return -7;
                }
                case "G", "GUI" -> {
                    return -8;
                }
                case "M", "MENU" -> {
                    return -9;
                }
            }

            if (state.isLuckyOfferPending()) {
                displayError("Enter A to accept or X to reject the lucky coin.");
                continue;
            }

            if (input.contains(",")) {
                return parseCoordinateInput(input);
            }

            try {
                int column = Integer.parseInt(input);
                if (column >= 1 && column <= cols) {
                    if (state.isValidMove(column)) {
                        return column;
                    } else {
                        if (state.getGameOver()) {
                            displayError("Game is over. Press R to restart or Q to quit.");
                        } else {
                            displayError("Column " + column + " is full.");
                        }
                    }
                } else {
                    displayError("Invalid column. Enter 1-" + cols + ".");
                }
            } catch (NumberFormatException e) {
                displayError("Invalid input.");
            }
        }
    }

    private int parseCoordinateInput(String input) {
        try {
            String[] parts = input.split(",");
            if (parts.length != 2) {
                displayError("Invalid format. Use 'column,row'.");
                return getPlayerInput();
            }

            int column = Integer.parseInt(parts[0].trim());
            int row = Integer.parseInt(parts[1].trim());

            if (column < 1 || column > state.getColumns()) {
                displayError("Column must be 1-" + state.getColumns() + ".");
                return getPlayerInput();
            }

            if (row < 1 || row > state.getRows()) {
                displayError("Row must be 1-" + state.getRows() + ".");
                return getPlayerInput();
            }

            if (state.getGameOver()) {
                displayError("Game is over.");
                return getPlayerInput();
            }

            Cell[][] cells = state.getCells();
            int col = column - 1;
            int targetRow = row - 1;

            int landingRow = 0;
            while (landingRow < state.getRows() && !cells[col][landingRow].isAvailable()) {
                landingRow++;
            }

            if (landingRow >= state.getRows()) {
                displayError("Column " + column + " is full.");
                return getPlayerInput();
            } else if (landingRow != targetRow) {
                displayError("Piece lands at row " + (landingRow + 1) + ", not " + row + ".");
                return getPlayerInput();
            }

            return column;
        } catch (NumberFormatException e) {
            displayError("Invalid coordinates.");
            return getPlayerInput();
        }
    }

    private void handleSave() {
        System.out.print("Enter save name (or press Enter for auto): ");
        String name = scanner.nextLine().trim();

        String filename;
        if (name.isEmpty()) {
            filename = SaveLoadManager.saveGameAuto(state, "Quick save");
        } else {
            filename = name;
            SaveLoadManager.saveGame(state, name, "User save: " + name);
        }

        if (filename != null) {
            displayMessage("Game saved: " + filename);
        } else {
            displayError("Failed to save game.");
        }
    }

    private void handleLoad() {
        String[] saves = SaveLoadManager.listSaves();
        if (saves.length == 0) {
            displayError("No saved games found.");
            return;
        }

        System.out.println(BOLD + "\nAvailable saves:" + RESET);
        String[] summaries = SaveLoadManager.getSaveSummaries();
        for (int i = 0; i < saves.length; i++) {
            System.out.println("  " + (i + 1) + ". " + summaries[i]);
        }

        System.out.print("\nEnter number to load (or 0 to cancel): ");
        try {
            int choice = Integer.parseInt(scanner.nextLine().trim());
            if (choice > 0 && choice <= saves.length) {
                SaveLoadManager.GameSnapshot snapshot = SaveLoadManager.loadGame(saves[choice - 1]);
                if (snapshot != null) {
                    state = SaveLoadManager.applySnapshot(snapshot);
                    setupAIIfNeeded();
                    displayMessage("Game loaded successfully!");
                } else {
                    displayError("Failed to load game.");
                }
            }
        } catch (NumberFormatException e) {
            displayError("Invalid selection.");
        }
    }

    private void handleStats() {
        System.out.println(BOLD + "\n=== Game Statistics ===" + RESET);
        System.out.println(state.getSettings().getPlayer1().getStatsString());
        System.out.println(state.getSettings().getPlayer2().getStatsString());
        System.out.println();
    }

    private void handleSwitchToGUI() {
        System.out.print("Switch to GUI mode? (Y/N): ");
        String confirm = scanner.nextLine().trim().toUpperCase();
        if (confirm.equals("Y") || confirm.equals("YES")) {
            displayMessage("Switching to GUI mode...");
            running = false;

            javax.swing.SwingUtilities.invokeLater(() -> {
                GUIView guiView = new GUIView(state);
                guiView.initialize();
                guiView.startGameLoop();
            });
        }
    }

    private void handleBackToMenu() {
        System.out.print("Return to main menu? Unsaved progress will be lost. (Y/N): ");
        String confirm = scanner.nextLine().trim().toUpperCase();
        if (confirm.equals("Y") || confirm.equals("YES")) {
            displayMessage("Returning to main menu...");
            running = false;

            javax.swing.SwingUtilities.invokeLater(() -> {
                MainMenu menu = new MainMenu();
                menu.setVisible(true);
            });
        }
    }

    private void setupAIIfNeeded() {
        if (state.getSettings().isVsComputer()) {
            Player computer = state.getSettings().getComputerPlayer();
            if (computer != null) {
                aiPlayer = new AIPlayer(state.getSettings().getDifficultyLevel(),
                        computer.getId());
            }
        } else {
            aiPlayer = null;
        }
    }

    private void makeAIMove() {
        if (aiPlayer == null)
            return;

        if (state.isLuckyOfferPending()) {
            boolean shouldAccept = aiPlayer.shouldAcceptLuckyOffer(state);
            if (shouldAccept) {
                displayMessage("Computer accepts the lucky coin!");
                state.acceptLuckyOffer();
            } else {
                displayMessage("Computer rejects the lucky coin.");
                state.rejectLuckyOffer();
            }
            return;
        }

        displayMessage("Computer is thinking...");
        int move = aiPlayer.getBestMove(state);

        if (move > 0) {
            state.move(move);
            displayMessage("Computer plays column " + move);
        }
    }

    @Override
    public void startGameLoop() {
        running = true;
        printInstructions();
        setupAIIfNeeded();

        while (running) {
            update();

            if (state.getError() != null) {
                displayError(state.getError());
            }

            if (!state.getGameOver() && aiPlayer != null &&
                    state.getCurrentPlayer().isComputer()) {
                makeAIMove();
                continue;
            }

            int input = getPlayerInput();

            switch (input) {
                case -7:
                    if (state.isLuckyOfferPending()) {
                        state.rejectLuckyOffer();
                        displayMessage("Lucky coin rejected.");
                    } else {
                        displayError("No lucky coin to reject.");
                    }
                    break;
                case -6:
                    if (state.isLuckyOfferPending()) {
                        state.acceptLuckyOffer();
                        displayMessage("Lucky coin accepted!");
                        if (state.getGameOver()) {
                            update();
                            promptPlayAgain();
                        }
                    } else {
                        displayError("No lucky coin to accept.");
                    }
                    break;
                case -9:
                    handleBackToMenu();
                    break;
                case -8:
                    handleSwitchToGUI();
                    break;
                case -5:
                    handleStats();
                    break;
                case -4:
                    handleLoad();
                    break;
                case -3:
                    handleSave();
                    break;
                case -2:
                    displayMessage("Thanks for playing Connect Four!");
                    running = false;
                    break;
                case -1:
                    state.restart();
                    clearScreen();
                    printWelcome();
                    printInstructions();
                    displayMessage("Game restarted!");
                    break;
                case 0:
                    if (state.undo())
                        displayMessage("Move undone.");
                    break;
                default:
                    if (input >= 1 && input <= state.getColumns()) {
                        if (state.move(input)) {
                            if (state.getGameOver()) {
                                update();
                                promptPlayAgain();
                            }
                        }
                    }
                    break;
            }
        }
    }

    private void promptPlayAgain() {
        System.out.println();
        System.out.print("Play again? (Y/N): ");
        String input = scanner.nextLine().trim().toUpperCase();

        if (input.equals("Y") || input.equals("YES")) {
            state.restart();
            clearScreen();
            printWelcome();
            printInstructions();
            displayMessage("New game started!");
        } else {
            displayMessage("Thanks for playing Connect Four!");
            running = false;
        }
    }

    @Override
    public void cleanup() {
        scanner.close();
        running = false;
    }

    public void setGameState(GameState newState) {
        this.state = newState;
        setupAIIfNeeded();
    }
}

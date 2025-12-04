package src.main.java.com.connect4.view;

import java.awt.Point;
import java.util.Stack;

import src.main.java.com.connect4.settings.GameSettings;
import src.main.java.com.connect4.player.Player;

import java.util.Random;
import java.util.ArrayList;

/**
 * GameState.java - MODIFIED CLASS (Extended Features)
 * This class manages the game state for Connect Four.
 * EXTENDED MODIFICATIONS:
 * - Support for variable board sizes (based on difficulty level)
 * - Player management (two humans or human vs computer)
 * - Integration with GameSettings for configuration
 * - Four corners win condition (for square boards only)
 * - Lucky coin offer/accept/reject mechanism
 * - Deep copy for AI simulation
 * - Methods for save/load support
 * 
 * @author Original + Refactored for multi-view support + Extended features
 */
public class GameState {

    private final int columns;
    private final int rows;

    private Cell[][] cells;
    private Stack<Point> moves;
    private String error;
    private boolean gameOver;
    private boolean player1Wins;
    private boolean player2Wins;
    private boolean player1Turn;

    private final GameSettings settings;
    private final Random random;
    private static final double LUCKY_COIN_OFFER_CHANCE = 0.15;

    private Stack<Boolean> wasLuckyCoin;

    private boolean luckyOfferPending;
    private int luckyOfferColumn;
    private int luckyOfferRow;

    private boolean isSimulation;

    public GameState() {
        this(new GameSettings());
    }

    public GameState(GameSettings settings) {
        this.settings = settings;
        this.columns = settings.getColumns();
        this.rows = settings.getRows();
        this.random = new Random();
        this.isSimulation = false;

        initializeBoard();
    }

    /**
     * Copy constructor for deep copying.
     */
    public GameState(GameState other) {
        this.settings = other.settings; // Settings are immutable-ish for the game duration
        this.columns = other.columns;
        this.rows = other.rows;
        this.random = new Random(); // New random to avoid coupling
        this.isSimulation = true; // Mark as simulation to prevent stats updates

        // Deep copy cells
        this.cells = new Cell[columns][rows];
        for (int i = 0; i < columns; i++) {
            for (int j = 0; j < rows; j++) {
                this.cells[i][j] = new Cell();
                this.cells[i][j].setState(other.cells[i][j].getState());
            }
        }

        this.gameOver = other.gameOver;
        this.player1Wins = other.player1Wins;
        this.player2Wins = other.player2Wins;
        this.player1Turn = other.player1Turn;

        // Deep copy stacks
        this.moves = new Stack<>();
        for (Point p : other.moves) {
            this.moves.push(new Point((int) p.getX(), (int) p.getY()));
        }

        this.wasLuckyCoin = new Stack<>();
        for (Boolean b : other.wasLuckyCoin) {
            this.wasLuckyCoin.push(b);
        }

        this.luckyOfferPending = other.luckyOfferPending;
        this.luckyOfferColumn = other.luckyOfferColumn;
        this.luckyOfferRow = other.luckyOfferRow;
        this.error = other.error;
    }

    private void initializeBoard() {
        cells = new Cell[columns][rows];
        for (int i = 0; i < columns; i++) {
            for (int j = 0; j < rows; j++) {
                cells[i][j] = new Cell();
            }
        }

        gameOver = false;
        player1Wins = false;
        player2Wins = false;
        player1Turn = true;
        moves = new Stack<>();
        wasLuckyCoin = new Stack<>();
        error = null;
        settings.resetLuckyCoins();

        luckyOfferPending = false;
        luckyOfferColumn = -1;
        luckyOfferRow = -1;
    }

    public boolean move(int column) {
        error = null;

        if (luckyOfferPending) {
            error = "A lucky coin offer is pending. Accept or reject it first.";
            return false;
        }

        if (gameOver) {
            error = "The game is over.";
            return false;
        }

        if (column < 1 || column > columns) {
            error = "Invalid column. Please enter 1-" + columns + ".";
            return false;
        }

        int col = column - 1;

        if (!cells[col][rows - 1].isAvailable()) {
            error = "That column is full.";
            return false;
        }

        int row = 0;
        while (!cells[col][row].isAvailable()) {
            row++;
        }

        Player currentPlayer = getCurrentPlayer();
        setColor(col, row, currentPlayer);
        wasLuckyCoin.push(false);

        moves.push(new Point(col, row));
        player1Turn = !player1Turn;
        checkForWin();

        if (!gameOver) {
            tryGenerateLuckyOffer();
        }

        return true;
    }

    private void setColor(int col, int row, Player currentPlayer) {
        switch (currentPlayer.getCoinColor()) {
            case RED:
                cells[col][row].setRed();
                break;
            case YELLOW:
                cells[col][row].setYellow();
                break;
            case BLUE:
                cells[col][row].setBlue();
                break;
            case GREEN:
                cells[col][row].setGreen();
                break;
            case PURPLE:
                cells[col][row].setPurple();
                break;
            case ORANGE:
                cells[col][row].setOrange();
                break;
        }
    }

    private void tryGenerateLuckyOffer() {
        if (!settings.canGenerateLuckyCoin()) {
            return;
        }

        if (random.nextDouble() >= LUCKY_COIN_OFFER_CHANCE) {
            return;
        }

        ArrayList<int[]> validPositions = new ArrayList<>();
        for (int col = 0; col < columns; col++) {
            int row = 0;
            while (row < rows && !cells[col][row].isAvailable()) {
                row++;
            }
            if (row < rows) {
                validPositions.add(new int[] { col, row });
            }
        }

        if (validPositions.isEmpty()) {
            return;
        }

        int[] selected = validPositions.get(random.nextInt(validPositions.size()));
        luckyOfferColumn = selected[0];
        luckyOfferRow = selected[1];
        luckyOfferPending = true;

        cells[luckyOfferColumn][luckyOfferRow].setLucky();
    }

    public boolean acceptLuckyOffer() {
        error = null;

        if (!luckyOfferPending) {
            error = "No lucky coin offer pending.";
            return false;
        }

        // The lucky coin belongs to the player who just moved (triggered the offer).
        // Since move() switched the turn, we need the *previous* player.
        // If it's currently P1's turn, P2 was the last to move.
        // If it's currently P2's turn, P1 was the last to move.
        Player luckyPlayer = player1Turn ? settings.getPlayer2() : settings.getPlayer1();

        setColor(luckyOfferColumn, luckyOfferRow, luckyPlayer);

        settings.incrementLuckyCoins();
        wasLuckyCoin.push(true);
        moves.push(new Point(luckyOfferColumn, luckyOfferRow));

        luckyOfferPending = false;
        luckyOfferColumn = -1;
        luckyOfferRow = -1;

        player1Turn = !player1Turn;
        checkForWin();

        return true;
    }

    public boolean rejectLuckyOffer() {
        error = null;

        if (!luckyOfferPending) {
            error = "No lucky coin offer pending.";
            return false;
        }

        cells[luckyOfferColumn][luckyOfferRow].clear();

        luckyOfferPending = false;
        luckyOfferColumn = -1;
        luckyOfferRow = -1;

        return true;
    }

    public boolean isLuckyOfferPending() {
        return luckyOfferPending;
    }

    public int getLuckyOfferColumn() {
        return luckyOfferColumn;
    }

    public int getLuckyOfferRow() {
        return luckyOfferRow;
    }

    public String getLuckyOfferDescription() {
        if (!luckyOfferPending)
            return null;
        return String.format("Lucky coin offered at column %d, row %d",
                luckyOfferColumn + 1, luckyOfferRow + 1);
    }

    /**
     * Gets the player who should accept/reject the lucky coin offer.
     * This is the player who triggered the offer (made the last move).
     * 
     * @return the player who owns the lucky coin offer, or null if no offer pending
     */
    public Player getLuckyOfferPlayer() {
        if (!luckyOfferPending)
            return null;
        // The lucky coin belongs to the player who just moved (triggered the offer).
        // Since move() switched the turn, we need the *previous* player.
        return player1Turn ? settings.getPlayer2() : settings.getPlayer1();
    }

    public void moveInternal(int column) {
        error = null;

        if (gameOver || column < 1 || column > columns) {
            return;
        }

        int col = column - 1;
        if (!cells[col][rows - 1].isAvailable()) {
            return;
        }

        int row = 0;
        while (!cells[col][row].isAvailable()) {
            row++;
        }

        Player currentPlayer = getCurrentPlayer();
        if (currentPlayer.getCoinColor() == Player.CoinColor.RED) {
            cells[col][row].setRed();
        } else {
            cells[col][row].setYellow();
        }
        wasLuckyCoin.push(false);

        moves.push(new Point(col, row));
        player1Turn = !player1Turn;
        checkForWin();

    }

    public boolean undo() {
        error = null;

        // If a lucky offer is pending, we must clear it first
        if (luckyOfferPending) {
            rejectLuckyOffer();
            // Do not return here, continue to undo the previous move that triggered this
            // offer
        }

        if (moves.empty()) {
            error = "No moves to undo.";
            return false;
        }

        Point lastMove = moves.pop();

        // Check if the move we are undoing was a lucky coin acceptance
        // or a regular move.
        if (!wasLuckyCoin.empty()) {
            boolean wasLucky = wasLuckyCoin.pop();
            if (wasLucky) {
                settings.decrementLuckyCoins();
            }
        }

        cells[(int) lastMove.getX()][(int) lastMove.getY()].clear();
        player1Turn = !player1Turn;

        if (gameOver) {
            gameOver = false;
            player1Wins = false;
            player2Wins = false;
        }

        return true;
    }

    public void restart() {
        initializeBoard();
    }

    private void checkForWin() {
        gameOver = false;
        player1Wins = false;
        player2Wins = false;

        Player.CoinColor p1Color = settings.getPlayer1().getCoinColor();
        Player.CoinColor p2Color = settings.getPlayer2().getCoinColor();

        if (checkWinForColor(p1Color)) {
            gameOver = true;
            player1Wins = true;
            if (!isSimulation) {
                settings.getPlayer1().recordWin();
                settings.getPlayer2().recordLoss();
            }
        } else if (checkWinForColor(p2Color)) {
            gameOver = true;
            player2Wins = true;
            if (!isSimulation) {
                settings.getPlayer2().recordWin();
                settings.getPlayer1().recordLoss();
            }
        } else if (settings.isFourCornersEnabled()) {
            // System.out.println("DEBUG: Checking square win...");
            int cornerWinner = checkSquareWin();
            if (cornerWinner == 1) {
                // System.out.println("DEBUG: Square win for P1 detected!");
                gameOver = true;
                player1Wins = true;
                if (!isSimulation) {
                    settings.getPlayer1().recordWin();
                    settings.getPlayer2().recordLoss();
                }
            } else if (cornerWinner == 2) {
                // System.out.println("DEBUG: Square win for P2 detected!");
                gameOver = true;
                player2Wins = true;
                if (!isSimulation) {
                    settings.getPlayer2().recordWin();
                    settings.getPlayer1().recordLoss();
                }
            }
        } else {
            // System.out.println("DEBUG: Four corners NOT enabled.");
        }

        if (!gameOver) {
            boolean isFull = true;
            for (int i = 0; i < columns; i++) {
                if (cells[i][rows - 1].isAvailable()) {
                    isFull = false;
                    break;
                }
            }
            if (isFull) {
                gameOver = true;
                if (!isSimulation) {
                    settings.getPlayer1().recordTie();
                    settings.getPlayer2().recordTie();
                }
            }
        }
    }

    private int checkSquareWin() {
        Player.CoinColor p1Color = settings.getPlayer1().getCoinColor();
        Player.CoinColor p2Color = settings.getPlayer2().getCoinColor();

        // Check every possible square on the board
        // Minimum square size is 2x2
        for (int size = 1; size < Math.min(columns, rows); size++) {
            for (int col = 0; col < columns - size; col++) {
                for (int row = 0; row < rows - size; row++) {
                    // Corners of the square
                    Cell c1 = cells[col][row];
                    Cell c2 = cells[col + size][row];
                    Cell c3 = cells[col][row + size];
                    Cell c4 = cells[col + size][row + size];

                    if (c1.isAvailable() || c2.isAvailable() || c3.isAvailable() || c4.isAvailable()) {
                        continue;
                    }

                    if (matchesColor(c1, p1Color) && matchesColor(c2, p1Color) &&
                            matchesColor(c3, p1Color) && matchesColor(c4, p1Color)) {
                        // System.out.println("DEBUG: Found square for P1 at " + col + "," + row + "
                        // size " + size);
                        return 1;
                    }

                    if (matchesColor(c1, p2Color) && matchesColor(c2, p2Color) &&
                            matchesColor(c3, p2Color) && matchesColor(c4, p2Color)) {
                        return 2;
                    }
                }
            }
        }
        return 0;
    }

    private boolean checkWinForColor(Player.CoinColor color) {
        // Horizontal
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col <= columns - 4; col++) {
                if (checkLine(col, row, 1, 0, color))
                    return true;
            }
        }

        // Vertical
        for (int col = 0; col < columns; col++) {
            for (int row = 0; row <= rows - 4; row++) {
                if (checkLine(col, row, 0, 1, color))
                    return true;
            }
        }

        // Diagonal /
        for (int col = 0; col <= columns - 4; col++) {
            for (int row = 0; row <= rows - 4; row++) {
                if (checkLine(col, row, 1, 1, color))
                    return true;
            }
        }

        // Diagonal \
        for (int col = 0; col <= columns - 4; col++) {
            for (int row = 3; row < rows; row++) {
                if (checkLine(col, row, 1, -1, color))
                    return true;
            }
        }

        return false;
    }

    private boolean checkLine(int startCol, int startRow, int dCol, int dRow, Player.CoinColor color) {
        for (int i = 0; i < 4; i++) {
            if (!matchesColor(cells[startCol + i * dCol][startRow + i * dRow], color)) {
                return false;
            }
        }
        return true;
    }

    private boolean matchesColor(Cell cell, Player.CoinColor color) {
        if (cell.isLucky()) {
            return true;
        }

        return switch (color) {
            case RED -> cell.isRed();
            case YELLOW -> cell.isYellow();
            case BLUE -> cell.isBlue();
            case GREEN -> cell.isGreen();
            case PURPLE -> cell.isPurple();
            case ORANGE -> cell.isOrange();
            default -> false;
        };
    }

    public boolean isValidMove(int column) {
        if (luckyOfferPending || gameOver)
            return false;
        if (column < 1 || column > columns)
            return false;
        return cells[column - 1][rows - 1].isAvailable();
    }

    public Player getCurrentPlayer() {
        return player1Turn ? settings.getPlayer1() : settings.getPlayer2();
    }

    public String getStatusMessage() {
        if (gameOver) {
            if (player1Wins) {
                String winType = settings.getDifficultyLevel().isFourCornersEnabled() ? " (Four Corners!)" : "";
                return settings.getPlayer1().getName() + " wins!" + winType;
            } else if (player2Wins) {
                String winType = settings.getDifficultyLevel().isFourCornersEnabled() ? " (Four Corners!)" : "";
                return settings.getPlayer2().getName() + " wins!" + winType;
            } else {
                return "It's a tie!";
            }
        }

        if (luckyOfferPending) {
            // The offer is for the player who just moved (previous player)
            Player luckyPlayer = player1Turn ? settings.getPlayer2() : settings.getPlayer1();
            return luckyPlayer.getName() + ": Lucky coin at col " +
                    (luckyOfferColumn + 1) + "! Accept (A) or Reject (R)?";
        }

        return getCurrentPlayer().getName() + "'s turn";
    }

    public String getStatsDisplay() {
        return settings.getPlayer1().getName() + ": " + settings.getPlayer1().getGamesWon() +
                " wins | " +
                settings.getPlayer2().getName() + ": " + settings.getPlayer2().getGamesWon() +
                " wins";
    }

    public GameState deepCopy() {
        return new GameState(this);
    }

    public int getColumns() {
        return columns;
    }

    public int getRows() {
        return rows;
    }

    public Cell[][] getCells() {
        return cells;
    }

    public Stack<Point> getMoves() {
        return moves;
    }

    public String getError() {
        return error;
    }

    public boolean getGameOver() {
        return gameOver;
    }

    public boolean getPlayer1Wins() {
        return player1Wins;
    }

    public boolean getPlayer2Wins() {
        return player2Wins;
    }

    public boolean isPlayer1Turn() {
        return player1Turn;
    }

    public GameSettings getSettings() {
        return settings;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public void setPlayer1Wins(boolean player1Wins) {
        this.player1Wins = player1Wins;
    }

    public void setPlayer2Wins(boolean player2Wins) {
        this.player2Wins = player2Wins;
    }

    public void setPlayer1Turn(boolean player1Turn) {
        this.player1Turn = player1Turn;
    }

    public void setLuckyOfferState(boolean pending, int col, int row) {
        this.luckyOfferPending = pending;
        this.luckyOfferColumn = col;
        this.luckyOfferRow = row;
    }

    @Override
    public String toString() {
        String res = "";
        for (int row = 0; row < cells.length; row++) {
            res += "\n";
            for (int col = 0; col < cells[row].length; col++) {
                res += cells[row][col].toString();
            }
        }
        return res;
    }
}

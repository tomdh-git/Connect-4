import java.awt.Point;
import java.util.Stack;
import java.util.Random;
import java.util.ArrayList;

/**
 * GameState.java - MODIFIED CLASS (Extended Features)
 * 
 * This class manages the game state for Connect Four.
 * 
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
    
    private int columns;
    private int rows;
    
    private Cell[][] cells;
    private Stack<Point> moves;
    private String error;
    private boolean gameOver;
    private boolean player1Wins;
    private boolean player2Wins;
    private boolean player1Turn;
    
    private GameSettings settings;
    private Random random;
    private static final double LUCKY_COIN_OFFER_CHANCE = 0.15;
    
    private Stack<Boolean> wasLuckyCoin;
    
    private boolean luckyOfferPending;
    private int luckyOfferColumn;
    private int luckyOfferRow;
    
    public GameState() {
        this(new GameSettings());
    }
    
    public GameState(GameSettings settings) {
        this.settings = settings;
        this.columns = settings.getColumns();
        this.rows = settings.getRows();
        this.random = new Random();
        
        initializeBoard();
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
        if (currentPlayer.getCoinColor() == Player.CoinColor.RED) {
            cells[col][row].setRed();
        } else {
            cells[col][row].setYellow();
        }
        wasLuckyCoin.push(false);
        
        moves.push(new Point(col, row));
        player1Turn = !player1Turn;
        checkForWin();
        
        if (!gameOver) {
            tryGenerateLuckyOffer();
        }
        
        return true;
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
                validPositions.add(new int[]{col, row});
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
        
        Player currentPlayer = getCurrentPlayer();
        if (currentPlayer.getCoinColor() == Player.CoinColor.RED) {
            cells[luckyOfferColumn][luckyOfferRow].setRed();
        } else {
            cells[luckyOfferColumn][luckyOfferRow].setYellow();
        }
        
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
        if (!luckyOfferPending) return null;
        return String.format("Lucky coin offered at column %d, row %d", 
            luckyOfferColumn + 1, luckyOfferRow + 1);
    }
    
    public boolean moveInternal(int column) {
        error = null;
        
        if (gameOver || column < 1 || column > columns) {
            return false;
        }
        
        int col = column - 1;
        if (!cells[col][rows - 1].isAvailable()) {
            return false;
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
        
        return true;
    }
    
    public boolean undo() {
        error = null;
        
        if (luckyOfferPending) {
            rejectLuckyOffer();
        }
        
        if (moves.empty()) {
            error = "No moves to undo.";
            return false;
        }
        
        Point lastMove = moves.pop();
        
        if (!wasLuckyCoin.empty() && wasLuckyCoin.pop()) {
            settings.decrementLuckyCoins();
        }
        
        cells[(int)lastMove.getX()][(int)lastMove.getY()].clear();
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
        
        if (fourInARow(p1Color)) {
            gameOver = true;
            player1Wins = true;
            settings.getPlayer1().recordWin();
            settings.getPlayer2().recordLoss();
        } else if (fourInARow(p2Color)) {
            gameOver = true;
            player2Wins = true;
            settings.getPlayer2().recordWin();
            settings.getPlayer1().recordLoss();
        } else if (settings.getDifficultyLevel().isFourCornersEnabled()) {
            int cornerWinner = checkFourCorners();
            if (cornerWinner == 1) {
                gameOver = true;
                player1Wins = true;
                settings.getPlayer1().recordWin();
                settings.getPlayer2().recordLoss();
            } else if (cornerWinner == 2) {
                gameOver = true;
                player2Wins = true;
                settings.getPlayer2().recordWin();
                settings.getPlayer1().recordLoss();
            }
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
                settings.getPlayer1().recordTie();
                settings.getPlayer2().recordTie();
            }
        }
    }
    
    private int checkFourCorners() {
        Cell bottomLeft = cells[0][0];
        Cell bottomRight = cells[columns - 1][0];
        Cell topLeft = cells[0][rows - 1];
        Cell topRight = cells[columns - 1][rows - 1];
        
        if (bottomLeft.isAvailable() || bottomRight.isAvailable() ||
            topLeft.isAvailable() || topRight.isAvailable()) {
            return 0;
        }
        
        Player.CoinColor p1Color = settings.getPlayer1().getCoinColor();
        Player.CoinColor p2Color = settings.getPlayer2().getCoinColor();
        
        if (matchesColor(bottomLeft, p1Color) && matchesColor(bottomRight, p1Color) &&
            matchesColor(topLeft, p1Color) && matchesColor(topRight, p1Color)) {
            return 1;
        }
        
        if (matchesColor(bottomLeft, p2Color) && matchesColor(bottomRight, p2Color) &&
            matchesColor(topLeft, p2Color) && matchesColor(topRight, p2Color)) {
            return 2;
        }
        
        return 0;
    }
    
    private boolean fourInARow(Player.CoinColor color) {
        int winLength = 4;
        
        for (int col = 0; col < columns; col++) {
            int count = 0;
            for (int row = 0; row < rows; row++) {
                if (cells[col][row].isAvailable()) break;
                if (matchesColor(cells[col][row], color)) {
                    count++;
                    if (count >= winLength) return true;
                } else {
                    count = 0;
                }
            }
        }
        
        for (int row = 0; row < rows; row++) {
            int count = 0;
            for (int col = 0; col < columns; col++) {
                if (!cells[col][row].isAvailable() && matchesColor(cells[col][row], color)) {
                    count++;
                    if (count >= winLength) return true;
                } else {
                    count = 0;
                }
            }
        }
        
        for (int startCol = 0; startCol <= columns - winLength; startCol++) {
            for (int startRow = 0; startRow <= rows - winLength; startRow++) {
                int count = 0;
                for (int i = 0; i < winLength; i++) {
                    if (!cells[startCol + i][startRow + i].isAvailable() &&
                        matchesColor(cells[startCol + i][startRow + i], color)) {
                        count++;
                    } else {
                        break;
                    }
                }
                if (count >= winLength) return true;
            }
        }
        
        for (int startCol = 0; startCol <= columns - winLength; startCol++) {
            for (int startRow = winLength - 1; startRow < rows; startRow++) {
                int count = 0;
                for (int i = 0; i < winLength; i++) {
                    if (!cells[startCol + i][startRow - i].isAvailable() &&
                        matchesColor(cells[startCol + i][startRow - i], color)) {
                        count++;
                    } else {
                        break;
                    }
                }
                if (count >= winLength) return true;
            }
        }
        
        return false;
    }
    
    private boolean matchesColor(Cell cell, Player.CoinColor color) {
        if (cell.isLucky()) {
            return true;
        }
        
        if (color == Player.CoinColor.RED) {
            return cell.isRed();
        } else if (color == Player.CoinColor.YELLOW) {
            return cell.isYellow();
        }
        return false;
    }
    
    public boolean isValidMove(int column) {
        if (luckyOfferPending || gameOver) return false;
        if (column < 1 || column > columns) return false;
        return cells[column - 1][rows - 1].isAvailable();
    }
    
    public Player getCurrentPlayer() {
        return player1Turn ? settings.getPlayer1() : settings.getPlayer2();
    }
    
    public String getStatusMessage() {
        if (gameOver) {
            if (player1Wins) {
                String winType = settings.getDifficultyLevel().isFourCornersEnabled() ? 
                    " (Four Corners!)" : "";
                return settings.getPlayer1().getName() + " wins!" + winType;
            } else if (player2Wins) {
                String winType = settings.getDifficultyLevel().isFourCornersEnabled() ? 
                    " (Four Corners!)" : "";
                return settings.getPlayer2().getName() + " wins!" + winType;
            } else {
                return "It's a tie!";
            }
        }
        
        if (luckyOfferPending) {
            return getCurrentPlayer().getName() + ": Lucky coin at col " + 
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
        GameState copy = new GameState(this.settings);
        
        for (int i = 0; i < columns; i++) {
            for (int j = 0; j < rows; j++) {
                copy.cells[i][j].setState(this.cells[i][j].getState());
            }
        }
        
        copy.gameOver = this.gameOver;
        copy.player1Wins = this.player1Wins;
        copy.player2Wins = this.player2Wins;
        copy.player1Turn = this.player1Turn;
        
        copy.moves.clear();
        for (Point p : this.moves) {
            copy.moves.push(new Point((int)p.getX(), (int)p.getY()));
        }
        
        copy.wasLuckyCoin.clear();
        for (Boolean b : this.wasLuckyCoin) {
            copy.wasLuckyCoin.push(b);
        }
        
        copy.luckyOfferPending = false;
        copy.luckyOfferColumn = -1;
        copy.luckyOfferRow = -1;
        
        return copy;
    }
    
    public int getColumns() { return columns; }
    public int getRows() { return rows; }
    public Cell[][] getCells() { return cells; }
    public Stack<Point> getMoves() { return moves; }
    public String getError() { return error; }
    public boolean getGameOver() { return gameOver; }
    public boolean getPlayer1Wins() { return player1Wins; }
    public boolean getPlayer2Wins() { return player2Wins; }
    public boolean isPlayer1Turn() { return player1Turn; }
    public GameSettings getSettings() { return settings; }
    
    public void setGameOver(boolean gameOver) { this.gameOver = gameOver; }
    public void setPlayer1Wins(boolean player1Wins) { this.player1Wins = player1Wins; }
    public void setPlayer2Wins(boolean player2Wins) { this.player2Wins = player2Wins; }
    public void setPlayer1Turn(boolean player1Turn) { this.player1Turn = player1Turn; }
    
    public void setLuckyOfferState(boolean pending, int col, int row) {
        this.luckyOfferPending = pending;
        this.luckyOfferColumn = col;
        this.luckyOfferRow = row;
    }
}

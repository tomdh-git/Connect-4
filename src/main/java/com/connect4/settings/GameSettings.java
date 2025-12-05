package com.connect4.settings;

import java.io.Serializable;

import com.connect4.player.Player;

/**
 * GameSettings.java - NEW CLASS
 * This class encapsulates all game configuration settings including:
 * - Game mode (two-player vs single-player against computer)
 * - Difficulty level (determines board size, lucky coins, AI complexity)
 * - Player configurations
 * GameSettings is created during the pre-game configuration phase and
 * passed to GameState to initialize the game appropriately.
 * 
 * @author Extended feature implementation
 */
public class GameSettings implements Serializable {

    private static final long serialVersionUID = 1L;

    public enum GameMode {
        TWO_PLAYER("Two Players", "Two human players compete against each other"),
        VS_COMPUTER("Vs Computer", "Single player competes against AI opponent");

        private final String displayName;
        private final String description;

        GameMode(String displayName, String description) {
            this.displayName = displayName;
            this.description = description;
        }

        public String getDisplayName() {
            return displayName;
        }

        public String getDescription() {
            return description;
        }

        @Override
        public String toString() {
            return displayName;
        }
    }

    private boolean fourCornersEnabled;
    private GameMode gameMode;
    private DifficultyLevel difficultyLevel;
    private Player player1;
    private Player player2;
    private int columns;
    private int rows;
    private int maxLuckyCoins;
    private int currentLuckyCoins;

    /**
     * Creates default game settings for two-player mode with standard board.
     */
    public GameSettings() {
        this.gameMode = GameMode.TWO_PLAYER;
        this.difficultyLevel = DifficultyLevel.BEGINNER;
        this.player1 = Player.createDefaultPlayer1();
        this.player2 = Player.createDefaultPlayer2();
        this.fourCornersEnabled = false;
        applyDifficultySettings();
    }

    /**
     * Creates game settings for playing against computer.
     * 
     * @param level           the difficulty level
     * @param humanPlayer     the human player
     * @param humanPlaysFirst true if human goes first
     */
    public GameSettings(DifficultyLevel level, Player humanPlayer, boolean humanPlaysFirst) {
        this(level, humanPlayer, humanPlaysFirst, false);
    }

    /**
     * Creates game settings for playing against computer with optional Four Corners
     * rule.
     * 
     * @param level              the difficulty level
     * @param humanPlayer        the human player
     * @param humanPlaysFirst    true if human goes first
     * @param fourCornersEnabled true if Four Corners rule is active
     */
    public GameSettings(DifficultyLevel level, Player humanPlayer, boolean humanPlaysFirst,
            boolean fourCornersEnabled) {
        this.gameMode = GameMode.VS_COMPUTER;
        this.difficultyLevel = level;
        this.fourCornersEnabled = fourCornersEnabled;

        // Determine computer's color
        Player.CoinColor computerColor = getOppositeColor(humanPlayer.getCoinColor());
        Player computerPlayer = Player.createComputerPlayer(computerColor);

        if (humanPlaysFirst) {
            humanPlayer.setId(1);
            computerPlayer.setId(2);
            this.player1 = humanPlayer;
            this.player2 = computerPlayer;
        } else {
            computerPlayer.setId(1);
            humanPlayer.setId(2);
            this.player1 = computerPlayer;
            this.player2 = humanPlayer;
        }

        applyDifficultySettings();
    }

    /**
     * Creates game settings for two-player mode with custom players.
     * 
     * @param player1 first player (goes first)
     * @param player2 second player
     */
    public GameSettings(Player player1, Player player2) {
        this(player1, player2, false);
    }

    /**
     * Creates game settings for two-player mode with custom players and optional
     * Four Corners rule.
     * 
     * @param player1            first player (goes first)
     * @param player2            second player
     * @param fourCornersEnabled true if Four Corners rule is active
     */
    public GameSettings(Player player1, Player player2, boolean fourCornersEnabled) {
        this.gameMode = GameMode.TWO_PLAYER;
        this.difficultyLevel = DifficultyLevel.BEGINNER;
        this.player1 = player1;
        this.player2 = player2;
        this.fourCornersEnabled = fourCornersEnabled;
        player1.setId(1);
        player2.setId(2);
        applyDifficultySettings();
    }

    /**
     * Applies board size and lucky coin settings from the difficulty level.
     */
    private void applyDifficultySettings() {
        this.columns = difficultyLevel.getColumns();
        this.rows = difficultyLevel.getRows();
        this.maxLuckyCoins = difficultyLevel.getMaxLuckyCoins();
        this.currentLuckyCoins = 0;
    }

    /**
     * Gets a color that's different from the given color.
     * 
     * @param color the color to avoid
     * @return a different color
     */
    private Player.CoinColor getOppositeColor(Player.CoinColor color) {
        if (color == Player.CoinColor.RED) {
            return Player.CoinColor.YELLOW;
        } else if (color == Player.CoinColor.YELLOW) {
            return Player.CoinColor.RED;
        } else {
            return Player.CoinColor.RED;
        }
    }

    /**
     * Checks if more lucky coins can be generated.
     * 
     * @return true if under the max limit
     */
    public boolean canGenerateLuckyCoin() {
        return currentLuckyCoins < maxLuckyCoins;
    }

    /**
     * Records that a lucky coin was generated.
     */
    public void incrementLuckyCoins() {
        if (currentLuckyCoins < maxLuckyCoins) {
            currentLuckyCoins++;
        }
    }

    /**
     * Records that a lucky coin was removed (undo).
     */
    public void decrementLuckyCoins() {
        if (currentLuckyCoins > 0) {
            currentLuckyCoins--;
        }
    }

    /**
     * Resets the lucky coin count to zero.
     */
    public void resetLuckyCoins() {
        currentLuckyCoins = 0;
    }

    /**
     * Checks if this is a game against the computer.
     * 
     * @return true if vs computer mode
     */
    public boolean isVsComputer() {
        return gameMode == GameMode.VS_COMPUTER;
    }

    /**
     * Checks if this is a two-player game.
     * 
     * @return true if two-player mode
     */
    public boolean isTwoPlayer() {
        return gameMode == GameMode.TWO_PLAYER;
    }

    /**
     * Gets the computer player if in vs computer mode.
     * 
     * @return the computer player, or null if two-player mode
     */
    public Player getComputerPlayer() {
        if (player1.isComputer())
            return player1;
        if (player2.isComputer())
            return player2;
        return null;
    }

    /**
     * Gets the human player in vs computer mode.
     * In two-player mode, returns player1.
     * 
     * @return the human player
     */
    public Player getHumanPlayer() {
        if (player1.isHuman())
            return player1;
        if (player2.isHuman())
            return player2;
        return player1;
    }

    /**
     * Gets a summary string of the current settings.
     * 
     * @return settings summary
     */
    public String getSettingsSummary() {
        StringBuilder sb = new StringBuilder();
        sb.append("Game Mode: ").append(gameMode.getDisplayName()).append("\n");
        sb.append("Board Size: ").append(columns).append(" x ").append(rows).append("\n");
        sb.append("Max Lucky Coins: ").append(maxLuckyCoins).append("\n");
        sb.append("Player 1: ").append(player1.getName())
                .append(" (").append(player1.getCoinColor()).append(")\n");
        sb.append("Player 2: ").append(player2.getName())
                .append(" (").append(player2.getCoinColor()).append(")");
        if (isVsComputer()) {
            sb.append("\nDifficulty: ").append(difficultyLevel.getDisplayName());
        }
        return sb.toString();
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    public void setGameMode(GameMode gameMode) {
        this.gameMode = gameMode;
    }

    public DifficultyLevel getDifficultyLevel() {
        return difficultyLevel;
    }

    public void setDifficultyLevel(DifficultyLevel level) {
        this.difficultyLevel = level;
        applyDifficultySettings();
    }

    public Player getPlayer1() {
        return player1;
    }

    public void setPlayer1(Player player1) {
        this.player1 = player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public void setPlayer2(Player player2) {
        this.player2 = player2;
    }

    public int getColumns() {
        return columns;
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getMaxLuckyCoins() {
        return maxLuckyCoins;
    }

    public void setMaxLuckyCoins(int maxLuckyCoins) {
        this.maxLuckyCoins = maxLuckyCoins;
    }

    public int getCurrentLuckyCoins() {
        return currentLuckyCoins;
    }

    public void setCurrentLuckyCoins(int currentLuckyCoins) {
        this.currentLuckyCoins = currentLuckyCoins;
    }

    public boolean isFourCornersEnabled() {
        return fourCornersEnabled;
    }

    public void setFourCornersEnabled(boolean fourCornersEnabled) {
        this.fourCornersEnabled = fourCornersEnabled;
    }
}


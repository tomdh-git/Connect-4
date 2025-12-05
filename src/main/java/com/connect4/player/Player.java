package com.connect4.player;

import java.io.Serializable;

/**
 * Player.java - NEW CLASS
 * This class represents a player in the Connect Four game.
 * It tracks player identity, type (human or AI), coin color preference,
 * and game statistics.
 * Supports both two-player mode and single-player vs computer mode.
 * 
 * @author Extended feature implementation
 */
public class Player implements Serializable {

    private static final long serialVersionUID = 1L;

    public enum PlayerType {
        HUMAN, // Human player controlled by user input
        COMPUTER // AI player controlled by computer algorithm
    }

    public enum CoinColor {
        RED("Red", "\u001B[31m"),
        YELLOW("Yellow", "\u001B[33m"),
        BLUE("Blue", "\u001B[34m"),
        GREEN("Green", "\u001B[32m"),
        PURPLE("Purple", "\u001B[35m"),
        ORANGE("Orange", "\u001B[38;5;208m");

        private final String displayName;
        private final String ansiCode;

        CoinColor(String displayName, String ansiCode) {
            this.displayName = displayName;
            this.ansiCode = ansiCode;
        }

        public String getDisplayName() {
            return displayName;
        }

        public String getAnsiCode() {
            return ansiCode;
        }

        @Override
        public String toString() {
            return displayName;
        }
    }

    private int id; // Unique player identifier (1 or 2)
    private String name; // Player's display name
    private PlayerType type; // Human or Computer
    private CoinColor coinColor; // Selected coin color
    private int gamesWon; // Total games won
    private int gamesPlayed; // Total games played

    /**
     * Creates a new player with specified attributes.
     * 
     * @param id        unique player ID (1 or 2)
     * @param name      player's display name
     * @param type      HUMAN or COMPUTER
     * @param coinColor selected coin color
     */
    public Player(int id, String name, PlayerType type, CoinColor coinColor) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.coinColor = coinColor;
        this.gamesWon = 0;
        this.gamesPlayed = 0;
    }

    /**
     * Creates a human player with default settings.
     * 
     * @param id        unique player ID
     * @param name      player's display name
     * @param coinColor selected coin color
     */
    public Player(int id, String name, CoinColor coinColor) {
        this(id, name, PlayerType.HUMAN, coinColor);
    }

    /**
     * Creates a default Player 1 with red coins.
     * 
     * @return default Player 1
     */
    public static Player createDefaultPlayer1() {
        return new Player(1, "Player 1", PlayerType.HUMAN, CoinColor.RED);
    }

    /**
     * Creates a default Player 2 with yellow coins.
     * 
     * @return default Player 2
     */
    public static Player createDefaultPlayer2() {
        return new Player(2, "Player 2", PlayerType.HUMAN, CoinColor.YELLOW);
    }

    /**
     * Creates a computer player with specified color.
     * 
     * @param coinColor the computer's coin color
     * @return computer player instance
     */
    public static Player createComputerPlayer(CoinColor coinColor) {
        return new Player(2, "Computer", PlayerType.COMPUTER, coinColor);
    }

    /**
     * Records a game win for this player.
     */
    public void recordWin() {
        gamesWon++;
        gamesPlayed++;
    }

    /**
     * Records a game loss for this player.
     */
    public void recordLoss() {
        gamesPlayed++;
    }

    /**
     * Records a tied game for this player.
     */
    public void recordTie() {
        gamesPlayed++;
    }

    /**
     * Resets all game statistics to zero.
     */
    public void resetStats() {
        gamesWon = 0;
        gamesPlayed = 0;
    }

    /**
     * Checks if this player is human-controlled.
     * 
     * @return true if human, false if computer
     */
    public boolean isHuman() {
        return type == PlayerType.HUMAN;
    }

    /**
     * Checks if this player is computer-controlled.
     * 
     * @return true if computer, false if human
     */
    public boolean isComputer() {
        return type == PlayerType.COMPUTER;
    }

    /**
     * Calculates the win percentage.
     * 
     * @return win percentage (0-100), or 0 if no games played
     */
    public double getWinPercentage() {
        if (gamesPlayed == 0)
            return 0.0;
        return (double) gamesWon / gamesPlayed * 100.0;
    }

    /**
     * Gets a formatted string of player statistics.
     * 
     * @return statistics string
     */
    public String getStatsString() {
        return String.format("%s: %d wins / %d games (%.1f%%)",
                name, gamesWon, gamesPlayed, getWinPercentage());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PlayerType getType() {
        return type;
    }

    public void setType(PlayerType type) {
        this.type = type;
    }

    public CoinColor getCoinColor() {
        return coinColor;
    }

    public void setCoinColor(CoinColor coinColor) {
        this.coinColor = coinColor;
    }

    public int getGamesWon() {
        return gamesWon;
    }

    public void setGamesWon(int gamesWon) {
        this.gamesWon = gamesWon;
    }

    public int getGamesPlayed() {
        return gamesPlayed;
    }

    public void setGamesPlayed(int gamesPlayed) {
        this.gamesPlayed = gamesPlayed;
    }

    @Override
    public String toString() {
        return String.format("Player[id=%d, name=%s, type=%s, color=%s]",
                id, name, type, coinColor);
    }
}


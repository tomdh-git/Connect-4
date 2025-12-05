package com.connect4.settings;

import java.io.Serializable;

/**
 * DifficultyLevel.java - MODIFIED CLASS
 * This enum defines the difficulty levels for playing against the computer.
 * Each level specifies:
 * - Board dimensions (columns x rows)
 * - Maximum number of lucky coins allowed
 * - AI search depth for min/max algorithm
 * DIFFICULTY LEVELS (Rectangular - Standard):
 * - BEGINNER: 7x6 board, max 3 lucky coins, shallow AI (depth 2)
 * - INTERMEDIATE: 14x12 board, max 7 lucky coins, moderate AI (depth 4)
 * - EXPERT: 21x18 board, max 11 lucky coins, deep AI (depth 6)
 * DIFFICULTY LEVELS (Square - Four Corners mode enabled):
 * - BEGINNER_SQUARE: 7x7 board, max 3 lucky coins, shallow AI (depth 2)
 * - INTERMEDIATE_SQUARE: 12x12 board, max 7 lucky coins, moderate AI (depth 4)
 * - EXPERT_SQUARE: 18x18 board, max 11 lucky coins, deep AI (depth 6)
 * 
 * @author Extended feature implementation
 */
public enum DifficultyLevel implements Serializable {

    BEGINNER(
            "Beginner",
            7, // columns
            6, // rows
            3, // max lucky coins
            2, // AI search depth
            4// win condition (4 in a row)
    ),

    INTERMEDIATE(
            "Intermediate",
            14, // columns
            12, // rows
            7, // max lucky coins
            4, // AI search depth
            4// win condition (4 in a row)
    ),

    EXPERT(
            "Expert",
            21, // columns
            18, // rows
            11, // max lucky coins
            4, // AI search depth (reduced from 6 for performance)
            4// win condition (4 in a row)
    );

    private final String displayName;
    private final int columns;
    private final int rows;
    private final int maxLuckyCoins;
    private final int aiSearchDepth;
    private final int winCondition;

    DifficultyLevel(String displayName, int columns, int rows,
            int maxLuckyCoins, int aiSearchDepth, int winCondition) {
        this.displayName = displayName;
        this.columns = columns;
        this.rows = rows;
        this.maxLuckyCoins = maxLuckyCoins;
        this.aiSearchDepth = aiSearchDepth;
        this.winCondition = winCondition;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getColumns() {
        return columns;
    }

    public int getRows() {
        return rows;
    }

    public int getMaxLuckyCoins() {
        return maxLuckyCoins;
    }

    public int getAiSearchDepth() {
        return aiSearchDepth;
    }

    public int getWinCondition() {
        return winCondition;
    }

    public boolean isFourCornersEnabled() {
        return false; // Deprecated in DifficultyLevel, moved to GameSettings
    }

    public String getBoardSizeString() {
        return columns + " x " + rows;
    }

    public String getDescription() {
        return String.format("%s: %dx%d board, max %d lucky coins, AI depth %d",
                displayName, columns, rows, maxLuckyCoins, aiSearchDepth);
    }

    @Override
    public String toString() {
        return displayName;
    }

    public static DifficultyLevel fromLevel(int level) {
        return switch (level) {
            case 1 -> BEGINNER;
            case 2 -> INTERMEDIATE;
            case 3 -> EXPERT;
            default -> BEGINNER;
        };
    }

    public static String[] getAllDescriptions() {
        DifficultyLevel[] levels = values();
        String[] descriptions = new String[levels.length];
        for (int i = 0; i < levels.length; i++) {
            descriptions[i] = (i + 1) + ". " + levels[i].getDescription();
        }
        return descriptions;
    }
}


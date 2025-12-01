package com.connect4.settings;

import java.io.Serializable;

/**
 * DifficultyLevel.java - MODIFIED CLASS (Extended Features)
 * 
 * This enum defines the difficulty levels for playing against the computer.
 * Each level specifies:
 * - Board dimensions (columns x rows)
 * - Maximum number of lucky coins allowed
 * - AI search depth for min/max algorithm
 * 
 * DIFFICULTY LEVELS (Rectangular - Standard):
 * - BEGINNER: 7x6 board, max 3 lucky coins, shallow AI (depth 2)
 * - INTERMEDIATE: 14x12 board, max 7 lucky coins, moderate AI (depth 4)
 * - EXPERT: 21x18 board, max 11 lucky coins, deep AI (depth 6)
 * 
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
            4, // win condition (4 in a row)
            false// not square
    ),

    INTERMEDIATE(
            "Intermediate",
            14, // columns
            12, // rows
            7, // max lucky coins
            4, // AI search depth
            4, // win condition (4 in a row)
            false// not square
    ),

    EXPERT(
            "Expert",
            21, // columns
            18, // rows
            11, // max lucky coins
            4, // AI search depth (reduced from 6 for performance)
            4, // win condition (4 in a row)
            false// not square
    ),

    BEGINNER_SQUARE(
            "Beginner (Square)",
            7, // columns
            7, // rows (square!)
            3, // max lucky coins
            2, // AI search depth
            4, // win condition (4 in a row)
            true// square - four corners enabled
    ),

    INTERMEDIATE_SQUARE(
            "Intermediate (Square)",
            12, // columns
            12, // rows (square!)
            7, // max lucky coins
            4, // AI search depth
            4, // win condition (4 in a row)
            true// square - four corners enabled
    ),

    EXPERT_SQUARE(
            "Expert (Square)",
            18, // columns
            18, // rows (square!)
            11, // max lucky coins
            4, // AI search depth (reduced from 6 for performance)
            4, // win condition (4 in a row)
            true// square - four corners enabled
    );

    private final String displayName;
    private final int columns;
    private final int rows;
    private final int maxLuckyCoins;
    private final int aiSearchDepth;
    private final int winCondition;
    private final boolean isSquare;

    DifficultyLevel(String displayName, int columns, int rows,
            int maxLuckyCoins, int aiSearchDepth, int winCondition,
            boolean isSquare) {
        this.displayName = displayName;
        this.columns = columns;
        this.rows = rows;
        this.maxLuckyCoins = maxLuckyCoins;
        this.aiSearchDepth = aiSearchDepth;
        this.winCondition = winCondition;
        this.isSquare = isSquare;
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

    public boolean isSquare() {
        return isSquare;
    }

    public boolean isFourCornersEnabled() {
        return isSquare;
    }

    public String getBoardSizeString() {
        return columns + " x " + rows + (isSquare ? " (Square)" : "");
    }

    public String getDescription() {
        String corners = isSquare ? ", 4-corners win" : "";
        return String.format("%s: %dx%d board, max %d lucky coins, AI depth %d%s",
                displayName, columns, rows, maxLuckyCoins, aiSearchDepth, corners);
    }

    @Override
    public String toString() {
        return displayName;
    }

    public static DifficultyLevel fromLevel(int level) {
        switch (level) {
            case 1:
                return BEGINNER;
            case 2:
                return INTERMEDIATE;
            case 3:
                return EXPERT;
            case 4:
                return BEGINNER_SQUARE;
            case 5:
                return INTERMEDIATE_SQUARE;
            case 6:
                return EXPERT_SQUARE;
            default:
                return BEGINNER;
        }
    }

    public static String[] getAllDescriptions() {
        DifficultyLevel[] levels = values();
        String[] descriptions = new String[levels.length];
        for (int i = 0; i < levels.length; i++) {
            descriptions[i] = (i + 1) + ". " + levels[i].getDescription();
        }
        return descriptions;
    }

    public static String[] getRectangularDescriptions() {
        return new String[] {
                "1. " + BEGINNER.getDescription(),
                "2. " + INTERMEDIATE.getDescription(),
                "3. " + EXPERT.getDescription()
        };
    }

    public static String[] getSquareDescriptions() {
        return new String[] {
                "1. " + BEGINNER_SQUARE.getDescription(),
                "2. " + INTERMEDIATE_SQUARE.getDescription(),
                "3. " + EXPERT_SQUARE.getDescription()
        };
    }

    public static DifficultyLevel fromSquareLevel(int level) {
        switch (level) {
            case 1:
                return BEGINNER_SQUARE;
            case 2:
                return INTERMEDIATE_SQUARE;
            case 3:
                return EXPERT_SQUARE;
            default:
                return BEGINNER_SQUARE;
        }
    }
}

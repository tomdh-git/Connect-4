package com.connect4;

import com.connect4.player.Player;
import com.connect4.settings.DifficultyLevel;
import com.connect4.settings.GameSettings;
import com.connect4.view.Cell;
import com.connect4.view.GameState;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * GridGenerationTest.java
 * 
 * Tests for grid/board generation based on difficulty levels.
 * Verifies that the correct board dimensions are created for each
 * difficulty level in both standard and square modes.
 */
public class GridGenerationTest {

    // ==================== STANDARD MODE TESTS ====================

    @Test
    public void testBeginnerStandardGrid() {
        DifficultyLevel level = DifficultyLevel.BEGINNER;
        Player p1 = new Player(1, "P1", Player.PlayerType.HUMAN, Player.CoinColor.RED);
        Player p2 = new Player(2, "P2", Player.PlayerType.HUMAN, Player.CoinColor.YELLOW);
        GameSettings settings = new GameSettings(p1, p2);
        settings.setDifficultyLevel(level);
        GameState state = new GameState(settings);

        assertEquals(7, state.getColumns(), "Beginner standard should be 7 columns");
        assertEquals(6, state.getRows(), "Beginner standard should be 6 rows");
        assertEquals(7, level.getColumns(), "DifficultyLevel should report 7 columns");
        assertEquals(6, level.getRows(), "DifficultyLevel should report 6 rows");
    }

    @Test
    public void testIntermediateStandardGrid() {
        DifficultyLevel level = DifficultyLevel.INTERMEDIATE;
        Player p1 = new Player(1, "P1", Player.PlayerType.HUMAN, Player.CoinColor.RED);
        Player p2 = new Player(2, "P2", Player.PlayerType.HUMAN, Player.CoinColor.YELLOW);
        GameSettings settings = new GameSettings(p1, p2);
        settings.setDifficultyLevel(level);
        GameState state = new GameState(settings);

        assertEquals(14, state.getColumns(), "Intermediate standard should be 14 columns");
        assertEquals(12, state.getRows(), "Intermediate standard should be 12 rows");
        assertEquals(14, level.getColumns(), "DifficultyLevel should report 14 columns");
        assertEquals(12, level.getRows(), "DifficultyLevel should report 12 rows");
    }

    @Test
    public void testExpertStandardGrid() {
        DifficultyLevel level = DifficultyLevel.EXPERT;
        Player p1 = new Player(1, "P1", Player.PlayerType.HUMAN, Player.CoinColor.RED);
        Player p2 = new Player(2, "P2", Player.PlayerType.HUMAN, Player.CoinColor.YELLOW);
        GameSettings settings = new GameSettings(p1, p2);
        settings.setDifficultyLevel(level);
        GameState state = new GameState(settings);

        assertEquals(21, state.getColumns(), "Expert standard should be 21 columns");
        assertEquals(18, state.getRows(), "Expert standard should be 18 rows");
        assertEquals(21, level.getColumns(), "DifficultyLevel should report 21 columns");
        assertEquals(18, level.getRows(), "DifficultyLevel should report 18 rows");
    }
    
    // ==================== FEATURE TESTS ====================

    @Test
    public void testBoardCellsAreInitialized() {
        DifficultyLevel level = DifficultyLevel.BEGINNER;
        Player p1 = new Player(1, "P1", Player.PlayerType.HUMAN, Player.CoinColor.RED);
        Player p2 = new Player(2, "P2", Player.PlayerType.HUMAN, Player.CoinColor.YELLOW);
        GameSettings settings = new GameSettings(p1, p2);
        settings.setDifficultyLevel(level);
        GameState state = new GameState(settings);

        Cell[][] cells = state.getCells();

        assertNotNull(cells, "Cells array should be initialized");
        assertEquals(7, cells.length, "Should have 7 columns");
        assertEquals(6, cells[0].length, "Should have 6 rows");

        // Verify all cells are empty initially
        for (int col = 0; col < 7; col++) {
            for (int row = 0; row < 6; row++) {
                assertNotNull(cells[col][row], "Cell at [" + col + "][" + row + "] should not be null");
                assertTrue(cells[col][row].isAvailable(),
                        "Cell at [" + col + "][" + row + "] should be initially empty");
            }
        }
    }

    @Test
    public void testDifficultyFromLevel() {
        // Test the fromLevel method
        assertEquals(DifficultyLevel.BEGINNER, DifficultyLevel.fromLevel(1),
                "Index 0 should be BEGINNER");
        assertEquals(DifficultyLevel.INTERMEDIATE, DifficultyLevel.fromLevel(2),
                "Index 1 should be INTERMEDIATE");
        assertEquals(DifficultyLevel.EXPERT, DifficultyLevel.fromLevel(3),
                "Index 2 should be EXPERT");
    }
}


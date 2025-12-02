package src.test.java.com.connect4;

import src.main.java.com.connect4.player.Player;
import src.main.java.com.connect4.settings.DifficultyLevel;
import src.main.java.com.connect4.settings.GameSettings;
import src.main.java.com.connect4.view.Cell;
import src.main.java.com.connect4.view.GameState;

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

    // ==================== SQUARE MODE TESTS ====================

    @Test
    public void testBeginnerSquareGrid() {
        DifficultyLevel level = DifficultyLevel.BEGINNER_SQUARE;
        Player p1 = new Player(1, "P1", Player.PlayerType.HUMAN, Player.CoinColor.RED);
        Player p2 = new Player(2, "P2", Player.PlayerType.HUMAN, Player.CoinColor.YELLOW);
        GameSettings settings = new GameSettings(p1, p2);
        settings.setDifficultyLevel(level);
        GameState state = new GameState(settings);

        assertEquals(7, state.getColumns(), "Beginner square should be 7 columns");
        assertEquals(7, state.getRows(), "Beginner square should be 7 rows");
        assertTrue(level.isFourCornersEnabled(), "Square mode should have four corners enabled");

        // Verify it's actually square
        assertEquals(state.getColumns(), state.getRows(),
                "Square mode should have equal columns and rows");
    }

    @Test
    public void testIntermediateSquareGrid() {
        DifficultyLevel level = DifficultyLevel.INTERMEDIATE_SQUARE;
        Player p1 = new Player(1, "P1", Player.PlayerType.HUMAN, Player.CoinColor.RED);
        Player p2 = new Player(2, "P2", Player.PlayerType.HUMAN, Player.CoinColor.YELLOW);
        GameSettings settings = new GameSettings(p1, p2);
        settings.setDifficultyLevel(level);
        GameState state = new GameState(settings);

        assertEquals(12, state.getColumns(), "Intermediate square should be 12 columns");
        assertEquals(12, state.getRows(), "Intermediate square should be 12 rows");
        assertTrue(level.isFourCornersEnabled(), "Square mode should have four corners enabled");

        // Verify it's actually square
        assertEquals(state.getColumns(), state.getRows(),
                "Square mode should have equal columns and rows");
    }

    @Test
    public void testExpertSquareGrid() {
        DifficultyLevel level = DifficultyLevel.EXPERT_SQUARE;
        Player p1 = new Player(1, "P1", Player.PlayerType.HUMAN, Player.CoinColor.RED);
        Player p2 = new Player(2, "P2", Player.PlayerType.HUMAN, Player.CoinColor.YELLOW);
        GameSettings settings = new GameSettings(p1, p2);
        settings.setDifficultyLevel(level);
        GameState state = new GameState(settings);

        assertEquals(18, state.getColumns(), "Expert square should be 18 columns");
        assertEquals(18, state.getRows(), "Expert square should be 18 rows");
        assertTrue(level.isFourCornersEnabled(), "Square mode should have four corners enabled");

        // Verify it's actually square
        assertEquals(state.getColumns(), state.getRows(),
                "Square mode should have equal columns and rows");
    }

    // ==================== FEATURE TESTS ====================

    @Test
    public void testFourCornersOnlyEnabledForSquare() {
        // Standard modes should NOT have four corners enabled
        assertFalse(DifficultyLevel.BEGINNER.isFourCornersEnabled(),
                "Standard beginner should not have four corners");
        assertFalse(DifficultyLevel.INTERMEDIATE.isFourCornersEnabled(),
                "Standard intermediate should not have four corners");
        assertFalse(DifficultyLevel.EXPERT.isFourCornersEnabled(),
                "Standard expert should not have four corners");

        // Square modes SHOULD have four corners enabled
        assertTrue(DifficultyLevel.BEGINNER_SQUARE.isFourCornersEnabled(),
                "Square beginner should have four corners");
        assertTrue(DifficultyLevel.INTERMEDIATE_SQUARE.isFourCornersEnabled(),
                "Square intermediate should have four corners");
        assertTrue(DifficultyLevel.EXPERT_SQUARE.isFourCornersEnabled(),
                "Square expert should have four corners");
    }

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
    public void testLargestBoardDimensions() {
        // Test the largest board (Expert Square 18x18)
        DifficultyLevel level = DifficultyLevel.EXPERT_SQUARE;
        Player p1 = new Player(1, "P1", Player.PlayerType.HUMAN, Player.CoinColor.RED);
        Player p2 = new Player(2, "P2", Player.PlayerType.HUMAN, Player.CoinColor.YELLOW);
        GameSettings settings = new GameSettings(p1, p2);
        settings.setDifficultyLevel(level);
        GameState state = new GameState(settings);

        Cell[][] cells = state.getCells();

        assertEquals(18, cells.length, "Should have 18 columns");
        assertEquals(18, cells[0].length, "Should have 18 rows");

        // Verify all 324 cells are initialized
        int totalCells = 18 * 18;
        int initializedCells = 0;
        for (int col = 0; col < 18; col++) {
            for (int row = 0; row < 18; row++) {
                if (cells[col][row] != null && cells[col][row].isAvailable()) {
                    initializedCells++;
                }
            }
        }
        assertEquals(totalCells, initializedCells, "All 324 cells should be initialized");
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

    @Test
    public void testDifficultyFromSquareLevel() {
        // Test the fromSquareLevel method
        assertEquals(DifficultyLevel.BEGINNER_SQUARE, DifficultyLevel.fromSquareLevel(1),
                "Square index 0 should be BEGINNER_SQUARE");
        assertEquals(DifficultyLevel.INTERMEDIATE_SQUARE, DifficultyLevel.fromSquareLevel(2),
                "Square index 1 should be INTERMEDIATE_SQUARE");
        assertEquals(DifficultyLevel.EXPERT_SQUARE, DifficultyLevel.fromSquareLevel(3),
                "Square index 2 should be EXPERT_SQUARE");
    }
}

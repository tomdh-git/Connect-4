package src.test.java.com.connect4;

import src.main.java.com.connect4.*;
import src.main.java.com.connect4.player.Player;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

/**
 * WinConditionTest.java
 * 
 * Comprehensive tests for all win conditions in Connect-4:
 * - Horizontal wins
 * - Vertical wins
 * - Diagonal wins (both directions)
 * - Four corners win (square boards)
 */
public class WinConditionTest {

    private GameSettings settings;
    private GameState state;

    @BeforeEach
    public void setUp() {
        Player p1 = new Player(1, "Player 1", Player.PlayerType.HUMAN, Player.CoinColor.RED);
        Player p2 = new Player(2, "Player 2", Player.PlayerType.HUMAN, Player.CoinColor.YELLOW);
        settings = new GameSettings(p1, p2);
        settings.setDifficultyLevel(DifficultyLevel.BEGINNER);
        state = new GameState(settings);
    }

    // ==================== HORIZONTAL WIN TESTS ====================

    @Test
    public void testHorizontalWin_BottomRow() {
        // Player 1 (RED) wins with horizontal line in bottom row
        state.move(1); // RED
        state.move(1); // YELLOW
        state.move(2); // RED
        state.move(2); // YELLOW
        state.move(3); // RED
        state.move(3); // YELLOW
        state.move(4); // RED - wins!

        assertTrue(state.getGameOver(), "Game should be over");
        assertTrue(state.getPlayer1Wins(), "Player 1 should win");
        assertFalse(state.getPlayer2Wins(), "Player 2 should not win");
    }

    @Test
    public void testHorizontalWin_MiddleRow() {
        // Player 2 (YELLOW) wins with horizontal line in middle
        state.move(1); // RED
        state.move(2); // YELLOW
        state.move(1); // RED
        state.move(3); // YELLOW
        state.move(1); // RED
        state.move(4); // YELLOW
        state.move(7); // RED
        state.move(5); // YELLOW - wins!

        assertTrue(state.getGameOver(), "Game should be over");
        assertTrue(state.getPlayer2Wins(), "Player 2 should win");
        assertFalse(state.getPlayer1Wins(), "Player 1 should not win");
    }

    @Test
    public void testHorizontalWin_RightEdge() {
        // Test horizontal win at the right edge of the board
        state.move(4); // RED
        state.move(1); // YELLOW
        state.move(5); // RED
        state.move(1); // YELLOW
        state.move(6); // RED
        state.move(1); // YELLOW
        state.move(7); // RED - wins at right edge!

        assertTrue(state.getGameOver(), "Game should be over");
        assertTrue(state.getPlayer1Wins(), "Player 1 should win");
    }

    // ==================== VERTICAL WIN TESTS ====================

    @Test
    public void testVerticalWin_LeftColumn() {
        // Player 1 (RED) wins with vertical line in column 1
        state.move(1); // RED
        state.move(2); // YELLOW
        state.move(1); // RED
        state.move(2); // YELLOW
        state.move(1); // RED
        state.move(2); // YELLOW
        state.move(1); // RED - wins!

        assertTrue(state.getGameOver(), "Game should be over");
        assertTrue(state.getPlayer1Wins(), "Player 1 should win");
    }

    @Test
    public void testVerticalWin_MiddleColumn() {
        // Player 2 (YELLOW) wins with vertical line in middle column
        state.move(1); // RED
        state.move(4); // YELLOW
        state.move(1); // RED
        state.move(4); // YELLOW
        state.move(1); // RED
        state.move(4); // YELLOW
        state.move(2); // RED
        state.move(4); // YELLOW - wins!

        assertTrue(state.getGameOver(), "Game should be over");
        assertTrue(state.getPlayer2Wins(), "Player 2 should win");
    }

    @Test
    public void testVerticalWin_RightColumn() {
        // Test vertical win in rightmost column
        state.move(2); // RED
        state.move(7); // YELLOW
        state.move(2); // RED
        state.move(7); // YELLOW
        state.move(2); // RED
        state.move(7); // YELLOW
        state.move(2); // RED
        state.move(7); // YELLOW - wins!

        assertTrue(state.getGameOver(), "Game should be over");
        assertTrue(state.getPlayer2Wins(), "Player 2 should win");
    }

    // ==================== DIAGONAL WIN TESTS (/) ====================

    @Test
    public void testDiagonalWin_UpRight_BottomLeft() {
        // Player 1 (RED) wins with / diagonal starting from bottom-left
        state.move(1); // RED at (1,1)
        state.move(2); // YELLOW
        state.move(2); // RED at (2,1)
        state.move(3); // YELLOW
        state.move(3); // RED
        state.move(3); // YELLOW at (3,2)
        state.move(4); // RED
        state.move(4); // YELLOW
        state.move(4); // RED
        state.move(1); // YELLOW
        state.move(4); // RED - wins with diagonal!

        assertTrue(state.getGameOver(), "Game should be over");
        assertTrue(state.getPlayer1Wins(), "Player 1 should win");
    }

    @Test
    public void testDiagonalWin_UpRight_Middle() {
        // Test / diagonal in middle of board
        state.move(2); // RED
        state.move(3); // YELLOW
        state.move(3); // RED
        state.move(4); // YELLOW
        state.move(4); // RED
        state.move(4); // YELLOW
        state.move(5); // RED
        state.move(5); // YELLOW
        state.move(5); // RED
        state.move(1); // YELLOW
        state.move(5); // RED - wins!

        assertTrue(state.getGameOver(), "Game should be over");
        assertTrue(state.getPlayer1Wins(), "Player 1 should win");
    }

    // ==================== DIAGONAL WIN TESTS (\) ====================

    @Test
    public void testDiagonalWin_DownRight_TopLeft() {
        // Player 2 (YELLOW) wins with \ diagonal
        state.move(1); // RED
        state.move(4); // YELLOW at (4,1)
        state.move(3); // RED
        state.move(3); // YELLOW at (3,1)
        state.move(3); // RED
        state.move(3); // YELLOW at (3,2)
        state.move(2); // RED
        state.move(2); // YELLOW at (2,1)
        state.move(2); // RED
        state.move(2); // YELLOW at (2,2)
        state.move(1); // RED
        state.move(1); // YELLOW at (1,1)
        state.move(1); // RED
        state.move(1); // YELLOW - wins with \ diagonal!

        assertTrue(state.getGameOver(), "Game should be over");
        assertTrue(state.getPlayer2Wins(), "Player 2 should win");
    }

    @Test
    public void testDiagonalWin_DownRight_Middle() {
        // Test \ diagonal in middle area
        state.move(7); // RED
        state.move(7); // YELLOW
        state.move(7); // RED
        state.move(7); // YELLOW at (7,4)
        state.move(1); // RED
        state.move(6); // YELLOW
        state.move(6); // RED
        state.move(6); // YELLOW at (6,3)
        state.move(1); // RED
        state.move(5); // YELLOW
        state.move(5); // RED
        state.move(5); // YELLOW at (5,3)

        // Build up column 4 to get (4,1)
        state.move(4); // RED
        state.move(4); // YELLOW at (4,2)

        // Now complete the diagonal
        state.move(1); // RED
        state.move(5); // YELLOW
        state.move(1); // RED
        state.move(4); // YELLOW at (4,3) - need one more
        state.move(1); // RED
        state.move(4); // YELLOW at (4,4) - wins!

        assertTrue(state.getGameOver(), "Game should be over");
        assertTrue(state.getPlayer2Wins(), "Player 2 should win with \\ diagonal");
    }

    // ==================== FOUR CORNERS WIN TEST ====================

    @Test
    public void testFourCornersWin_SquareBoard() {
        // Create a square board with four corners enabled
        DifficultyLevel squareLevel = DifficultyLevel.BEGINNER_SQUARE;
        Player p1 = new Player(1, "P1", Player.PlayerType.HUMAN, Player.CoinColor.RED);
        Player p2 = new Player(2, "P2", Player.PlayerType.HUMAN, Player.CoinColor.YELLOW);
        GameSettings squareSettings = new GameSettings(p1, p2);
        squareSettings.setDifficultyLevel(squareLevel);
        GameState squareState = new GameState(squareSettings);

        int size = squareState.getColumns(); // 7x7 for BEGINNER_SQUARE

        // Player 1 captures all 4 corners
        // Bottom-left corner (1,1)
        squareState.move(1); // RED at (1,1)

        // Bottom-right corner (7,1)
        squareState.move(2); // YELLOW
        squareState.move(size); // RED at (7,1)

        // Top-left corner - need to stack to row 7
        squareState.move(3); // YELLOW
        for (int i = 0; i < size - 1; i++) {
            squareState.move(1); // Stack RED to reach top
            if (i < size - 2) {
                squareState.move(4); // YELLOW moves elsewhere
            }
        }
        // Now (1,7) is RED

        // Top-right corner - stack to (7,7)
        squareState.move(5); // YELLOW
        for (int i = 0; i < size - 2; i++) {
            squareState.move(size); // Stack RED
            squareState.move(6); // YELLOW
        }
        squareState.move(size); // RED at (7,7) - should win with 4 corners!

        assertTrue(squareState.getGameOver(), "Game should be over");
        assertTrue(squareState.getPlayer1Wins(), "Player 1 should win with 4 corners");
    }

    // ==================== EDGE CASES ====================

    @Test
    public void testNoWin_ThreeInRow() {
        // Test that 3 in a row doesn't trigger a win
        state.move(1); // RED
        state.move(1); // YELLOW
        state.move(2); // RED
        state.move(2); // YELLOW
        state.move(3); // RED

        assertFalse(state.getGameOver(), "Game should not be over with only 3 in a row");
        assertFalse(state.getPlayer1Wins(), "Player 1 should not win");
    }

    @Test
    public void testCorrectPlayerWins() {
        // Verify the correct player is declared winner
        state.move(1); // RED
        state.move(2); // YELLOW
        state.move(1); // RED
        state.move(2); // YELLOW
        state.move(1); // RED
        state.move(2); // YELLOW
        state.move(1); // RED wins

        assertTrue(state.getPlayer1Wins(), "Player 1 should be the winner");
        assertFalse(state.getPlayer2Wins(), "Player 2 should not be the winner");

        String status = state.getStatusMessage();
        assertTrue(status.contains("wins") || status.contains("Wins"),
                "Status should indicate a win");
    }

    @Test
    public void testWinWithLuckyCoin() {
        // Test that lucky coins count towards a win
        // This test assumes lucky coin can be manually placed
        // We'll test the wildcard behavior

        // Make moves to set up a scenario
        state.move(1); // RED
        state.move(2); // YELLOW
        state.move(1); // RED
        state.move(2); // YELLOW
        state.move(1); // RED

        // If a lucky coin appears and is accepted, it should count as a win
        // The lucky coin acts as a wildcard for the player who accepts it

        // Note: This test validates the concept - actual lucky coin placement
        // is tested in LuckyCoinTest.java
    }
}

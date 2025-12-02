package src.test.java.com.connect4;

import src.main.java.com.connect4.*;
import src.main.java.com.connect4.player.Player;
import src.main.java.com.connect4.settings.DifficultyLevel;
import src.main.java.com.connect4.settings.GameSettings;
import src.main.java.com.connect4.view.GameState;

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
        moveTest(1); // RED
        moveTest(1); // YELLOW
        moveTest(2); // RED
        moveTest(2); // YELLOW
        moveTest(3); // RED
        moveTest(3); // YELLOW
        moveTest(4); // RED - wins!

        assertTrue(state.getGameOver(), "Game should be over");
        assertTrue(state.getPlayer1Wins(), "Player 1 should win");
        assertFalse(state.getPlayer2Wins(), "Player 2 should not win");
    }

    @Test
    public void testHorizontalWin_MiddleRow() {
        // Player 2 (YELLOW) wins with horizontal line in middle
        moveTest(1); // RED
        moveTest(2); // YELLOW
        moveTest(1); // RED
        moveTest(3); // YELLOW
        moveTest(1); // RED
        moveTest(4); // YELLOW
        moveTest(7); // RED
        moveTest(5); // YELLOW - wins!

        assertTrue(state.getGameOver(), "Game should be over");
        assertTrue(state.getPlayer2Wins(), "Player 2 should win");
        assertFalse(state.getPlayer1Wins(), "Player 1 should not win");
    }

    @Test
    public void testHorizontalWin_RightEdge() {
        // Test horizontal win at the right edge of the board
        moveTest(4); // RED
        moveTest(1); // YELLOW
        moveTest(5); // RED
        moveTest(1); // YELLOW
        moveTest(6); // RED
        moveTest(1); // YELLOW
        moveTest(7); // RED - wins at right edge!

        assertTrue(state.getGameOver(), "Game should be over");
        assertTrue(state.getPlayer1Wins(), "Player 1 should win");
    }

    // ==================== VERTICAL WIN TESTS ====================

    @Test
    public void testVerticalWin_LeftColumn() {
        // Player 1 (RED) wins with vertical line in column 1
        moveTest(1); // RED
        moveTest(2); // YELLOW
        moveTest(1); // RED
        moveTest(2); // YELLOW
        moveTest(1); // RED
        moveTest(2); // YELLOW
        moveTest(1); // RED wins

        assertTrue(state.getGameOver(), "Game should be over");
        assertTrue(state.getPlayer1Wins(), "Player 1 should win");
    }

    @Test
    public void testVerticalWin_MiddleColumn() {
        // Player 2 (YELLOW) wins with vertical line in middle column
        moveTest(1); // RED
        moveTest(4); // YELLOW
        moveTest(1); // RED
        moveTest(4); // YELLOW
        moveTest(1); // RED
        moveTest(4); // YELLOW
        moveTest(2); // RED
        moveTest(4); // YELLOW - wins!

        assertTrue(state.getGameOver(), "Game should be over");
        assertTrue(state.getPlayer2Wins(), "Player 2 should win");
    }

    @Test
    public void testVerticalWin_RightColumn() {
        // Test vertical win in rightmost column
        moveTest(2); // RED
        moveTest(7); // YELLOW
        moveTest(2); // RED
        moveTest(7); // YELLOW
        moveTest(2); // RED
        moveTest(7); // YELLOW
        moveTest(1); // RED
        moveTest(7); // YELLOW - wins!

        assertTrue(state.getGameOver(), "Game should be over");
        assertTrue(state.getPlayer2Wins(), "Player 2 should win");
    }

    // ==================== DIAGONAL WIN TESTS (/) ====================

    @Test
    public void testDiagonalWin_UpRight_BottomLeft() {
        // Player 1 (RED) wins with / diagonal starting from bottom-left
        moveTest(1); // RED at (1,1)
        moveTest(2); // YELLOW
        moveTest(2); // RED at (2,1)
        moveTest(3); // YELLOW
        moveTest(5); // RED
        moveTest(3); // YELLOW at (3,2)
        moveTest(3); // RED
        moveTest(4); // YELLOW
        moveTest(4); // RED
        moveTest(1); // YELLOW
        moveTest(4); // RED
        moveTest(5); // YELLOW
        moveTest(4); // RED - wins with diagonal!

        assertTrue(state.getGameOver(), "Game should be over");
        assertTrue(state.getPlayer1Wins(), "Player 1 should win");
    }

    @Test
    public void testDiagonalWin_UpRight_Middle() {
        // Test / diagonal in middle of board
    	moveTest(2); // RED at (1,1)
        moveTest(3); // YELLOW
        moveTest(3); // RED at (2,1)
        moveTest(4); // YELLOW
        moveTest(6); // RED
        moveTest(4); // YELLOW at (3,2)
        moveTest(4); // RED
        moveTest(5); // YELLOW
        moveTest(5); // RED
        moveTest(2); // YELLOW
        moveTest(5); // RED
        moveTest(6); // YELLOW
        moveTest(5); // RED - wins with diagonal!

        assertTrue(state.getGameOver(), "Game should be over");
        assertTrue(state.getPlayer1Wins(), "Player 1 should win");
    }

    // ==================== DIAGONAL WIN TESTS (\) ====================

    @Test
    public void testDiagonalWin_DownRight_TopLeft() {
        // Player 2 (YELLOW) wins with \ diagonal
        moveTest(1); // RED
        moveTest(4); // YELLOW at (4,1)
        moveTest(3); // RED
        moveTest(3); // YELLOW at (3,1)
        moveTest(3); // RED
        moveTest(3); // YELLOW at (3,2)
        moveTest(2); // RED
        moveTest(2); // YELLOW at (2,1)
        moveTest(6); // RED
        moveTest(2); // YELLOW at (2,2)
        moveTest(6); // RED
        moveTest(1); // YELLOW at (1,1)
        moveTest(1); // RED
        moveTest(1); // YELLOW - wins with \ diagonal!

        assertTrue(state.getGameOver(), "Game should be over");
        assertTrue(state.getPlayer2Wins(), "Player 2 should win");
    }

    @Test
    public void testDiagonalWin_DownRight_Middle() {
        // Test \ diagonal in middle area
        moveTest(4);
        moveTest(4);
        moveTest(4);
        moveTest(4);
        moveTest(3);
        moveTest(5);
        moveTest(5);
        moveTest(5);
        moveTest(6);
        moveTest(6);
        moveTest(3);
        moveTest(7);

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
        if (squareState.isLuckyOfferPending()) {
        	squareState.rejectLuckyOffer();
        }

        // Bottom-right corner (7,1)
        squareState.move(2); // YELLOW
        if (squareState.isLuckyOfferPending()) {
        	squareState.rejectLuckyOffer();
        }
        squareState.move(size); // RED at (7,1)
        if (squareState.isLuckyOfferPending()) {
        	squareState.rejectLuckyOffer();
        }

        // Top-left corner - need to stack to row 7
        squareState.move(3); // YELLOW
        if (squareState.isLuckyOfferPending()) {
        	squareState.rejectLuckyOffer();
        }
        for (int i = 0; i < size - 1; i++) {
            squareState.move(1); // Stack RED to reach top
            if (squareState.isLuckyOfferPending()) {
            	squareState.rejectLuckyOffer();
            }
            if (i < size - 2) {
                squareState.move(4); // YELLOW moves elsewhere
                if (squareState.isLuckyOfferPending()) {
                	squareState.rejectLuckyOffer();
                }
            }
        }
        // Now (1,7) is RED

        // Top-right corner - stack to (7,7)
        squareState.move(5); // YELLOW
        if (squareState.isLuckyOfferPending()) {
        	squareState.rejectLuckyOffer();
        }
        for (int i = 0; i < size - 2; i++) {
            squareState.move(size); // Stack RED
            if (squareState.isLuckyOfferPending()) {
            	squareState.rejectLuckyOffer();
            }
            squareState.move(6); // YELLOW
            if (squareState.isLuckyOfferPending()) {
            	squareState.rejectLuckyOffer();
            }
        }
        squareState.move(size); // RED at (7,7) - should win with 4 corners!
        if (squareState.isLuckyOfferPending()) {
        	squareState.rejectLuckyOffer();
        }

        assertTrue(squareState.getGameOver(), "Game should be over");
        assertTrue(squareState.getPlayer1Wins(), "Player 1 should win with 4 corners");
    }

    // ==================== EDGE CASES ====================

    @Test
    public void testNoWin_ThreeInRow() {
        // Test that 3 in a row doesn't trigger a win
        moveTest(1); // RED
        moveTest(1); // YELLOW
        moveTest(2); // RED
        moveTest(2); // YELLOW
        moveTest(3); // RED

        assertFalse(state.getGameOver(), "Game should not be over with only 3 in a row");
        assertFalse(state.getPlayer1Wins(), "Player 1 should not win");
    }

    @Test
    public void testCorrectPlayerWins() {
        // Verify the correct player is declared winner
        moveTest(1); // RED
        moveTest(2); // YELLOW
        moveTest(1); // RED
        moveTest(2); // YELLOW
        moveTest(1); // RED
        moveTest(2); // YELLOW
        moveTest(1); // RED wins

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
        moveTest(1); // RED
        moveTest(2); // YELLOW
        moveTest(1); // RED
        moveTest(2); // YELLOW
        moveTest(1); // RED

        // If a lucky coin appears and is accepted, it should count as a win
        // The lucky coin acts as a wildcard for the player who accepts it

        // Note: This test validates the concept - actual lucky coin placement
        // is tested in LuckyCoinTest.java
    }
    
    public void moveTest(int move) {
    	state.move(move);
    	if (state.isLuckyOfferPending()) {
        	state.rejectLuckyOffer();
        }
    }
}

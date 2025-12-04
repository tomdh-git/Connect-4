package src.test.java.com.connect4;

import src.main.java.com.connect4.view.GameState;
import src.main.java.com.connect4.settings.GameSettings;
import src.main.java.com.connect4.settings.DifficultyLevel;
import src.main.java.com.connect4.player.Player;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

/**
 * GameStateTest.java
 * 
 * Tests for core game state logic not covered by other tests:
 * - Undo functionality
 * - Undo with lucky coins
 * - State consistency after undo
 * - Deep copy functionality (for AI)
 */
public class GameStateTest {

    private GameSettings settings;
    private GameState state;

    @BeforeEach
    public void setUp() {
        Player p1 = new Player(1, "Alice", Player.PlayerType.HUMAN, Player.CoinColor.RED);
        Player p2 = new Player(2, "Bob", Player.PlayerType.HUMAN, Player.CoinColor.YELLOW);
        settings = new GameSettings(p1, p2);
        settings.setDifficultyLevel(DifficultyLevel.BEGINNER);
        state = new GameState(settings);
    }

    // ==================== UNDO TESTS ====================

    @Test
    public void testUndoSingleMove() {
        // Make a move
        state.move(1);
        if (state.isLuckyOfferPending()) {
            state.rejectLuckyOffer();
        }
        assertFalse(state.getCells()[0][0].isAvailable(), "Cell should be filled");
        assertEquals(2, state.getCurrentPlayer().getId(), "Should be P2 turn");

        // Undo
        boolean success = state.undo();
        assertTrue(success, "Undo should succeed");
        assertTrue(state.getCells()[0][0].isAvailable(), "Cell should be empty after undo");
        assertEquals(1, state.getCurrentPlayer().getId(), "Should be P1 turn again");
    }

    @Test
    public void testUndoMultipleMoves() {
        // Make 3 moves
        state.move(1); // P1
        if (state.isLuckyOfferPending()) {
            state.rejectLuckyOffer();
        }
        state.move(2); // P2
        if (state.isLuckyOfferPending()) {
            state.rejectLuckyOffer();
        }
        state.move(3); // P1
        if (state.isLuckyOfferPending()) {
            state.rejectLuckyOffer();
        }

        // Undo 3
        state.undo(); // Undo P1
        assertEquals(1, state.getCurrentPlayer().getId(), "Should be P1 turn");
        assertTrue(state.getCells()[2][0].isAvailable(), "Col 3 should be empty");

        state.undo(); // Undo P2
        assertEquals(2, state.getCurrentPlayer().getId(), "Should be P2 turn");
        assertTrue(state.getCells()[1][0].isAvailable(), "Col 2 should be empty");

        state.undo(); // Undo P1
        assertEquals(1, state.getCurrentPlayer().getId(), "Should be P1 turn");
        assertTrue(state.getCells()[0][0].isAvailable(), "Col 1 should be empty");
    }

    @Test
    public void testUndoWhenNoMoves() {
        boolean success = state.undo();
        assertFalse(success, "Undo should fail when no moves");
        assertNotNull(state.getError(), "Error should be set");
    }

    @Test
    public void testUndoClearsWinStatus() {
        // Create a win
        state.move(1); // P1
        if (state.isLuckyOfferPending()) {
            state.rejectLuckyOffer();
        }
        state.move(2); // P2
        if (state.isLuckyOfferPending()) {
            state.rejectLuckyOffer();
        }
        state.move(1); // P1
        if (state.isLuckyOfferPending()) {
            state.rejectLuckyOffer();
        }
        state.move(2); // P2
        if (state.isLuckyOfferPending()) {
            state.rejectLuckyOffer();
        }
        state.move(1); // P1
        if (state.isLuckyOfferPending()) {
            state.rejectLuckyOffer();
        }
        state.move(2); // P2
        if (state.isLuckyOfferPending()) {
            state.rejectLuckyOffer();
        }
        state.move(1); // P1 Wins
        if (state.isLuckyOfferPending()) {
            state.rejectLuckyOffer();
        }

        assertTrue(state.getGameOver(), "Game should be over");
        assertTrue(state.getPlayer1Wins(), "P1 should win");

        // Undo winning move
        state.undo();

        assertFalse(state.getGameOver(), "Game should not be over after undo");
    }

    // ==================== DEEP COPY TESTS ====================

    @Test
    public void testDeepCopy() {
        // Setup state
        state.move(1);
        if (state.isLuckyOfferPending()) {
            state.rejectLuckyOffer();
        }
        state.move(2);
        if (state.isLuckyOfferPending()) {
            state.rejectLuckyOffer();
        }

        // Create copy
        GameState copy = state.deepCopy();

        // Verify copy matches
        assertEquals(state.getColumns(), copy.getColumns());
        assertEquals(state.getRows(), copy.getRows());
        assertEquals(state.getCurrentPlayer().getId(), copy.getCurrentPlayer().getId());

        // Modify copy
        copy.move(3);
        if (copy.isLuckyOfferPending()) {
            copy.rejectLuckyOffer();
        }

        // Verify original is unchanged
        assertTrue(state.getCells()[2][0].isAvailable(), "Original state should not have move 3");
        assertFalse(copy.getCells()[2][0].isAvailable(), "Copy state should have move 3");

        // Modify original
        state.move(4);
        if (state.isLuckyOfferPending()) {
            state.rejectLuckyOffer();
        }

        // Verify copy is unchanged
        assertTrue(copy.getCells()[3][0].isAvailable(), "Copy state should not have move 4");
    }
}

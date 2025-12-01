package src.test.java.com.connect4;

import src.main.java.com.connect4.*;
import src.main.java.com.connect4.player.AIPlayer;
import src.main.java.com.connect4.player.Player;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * PlayerModeTest.java
 * 
 * Tests for different player modes:
 * - Two-player mode (human vs human)
 * - One-player mode (human vs computer)
 * - Turn alternation
 * - AI functionality
 */
public class PlayerModeTest {

    // ==================== TWO-PLAYER MODE TESTS ====================

    @Test
    public void testTwoPlayerModeCreation() {
        Player p1 = new Player(1, "Alice", Player.PlayerType.HUMAN, Player.CoinColor.RED);
        Player p2 = new Player(2, "Bob", Player.PlayerType.HUMAN, Player.CoinColor.YELLOW);
        GameSettings settings = new GameSettings(p1, p2);
        GameState state = new GameState(settings);

        assertFalse(settings.isVsComputer(), "Should not be vs computer mode");
        assertEquals(2, settings.getPlayerCount(), "Should have 2 players");
        assertNotNull(settings.getPlayer1(), "Player 1 should exist");
        assertNotNull(settings.getPlayer2(), "Player 2 should exist");
        assertEquals("Alice", settings.getPlayer1().getName(), "Player 1 name should be Alice");
        assertEquals("Bob", settings.getPlayer2().getName(), "Player 2 name should be Bob");
    }

    @Test
    public void testTwoPlayerTurnAlternation() {
        Player p1 = new Player(1, "Player 1", Player.PlayerType.HUMAN, Player.CoinColor.RED);
        Player p2 = new Player(2, "Player 2", Player.PlayerType.HUMAN, Player.CoinColor.YELLOW);
        GameSettings settings = new GameSettings(p1, p2);
        GameState state = new GameState(settings);

        // Player 1 starts
        Player current = state.getCurrentPlayer();
        assertEquals(p1, current, "Player 1 should start");
        assertEquals(1, current.getId(), "Current player ID should be 1");

        // Make a move
        state.move(1);

        // Should switch to Player 2
        current = state.getCurrentPlayer();
        assertEquals(p2, current, "Should switch to Player 2");
        assertEquals(2, current.getId(), "Current player ID should be 2");

        // Make another move
        state.move(2);

        // Should switch back to Player 1
        current = state.getCurrentPlayer();
        assertEquals(p1, current, "Should switch back to Player 1");
        assertEquals(1, current.getId(), "Current player ID should be 1");
    }

    @Test
    public void testBothPlayersAreHuman() {
        Player p1 = new Player(1, "P1", Player.PlayerType.HUMAN, Player.CoinColor.RED);
        Player p2 = new Player(2, "P2", Player.PlayerType.HUMAN, Player.CoinColor.YELLOW);
        GameSettings settings = new GameSettings(p1, p2);

        assertTrue(settings.getPlayer1().isHuman(), "Player 1 should be human");
        assertTrue(settings.getPlayer2().isHuman(), "Player 2 should be human");
        assertFalse(settings.getPlayer1().isComputer(), "Player 1 should not be computer");
        assertFalse(settings.getPlayer2().isComputer(), "Player 2 should not be computer");
    }

    // ==================== ONE-PLAYER MODE TESTS ====================

    @Test
    public void testOnePlayerModeCreation() {
        Player p1 = new Player(1, "Human", Player.PlayerType.HUMAN, Player.CoinColor.RED);
        GameSettings settings = new GameSettings(DifficultyLevel.INTERMEDIATE, p1, true);
        GameState state = new GameState(settings);

        assertTrue(settings.isVsComputer(), "Should be vs computer mode");
        assertEquals(2, settings.getPlayerCount(), "Should have 2 players (human + computer)");
        assertNotNull(settings.getPlayer1(), "Player 1 should exist");
        assertNotNull(settings.getPlayer2(), "Player 2 (computer) should exist");
        assertTrue(settings.getPlayer2().isComputer(), "Player 2 should be computer");
        assertFalse(settings.getPlayer1().isComputer(), "Player 1 should not be computer");
    }

    @Test
    public void testComputerPlayerIdentification() {
        Player p1 = new Player(1, "Human", Player.PlayerType.HUMAN, Player.CoinColor.RED);
        GameSettings settings = new GameSettings(DifficultyLevel.BEGINNER, p1, true);

        Player computer = settings.getComputerPlayer();
        assertNotNull(computer, "Computer player should exist");
        assertTrue(computer.isComputer(), "Computer player should be identified as computer");
        assertEquals(Player.PlayerType.COMPUTER, computer.getType(),
                "Computer player type should be COMPUTER");
    }

    @Test
    public void testOnePlayerTurnAlternation() {
        Player p1 = new Player(1, "Human", Player.PlayerType.HUMAN, Player.CoinColor.RED);
        GameSettings settings = new GameSettings(DifficultyLevel.BEGINNER, p1, true);
        GameState state = new GameState(settings);

        // Player 1 (human) starts
        Player current = state.getCurrentPlayer();
        assertTrue(current.isHuman(), "Human should start");

        // Make a move
        state.move(1);

        // Should switch to computer
        current = state.getCurrentPlayer();
        assertTrue(current.isComputer(), "Should switch to computer");

        // Computer makes a move
        state.move(2);

        // Should switch back to human
        current = state.getCurrentPlayer();
        assertTrue(current.isHuman(), "Should switch back to human");
    }

    @Test
    public void testAIDifficultyLevels() {
        Player p1 = new Player(1, "Human", Player.PlayerType.HUMAN, Player.CoinColor.RED);

        // Test Beginner
        GameSettings beginnerSettings = new GameSettings(DifficultyLevel.BEGINNER, p1, true);
        assertEquals(DifficultyLevel.BEGINNER, beginnerSettings.getDifficultyLevel(),
                "Should be beginner difficulty");

        // Test Intermediate
        GameSettings intermediateSettings = new GameSettings(DifficultyLevel.INTERMEDIATE, p1, true);
        assertEquals(DifficultyLevel.INTERMEDIATE, intermediateSettings.getDifficultyLevel(),
                "Should be intermediate difficulty");

        // Test Expert
        GameSettings expertSettings = new GameSettings(DifficultyLevel.EXPERT, p1, true);
        assertEquals(DifficultyLevel.EXPERT, expertSettings.getDifficultyLevel(),
                "Should be expert difficulty");
    }

    @Test
    public void testAIMakesValidMoves() {
        Player p1 = new Player(1, "Human", Player.PlayerType.HUMAN, Player.CoinColor.RED);
        GameSettings settings = new GameSettings(DifficultyLevel.BEGINNER, p1, true);
        GameState state = new GameState(settings);
        AIPlayer ai = new AIPlayer(DifficultyLevel.BEGINNER, settings.getComputerPlayer().getId());

        // Human makes first move
        state.move(1);

        // AI should be able to make a move
        int aiMove = ai.getBestMove(state);
        assertTrue(aiMove >= 1 && aiMove <= state.getColumns(),
                "AI move should be within valid column range");
        assertTrue(state.isValidMove(aiMove), "AI move should be valid");

        // Execute AI move
        boolean success = state.move(aiMove);
        assertTrue(success, "AI move should succeed");
    }

    // ==================== TURN ALTERNATION EDGE CASES ====================

    @Test
    public void testTurnAlternationAfterUndo() {
        Player p1 = new Player(1, "P1", Player.PlayerType.HUMAN, Player.CoinColor.RED);
        Player p2 = new Player(2, "P2", Player.PlayerType.HUMAN, Player.CoinColor.YELLOW);
        GameSettings settings = new GameSettings(p1, p2);
        GameState state = new GameState(settings);

        // P1 moves
        Player firstPlayer = state.getCurrentPlayer();
        state.move(1);

        // P2's turn
        Player secondPlayer = state.getCurrentPlayer();
        assertNotEquals(firstPlayer, secondPlayer, "Turn should have switched");

        // Undo
        state.undo();

        // Should be back to P1's turn
        Player afterUndo = state.getCurrentPlayer();
        assertEquals(firstPlayer, afterUndo, "After undo, should be first player's turn again");
    }

    @Test
    public void testNoTurnChangeWhenGameOver() {
        Player p1 = new Player(1, "P1", Player.PlayerType.HUMAN, Player.CoinColor.RED);
        Player p2 = new Player(2, "P2", Player.PlayerType.HUMAN, Player.CoinColor.YELLOW);
        GameSettings settings = new GameSettings(p1, p2);
        GameState state = new GameState(settings);

        // Create a winning condition for P1
        state.move(1); // RED
        state.move(2); // YELLOW
        state.move(1); // RED
        state.move(2); // YELLOW
        state.move(1); // RED
        state.move(2); // YELLOW
        state.move(1); // RED wins

        assertTrue(state.getGameOver(), "Game should be over");

        // Try to make another move - should fail
        Player currentBeforeMove = state.getCurrentPlayer();
        boolean moveSuccess = state.move(3);
        assertFalse(moveSuccess, "Move should fail when game is over");

        Player currentAfterMove = state.getCurrentPlayer();
        assertEquals(currentBeforeMove, currentAfterMove,
                "Current player should not change when game is over");
    }

    @Test
    public void testPlayerColors() {
        Player p1 = new Player(1, "P1", Player.PlayerType.HUMAN, Player.CoinColor.BLUE);
        Player p2 = new Player(2, "P2", Player.PlayerType.HUMAN, Player.CoinColor.GREEN);
        GameSettings settings = new GameSettings(p1, p2);

        assertEquals(Player.CoinColor.BLUE, settings.getPlayer1().getCoinColor(),
                "Player 1 should have BLUE");
        assertEquals(Player.CoinColor.GREEN, settings.getPlayer2().getCoinColor(),
                "Player 2 should have GREEN");
        assertNotEquals(settings.getPlayer1().getCoinColor(), settings.getPlayer2().getCoinColor(),
                "Players should have different colors");
    }

    @Test
    public void testComputerPlayerName() {
        Player p1 = new Player(1, "Alice", Player.PlayerType.HUMAN, Player.CoinColor.RED);
        GameSettings settings = new GameSettings(DifficultyLevel.INTERMEDIATE, p1, true);

        Player computer = settings.getComputerPlayer();
        assertNotNull(computer.getName(), "Computer should have a name");
        assertTrue(computer.getName().toLowerCase().contains("computer") ||
                computer.getName().toLowerCase().contains("ai"),
                "Computer name should indicate it's a computer/AI");
    }
}

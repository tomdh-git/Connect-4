package src.test.java.com.connect4;

import src.main.java.com.connect4.settings.SaveLoadManager;
import src.main.java.com.connect4.view.GameState;
import src.main.java.com.connect4.settings.GameSettings;
import src.main.java.com.connect4.settings.DifficultyLevel;
import src.main.java.com.connect4.player.Player;
import src.main.java.com.connect4.view.Cell;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;

/**
 * SaveLoadTest.java
 * 
 * Tests for Save/Load functionality:
 * - Save empty board
 * - Save full board
 * - Load saved state
 * - Verify file creation and content integrity
 */
public class SaveLoadTest {

    private static final String TEST_SAVE_FILE = "test_save_game.dat";
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

    @AfterEach
    public void tearDown() {
        // Clean up test file
        File file = new File("saves" + File.separator + TEST_SAVE_FILE + ".c4save");
        if (file.exists()) {
            file.delete();
        }
    }

    @Test
    public void testSaveAndLoadEmptyBoard() {
        // Save initial state
        boolean saveSuccess = SaveLoadManager.saveGame(state, TEST_SAVE_FILE, "Test Save");
        assertTrue(saveSuccess, "Save should succeed");

        File file = new File("saves" + File.separator + TEST_SAVE_FILE + ".c4save");
        if (!file.exists()) {
            file = new File(TEST_SAVE_FILE + ".c4save");
        }

        // Load state
        SaveLoadManager.GameSnapshot snapshot = SaveLoadManager.loadGame(TEST_SAVE_FILE);
        assertNotNull(snapshot, "Load should return a snapshot");

        GameState loadedState = SaveLoadManager.applySnapshot(snapshot);
        assertNotNull(loadedState, "Applied snapshot should return a GameState");

        // Verify loaded state matches original
        assertEquals(state.getRows(), loadedState.getRows(), "Rows should match");
        assertEquals(state.getColumns(), loadedState.getColumns(), "Columns should match");
        assertEquals(state.getCurrentPlayer().getId(), loadedState.getCurrentPlayer().getId(),
                "Current player should match");

        // Verify board is empty
        Cell[][] cells = loadedState.getCells();
        for (int col = 0; col < state.getColumns(); col++) {
            for (int row = 0; row < state.getRows(); row++) {
                assertTrue(cells[col][row].isAvailable(), "Cell should be empty");
            }
        }
    }

    @Test
    public void testSaveAndLoadMidGame() {
        // Make some moves
        state.move(1); // RED
        if (state.isLuckyOfferPending()) {
            state.rejectLuckyOffer();
        }
        state.move(2); // YELLOW
        if (state.isLuckyOfferPending()) {
            state.rejectLuckyOffer();
        }
        state.move(1); // RED
        if (state.isLuckyOfferPending()) {
            state.rejectLuckyOffer();
        }

        // Save state
        boolean saveSuccess = SaveLoadManager.saveGame(state, TEST_SAVE_FILE, "Mid-game Save");
        assertTrue(saveSuccess, "Save should succeed");

        // Load state
        SaveLoadManager.GameSnapshot snapshot = SaveLoadManager.loadGame(TEST_SAVE_FILE);
        assertNotNull(snapshot, "Load should return a snapshot");

        GameState loadedState = SaveLoadManager.applySnapshot(snapshot);
        assertNotNull(loadedState, "Applied snapshot should return a GameState");

        // Verify board state
        Cell[][] cells = loadedState.getCells();
        assertTrue(cells[0][0].isRed(), "Cell (1,1) should be RED"); // 0-indexed
        assertTrue(cells[1][0].isYellow(), "Cell (2,1) should be YELLOW");
        assertTrue(cells[0][1].isRed(), "Cell (1,2) should be RED");

        // Verify next player (should be YELLOW's turn)
        assertEquals(2, loadedState.getCurrentPlayer().getId(), "Should be Player 2's turn");
    }

    @Test
    public void testSaveOverwritesOldFile() {
        // Save initial state
        SaveLoadManager.saveGame(state, TEST_SAVE_FILE, "First Save");

        File file = new File("saves" + File.separator + TEST_SAVE_FILE + ".c4save");
        if (!file.exists()) {
            file = new File(TEST_SAVE_FILE + ".c4save");
        }

        long firstModified = file.lastModified();

        // Wait a bit to ensure timestamp difference
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
        }

        // Make moves and save again
        state.move(1);
        if (state.isLuckyOfferPending()) {
            state.rejectLuckyOffer();
        }
        boolean save2Success = SaveLoadManager.saveGame(state, TEST_SAVE_FILE, "Second Save");

        long secondModified = file.lastModified();

        assertTrue(secondModified > firstModified,
                "File should be updated. First: " + firstModified + ", Second: " + secondModified);

        // Load and verify it's the new state
        SaveLoadManager.GameSnapshot snapshot = SaveLoadManager.loadGame(TEST_SAVE_FILE);
        GameState loadedState = SaveLoadManager.applySnapshot(snapshot);
        assertFalse(loadedState.getCells()[0][0].isAvailable(), "Should have the move");
    }

    @Test
    public void testLoadNonExistentFile() {
        SaveLoadManager.GameSnapshot snapshot = SaveLoadManager.loadGame("non_existent_file.dat");
        assertNull(snapshot, "Loading non-existent file should return null");
    }
}

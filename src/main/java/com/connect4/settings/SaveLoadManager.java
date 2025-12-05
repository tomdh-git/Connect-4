package com.connect4.settings;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Stack;

import com.connect4.player.Player;
import com.connect4.view.Cell;
import com.connect4.view.GameState;

import java.awt.Point;

/**
 * SaveLoadManager.java - NEW CLASS
 * This class handles saving and loading game state to/from files.
 * It provides functionality to:
 * - Save current game state to a file
 * - Load a previously saved game
 * - List available saved games
 * Save format uses Java serialization for simplicity and reliability.
 * 
 * @author Extended feature implementation
 */
public class SaveLoadManager {

    public static final String SAVE_DIRECTORY = "saves";
    private static final String FILE_EXTENSION = ".c4save";

    /**
     * GameSnapshot captures the complete state of a game for save/load.
     */
    public static class GameSnapshot implements Serializable {
        private static final long serialVersionUID = 1L;

        // Game configuration
        public GameSettings settings;

        // Board state (serialized as cell states)
        public Cell.CellState[][] boardState;

        // Move history
        public int[][] moveHistory; // Array of [column, row] pairs

        // Game status
        public boolean gameOver;
        public boolean player1Wins;
        public boolean player2Wins;
        public boolean player1Turn;

        // Timestamp
        public long savedTimestamp;
        public String saveDescription;

        /**
         * Creates a snapshot from current game state.
         * 
         * @param gameState   the current game state
         * @param description optional description for this save
         */
        public GameSnapshot(GameState gameState, String description) {
            this.settings = gameState.getSettings();
            this.savedTimestamp = System.currentTimeMillis();
            this.saveDescription = description;

            // Copy board state
            int cols = gameState.getColumns();
            int rows = gameState.getRows();
            this.boardState = new Cell.CellState[cols][rows];
            Cell[][] cells = gameState.getCells();
            for (int i = 0; i < cols; i++) {
                for (int j = 0; j < rows; j++) {
                    this.boardState[i][j] = cells[i][j].getState();
                }
            }

            // Copy move history
            Stack<Point> moves = gameState.getMoves();
            this.moveHistory = new int[moves.size()][2];
            int index = 0;
            for (Point move : moves) {
                this.moveHistory[index][0] = (int) move.getX();
                this.moveHistory[index][1] = (int) move.getY();
                index++;
            }

            // Copy game status
            this.gameOver = gameState.getGameOver();
            this.player1Wins = gameState.getPlayer1Wins();
            this.player2Wins = gameState.getPlayer2Wins();
            this.player1Turn = gameState.isPlayer1Turn();
        }

        /**
         * Gets a formatted save date string.
         * 
         * @return formatted date string
         */
        public String getFormattedDate() {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return sdf.format(new Date(savedTimestamp));
        }

        /**
         * Gets a summary of the saved game.
         * 
         * @return summary string
         */
        public String getSummary() {
            String status;
            if (gameOver) {
                if (player1Wins) {
                    status = settings.getPlayer1().getName() + " won";
                } else if (player2Wins) {
                    status = settings.getPlayer2().getName() + " won";
                } else {
                    status = "Tie game";
                }
            } else {
                Player currentPlayer = player1Turn ? settings.getPlayer1() : settings.getPlayer2();
                status = currentPlayer.getName() + "'s turn";
            }

            return String.format("[%s] %s - %s (%d moves)",
                    getFormattedDate(), saveDescription, status, moveHistory.length);
        }
    }

    /**
     * Ensures the save directory exists, creating it if necessary.
     *
     * @return true if directory exists or was created
     */
    public static boolean ensureSaveDirectory() {
        File dir = new File(SAVE_DIRECTORY);
        if (!dir.exists()) {
            return dir.mkdirs();
        }
        return dir.isDirectory();
    }

    /**
     * Generates a default filename based on current timestamp.
     * 
     * @return generated filename (without path)
     */
    public static String generateDefaultFilename() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        return "game_" + sdf.format(new Date()) + FILE_EXTENSION;
    }

    /**
     * Saves the current game state to a file.
     * 
     * @param gameState   the game state to save
     * @param filename    the filename (with or without path)
     * @param description optional description for the save
     * @return true if save was successful
     */
    public static boolean saveGame(GameState gameState, String filename, String description) {
        ensureSaveDirectory();

        // Add extension if not present
        if (!filename.endsWith(FILE_EXTENSION)) {
            filename += FILE_EXTENSION;
        }

        // Add directory if not an absolute path
        if (!filename.contains(File.separator) && !filename.contains("/")) {
            filename = SAVE_DIRECTORY + File.separator + filename;
        }

        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(filename))) {

            GameSnapshot snapshot = new GameSnapshot(gameState,
                    description != null ? description : "Saved game");
            oos.writeObject(snapshot);
            return true;

        } catch (IOException e) {
            System.err.println("Error saving game: " + e.getMessage());
            return false;
        }
    }

    /**
     * Saves the game with an auto-generated filename.
     * 
     * @param gameState   the game state to save
     * @param description optional description
     * @return the filename used, or null if failed
     */
    public static String saveGameAuto(GameState gameState, String description) {
        String filename = generateDefaultFilename();
        if (saveGame(gameState, filename, description)) {
            return SAVE_DIRECTORY + File.separator + filename;
        }
        return null;
    }

    /**
     * Loads a game state from a file.
     * 
     * @param filename the filename to load from
     * @return the loaded GameSnapshot, or null if failed
     */
    public static GameSnapshot loadGame(String filename) {
        // Add extension if not present
        if (!filename.endsWith(FILE_EXTENSION)) {
            filename += FILE_EXTENSION;
        }

        // Add directory if not an absolute path
        if (!filename.contains(File.separator) && !filename.contains("/")) {
            filename = SAVE_DIRECTORY + File.separator + filename;
        }

        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(filename))) {

            return (GameSnapshot) ois.readObject();

        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading game: " + e.getMessage());
            return null;
        }
    }

    /**
     * Applies a loaded snapshot to a new GameState.
     * 
     * @param snapshot the snapshot to apply
     * @return a new GameState with the loaded state
     */
    public static GameState applySnapshot(GameSnapshot snapshot) {
        // Create new game state with saved settings
        GameState gameState = new GameState(snapshot.settings);

        // Restore board state
        Cell[][] cells = gameState.getCells();
        for (int i = 0; i < snapshot.boardState.length; i++) {
            for (int j = 0; j < snapshot.boardState[i].length; j++) {
                cells[i][j].setState(snapshot.boardState[i][j]);
            }
        }

        // Restore move history
        Stack<Point> moves = gameState.getMoves();
        moves.clear();
        for (int[] move : snapshot.moveHistory) {
            moves.push(new Point(move[0], move[1]));
        }

        // Restore game status
        gameState.setGameOver(snapshot.gameOver);
        gameState.setPlayer1Wins(snapshot.player1Wins);
        gameState.setPlayer2Wins(snapshot.player2Wins);
        gameState.setPlayer1Turn(snapshot.player1Turn);

        return gameState;
    }

    /**
     * Lists all available save files.
     * 
     * @return array of save file names
     */
    public static String[] listSaves() {
        ensureSaveDirectory();
        File dir = new File(SAVE_DIRECTORY);

        File[] files = dir.listFiles((d, name) -> name.endsWith(FILE_EXTENSION));
        if (files == null || files.length == 0) {
            return new String[0];
        }

        String[] filenames = new String[files.length];
        for (int i = 0; i < files.length; i++) {
            filenames[i] = files[i].getName();
        }

        // Sort by modification time (newest first)
        java.util.Arrays.sort(filenames, (a, b) -> {
            File fa = new File(SAVE_DIRECTORY, a);
            File fb = new File(SAVE_DIRECTORY, b);
            return Long.compare(fb.lastModified(), fa.lastModified());
        });

        return filenames;
    }

    /**
     * Gets summaries of all available saves.
     * 
     * @return array of summary strings
     */
    public static String[] getSaveSummaries() {
        String[] filenames = listSaves();
        String[] summaries = new String[filenames.length];

        for (int i = 0; i < filenames.length; i++) {
            GameSnapshot snapshot = loadGame(filenames[i]);
            if (snapshot != null) {
                summaries[i] = snapshot.getSummary();
            } else {
                summaries[i] = filenames[i] + " (unable to read)";
            }
        }

        return summaries;
    }

    /**
     * Deletes a save file.
     * 
     * @param filename the filename to delete
     * @return true if deleted successfully
     */
    public static boolean deleteSave(String filename) {
        if (!filename.endsWith(FILE_EXTENSION)) {
            filename += FILE_EXTENSION;
        }

        if (!filename.contains(File.separator) && !filename.contains("/")) {
            filename = SAVE_DIRECTORY + File.separator + filename;
        }

        File file = new File(filename);
        return file.delete();
    }
}


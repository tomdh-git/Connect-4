/**
 * GameView.java - NEW INTERFACE
 * 
 * This interface defines the contract for all game views (GUI and Text-based).
 * It follows the Model-View-Controller (MVC) pattern by separating the view
 * from the game logic contained in GameState.
 * 
 * By using this interface, we can easily swap between different view implementations
 * (GUIView, TextView) without changing the game logic.
 * 
 * @author Refactored for multi-view support
 */
public interface GameView {
    
    // =========================================================================
    // NEW: Initialize and display the view
    // =========================================================================
    /**
     * Initializes and displays the game view.
     * For GUI, this creates and shows the window.
     * For text-based, this sets up the console interface.
     */
    void initialize();
    
    // =========================================================================
    // NEW: Update the display to reflect current game state
    // =========================================================================
    /**
     * Updates the view to reflect the current state of the game.
     * This should be called after every move or game action.
     */
    void update();
    
    // =========================================================================
    // NEW: Display the game board
    // =========================================================================
    /**
     * Displays the current state of the game board.
     */
    void displayBoard();
    
    // =========================================================================
    // NEW: Display game status message
    // =========================================================================
    /**
     * Displays a message about the current game status.
     * @param message the status message to display
     */
    void displayMessage(String message);
    
    // =========================================================================
    // NEW: Display error message
    // =========================================================================
    /**
     * Displays an error message to the user.
     * @param error the error message to display
     */
    void displayError(String error);
    
    // =========================================================================
    // NEW: Get player input for column selection
    // =========================================================================
    /**
     * Gets the player's input for column selection.
     * For GUI, this might wait for a click event.
     * For text-based, this reads from console input.
     * @return the column number (1-7) selected by the player, 
     *         0 for undo, -1 for restart, or -2 to quit
     */
    int getPlayerInput();
    
    // =========================================================================
    // NEW: Start the game loop
    // =========================================================================
    /**
     * Starts the main game loop.
     * Handles the flow of getting input, making moves, and updating display.
     */
    void startGameLoop();
    
    // =========================================================================
    // NEW: Clean up resources when game ends
    // =========================================================================
    /**
     * Cleans up any resources used by the view.
     * For GUI, this might close windows.
     * For text-based, this might close streams.
     */
    void cleanup();
}

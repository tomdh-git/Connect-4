package src.main.java.com.connect4.view;

/**
 * GameView.java - NEW INTERFACE
 * 
 * This interface defines the contract for all game views (GUI and Text-based).
 * It follows the Model-View-Controller (MVC) pattern by separating the view
 * from the game logic contained in GameState.
 * 
 * By using this interface, we can easily swap between different view
 * implementations
 * (GUIView, TextView) without changing the game logic.
 * 
 * @author Refactored for multi-view support
 */
public interface GameView {
    /**
     * Initializes and displays the game view.
     * For GUI, this creates and shows the window.
     * For text-based, this sets up the console interface.
     */
    void initialize();

    /**
     * Updates the view to reflect the current state of the game.
     * This should be called after every move or game action.
     */
    void update();

    /**
     * Displays the current state of the game board.
     */
    void displayBoard();

    /**
     * Displays a message about the current game status.
     * 
     * @param message the status message to display
     */
    void displayMessage(String message);

    /**
     * Displays an error message to the user.
     * 
     * @param error the error message to display
     */
    void displayError(String error);

    /**
     * Gets the player's input for column selection.
     * For GUI, this might wait for a click event.
     * For text-based, this reads from console input.
     * 
     * @return the column number (1-7) selected by the player,
     *         0 for undo, -1 for restart, or -2 to quit
     */
    int getPlayerInput();

    /**
     * Starts the main game loop.
     * Handles the flow of getting input, making moves, and updating display.
     */
    void startGameLoop();

    /**
     * Cleans up any resources used by the view.
     * For GUI, this might close windows.
     * For text-based, this might close streams.
     */
    void cleanup();
}

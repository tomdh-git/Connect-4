/**
 * Cell.java - NEW CLASS
 * 
 * This class represents a single cell on the Connect Four game board.
 * It encapsulates the state of a cell using object-oriented principles,
 * replacing the previous Boolean-based representation.
 * 
 * The Cell class provides:
 *   i.   A value representing whether the cell is available or not
 *   ii.  A value stating whether the cell is occupied by a red coin
 *   iii. A value stating whether the cell is occupied by a yellow coin
 *   iv.  A value representing a lucky coin (turquoise with 4-leaf clover)
 * 
 * @author Refactored for multi-view support
 */
public class Cell {
    
    // =========================================================================
    // NEW: Enum to represent all possible cell states
    // =========================================================================
    public enum CellState {
        EMPTY,      // Cell is available for placement
        RED,        // Cell contains a red coin
        YELLOW,     // Cell contains a yellow coin
        LUCKY       // Cell contains a lucky coin (turquoise with 4-leaf clover)
    }
    
    // =========================================================================
    // NEW: Instance variable to store the cell's current state
    // =========================================================================
    private CellState state;
    
    // =========================================================================
    // NEW: Constructor - initializes cell as empty (available)
    // =========================================================================
    public Cell() {
        this.state = CellState.EMPTY;
    }
    
    // =========================================================================
    // NEW: Check if cell is available for a new piece
    // =========================================================================
    /**
     * Checks if the cell is available for placing a new coin.
     * @return true if the cell is empty, false otherwise
     */
    public boolean isAvailable() {
        return state == CellState.EMPTY;
    }
    
    // =========================================================================
    // NEW: Check if cell is occupied by a red coin
    // =========================================================================
    /**
     * Checks if the cell contains a red coin.
     * @return true if occupied by red, false otherwise
     */
    public boolean isRed() {
        return state == CellState.RED;
    }
    
    // =========================================================================
    // NEW: Check if cell is occupied by a yellow coin
    // =========================================================================
    /**
     * Checks if the cell contains a yellow coin.
     * @return true if occupied by yellow, false otherwise
     */
    public boolean isYellow() {
        return state == CellState.YELLOW;
    }
    
    // =========================================================================
    // NEW: Check if cell contains a lucky coin
    // =========================================================================
    /**
     * Checks if the cell contains a lucky coin (turquoise with 4-leaf clover).
     * @return true if it's a lucky coin, false otherwise
     */
    public boolean isLucky() {
        return state == CellState.LUCKY;
    }
    
    // =========================================================================
    // NEW: Check if cell is occupied (not empty)
    // =========================================================================
    /**
     * Checks if the cell is occupied by any type of coin.
     * @return true if occupied, false if empty
     */
    public boolean isOccupied() {
        return state != CellState.EMPTY;
    }
    
    // =========================================================================
    // NEW: Get the current state of the cell
    // =========================================================================
    /**
     * Gets the current state of the cell.
     * @return the CellState enum value
     */
    public CellState getState() {
        return state;
    }
    
    // =========================================================================
    // NEW: Set the cell state
    // =========================================================================
    /**
     * Sets the state of the cell.
     * @param state the new CellState to set
     */
    public void setState(CellState state) {
        this.state = state;
    }
    
    // =========================================================================
    // NEW: Convenience method to place a red coin
    // =========================================================================
    /**
     * Places a red coin in this cell.
     */
    public void setRed() {
        this.state = CellState.RED;
    }
    
    // =========================================================================
    // NEW: Convenience method to place a yellow coin
    // =========================================================================
    /**
     * Places a yellow coin in this cell.
     */
    public void setYellow() {
        this.state = CellState.YELLOW;
    }
    
    // =========================================================================
    // NEW: Convenience method to place a lucky coin
    // =========================================================================
    /**
     * Places a lucky coin (turquoise with 4-leaf clover) in this cell.
     */
    public void setLucky() {
        this.state = CellState.LUCKY;
    }
    
    // =========================================================================
    // NEW: Clear the cell (make it empty/available)
    // =========================================================================
    /**
     * Clears the cell, making it available for new coins.
     */
    public void clear() {
        this.state = CellState.EMPTY;
    }
    
    // =========================================================================
    // NEW: String representation for text-based view
    // =========================================================================
    /**
     * Returns a string representation of the cell for text display.
     * @return a single character representing the cell state
     */
    @Override
    public String toString() {
        switch (state) {
            case RED:
                return "R";
            case YELLOW:
                return "Y";
            case LUCKY:
                return "L";
            default:
                return ".";
        }
    }
    
    // =========================================================================
    // NEW: Get color representation for display purposes
    // =========================================================================
    /**
     * Returns a descriptive color name for the cell contents.
     * @return color name string
     */
    public String getColorName() {
        switch (state) {
            case RED:
                return "Red";
            case YELLOW:
                return "Yellow";
            case LUCKY:
                return "Lucky (Turquoise)";
            default:
                return "Empty";
        }
    }
}

package src.main.java.com.connect4.view;

/**
 * Cell.java - NEW CLASS
 * This class represents a single cell on the Connect Four game board.
 * It encapsulates the state of a cell using object-oriented principles,
 * replacing the previous Boolean-based representation.
 * The Cell class provides:
 * i. A value representing whether the cell is available or not
 * ii. A value stating whether the cell is occupied by a red coin
 * iii. A value stating whether the cell is occupied by a yellow coin
 * iv. A value representing a lucky coin (turquoise with 4-leaf clover)
 * 
 * @author Refactored for multi-view support
 */
public class Cell {
    public enum CellState {
        EMPTY, // Cell is available for placement
        RED, // Cell contains a red coin
        YELLOW, // Cell contains a yellow coin
        BLUE, // Cell contains a blue coin
        GREEN, // Cell contains a green coin
        PURPLE, // Cell contains a purple coin
        ORANGE, // Cell contains an orange coin
        LUCKY // Cell contains a lucky coin (turquoise with 4-leaf clover)
    }

    private CellState state;

    public Cell() {
        this.state = CellState.EMPTY;
    }

    /**
     * Checks if the cell is available for placing a new coin.
     * 
     * @return true if the cell is empty, false otherwise
     */
    public boolean isAvailable() {
        return state == CellState.EMPTY;
    }

    /**
     * Checks if the cell contains a red coin.
     * 
     * @return true if occupied by red, false otherwise
     */
    public boolean isRed() {
        return state == CellState.RED;
    }

    /**
     * Checks if the cell contains a yellow coin.
     * 
     * @return true if occupied by yellow, false otherwise
     */
    public boolean isYellow() {
        return state == CellState.YELLOW;
    }

    /**
     * Checks if the cell contains a blue coin.
     * 
     * @return true if occupied by blue, false otherwise
     */
    public boolean isBlue() {
        return state == CellState.BLUE;
    }

    /**
     * Checks if the cell contains a green coin.
     * 
     * @return true if occupied by green, false otherwise
     */
    public boolean isGreen() {
        return state == CellState.GREEN;
    }

    /**
     * Checks if the cell contains a purple coin.
     * 
     * @return true if occupied by purple, false otherwise
     */
    public boolean isPurple() {
        return state == CellState.PURPLE;
    }

    /**
     * Checks if the cell contains an orange coin.
     * 
     * @return true if occupied by orange, false otherwise
     */
    public boolean isOrange() {
        return state == CellState.ORANGE;
    }

    /**
     * Checks if the cell contains a lucky coin (turquoise with 4-leaf clover).
     * 
     * @return true if it's a lucky coin, false otherwise
     */
    public boolean isLucky() {
        return state == CellState.LUCKY;
    }

    /**
     * Checks if the cell is occupied by any type of coin.
     * 
     * @return true if occupied, false if empty
     */
    public boolean isOccupied() {
        return state != CellState.EMPTY;
    }

    /**
     * Gets the current state of the cell.
     * 
     * @return the CellState enum value
     */
    public CellState getState() {
        return state;
    }

    /**
     * Sets the state of the cell.
     * 
     * @param state the new CellState to set
     */
    public void setState(CellState state) {
        this.state = state;
    }

    /**
     * Places a red coin in this cell.
     */
    public void setRed() {
        this.state = CellState.RED;
    }

    /**
     * Places a yellow coin in this cell.
     */
    public void setYellow() {
        this.state = CellState.YELLOW;
    }

    /**
     * Places a blue coin in this cell.
     */
    public void setBlue() {
        this.state = CellState.BLUE;
    }

    /**
     * Places a green coin in this cell.
     */
    public void setGreen() {
        this.state = CellState.GREEN;
    }

    /**
     * Places a purple coin in this cell.
     */
    public void setPurple() {
        this.state = CellState.PURPLE;
    }

    /**
     * Places an orange coin in this cell.
     */
    public void setOrange() {
        this.state = CellState.ORANGE;
    }

    public void setColor(CellState color) {
        this.state = color;
    }

    /**
     * Places a lucky coin (turquoise with 4-leaf clover) in this cell.
     */
    public void setLucky() {
        this.state = CellState.LUCKY;
    }

    /**
     * Clears the cell, making it available for new coins.
     */
    public void clear() {
        this.state = CellState.EMPTY;
    }

    /**
     * Returns a string representation of the cell for text display.
     * 
     * @return a single character representing the cell state
     */
    @Override
    public String toString() {
        return switch (state) {
            case RED -> "R";
            case YELLOW -> "Y";
            case BLUE -> "B";
            case GREEN -> "G";
            case PURPLE -> "P";
            case ORANGE -> "O";
            case LUCKY -> "L";
            default -> ".";
        };
    }

    /**
     * Returns a descriptive color name for the cell contents.
     * 
     * @return color name string
     */
    public String getColorName() {
        return switch (state) {
            case RED -> "Red";
            case YELLOW -> "Yellow";
            case BLUE -> "Blue";
            case GREEN -> "Green";
            case PURPLE -> "Purple";
            case ORANGE -> "Orange";
            case LUCKY -> "Lucky (Turquoise)";
            default -> "Empty";
        };
    }
}

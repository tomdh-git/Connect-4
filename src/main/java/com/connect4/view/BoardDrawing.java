package src.main.java.com.connect4.view;

import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import javax.swing.*;

import src.main.java.com.connect4.player.Player;

/**
 * BoardDrawing.java - MODIFIED CLASS
 * This class handles the graphical rendering of the Connect Four game board.
 * EXTENDED MODIFICATIONS:
 * - Support for variable board sizes
 * - Dynamic cell sizing based on board dimensions
 * - Player color support beyond red/yellow
 * - Statistics display area
 * 
 * @author Original + Refactored + Extended features
 */
public class BoardDrawing extends JComponent implements MouseListener {

    private GameState state;
    private GUIView guiView;

    // Dynamic board dimensions
    private int boardCols;
    private int boardRows;

    // Calculated dimensions
    private int cellSize;
    private int boardStartX;
    private int boardStartY;
    private int boardWidth;
    private int boardHeight;
    private int holeSize;
    private final int headerHeight = 40;

    private Rectangle[] columnZones;

    public BoardDrawing(GameState gs) {
        state = gs;
        guiView = null;
        updateDimensions();
        addMouseListener(this);
    }

    private void updateDimensions() {
        boardCols = state.getColumns();
        boardRows = state.getRows();

        // Calculate cell size based on available space
        int maxWidth = 700;
        int maxHeight = 450;

        int cellByWidth = maxWidth / (boardCols + 2);
        int cellByHeight = maxHeight / (boardRows + 2);
        cellSize = Math.min(cellByWidth, cellByHeight);
        cellSize = Math.max(cellSize, 20); // Minimum cell size

        holeSize = (int) (cellSize * 0.75);

        boardWidth = boardCols * cellSize + 20;
        boardHeight = boardRows * cellSize + 20;

        boardStartX = (750 - boardWidth) / 2;
        boardStartY = 100;

        // Initialize column zones
        columnZones = new Rectangle[boardCols];
        for (int i = 0; i < boardCols; i++) {
            int x = boardStartX + 10 + i * cellSize + (cellSize - holeSize) / 2 - 5;
            columnZones[i] = new Rectangle(x, boardStartY - headerHeight - 20,
                    holeSize + 10, headerHeight);
        }
    }

    public void setGUIView(GUIView view) {
        this.guiView = view;
    }

    @Override
    public Dimension getPreferredSize() {
        int width = Math.max(750, boardCols * cellSize + 150);
        int height = Math.max(500, boardRows * cellSize + 200);
        return new Dimension(width, height);
    }

    public void setGameState(GameState newState) {
        this.state = newState;
        updateDimensions();
        repaint();
    }

    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Update dimensions in case they changed
        if (boardCols != state.getColumns() || boardRows != state.getRows()) {
            updateDimensions();
        }

        // Draw column numbers (above board, no button backgrounds)
        g2.setFont(new Font("SansSerif", Font.BOLD, Math.min(16, cellSize / 2)));
        g2.setColor(Color.DARK_GRAY);
        for (int i = 0; i < boardCols; i++) {
            String num = Integer.toString(i + 1);
            int w = g2.getFontMetrics().stringWidth(num);
            int x = boardStartX + 10 + i * cellSize + (cellSize - w) / 2;
            int y = boardStartY - 15;
            g2.drawString(num, x, y);
        }

        // Draw row numbers
        g2.setColor(Color.DARK_GRAY);
        for (int i = 0; i < boardRows; i++) {
            String num = Integer.toString(i + 1);
            int w = g2.getFontMetrics().stringWidth(num);
            int y = boardStartY + 10 + (boardRows - 1 - i) * cellSize + (cellSize + 10) / 2;
            g2.drawString(num, boardStartX - 20, y);
        }

        // Draw instruction
        g2.setColor(Color.DARK_GRAY);
        g2.setFont(new Font("SansSerif", Font.ITALIC, 12));
        String instr = "Click column to drop piece";
        int instrW = g2.getFontMetrics().stringWidth(instr);
        g2.drawString(instr, boardStartX + (boardWidth - instrW) / 2, boardStartY - 35);

        // Draw board
        g2.setColor(Color.BLUE);
        g2.fillRoundRect(boardStartX, boardStartY, boardWidth, boardHeight, 15, 15);

        // Draw cells
        Cell[][] cells = state.getCells();
        for (int col = 0; col < boardCols; col++) {
            for (int row = 0; row < boardRows; row++) {
                int x = boardStartX + 10 + col * cellSize + (cellSize - holeSize) / 2;
                int y = boardStartY + 10 + (boardRows - 1 - row) * cellSize + (cellSize - holeSize) / 2;

                Cell cell = cells[col][row];

                if (cell.isAvailable()) {
                    g2.setColor(Color.WHITE);
                    g2.fill(new Ellipse2D.Double(x, y, holeSize, holeSize));
                } else if (cell.isRed()) {
                    g2.setColor(getPlayerColor(Player.CoinColor.RED));
                    g2.fill(new Ellipse2D.Double(x, y, holeSize, holeSize));
                } else if (cell.isYellow()) {
                    g2.setColor(getPlayerColor(Player.CoinColor.YELLOW));
                    g2.fill(new Ellipse2D.Double(x, y, holeSize, holeSize));
                } else if (cell.isLucky()) {
                    drawLuckyCoin(g2, x, y, holeSize);
                }
            }
        }

        // Draw status message
        g2.setFont(new Font("SansSerif", Font.BOLD, 18));
        String status = state.getStatusMessage();

        // Determine which player's color to show
        Player displayPlayer;
        if (state.getGameOver()) {
            // Game is over - show the winner's color (or current if it's a tie)
            if (state.getPlayer1Wins()) {
                displayPlayer = state.getSettings().getPlayer1();
            } else if (state.getPlayer2Wins()) {
                displayPlayer = state.getSettings().getPlayer2();
            } else {
                // Tie - show current player
                displayPlayer = state.getCurrentPlayer();
            }
        } else {
            // Game in progress - show current player
            displayPlayer = state.getCurrentPlayer();
        }

        int statusY = boardStartY + boardHeight + 35;
        g2.setColor(Color.LIGHT_GRAY);
        g2.fillRoundRect(boardStartX + boardWidth / 2 - 120, statusY - 25, 240, 35, 10, 10);

        g2.setColor(getPlayerColor(displayPlayer.getCoinColor()));
        int sw = g2.getFontMetrics().stringWidth(status);
        g2.drawString(status, boardStartX + (boardWidth - sw) / 2, statusY);

        // Draw statistics
        g2.setFont(new Font("SansSerif", Font.PLAIN, 12));
        g2.setColor(Color.DARK_GRAY);
        String stats = state.getStatsDisplay();
        int statsW = g2.getFontMetrics().stringWidth(stats);
        g2.drawString(stats, boardStartX + (boardWidth - statsW) / 2, statusY + 25);

        // Draw error if any
        if (state.getError() != null) {
            g2.setColor(Color.RED);
            g2.setFont(new Font("SansSerif", Font.BOLD, 14));
            g2.drawString("Error: " + state.getError(), 15, boardStartY + boardHeight + 70);
        }
    }

    private Color getPlayerColor(Player.CoinColor coinColor) {
        return switch (coinColor) {
            case RED -> Color.RED;
            case YELLOW -> Color.YELLOW;
            case BLUE -> new Color(100, 149, 237);
            case GREEN -> new Color(34, 139, 34);
            case PURPLE -> new Color(148, 0, 211);
            case ORANGE -> new Color(255, 165, 0);
        };
    }

    private void drawLuckyCoin(Graphics2D g2, int x, int y, int size) {
        Color turquoise = new Color(64, 224, 208);
        g2.setColor(turquoise);
        g2.fill(new Ellipse2D.Double(x, y, size, size));

        Color darkGreen = new Color(0, 100, 0);
        g2.setColor(darkGreen);

        int centerX = x + size / 2;
        int centerY = y + size / 2;
        int leafSize = size / 5;
        double leafStart = (double) leafSize / 2;

        g2.fill(new Ellipse2D.Double(centerX - leafStart, centerY - leafSize - 2, leafSize, leafSize));
        g2.fill(new Ellipse2D.Double(centerX - leafStart, centerY + 2, leafSize, leafSize));
        g2.fill(new Ellipse2D.Double(centerX - leafSize - 2, centerY - leafStart, leafSize, leafSize));
        g2.fill(new Ellipse2D.Double(centerX + 2, centerY - leafStart, leafSize, leafSize));
        g2.fillRect(centerX - 1, centerY, 2, size / 4);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int clickX = e.getX();
        int clickY = e.getY();

        // Check if click is within the board area (vertically)
        if (clickY >= boardStartY && clickY <= boardStartY + boardHeight) {
            // Determine which column was clicked based on X position
            for (int i = 0; i < boardCols; i++) {
                int colStartX = boardStartX + 10 + i * cellSize;
                int colEndX = colStartX + cellSize;

                if (clickX >= colStartX && clickX < colEndX) {
                    if (guiView != null) {
                        guiView.onColumnClicked(i + 1);
                    }
                    break;
                }
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
}

import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;

/**
 * BoardDrawing.java - MODIFIED CLASS (Extended Features)
 * 
 * This class handles the graphical rendering of the Connect Four game board.
 * 
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
    private int headerHeight = 40;
    
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
        
        holeSize = (int)(cellSize * 0.75);
        
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
        
        // Draw column headers
        g2.setFont(new Font("SansSerif", Font.BOLD, Math.min(16, cellSize / 2)));
        for (int i = 0; i < boardCols; i++) {
            g2.setColor(new Color(100, 100, 200));
            g2.fill(columnZones[i]);
            g2.setColor(Color.DARK_GRAY);
            g2.draw(columnZones[i]);
            
            g2.setColor(Color.WHITE);
            String num = Integer.toString(i + 1);
            int w = g2.getFontMetrics().stringWidth(num);
            g2.drawString(num, 
                columnZones[i].x + (columnZones[i].width - w) / 2,
                columnZones[i].y + columnZones[i].height - 10);
        }
        
        // Draw instruction
        g2.setColor(Color.DARK_GRAY);
        g2.setFont(new Font("SansSerif", Font.ITALIC, 12));
        String instr = "Click column number to drop piece";
        int instrW = g2.getFontMetrics().stringWidth(instr);
        g2.drawString(instr, boardStartX + (boardWidth - instrW) / 2, boardStartY - headerHeight - 30);
        
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
        Player current = state.getCurrentPlayer();
        
        int statusY = boardStartY + boardHeight + 35;
        g2.setColor(Color.LIGHT_GRAY);
        g2.fillRoundRect(boardStartX + boardWidth/2 - 120, statusY - 25, 240, 35, 10, 10);
        
        g2.setColor(getPlayerColor(current.getCoinColor()));
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
        switch (coinColor) {
            case RED: return Color.RED;
            case YELLOW: return Color.YELLOW;
            case BLUE: return new Color(30, 144, 255);
            case GREEN: return new Color(34, 139, 34);
            case PURPLE: return new Color(148, 0, 211);
            case ORANGE: return new Color(255, 165, 0);
            default: return Color.GRAY;
        }
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
        
        g2.fill(new Ellipse2D.Double(centerX - leafSize/2, centerY - leafSize - 2, leafSize, leafSize));
        g2.fill(new Ellipse2D.Double(centerX - leafSize/2, centerY + 2, leafSize, leafSize));
        g2.fill(new Ellipse2D.Double(centerX - leafSize - 2, centerY - leafSize/2, leafSize, leafSize));
        g2.fill(new Ellipse2D.Double(centerX + 2, centerY - leafSize/2, leafSize, leafSize));
        g2.fillRect(centerX - 1, centerY, 2, size / 4);
    }
    
    @Override
    public void mouseClicked(MouseEvent e) {
        for (int i = 0; i < boardCols; i++) {
            if (columnZones[i].contains(e.getPoint())) {
                if (guiView != null) {
                    guiView.onColumnClicked(i + 1);
                }
                break;
            }
        }
    }
    
    @Override public void mousePressed(MouseEvent e) {}
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}
}

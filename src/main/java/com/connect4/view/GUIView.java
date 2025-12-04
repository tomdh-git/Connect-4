package src.main.java.com.connect4.view;

import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.InvocationTargetException;
import javax.swing.*;

import src.main.java.com.connect4.player.AIPlayer;
import src.main.java.com.connect4.player.Player;
import src.main.java.com.connect4.settings.DifficultyLevel;
import src.main.java.com.connect4.settings.GameSettings;
import src.main.java.com.connect4.settings.SaveLoadManager;

/**
 * GUIView.java - MODIFIED CLASS
 * EXTENDED FEATURES:
 * - Configuration dialog for game mode and difficulty
 * - Save/Load menu options
 * - Statistics display
 * - AI opponent integration
 * - Variable board size support
 * - Lucky coin offer accept/reject dialog
 * - Four corners win mode for square boards
 * 
 * @author Created for multi-view support + Extended features
 */
public class GUIView implements GameView {

    private GameState state;
    private JFrame frame;
    private BoardDrawing board;
    private AIPlayer aiPlayer;

    private volatile boolean windowClosed = false;
    private final Object windowLock = new Object();

    public GUIView(GameState gameState) {
        this.state = gameState;
        this.aiPlayer = null;
    }

    private void triggerAIMove() {
        if (aiPlayer == null || state.getGameOver() || windowClosed)
            return;
        if (!state.getCurrentPlayer().isComputer())
            return;

        Timer timer = new Timer(300, e -> {
            ((Timer) e.getSource()).stop();
            makeAIMove();
        });
        timer.setRepeats(false);
        timer.start();
    }

    @Override
    public void initialize() {
        if (SwingUtilities.isEventDispatchThread()) {
            createAndShowGUI();
        } else {
            try {
                SwingUtilities.invokeAndWait(this::createAndShowGUI);
            } catch (InterruptedException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    private void createAndShowGUI() {
        frame = new JFrame("CONNECT 4 - GUI Mode");
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                handleWindowClose();
            }
        });

        JMenuBar menuBar = createMenuBar();
        frame.setJMenuBar(menuBar);

        board = new BoardDrawing(state);
        board.setGUIView(this);

        JButton btnUndo = new JButton("Undo");
        btnUndo.addActionListener(e -> performUndo());

        JButton btnRestart = new JButton("Restart");
        btnRestart.addActionListener(e -> performRestart());

        JButton btnNewGame = new JButton("New Game");
        btnNewGame.addActionListener(e -> showConfigDialog());

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(btnUndo);
        buttonPanel.add(btnRestart);
        buttonPanel.add(btnNewGame);

        frame.add(board, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        // Get screen dimensions to avoid windows larger than screen
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int maxWidth = (int) (screenSize.width * 0.9); // 90% of screen width
        int maxHeight = (int) (screenSize.height * 0.85); // 85% of screen height

        // Calculate ideal window size based on board size
        int width = Math.max(750, state.getColumns() * 50 + 200);
        int height = Math.max(600, state.getRows() * 50 + 300);

        // Cap at screen size
        width = Math.min(width, maxWidth);
        height = Math.min(height, maxHeight);

        frame.setSize(width, height);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        setupAIIfNeeded();
        update();
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu gameMenu = getJMenu();

        JMenu statsMenu = new JMenu("Stats");

        JMenuItem viewStats = new JMenuItem("View Statistics");
        viewStats.addActionListener(e -> showStats());

        JMenuItem resetStats = new JMenuItem("Reset Statistics");
        resetStats.addActionListener(e -> resetStats());

        statsMenu.add(viewStats);
        statsMenu.add(resetStats);

        JMenu viewMenu = new JMenu("View");

        JMenuItem switchToText = new JMenuItem("Switch to Text Mode");
        switchToText.addActionListener(e -> switchToTextView());

        JMenuItem exitGame = new JMenuItem("Exit Game");
        exitGame.addActionListener(e -> backToMainMenu());

        viewMenu.add(switchToText);
        viewMenu.addSeparator();
        viewMenu.add(exitGame);

        menuBar.add(gameMenu);
        menuBar.add(viewMenu);
        menuBar.add(statsMenu);

        return menuBar;
    }

    private JMenu getJMenu() {
        JMenu gameMenu = new JMenu("Game");

        JMenuItem newGameItem = new JMenuItem("New Game...");
        newGameItem.addActionListener(e -> showConfigDialog());

        JMenuItem saveItem = new JMenuItem("Save Game...");
        saveItem.addActionListener(e -> handleSave());

        JMenuItem loadItem = new JMenuItem("Load Game...");
        loadItem.addActionListener(e -> handleLoad());

        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> handleWindowClose());

        gameMenu.add(newGameItem);
        gameMenu.addSeparator();
        gameMenu.add(saveItem);
        gameMenu.add(loadItem);
        gameMenu.addSeparator();
        gameMenu.add(exitItem);
        return gameMenu;
    }

    private void switchToTextView() {
        int choice = JOptionPane.showConfirmDialog(
                frame,
                "Switch to Text Mode?\n\nThe game will continue in the TERMINAL window.\n\nLook for the console to type your moves.",
                "Switch View",
                JOptionPane.YES_NO_OPTION);

        if (choice == JOptionPane.YES_OPTION) {
            frame.dispose();
            windowClosed = true;

            System.out.println("\n" + "=".repeat(50));
            System.out.println("  SWITCHED TO TEXT MODE - Type commands here!");
            System.out.println("=".repeat(50) + "\n");

            TextView textView = new TextView(state);
            textView.initialize();
            textView.startGameLoop();
        }
    }

    private void backToMainMenu() {
        int choice = JOptionPane.showConfirmDialog(
                frame,
                "Exit game?\nUnsaved progress will be lost.",
                "Exit",
                JOptionPane.YES_NO_OPTION);

        if (choice == JOptionPane.YES_OPTION) {
            synchronized (windowLock) {
                windowClosed = true;
                windowLock.notifyAll();
            }
            frame.dispose();
            System.exit(0);
        }
    }

    private void showConfigDialog() {
        JDialog dialog = new JDialog(frame, "New Game Configuration", true);
        dialog.setLayout(new BorderLayout(10, 10));

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(new JLabel("Game Mode:"), gbc);

        JComboBox<String> modeCombo = new JComboBox<>(new String[] { "Two Players", "Vs Computer" });
        gbc.gridx = 1;
        mainPanel.add(modeCombo, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        mainPanel.add(new JLabel("Four Corners Rule:"), gbc);

        JCheckBox fourCornersCheck = new JCheckBox("Enable (Any Square)");
        gbc.gridx = 1;
        mainPanel.add(fourCornersCheck, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel diffLabel = new JLabel("Difficulty:");
        mainPanel.add(diffLabel, gbc);

        JComboBox<String> diffCombo = new JComboBox<>(new String[] {
                "Beginner (7x6)", "Intermediate (14x12)", "Expert (21x18)"
        });
        diffCombo.setEnabled(false);
        gbc.gridx = 1;
        mainPanel.add(diffCombo, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        mainPanel.add(new JLabel("Player 1 Name:"), gbc);

        JTextField p1Name = new JTextField("Player 1", 15);
        gbc.gridx = 1;
        mainPanel.add(p1Name, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        mainPanel.add(new JLabel("Player 1 Color:"), gbc);

        JComboBox<String> p1Color = new JComboBox<>(
                new String[] { "Red", "Yellow", "Blue", "Green", "Purple", "Orange" });
        gbc.gridx = 1;
        mainPanel.add(p1Color, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        JLabel p2Label = new JLabel("Player 2 Name:");
        mainPanel.add(p2Label, gbc);

        JTextField p2Name = new JTextField("Player 2", 15);
        gbc.gridx = 1;
        mainPanel.add(p2Name, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        mainPanel.add(new JLabel("Player 2 Color:"), gbc);

        JComboBox<String> p2Color = new JComboBox<>(
                new String[] { "Red", "Yellow", "Blue", "Green", "Purple", "Orange" });
        p2Color.setSelectedIndex(1); // Default to Yellow
        gbc.gridx = 1;
        mainPanel.add(p2Color, gbc);

        modeCombo.addActionListener(e -> {
            boolean vsComputer = modeCombo.getSelectedIndex() == 1;
            diffCombo.setEnabled(vsComputer);
            p2Name.setEnabled(!vsComputer);
            p2Label.setText(vsComputer ? "Computer:" : "Player 2 Name:");
            if (vsComputer)
                p2Name.setText("Computer");
            else
                p2Name.setText("Player 2");
        });

        JPanel btnPanel = new JPanel(new FlowLayout());
        JButton startBtn = new JButton("Start Game");
        JButton cancelBtn = new JButton("Cancel");

        startBtn.addActionListener(e -> {
            boolean vsComputer = modeCombo.getSelectedIndex() == 1;
            boolean useFourCorners = fourCornersCheck.isSelected();
            int diffIndex = diffCombo.getSelectedIndex() + 1;

            DifficultyLevel level = DifficultyLevel.fromLevel(diffIndex);

            Player.CoinColor color1 = Player.CoinColor.values()[p1Color.getSelectedIndex()];
            Player.CoinColor color2 = Player.CoinColor.values()[p2Color.getSelectedIndex()];

            if (color1 == color2) {
                JOptionPane.showMessageDialog(dialog, "Players must have different colors!",
                        "Invalid Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Player player1 = new Player(1, p1Name.getText(), Player.PlayerType.HUMAN, color1);

            GameSettings settings;
            if (vsComputer) {
                settings = new GameSettings(level, player1, true, useFourCorners);
            } else {
                Player player2 = new Player(2, p2Name.getText(), Player.PlayerType.HUMAN, color2);
                settings = new GameSettings(player1, player2, useFourCorners);
                settings.setDifficultyLevel(level);
            }

            state = new GameState(settings);
            board.setGameState(state);
            setupAIIfNeeded();

            // Get screen dimensions
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            int maxWidth = (int) (screenSize.width * 0.9);
            int maxHeight = (int) (screenSize.height * 0.85);

            // Calculate and cap window size
            int width = Math.min(Math.max(750, state.getColumns() * 50 + 200), maxWidth);
            int height = Math.min(Math.max(600, state.getRows() * 50 + 300), maxHeight);
            frame.setSize(width, height);

            dialog.dispose();
            update();
        });

        cancelBtn.addActionListener(e -> dialog.dispose());

        btnPanel.add(startBtn);
        btnPanel.add(cancelBtn);

        dialog.add(mainPanel, BorderLayout.CENTER);
        dialog.add(btnPanel, BorderLayout.SOUTH);
        dialog.pack();
        dialog.setLocationRelativeTo(frame);
        dialog.setVisible(true);
    }

    private void handleSave() {
        String name = JOptionPane.showInputDialog(frame,
                "Enter save name:", "Save Game", JOptionPane.PLAIN_MESSAGE);

        if (name != null && !name.trim().isEmpty()) {
            if (SaveLoadManager.saveGame(state, name.trim(), "GUI save")) {
                JOptionPane.showMessageDialog(frame, "Game saved successfully!");
            } else {
                JOptionPane.showMessageDialog(frame, "Failed to save game.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else if (name != null) {
            String filename = SaveLoadManager.saveGameAuto(state, "Quick save");
            if (filename != null) {
                JOptionPane.showMessageDialog(frame, "Game saved: " + filename);
            }
        }
    }

    private void handleLoad() {
        String[] saves = SaveLoadManager.listSaves();
        if (saves.length == 0) {
            JOptionPane.showMessageDialog(frame, "No saved games found.");
            return;
        }

        String[] summaries = SaveLoadManager.getSaveSummaries();
        String selected = (String) JOptionPane.showInputDialog(frame,
                "Select a save to load:", "Load Game",
                JOptionPane.PLAIN_MESSAGE, null, summaries, summaries[0]);

        if (selected != null) {
            int index = java.util.Arrays.asList(summaries).indexOf(selected);
            if (index >= 0) {
                SaveLoadManager.GameSnapshot snapshot = SaveLoadManager.loadGame(saves[index]);
                if (snapshot != null) {
                    state = SaveLoadManager.applySnapshot(snapshot);
                    board.setGameState(state);
                    setupAIIfNeeded();

                    // Get screen dimensions
                    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                    int maxWidth = (int) (screenSize.width * 0.9);
                    int maxHeight = (int) (screenSize.height * 0.85);

                    // Calculate and cap window size
                    int width = Math.min(Math.max(750, state.getColumns() * 50 + 200), maxWidth);
                    int height = Math.min(Math.max(600, state.getRows() * 50 + 300), maxHeight);
                    frame.setSize(width, height);

                    update();
                    JOptionPane.showMessageDialog(frame, "Game loaded successfully!");
                } else {
                    JOptionPane.showMessageDialog(frame, "Failed to load game.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void showStats() {
        String stats = "=== Game Statistics ===\n\n" +
                state.getSettings().getPlayer1().getStatsString() + "\n" +
                state.getSettings().getPlayer2().getStatsString();
        JOptionPane.showMessageDialog(frame, stats, "Statistics", JOptionPane.INFORMATION_MESSAGE);
    }

    private void resetStats() {
        int result = JOptionPane.showConfirmDialog(frame,
                "Are you sure you want to reset all statistics?",
                "Reset Statistics", JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            state.getSettings().getPlayer1().resetStats();
            state.getSettings().getPlayer2().resetStats();
            update();
            JOptionPane.showMessageDialog(frame, "Statistics reset.");
        }
    }

    private void setupAIIfNeeded() {
        if (state.getSettings().isVsComputer()) {
            Player computer = state.getSettings().getComputerPlayer();
            if (computer != null) {
                aiPlayer = new AIPlayer(state.getSettings().getDifficultyLevel(),
                        computer.getId());
            }
        } else {
            aiPlayer = null;
        }
    }

    private void performUndo() {
        if (state.undo()) {
            update();
        } else if (state.getError() != null) {
            JOptionPane.showMessageDialog(frame, state.getError(),
                    "Cannot Undo", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void performRestart() {
        state.restart();
        update();
    }

    private void handleWindowClose() {
        int result = JOptionPane.showConfirmDialog(frame,
                "Are you sure you want to quit?", "Confirm Exit", JOptionPane.YES_NO_OPTION);

        if (result == JOptionPane.YES_OPTION) {
            synchronized (windowLock) {
                windowClosed = true;
                windowLock.notifyAll();
            }
            frame.dispose();
        }
    }

    public void onColumnClicked(int column) {
        if (state.getGameOver()) {
            showGameOverMessage();
            return;
        }

        if (state.isLuckyOfferPending()) {
            // Check if the lucky coin belongs to a human player
            Player luckyOwner = state.getLuckyOfferPlayer();
            if (luckyOwner != null && luckyOwner.isHuman()) {
                showLuckyOfferDialog();
            }
            // If it belongs to computer, AI will handle it automatically
            return;
        }

        if (aiPlayer != null && state.getCurrentPlayer().isComputer()) {
            return;
        }

        if (state.move(column)) {
            update();

            if (state.isLuckyOfferPending()) {
                // Check if the lucky coin belongs to a human player
                Player luckyOwner = state.getLuckyOfferPlayer();
                if (luckyOwner != null && luckyOwner.isHuman()) {
                    showLuckyOfferDialog();
                } else {
                    // Computer's lucky coin - trigger AI to handle it
                    triggerAIMove();
                }
            } else if (state.getGameOver()) {
                showGameOverMessage();
            } else {
                triggerAIMove();
            }
        } else if (state.getError() != null) {
            JOptionPane.showMessageDialog(frame, state.getError(),
                    "Invalid Move", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void showLuckyOfferDialog() {
        String message = String.format(
                """
                        A Lucky Coin appeared at column %d, row %d!

                        Do you want to claim it?""",
                state.getLuckyOfferColumn() + 1,
                state.getLuckyOfferRow() + 1);

        int result = JOptionPane.showConfirmDialog(frame,
                message, "Lucky Coin Offer!",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (result == JOptionPane.YES_OPTION) {
            state.acceptLuckyOffer();
        } else {
            state.rejectLuckyOffer();
        }

        update();

        if (state.getGameOver()) {
            showGameOverMessage();
        } else {
            triggerAIMove();
        }
    }

    private void makeAIMove() {
        if (aiPlayer == null || state.getGameOver())
            return;

        if (state.isLuckyOfferPending()) {
            boolean shouldAccept = aiPlayer.shouldAcceptLuckyOffer(state);
            if (shouldAccept) {
                state.acceptLuckyOffer();
            } else {
                state.rejectLuckyOffer();
            }
            update();

            if (state.getGameOver()) {
                showGameOverMessage();
            }
            return;
        }

        frame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        SwingWorker<Integer, Void> worker = new SwingWorker<>() {
            @Override
            protected Integer doInBackground() {
                return aiPlayer.getBestMove(state);
            }

            @Override
            protected void done() {
                try {
                    int move = get();
                    if (move > 0) {
                        state.move(move);
                        update();

                        if (state.isLuckyOfferPending()) {
                            boolean shouldAccept = aiPlayer.shouldAcceptLuckyOffer(state);
                            if (shouldAccept) {
                                state.acceptLuckyOffer();
                            } else {
                                state.rejectLuckyOffer();
                            }
                            update();
                        }

                        if (state.getGameOver()) {
                            showGameOverMessage();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    frame.setCursor(Cursor.getDefaultCursor());
                }
            }
        };
        worker.execute();
    }

    private void showGameOverMessage() {
        String message;
        String title;

        if (state.getPlayer1Wins()) {
            message = state.getSettings().getPlayer1().getName() + " wins!";
            if (state.getSettings().getDifficultyLevel().isFourCornersEnabled()) {
                message += "\n(Four Corners Victory!)";
            }
            title = "Game Over";
        } else if (state.getPlayer2Wins()) {
            message = state.getSettings().getPlayer2().getName() + " wins!";
            if (state.getSettings().getDifficultyLevel().isFourCornersEnabled()) {
                message += "\n(Four Corners Victory!)";
            }
            title = "Game Over";
        } else {
            message = "The game ends in a tie!";
            title = "It's a Tie!";
        }

        message += "\n\nPlay again?";

        int result = JOptionPane.showConfirmDialog(frame, message, title,
                JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);

        if (result == JOptionPane.YES_OPTION) {
            state.restart();
            update();
            triggerAIMove();
        }
    }

    @Override
    public void update() {
        if (board != null)
            board.repaint();
    }

    @Override
    public void displayBoard() {
        update();
    }

    @Override
    public void displayMessage(String message) {
        update();
    }

    @Override
    public void displayError(String error) {
        update();
    }

    @Override
    public int getPlayerInput() {
        return 0;
    }

    @Override
    public void startGameLoop() {
        triggerAIMove();

        if (SwingUtilities.isEventDispatchThread()) {
            return;
        }

        synchronized (windowLock) {
            while (!windowClosed) {
                try {
                    windowLock.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }
    }

    @Override
    public void cleanup() {
        if (frame != null) {
            SwingUtilities.invokeLater(() -> frame.dispose());
        }
        synchronized (windowLock) {
            windowClosed = true;
            windowLock.notifyAll();
        }
    }
}

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class MainMenu extends JFrame {
    
    private JPanel mainPanel;
    private CardLayout cardLayout;
    
    private GameSettings currentSettings;
    
    public MainMenu() {
        super("Connect Four - Extended Edition");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 400);
        setLocationRelativeTo(null);
        setResizable(false);
        
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        
        mainPanel.add(createTitleScreen(), "title");
        mainPanel.add(createModeScreen(), "mode");
        mainPanel.add(createViewTypeScreen(), "viewType");
        mainPanel.add(createBoardTypeScreen(), "boardType");
        mainPanel.add(createDifficultyScreen(), "difficulty");
        mainPanel.add(createPlayerSetupScreen(), "playerSetup");
        mainPanel.add(createVsComputerSetupScreen(), "vsComputerSetup");
        
        add(mainPanel);
        cardLayout.show(mainPanel, "title");
    }
    
    private JPanel createTitleScreen() {
        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBackground(new Color(0, 51, 102));
        panel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        
        JLabel titleLabel = new JLabel("CONNECT FOUR", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 42));
        titleLabel.setForeground(Color.WHITE);
        
        JLabel subtitleLabel = new JLabel("Extended Edition", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Arial", Font.ITALIC, 18));
        subtitleLabel.setForeground(new Color(255, 215, 0));
        
        JPanel titlePanel = new JPanel(new GridLayout(2, 1, 0, 5));
        titlePanel.setOpaque(false);
        titlePanel.add(titleLabel);
        titlePanel.add(subtitleLabel);
        
        JPanel buttonPanel = new JPanel(new GridLayout(4, 1, 10, 15));
        buttonPanel.setOpaque(false);
        
        JButton newGameBtn = createMenuButton("New Game");
        JButton loadGameBtn = createMenuButton("Load Game");
        JButton statsBtn = createMenuButton("Statistics");
        JButton exitBtn = createMenuButton("Exit");
        
        newGameBtn.addActionListener(e -> cardLayout.show(mainPanel, "mode"));
        loadGameBtn.addActionListener(e -> loadGame());
        statsBtn.addActionListener(e -> showStats());
        exitBtn.addActionListener(e -> System.exit(0));
        
        buttonPanel.add(newGameBtn);
        buttonPanel.add(loadGameBtn);
        buttonPanel.add(statsBtn);
        buttonPanel.add(exitBtn);
        
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        centerPanel.setOpaque(false);
        centerPanel.add(buttonPanel);
        
        panel.add(titlePanel, BorderLayout.NORTH);
        panel.add(centerPanel, BorderLayout.CENTER);
        
        JLabel footer = new JLabel("Four in a Row | Four Corners | Lucky Coins", SwingConstants.CENTER);
        footer.setForeground(new Color(150, 150, 150));
        footer.setFont(new Font("Arial", Font.PLAIN, 12));
        panel.add(footer, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createModeScreen() {
        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBackground(new Color(0, 51, 102));
        panel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        
        JLabel label = new JLabel("Select Game Mode", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 28));
        label.setForeground(Color.WHITE);
        
        JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 10, 20));
        buttonPanel.setOpaque(false);
        
        JButton twoPlayerBtn = createMenuButton("Two Players");
        JButton vsComputerBtn = createMenuButton("Vs Computer");
        JButton backBtn = createMenuButton("Back");
        
        twoPlayerBtn.addActionListener(e -> {
            currentSettings = null;
            mainPanel.putClientProperty("gameMode", "twoPlayer");
            cardLayout.show(mainPanel, "viewType");
        });
        
        vsComputerBtn.addActionListener(e -> {
            currentSettings = null;
            mainPanel.putClientProperty("gameMode", "vsComputer");
            cardLayout.show(mainPanel, "viewType");
        });
        
        backBtn.addActionListener(e -> cardLayout.show(mainPanel, "title"));
        
        buttonPanel.add(twoPlayerBtn);
        buttonPanel.add(vsComputerBtn);
        buttonPanel.add(backBtn);
        
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        centerPanel.setOpaque(false);
        centerPanel.add(buttonPanel);
        
        panel.add(label, BorderLayout.NORTH);
        panel.add(centerPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createViewTypeScreen() {
        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBackground(new Color(0, 51, 102));
        panel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        
        JLabel label = new JLabel("Select View Type", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 28));
        label.setForeground(Color.WHITE);
        
        JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 10, 20));
        buttonPanel.setOpaque(false);
        
        JButton guiBtn = createMenuButton("Graphical Interface (GUI)");
        JButton textBtn = createMenuButton("Text Mode (Console)");
        JButton backBtn = createMenuButton("Back");
        
        guiBtn.addActionListener(e -> {
            mainPanel.putClientProperty("viewType", "gui");
            cardLayout.show(mainPanel, "boardType");
        });
        
        textBtn.addActionListener(e -> {
            mainPanel.putClientProperty("viewType", "text");
            cardLayout.show(mainPanel, "boardType");
        });
        
        backBtn.addActionListener(e -> cardLayout.show(mainPanel, "mode"));
        
        buttonPanel.add(guiBtn);
        buttonPanel.add(textBtn);
        buttonPanel.add(backBtn);
        
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        centerPanel.setOpaque(false);
        centerPanel.add(buttonPanel);
        
        JLabel info = new JLabel("<html><center>GUI: Click buttons to play<br>Text: Type commands in console</center></html>", SwingConstants.CENTER);
        info.setForeground(new Color(200, 200, 200));
        info.setFont(new Font("Arial", Font.PLAIN, 12));
        
        panel.add(label, BorderLayout.NORTH);
        panel.add(centerPanel, BorderLayout.CENTER);
        panel.add(info, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createBoardTypeScreen() {
        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBackground(new Color(0, 51, 102));
        panel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        
        JLabel label = new JLabel("Select Board Type", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 28));
        label.setForeground(Color.WHITE);
        
        JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 10, 20));
        buttonPanel.setOpaque(false);
        
        JButton rectBtn = createMenuButton("Rectangular (Standard)");
        JButton squareBtn = createMenuButton("Square (Four Corners Mode)");
        JButton backBtn = createMenuButton("Back");
        
        rectBtn.addActionListener(e -> {
            mainPanel.putClientProperty("boardType", "rectangular");
            cardLayout.show(mainPanel, "difficulty");
        });
        
        squareBtn.addActionListener(e -> {
            mainPanel.putClientProperty("boardType", "square");
            cardLayout.show(mainPanel, "difficulty");
        });
        
        backBtn.addActionListener(e -> cardLayout.show(mainPanel, "viewType"));
        
        buttonPanel.add(rectBtn);
        buttonPanel.add(squareBtn);
        buttonPanel.add(backBtn);
        
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        centerPanel.setOpaque(false);
        centerPanel.add(buttonPanel);
        
        JLabel info = new JLabel("<html><center>Square boards enable Four Corners win mode:<br>Capture all 4 corners to win!</center></html>", SwingConstants.CENTER);
        info.setForeground(new Color(200, 200, 200));
        info.setFont(new Font("Arial", Font.PLAIN, 12));
        
        panel.add(label, BorderLayout.NORTH);
        panel.add(centerPanel, BorderLayout.CENTER);
        panel.add(info, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createDifficultyScreen() {
        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBackground(new Color(0, 51, 102));
        panel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        
        JLabel label = new JLabel("Select Difficulty", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 28));
        label.setForeground(Color.WHITE);
        
        JPanel buttonPanel = new JPanel(new GridLayout(4, 1, 10, 15));
        buttonPanel.setOpaque(false);
        
        JButton beginnerBtn = createMenuButton("Beginner (7x6 or 7x7)");
        JButton intermediateBtn = createMenuButton("Intermediate (14x12 or 12x12)");
        JButton expertBtn = createMenuButton("Expert (21x18 or 18x18)");
        JButton backBtn = createMenuButton("Back");
        
        beginnerBtn.addActionListener(e -> {
            mainPanel.putClientProperty("difficulty", 1);
            String mode = (String) mainPanel.getClientProperty("gameMode");
            if ("vsComputer".equals(mode)) {
                cardLayout.show(mainPanel, "vsComputerSetup");
            } else {
                cardLayout.show(mainPanel, "playerSetup");
            }
        });
        
        intermediateBtn.addActionListener(e -> {
            mainPanel.putClientProperty("difficulty", 2);
            String mode = (String) mainPanel.getClientProperty("gameMode");
            if ("vsComputer".equals(mode)) {
                cardLayout.show(mainPanel, "vsComputerSetup");
            } else {
                cardLayout.show(mainPanel, "playerSetup");
            }
        });
        
        expertBtn.addActionListener(e -> {
            mainPanel.putClientProperty("difficulty", 3);
            String mode = (String) mainPanel.getClientProperty("gameMode");
            if ("vsComputer".equals(mode)) {
                cardLayout.show(mainPanel, "vsComputerSetup");
            } else {
                cardLayout.show(mainPanel, "playerSetup");
            }
        });
        
        backBtn.addActionListener(e -> cardLayout.show(mainPanel, "boardType"));
        
        buttonPanel.add(beginnerBtn);
        buttonPanel.add(intermediateBtn);
        buttonPanel.add(expertBtn);
        buttonPanel.add(backBtn);
        
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        centerPanel.setOpaque(false);
        centerPanel.add(buttonPanel);
        
        panel.add(label, BorderLayout.NORTH);
        panel.add(centerPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createPlayerSetupScreen() {
        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBackground(new Color(0, 51, 102));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        
        JLabel label = new JLabel("Player Setup", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 28));
        label.setForeground(Color.WHITE);
        
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JLabel p1Label = createFormLabel("Player 1 Name:");
        JTextField p1Name = new JTextField("Player 1", 15);
        JLabel p1ColorLabel = createFormLabel("Player 1 Color:");
        JComboBox<String> p1Color = new JComboBox<>(new String[]{"Red", "Yellow", "Blue", "Green", "Orange", "Purple"});
        
        JLabel p2Label = createFormLabel("Player 2 Name:");
        JTextField p2Name = new JTextField("Player 2", 15);
        JLabel p2ColorLabel = createFormLabel("Player 2 Color:");
        JComboBox<String> p2Color = new JComboBox<>(new String[]{"Yellow", "Red", "Blue", "Green", "Orange", "Purple"});
        
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(p1Label, gbc);
        gbc.gridx = 1;
        formPanel.add(p1Name, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(p1ColorLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(p1Color, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(p2Label, gbc);
        gbc.gridx = 1;
        formPanel.add(p2Name, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(p2ColorLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(p2Color, gbc);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setOpaque(false);
        
        JButton startBtn = createMenuButton("Start Game");
        JButton backBtn = createMenuButton("Back");
        
        startBtn.addActionListener(e -> {
            String boardType = (String) mainPanel.getClientProperty("boardType");
            Integer diff = (Integer) mainPanel.getClientProperty("difficulty");
            boolean isSquare = "square".equals(boardType);
            
            DifficultyLevel level;
            if (isSquare) {
                level = DifficultyLevel.fromSquareLevel(diff != null ? diff : 1);
            } else {
                level = DifficultyLevel.fromLevel(diff != null ? diff : 1);
            }
            
            Player.CoinColor color1 = Player.CoinColor.values()[p1Color.getSelectedIndex()];
            Player.CoinColor color2 = Player.CoinColor.values()[p2Color.getSelectedIndex()];
            
            if (color1 == color2) {
                JOptionPane.showMessageDialog(this, "Players must choose different colors!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            Player player1 = new Player(1, p1Name.getText().trim(), Player.PlayerType.HUMAN, color1);
            Player player2 = new Player(2, p2Name.getText().trim(), Player.PlayerType.HUMAN, color2);
            
            GameSettings settings = new GameSettings(player1, player2);
            settings.setDifficultyLevel(level);
            
            startGame(settings);
        });
        
        backBtn.addActionListener(e -> cardLayout.show(mainPanel, "difficulty"));
        
        buttonPanel.add(startBtn);
        buttonPanel.add(backBtn);
        
        panel.add(label, BorderLayout.NORTH);
        panel.add(formPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createVsComputerSetupScreen() {
        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBackground(new Color(0, 51, 102));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        
        JLabel label = new JLabel("Player Setup", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 28));
        label.setForeground(Color.WHITE);
        
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JLabel nameLabel = createFormLabel("Your Name:");
        JTextField playerName = new JTextField("Player", 15);
        JLabel colorLabel = createFormLabel("Your Color:");
        JComboBox<String> playerColor = new JComboBox<>(new String[]{"Red", "Yellow", "Blue", "Green", "Orange", "Purple"});
        JLabel firstLabel = createFormLabel("Who Goes First:");
        JComboBox<String> goesFirst = new JComboBox<>(new String[]{"You", "Computer"});
        
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(nameLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(playerName, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(colorLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(playerColor, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(firstLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(goesFirst, gbc);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setOpaque(false);
        
        JButton startBtn = createMenuButton("Start Game");
        JButton backBtn = createMenuButton("Back");
        
        startBtn.addActionListener(e -> {
            String boardType = (String) mainPanel.getClientProperty("boardType");
            Integer diff = (Integer) mainPanel.getClientProperty("difficulty");
            boolean isSquare = "square".equals(boardType);
            
            DifficultyLevel level;
            if (isSquare) {
                level = DifficultyLevel.fromSquareLevel(diff != null ? diff : 1);
            } else {
                level = DifficultyLevel.fromLevel(diff != null ? diff : 1);
            }
            
            Player.CoinColor color = Player.CoinColor.values()[playerColor.getSelectedIndex()];
            Player humanPlayer = new Player(1, playerName.getText().trim(), Player.PlayerType.HUMAN, color);
            boolean humanFirst = goesFirst.getSelectedIndex() == 0;
            
            GameSettings settings = new GameSettings(level, humanPlayer, humanFirst);
            startGame(settings);
        });
        
        backBtn.addActionListener(e -> cardLayout.show(mainPanel, "difficulty"));
        
        buttonPanel.add(startBtn);
        buttonPanel.add(backBtn);
        
        panel.add(label, BorderLayout.NORTH);
        panel.add(formPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JButton createMenuButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setPreferredSize(new Dimension(280, 45));
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.setBackground(new Color(255, 215, 0));
        button.setForeground(new Color(0, 51, 102));
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 170, 0), 2),
            BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(255, 235, 100));
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(255, 215, 0));
            }
        });
        
        return button;
    }
    
    private JLabel createFormLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        label.setForeground(Color.WHITE);
        return label;
    }
    
    private void startGame(GameSettings settings) {
        String viewType = (String) mainPanel.getClientProperty("viewType");
        dispose();
        
        GameState gameState = new GameState(settings);
        
        if ("text".equals(viewType)) {
            TextView textView = new TextView(gameState);
            textView.initialize();
            textView.startGameLoop();
        } else {
            GUIView guiView = new GUIView(gameState);
            guiView.initialize();
            guiView.startGameLoop();
        }
    }
    
    private void loadGame() {
        String[] saves = SaveLoadManager.listSaves();
        
        if (saves.length == 0) {
            JOptionPane.showMessageDialog(this, "No saved games found.", "Load Game", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        String[] viewOptions = {"GUI Mode", "Text Mode"};
        int viewChoice = JOptionPane.showOptionDialog(
            this,
            "How would you like to play the loaded game?",
            "Select View",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            viewOptions,
            viewOptions[0]
        );
        
        if (viewChoice < 0) return;
        
        String selected = (String) JOptionPane.showInputDialog(
            this,
            "Select a saved game:",
            "Load Game",
            JOptionPane.PLAIN_MESSAGE,
            null,
            saves,
            saves[0]
        );
        
        if (selected != null) {
            SaveLoadManager.GameSnapshot snapshot = SaveLoadManager.loadGame(selected.replace(".c4save", ""));
            if (snapshot != null) {
                GameState loaded = SaveLoadManager.applySnapshot(snapshot);
                dispose();
                
                if (viewChoice == 1) {
                    TextView textView = new TextView(loaded);
                    textView.initialize();
                    textView.startGameLoop();
                } else {
                    GUIView guiView = new GUIView(loaded);
                    guiView.initialize();
                    guiView.startGameLoop();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Failed to load game.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void showStats() {
        JOptionPane.showMessageDialog(this, 
            "Statistics are tracked per game session.\nStart a game to view player statistics.",
            "Statistics",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {}
            
            MainMenu menu = new MainMenu();
            menu.setVisible(true);
        });
    }
}

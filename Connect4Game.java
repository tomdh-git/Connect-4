import java.util.Scanner;

/**
 * Connect4Game.java - MODIFIED CLASS (Extended Features)
 * 
 * Main launcher with game configuration options:
 * - Two-player mode with color selection
 * - Single-player vs computer with difficulty levels
 * - Square board option with four corners win mode
 * - Command-line arguments for quick start
 * 
 * @author Created for multi-view support + Extended features
 */
public class Connect4Game {
    
    private static Scanner scanner = new Scanner(System.in);
    
    public static void main(String[] args) {
        String viewChoice = parseArguments(args);
        
        if (viewChoice == null) {
            viewChoice = promptForViewSelection();
        }
        
        GameSettings settings = configureGame(viewChoice);
        GameState gameState = new GameState(settings);
        
        GameView view = createView(viewChoice, gameState);
        
        if (view != null) {
            view.initialize();
            view.startGameLoop();
        } else {
            System.err.println("Failed to create game view.");
            System.exit(1);
        }
    }
    
    private static String parseArguments(String[] args) {
        if (args.length > 0) {
            String arg = args[0].toLowerCase().trim();
            
            switch (arg) {
                case "gui": case "g": case "-gui": case "--gui":
                    return "gui";
                case "text": case "t": case "console": case "-text": case "--text":
                    return "text";
                case "help": case "-h": case "--help":
                    printHelp();
                    System.exit(0);
                    return null;
                default:
                    System.out.println("Unknown argument: " + arg);
                    printHelp();
                    System.exit(1);
                    return null;
            }
        }
        return null;
    }
    
    private static void printHelp() {
        System.out.println("\nConnect Four Game - Extended Edition");
        System.out.println("=====================================\n");
        System.out.println("Usage: java Connect4Game [view]\n");
        System.out.println("Views:");
        System.out.println("  gui, g     - Graphical user interface");
        System.out.println("  text, t    - Text-based console interface\n");
        System.out.println("Features:");
        System.out.println("  - Two-player mode with color selection");
        System.out.println("  - Single-player vs AI with 3 difficulty levels");
        System.out.println("  - Square boards with Four Corners win mode");
        System.out.println("  - Lucky coin offer/accept/reject system");
        System.out.println("  - Save/Load game functionality");
        System.out.println("  - Player statistics tracking\n");
    }
    
    private static String promptForViewSelection() {
        System.out.println("\n╔═══════════════════════════════════════════╗");
        System.out.println("║      CONNECT FOUR - VIEW SELECTION        ║");
        System.out.println("╚═══════════════════════════════════════════╝\n");
        System.out.println("Select a view:\n");
        System.out.println("  [1] GUI Mode  - Graphical interface");
        System.out.println("  [2] Text Mode - Console interface\n");
        
        while (true) {
            System.out.print("Enter choice (1 or 2): ");
            String input = scanner.nextLine().trim().toLowerCase();
            
            switch (input) {
                case "1": case "gui": case "g": return "gui";
                case "2": case "text": case "t": return "text";
                default:
                    System.out.println("Invalid choice. Enter 1 or 2.");
            }
        }
    }
    
    private static GameSettings configureGame(String viewChoice) {
        System.out.println("\n╔═══════════════════════════════════════════╗");
        System.out.println("║      GAME CONFIGURATION                   ║");
        System.out.println("╚═══════════════════════════════════════════╝\n");
        
        System.out.println("Game Mode:");
        System.out.println("  [1] Two Players");
        System.out.println("  [2] Vs Computer\n");
        
        int mode = 1;
        while (true) {
            System.out.print("Select mode (1 or 2): ");
            try {
                mode = Integer.parseInt(scanner.nextLine().trim());
                if (mode == 1 || mode == 2) break;
            } catch (NumberFormatException e) {}
            System.out.println("Enter 1 or 2.");
        }
        
        if (mode == 1) {
            return configureTwoPlayer();
        } else {
            return configureVsComputer();
        }
    }
    
    private static GameSettings configureTwoPlayer() {
        System.out.println("\n--- Two Player Setup ---\n");
        
        boolean useSquare = promptForBoardType();
        DifficultyLevel level = useSquare ? 
            DifficultyLevel.BEGINNER_SQUARE : DifficultyLevel.BEGINNER;
        
        System.out.print("Player 1 name [Player 1]: ");
        String name1 = scanner.nextLine().trim();
        if (name1.isEmpty()) name1 = "Player 1";
        
        Player.CoinColor color1 = selectColor("Player 1", null);
        Player player1 = new Player(1, name1, Player.PlayerType.HUMAN, color1);
        
        System.out.print("Player 2 name [Player 2]: ");
        String name2 = scanner.nextLine().trim();
        if (name2.isEmpty()) name2 = "Player 2";
        
        Player.CoinColor color2 = selectColor("Player 2", color1);
        Player player2 = new Player(2, name2, Player.PlayerType.HUMAN, color2);
        
        System.out.println("\nGame configured:");
        System.out.println("  Board: " + level.getBoardSizeString());
        if (useSquare) {
            System.out.println("  Four Corners Win: ENABLED");
        }
        System.out.println("  " + player1.getName() + " (" + color1 + ")");
        System.out.println("  " + player2.getName() + " (" + color2 + ")\n");
        
        GameSettings settings = new GameSettings(player1, player2);
        settings.setDifficultyLevel(level);
        return settings;
    }
    
    private static GameSettings configureVsComputer() {
        System.out.println("\n--- Vs Computer Setup ---\n");
        
        boolean useSquare = promptForBoardType();
        
        System.out.println("Difficulty Level:");
        if (useSquare) {
            for (String desc : DifficultyLevel.getSquareDescriptions()) {
                System.out.println("  " + desc);
            }
        } else {
            for (String desc : DifficultyLevel.getRectangularDescriptions()) {
                System.out.println("  " + desc);
            }
        }
        
        DifficultyLevel level = DifficultyLevel.BEGINNER;
        while (true) {
            System.out.print("\nSelect difficulty (1-3): ");
            try {
                int diff = Integer.parseInt(scanner.nextLine().trim());
                if (diff >= 1 && diff <= 3) {
                    level = useSquare ? 
                        DifficultyLevel.fromSquareLevel(diff) : 
                        DifficultyLevel.fromLevel(diff);
                    break;
                }
            } catch (NumberFormatException e) {}
            System.out.println("Enter 1, 2, or 3.");
        }
        
        System.out.print("\nYour name [Player]: ");
        String name = scanner.nextLine().trim();
        if (name.isEmpty()) name = "Player";
        
        Player.CoinColor color = selectColor("Your", null);
        Player humanPlayer = new Player(1, name, Player.PlayerType.HUMAN, color);
        
        System.out.println("\nWho goes first?");
        System.out.println("  [1] You");
        System.out.println("  [2] Computer");
        
        boolean humanFirst = true;
        while (true) {
            System.out.print("Select (1 or 2): ");
            try {
                int first = Integer.parseInt(scanner.nextLine().trim());
                if (first == 1 || first == 2) {
                    humanFirst = (first == 1);
                    break;
                }
            } catch (NumberFormatException e) {}
            System.out.println("Enter 1 or 2.");
        }
        
        GameSettings settings = new GameSettings(level, humanPlayer, humanFirst);
        
        System.out.println("\nGame configured:");
        System.out.println("  Difficulty: " + level.getDisplayName());
        System.out.println("  Board: " + level.getBoardSizeString());
        if (useSquare) {
            System.out.println("  Four Corners Win: ENABLED");
        }
        System.out.println("  " + settings.getPlayer1().getName() + " goes first\n");
        
        return settings;
    }
    
    private static boolean promptForBoardType() {
        System.out.println("Board Type:");
        System.out.println("  [1] Rectangular (Standard)");
        System.out.println("  [2] Square (Four Corners Win Mode)\n");
        
        while (true) {
            System.out.print("Select board type (1 or 2): ");
            try {
                int choice = Integer.parseInt(scanner.nextLine().trim());
                if (choice == 1) return false;
                if (choice == 2) return true;
            } catch (NumberFormatException e) {}
            System.out.println("Enter 1 or 2.");
        }
    }
    
    private static Player.CoinColor selectColor(String playerName, Player.CoinColor exclude) {
        Player.CoinColor[] colors = Player.CoinColor.values();
        
        System.out.println("\n" + playerName + " color:");
        int index = 1;
        for (Player.CoinColor c : colors) {
            if (c != exclude) {
                System.out.println("  [" + index + "] " + c.getDisplayName());
            }
            index++;
        }
        
        while (true) {
            System.out.print("Select color: ");
            try {
                int choice = Integer.parseInt(scanner.nextLine().trim());
                if (choice >= 1 && choice <= colors.length) {
                    Player.CoinColor selected = colors[choice - 1];
                    if (selected != exclude) {
                        return selected;
                    }
                    System.out.println("That color is taken.");
                }
            } catch (NumberFormatException e) {}
            System.out.println("Enter a valid number.");
        }
    }
    
    private static GameView createView(String viewChoice, GameState gameState) {
        switch (viewChoice.toLowerCase()) {
            case "gui":
                System.out.println("Launching GUI mode...\n");
                return new GUIView(gameState);
            case "text":
                System.out.println("Launching Text mode...\n");
                return new TextView(gameState);
            default:
                return null;
        }
    }
}

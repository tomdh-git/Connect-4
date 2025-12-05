package com.connect4.player;

import java.util.ArrayList;
import java.util.Random;

import com.connect4.view.Cell;
import com.connect4.settings.DifficultyLevel;
import com.connect4.view.GameState;

/**
 * AIPlayer.java
 * This class implements the computer opponent AI using the min/max algorithm
 * with alpha-beta pruning. The AI complexity varies by difficulty level:
 * BEGINNER (depth 2):
 * - Shallow search with basic heuristics
 * - Focus on blocking immediate wins and taking simple opportunities
 * - Some randomness to make play less predictable
 * INTERMEDIATE (depth 4):
 * - Medium depth search with improved evaluation
 * - Considers center control and multiple threat creation
 * - Balanced between defense and offense
 * EXPERT (depth 6):
 * - Deep search with comprehensive evaluation
 * - Evaluates line potentials, center control, and trap setups
 * - Aggressive play with optimal blocking
 * The min/max algorithm works by:
 * 1. Generating all possible moves
 * 2. For each move, recursively evaluating resulting positions
 * 3. AI (maximizing player) picks moves that maximize score
 * 4. Opponent (minimizing player) picks moves that minimize score
 * 5. Alpha-beta pruning eliminates branches that won't affect final decision
 * 
 * @author Extended feature implementation
 */
public class AIPlayer {

    private static final int WIN_SCORE = 1000000;
    private static final int LOSE_SCORE = -1000000;
    private static final int DRAW_SCORE = 0;

    private final DifficultyLevel difficulty;
    private final int playerNumber; // Which player the AI is (1 or 2)
    private final Random random;

    /**
     * Creates an AI player with the specified difficulty.
     * 
     * @param difficulty   the difficulty level
     * @param playerNumber which player the AI controls (1 or 2)
     */
    public AIPlayer(DifficultyLevel difficulty, int playerNumber) {
        this.difficulty = difficulty;
        this.playerNumber = playerNumber;
        this.random = new Random();
    }

    /**
     * Calculates and returns the best move for the AI.
     * Uses min/max algorithm with alpha-beta pruning.
     * The search depth is determined by the difficulty level:
     * - BEGINNER: depth 2
     * - INTERMEDIATE: depth 4
     * - EXPERT: depth 6
     * 
     * @param gameState the current game state
     * @return the column number (1-based) to play, or -1 if no valid moves
     */
    public int getBestMove(GameState gameState) {
        int depth = difficulty.getAiSearchDepth();

        ArrayList<Integer> validMoves = getValidMoves(gameState);
        if (validMoves.isEmpty()) {
            return -1;
        }

        // For beginner, add some randomness
        if (difficulty == DifficultyLevel.BEGINNER && random.nextDouble() < 0.3) {
            // 30% chance to make a random move at beginner level
            return validMoves.get(random.nextInt(validMoves.size()));
        }

        int bestMove = validMoves.get(0);
        int bestScore = Integer.MIN_VALUE;
        int alpha = Integer.MIN_VALUE;
        int beta = Integer.MAX_VALUE;

        // Evaluate each possible move
        for (int column : validMoves) {
            // Make the move on a copy of the state
            GameState clonedState = cloneGameState(gameState);
            clonedState.moveInternal(column);

            // Use min/max to evaluate this move
            // After AI moves, it's opponent's turn
            int score = minimax(clonedState, depth - 1, alpha, beta, false);

            // Add some randomness to break ties
            if (score > bestScore || (score == bestScore && random.nextBoolean())) {
                bestScore = score;
                bestMove = column;
            }

            alpha = Math.max(alpha, score);
        }

        return bestMove;
    }

    /**
     * Recursive min/max evaluation with alpha-beta pruning.
     * This is the core of the AI decision making:
     * - At MAX nodes (AI's turn): choose the move with highest score
     * - At MIN nodes (opponent's turn): assume opponent picks lowest score
     * - Alpha-beta pruning skips branches that can't affect the outcome
     * 
     * @param state        current game state
     * @param depth        remaining search depth
     * @param alpha        best score achievable by maximizer
     * @param beta         best score achievable by minimizer
     * @param isMaximizing true if it's AI's turn (maximize), false for opponent
     * @return the evaluation score of this position
     */
    private int minimax(GameState state, int depth, int alpha, int beta, boolean isMaximizing) {
        if (state.getGameOver()) {
            if (state.getPlayer1Wins()) {
                return playerNumber == 1 ? WIN_SCORE : LOSE_SCORE;
            } else if (state.getPlayer2Wins()) {
                return playerNumber == 2 ? WIN_SCORE : LOSE_SCORE;
            } else {
                return DRAW_SCORE;
            }
        }

        if (depth == 0) {
            return evaluatePosition(state);
        }

        ArrayList<Integer> validMoves = getValidMoves(state);
        if (validMoves.isEmpty()) {
            return DRAW_SCORE;
        }

        if (isMaximizing) {
            int maxScore = Integer.MIN_VALUE;

            for (int column : validMoves) {
                GameState clonedState = cloneGameState(state);
                clonedState.moveInternal(column);

                int score = minimax(clonedState, depth - 1, alpha, beta, false);
                maxScore = Math.max(maxScore, score);

                alpha = Math.max(alpha, score);
                if (beta <= alpha) {
                    break; // prune remaining branches
                }
            }

            return maxScore;

        } else {
            int minScore = Integer.MAX_VALUE;

            for (int column : validMoves) {
                GameState clonedState = cloneGameState(state);
                clonedState.moveInternal(column);

                int score = minimax(clonedState, depth - 1, alpha, beta, true);
                minScore = Math.min(minScore, score);

                beta = Math.min(beta, score);
                if (beta <= alpha) {
                    break; // Prune remaining branches
                }
            }

            return minScore;
        }
    }

    /**
     * Evaluates a non-terminal game position.
     * The evaluation considers:
     * - Number of potential winning lines (2-in-a-row, 3-in-a-row)
     * - Center column control (center positions are more valuable)
     * - Blocking opponent's threats
     * - Lucky coin positions (count as owned piece)
     * Higher scores favor the AI, lower scores favor the opponent.
     * 
     * @param state the game state to evaluate
     * @return evaluation score
     */
    private int evaluatePosition(GameState state) {
        int score = 0;
        int cols = state.getColumns();
        int rows = state.getRows();
        Cell[][] cells = state.getCells();

        int centerCol = cols / 2;
        for (int row = 0; row < rows; row++) {
            Cell cell = cells[centerCol][row];
            if (!cell.isAvailable()) {
                int value = 3; // Center column bonus
                if (isOwnPiece(cell, state)) {
                    score += value;
                } else {
                    score -= value;
                }
            }
        }

        score += evaluateLines(state);

        score += evaluateThreats(state);

        return score;
    }

    /**
     * Evaluates potential winning lines.
     * Scoring:
     * - 2 in a row with 2 empty: +10 / -10
     * - 3 in a row with 1 empty: +100 / -100
     * 
     * @param state game state
     * @return line evaluation score
     */
    private int evaluateLines(GameState state) {
        int score = 0;
        int cols = state.getColumns();
        int rows = state.getRows();
        Cell[][] cells = state.getCells();

        // Check all possible 4-length windows
        // Horizontal windows
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col <= cols - 4; col++) {
                score += evaluateWindow(cells, col, row, 1, 0, state);
            }
        }

        // Vertical windows
        for (int row = 0; row <= rows - 4; row++) {
            for (int col = 0; col < cols; col++) {
                score += evaluateWindow(cells, col, row, 0, 1, state);
            }
        }

        // Diagonal (/) windows
        for (int row = 0; row <= rows - 4; row++) {
            for (int col = 0; col <= cols - 4; col++) {
                score += evaluateWindow(cells, col, row, 1, 1, state);
            }
        }

        // Diagonal (\) windows
        for (int row = 3; row < rows; row++) {
            for (int col = 0; col <= cols - 4; col++) {
                score += evaluateWindow(cells, col, row, 1, -1, state);
            }
        }

        return score;
    }

    /**
     * Evaluates a window of 4 cells for scoring potential.
     * 
     * @param cells    the board
     * @param startCol starting column
     * @param startRow starting row
     * @param colDir   column direction
     * @param rowDir   row direction
     * @param state    game state for reference
     * @return window score
     */
    private int evaluateWindow(Cell[][] cells, int startCol, int startRow,
            int colDir, int rowDir, GameState state) {
        int ownCount = 0;
        int oppCount = 0;
        int emptyCount = 0;

        for (int i = 0; i < 4; i++) {
            Cell cell = cells[startCol + i * colDir][startRow + i * rowDir];

            if (cell.isAvailable()) {
                emptyCount++;
            } else if (isOwnPiece(cell, state)) {
                ownCount++;
            } else {
                oppCount++;
            }
        }

        // Score the window
        if (ownCount > 0 && oppCount > 0) {
            return 0; // cant win here
        }

        if (ownCount == 4)
            return WIN_SCORE / 10;
        if (oppCount == 4)
            return LOSE_SCORE / 10;

        if (ownCount == 3 && emptyCount == 1)
            return 100;
        if (oppCount == 3 && emptyCount == 1)
            return -100;

        if (ownCount == 2 && emptyCount == 2)
            return 10;
        if (oppCount == 2 && emptyCount == 2)
            return -10;

        return 0;
    }

    /**
     * Evaluates immediate win/block threats.
     * 
     * @param state game state
     * @return threat score
     */
    private int evaluateThreats(GameState state) {
        int score = 0;

        // Check each column for immediate win/block
        for (int col = 1; col <= state.getColumns(); col++) {
            if (!state.isValidMove(col))
                continue;

            // Simulate AI move
            GameState testState = cloneGameState(state);
            testState.moveInternal(col);

            if (testState.getPlayer1Wins() || testState.getPlayer2Wins()) {
                // This move wins
                boolean aiWins = (playerNumber == 1 && testState.getPlayer1Wins()) ||
                        (playerNumber == 2 && testState.getPlayer2Wins());
                score += aiWins ? 10000 : -10000;
            }
        }

        return score;
    }

    /**
     * Checks if a cell's piece belongs to the AI player.
     * 
     * @param cell  the cell to check
     * @param state game state for player info
     * @return true if AI's piece
     */
    private boolean isOwnPiece(Cell cell, GameState state) {
        Player aiPlayer = playerNumber == 1 ? state.getSettings().getPlayer1() : state.getSettings().getPlayer2();

        Player.CoinColor aiColor = aiPlayer.getCoinColor();

        if (cell.isRed() && aiColor == Player.CoinColor.RED)
            return true;
        if (cell.isYellow() && aiColor == Player.CoinColor.YELLOW)
            return true;
        return cell.isLucky();
    }

    /**
     * Gets a list of valid column numbers for moves.
     * 
     * @param state game state
     * @return list of valid columns (1-based)
     */
    private ArrayList<Integer> getValidMoves(GameState state) {
        ArrayList<Integer> moves = new ArrayList<>();
        int cols = state.getColumns();

        // Prioritize center columns
        int center = cols / 2 + 1;
        if (state.isValidMove(center))
            moves.add(center);

        for (int offset = 1; offset <= cols / 2; offset++) {
            if (center - offset >= 1 && state.isValidMove(center - offset)) {
                moves.add(center - offset);
            }
            if (center + offset <= cols && state.isValidMove(center + offset)) {
                moves.add(center + offset);
            }
        }

        return moves;
    }

    /**
     * Creates a deep copy of the game state for AI simulation.
     * 
     * @param original the original game state
     * @return cloned game state
     */
    private GameState cloneGameState(GameState original) {
        return new GameState(original);
    }

    /**
     * Decides whether the AI should accept a lucky coin offer.
     * Uses evaluation to compare accepting vs rejecting:
     * - Accepting: Places piece at offered location, uses a turn
     * - Rejecting: No placement, no turn used
     * 
     * @param state current game state with pending offer
     * @return true if AI should accept the offer
     */
    public boolean shouldAcceptLuckyOffer(GameState state) {
        if (!state.isLuckyOfferPending()) {
            return false;
        }

        int offerCol = state.getLuckyOfferColumn();
        int offerRow = state.getLuckyOfferRow();

        // Evaluate position if we accept
        GameState acceptState = cloneGameState(state);
        acceptState.acceptLuckyOffer();
        int acceptScore = evaluatePosition(acceptState);

        // Evaluate position if we reject (clear the lucky coin cell)
        GameState rejectState = cloneGameState(state);
        rejectState.rejectLuckyOffer();
        int rejectScore = evaluatePosition(rejectState);

        // Check if accepting would win
        if (acceptState.getGameOver()) {
            boolean aiWins = (playerNumber == 1 && acceptState.getPlayer1Wins()) ||
                    (playerNumber == 2 && acceptState.getPlayer2Wins());
            if (aiWins)
                return true;
        }

        // Check strategic value of the position
        // Corner positions are very valuable on square boards
        DifficultyLevel level = state.getSettings().getDifficultyLevel();
        if (level.isFourCornersEnabled()) {
            int cols = state.getColumns();
            int rows = state.getRows();
            boolean isCorner = (offerCol == 0 || offerCol == cols - 1) &&
                    (offerRow == 0 || offerRow == rows - 1);
            if (isCorner) {
                // Always accept corners on square boards
                return true;
            }
        }

        // Accept if the evaluation is better or equal
        // Add small bias towards accepting since it's a free piece
        return acceptScore >= rejectScore - 5;
    }

    public DifficultyLevel getDifficulty() {
        return difficulty;
    }

    public int getPlayerNumber() {
        return playerNumber;
    }
}


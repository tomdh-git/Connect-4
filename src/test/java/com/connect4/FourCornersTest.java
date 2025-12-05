package com.connect4;

import com.connect4.settings.DifficultyLevel;
import com.connect4.settings.GameSettings;
import com.connect4.view.GameState;
import com.connect4.player.Player;

public class FourCornersTest {

    public static void main(String[] args) {
        test2x2Square();
        test3x3Square();
        testMixedSquare();
        System.out.println("All tests passed!");
    }

    /**
     * Helper method to make a move and reject any lucky coin offers
     */
    private static void makeMove(GameState state, int col) {
        state.move(col);
        if (state.isLuckyOfferPending()) {
            state.rejectLuckyOffer();
        }
    }

    private static void test2x2Square() {
        System.out.println("Testing 2x2 Square...");
        GameSettings settings = new GameSettings();
        settings.setFourCornersEnabled(true);
        GameState state = new GameState(settings);

        // P1: Red, P2: Yellow

        makeMove(state, 1); // R at (0,0)
        makeMove(state, 3); // Y at (2,0)
        makeMove(state, 2); // R at (1,0)
        makeMove(state, 3); // Y at (2,1)

        makeMove(state, 1); // R at (0,1) - Top Left of 2x2
        makeMove(state, 4); // Y at (3,0)
        makeMove(state, 2); // R at (1,1) - Top Right of 2x2 -> WIN!

        if (state.getPlayer1Wins()) {
            System.out.println("PASS: 2x2 Square detected.");
        } else {
            System.err.println("FAIL: 2x2 Square NOT detected.");
            System.exit(1);
        }
    }

    private static void test3x3Square() {
        System.out.println("Testing 3x3 Square...");
        GameSettings settings = new GameSettings();
        settings.setFourCornersEnabled(true);
        GameState state = new GameState(settings);

        // 3x3 Square corners:
        // (0,0) (2,0)
        //
        // (0,2) (2,2)

        // P1 (Red) wants corners at (0,0), (2,0), (0,2), (2,2)
        // P2 (Yellow) wastes turns

        // Row 0
        makeMove(state, 1); // R (0,0)
        makeMove(state, 2); // Y (1,0)
        makeMove(state, 3); // R (2,0)
        makeMove(state, 4); // Y (3,0)

        // Row 1
        makeMove(state, 1); // R (0,1)
        makeMove(state, 2); // Y (1,1)
        makeMove(state, 3); // R (2,1)
        makeMove(state, 4); // Y (3,1)

        // Row 2
        makeMove(state, 1); // R (0,2)
        makeMove(state, 5); // Y (4,0)
        makeMove(state, 3); // R (2,2) -> WIN

        if (state.getPlayer1Wins()) {
            System.out.println("PASS: 3x3 Square detected.");
        } else {
            System.err.println("FAIL: 3x3 Square NOT detected.");
            if (state.getPlayer2Wins()) {
                System.err.println("Player 2 won instead!");
            }
            System.exit(1);
        }
    }

    private static void testMixedSquare() {
        // Test that a mixed color square does NOT win
        System.out.println("Testing Mixed Square (No Win)...");
        GameSettings settings = new GameSettings();
        settings.setFourCornersEnabled(true);
        GameState state = new GameState(settings);

        makeMove(state, 1); // R (0,0)
        makeMove(state, 2); // Y (1,0)
        makeMove(state, 1); // R (0,1)
        makeMove(state, 2); // Y (1,1)

        if (state.getGameOver()) {
            System.err.println("FAIL: Mixed square caused a win.");
            System.exit(1);
        } else {
            System.out.println("PASS: Mixed square did not win.");
        }
    }
}


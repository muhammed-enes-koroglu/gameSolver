package test;

import util.Board;

import java.util.List;
import games.tictactoe.TicTacToeState;
import interfaces.TwoPersonGameState;
import search_algorithms.GameAI;

public class TestGameAI {

    private TestGameAI() {
        throw new IllegalStateException("Utility class");
    }

    public static void testAStarMinimax() {
        System.out.println("[TESTING] AStarMinimax");
        float minSearchTime = 1f;

        Board board;
        TicTacToeState ticState;
        List<TicTacToeState> solution;

        // Test scenario configurations
        int[][] testBoards = {
            {1,0,1, -1,-1,0, 0,0,0},
            {0,1,0, 1,0,-1, 0,-1,0},
            {0,-1,0, -1,0,1, 0,1,0},
            {-1,-1,0, 1,0,0, 1,0,0},
            {-1,1,1, 0,-1,0, 0,0,0},
            {0,0,0, 0,0,0, 0,0,0},
            {1,1,0, 1,0,-1, 0,-1,0}
        };

        boolean[] isMaxTurn = {true, true, true, true, true, true, false};

        for (int i = 0; i < testBoards.length; i++) {
            board = new Board(testBoards[i]);
            ticState = new TicTacToeState(board, isMaxTurn[i]);
            solution = GameAI.aStarMinimax(ticState, minSearchTime);
            
            switch (i) {
                case 0: case 1: case 2:  // Tests where X is expected to win
                    assert solution.get(solution.size() - 1).xWon();
                    break;
                case 3:  // Preventing O from winning, next move should be a win
                    board = new Board(new int[]{-1,-1,1, 1,0,0, 1,0,0});
                    ticState = new TicTacToeState(board, true);
                    assert solution.get(1).equals(ticState);
                    break;
                case 4:  // Another preventing O from winning scenario
                    board = new Board(new int[]{-1,1,1, 0,-1,0, 0,0,1});
                    ticState = new TicTacToeState(board, true);
                    assert solution.get(1).equals(ticState);
                    break;
                case 5:  // Neutral start state
                    assert solution.get(solution.size() - 1).score() == 0;
                    break;
                case 6:  // Test where O is losing
                    assert solution.get(solution.size() - 1).score() == TwoPersonGameState.MIN_SCORE;
                    break;
                default:  // Default case
                    System.out.println("Unexpected value: " + i);
                    break;
            }
        }

        System.out.println("[TESTING] AStarMinimax - DONE\n");
    }

}

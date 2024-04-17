package test;

import util.Board;

import java.util.List;

import games.tictactoe.TicTacToeState;
import interfaces.TwoPersonGameState;
import search_algorithms.MiniMaxSearch;

public class TestGameSolver {

    private TestGameSolver(){
        throw new IllegalStateException("Utility class");
    }
    
    public static void testIterDeepeningMiniMax(){
        System.out.println("[TESTING] iterativeDeepeningMiniMax");
        float minSearchTime = 1f;

        Board board = new Board( new int[]{1,0,1, -1,-1,0, 0,0,0});
        TicTacToeState ticState = new TicTacToeState(board, true);
        List<TicTacToeState> solution = MiniMaxSearch.iterativeDeepeningMiniMax(ticState, minSearchTime);
        assert solution.get(solution.size()-1).xWon();

        // Tricky
        board = new Board( new int[]{0,1,0, 1,0,-1, 0,-1,0});
        ticState = new TicTacToeState(board, true);
        solution = MiniMaxSearch.iterativeDeepeningMiniMax(ticState, minSearchTime);
        assert solution.get(solution.size()-1).xWon();

        // Tricky
        board = new Board( new int[]{0,-1,0, -1,0,1, 0,1,0});
        ticState = new TicTacToeState(board, true);
        solution = MiniMaxSearch.iterativeDeepeningMiniMax(ticState, minSearchTime);
        assert solution.get(solution.size()-1).xWon();

        // Preventing O from winning.
        board = new Board( new int[]{-1,-1,0, 1,0,0, 1,0,0});
        ticState = new TicTacToeState(board, true);
        solution = MiniMaxSearch.iterativeDeepeningMiniMax(ticState, minSearchTime);
        board = new Board( new int[]{-1,-1,1, 1,0,0, 1,0,0});
        ticState = new TicTacToeState(board, true);
        assert solution.get(1).equals(ticState);
        
        // Preventing O from winning.
        board = new Board( new int[]{-1,1,1, 0,-1,0, 0,0,0});
        ticState = new TicTacToeState(board, true);
        solution = MiniMaxSearch.iterativeDeepeningMiniMax(ticState, minSearchTime);
        board = new Board( new int[]{-1,1,1, 0,-1,0, 0,0,1});
        ticState = new TicTacToeState(board, true);
        assert solution.get(1).equals(ticState);


        // Will produce a more meaningful result when score() is improved.
        board = new Board( new int[]{0,0,0, 0,0,0, 0,0,0});
        ticState = new TicTacToeState(board, true);
        solution = MiniMaxSearch.iterativeDeepeningMiniMax(ticState, minSearchTime);
        assert solution.get(solution.size()-1).score() == 0;

        board = new Board( new int[]{1,1,0, 1,0,-1, 0,-1,0});
        ticState = new TicTacToeState(board, false);
        solution = MiniMaxSearch.iterativeDeepeningMiniMax(ticState, minSearchTime);
        assert solution.get(solution.size()-1).score() == TwoPersonGameState.MIN_SCORE;
        

        System.out.println("[TESTING] iterativeDeepeningMiniMax - DONE\n");
    }

    
}

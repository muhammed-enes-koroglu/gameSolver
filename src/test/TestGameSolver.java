package test;

import util.Board;
import util.GameSolver;
import util.TwoPersonGameState;

import java.util.List;

import games.TicTacToe.TicTacToeState;

public class TestGameSolver {

    private TestGameSolver(){
        throw new IllegalStateException("Utility class");
    }
    
    public static void testFindBestPathForMax(){
        System.out.println("\nTesting findBestPathForMax");
        Board board = new Board( new int[]{1,0,1, -1,-1,0, 0,0,0});
        TicTacToeState ticState = new TicTacToeState(board, true);
        List<TicTacToeState> solution = GameSolver.findBestPathForMax(ticState, 1);
        assert solution.get(solution.size()-1).xWon();

        // Tricky
        board = new Board( new int[]{0,1,0, 1,0,-1, 0,-1,0});
        ticState = new TicTacToeState(board, true);
        solution = GameSolver.findBestPathForMax(ticState, 1);
        assert solution.get(solution.size()-1).xWon();

        // Tricky
        board = new Board( new int[]{0,-1,0, -1,0,1, 0,1,0});
        ticState = new TicTacToeState(board, true);
        solution = GameSolver.findBestPathForMax(ticState, 1);
        assert solution.get(solution.size()-1).xWon();

        // Preventing O from winning.
        board = new Board( new int[]{-1,-1,0, 1,0,0, 1,0,0});
        ticState = new TicTacToeState(board, true);
        solution = GameSolver.findBestPathForMax(ticState, 2);
        board = new Board( new int[]{-1,-1,1, 1,0,0, 1,0,0});
        ticState = new TicTacToeState(board, true);
        assert solution.get(1).equals(ticState);
        
        // Preventing O from winning.
        board = new Board( new int[]{-1,1,1, 0,-1,0, 0,0,0});
        ticState = new TicTacToeState(board, true);
        solution = GameSolver.findBestPathForMax(ticState, 10);
        board = new Board( new int[]{-1,1,1, 0,-1,0, 0,0,1});
        ticState = new TicTacToeState(board, true);
        assert solution.get(1).equals(ticState);


        // Will produce a more meaningful result when score() is improved.
        board = new Board( new int[]{0,0,0, 0,0,0, 0,0,0});
        ticState = new TicTacToeState(board, true);
        solution = GameSolver.findBestPathForMax(ticState, 3);
        assert solution.get(solution.size()-1).score() == 0;

        board = new Board( new int[]{1,1,0, 1,0,-1, 0,-1,0});
        ticState = new TicTacToeState(board, false);
        solution = GameSolver.findBestPathForMax(ticState, 1);
        System.out.println(solution.get(solution.size()-1).score());
        assert solution.get(solution.size()-1).score() == TwoPersonGameState.MIN_SCORE;
        

        System.out.println("\nAll findBestPathForMax tests are Successful.");
    }

    
}

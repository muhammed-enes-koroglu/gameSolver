package test;

import games.TicTacToeState;
import util.Board;
import util.GameSolver;

import java.util.List;

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
        
        // Will produce a more meaningful result when score() is improved.
        board = new Board( new int[]{0,0,0, 0,0,0, 0,0,0});
        ticState = new TicTacToeState(board, true);
        solution = GameSolver.findBestPathForMax(ticState, 3);
        assert solution.get(solution.size()-1).score() == 0;

        board = new Board( new int[]{1,1,0, 1,0,-1, 0,-1,0});
        ticState = new TicTacToeState(board, false);
        solution = GameSolver.findBestPathForMax(ticState, 1);
        assert solution.get(solution.size()-1).score() == 0;
        

        System.out.println("\nAll findBestPathForMax tests are Successful.");
    }

    
}

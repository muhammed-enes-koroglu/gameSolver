import java.util.Arrays;
import java.util.List;

public class TestSuite{

    public static void main(String[] args){

        TestTicTacToeState.testTicTacToeState();
        System.out.println("#######");
        testFindBestPathForMax();

        System.out.println("\nALL TESTS SUCCESSFULL!");
    }
    
    public static void testFindBestPathForMax(){
        System.out.println("\nTesting findBestPathForMax");
        int[] board = new int[]{1,0,1, -1,-1,0, 0,0,0};
        TicTacToeState ticState = new TicTacToeState(board);
        List<TicTacToeState> solution = Searcher.findBestPathForMax(ticState, 1);
        // System.out.println("Solution: " + solution);
        assert solution.get(solution.size()-1).xWon();

        // Tricky
        board = new int[]{0,1,0, 1,0,-1, 0,-1,0};
        ticState = new TicTacToeState(board);
        solution = Searcher.findBestPathForMax(ticState, 1);
        // System.out.println("Solution: " + solution);
        assert solution.get(solution.size()-1).xWon();
        
        // Tricky
        board = new int[]{0,-1,0, -1,0,1, 0,1,0};
        ticState = new TicTacToeState(board);
        solution = Searcher.findBestPathForMax(ticState, 1);
        // System.out.println("Solution: " + solution);
        assert solution.get(solution.size()-1).xWon();
        
        // Will produce meaningful result when TicTacToeState.score is made better.
        board = new int[]{0,0,0, 0,0,0, 0,0,0};
        ticState = new TicTacToeState(board);
        solution = Searcher.findBestPathForMax(ticState, 3);
        // System.out.println("Solution: " + solution);

        System.out.println("\nAll findBestPathForMax tests are Successful.");
    }


}

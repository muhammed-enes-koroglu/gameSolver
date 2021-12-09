import java.util.Arrays;
import java.util.List;

public class TestSuite{

    public static void main(String[] args){

        TestTicTacToeState.testTicTacToeState();
        testBasicSearch();

        System.out.println("ALL TESTS SUCCESSFULL!");
    }
    
    public static void testBasicSearch(){

        int[] board = new int[]{1,0,1, -1,-1,0, 0,0,0};
        TicTacToeState ticState = new TicTacToeState(board);
        List<TicTacToeState> solution = Searcher.findBestPathForMax(ticState, 1);
        System.out.println("Solution: " + solution);
        assert solution.get(solution.size()-1).xWon();

        // Tricky
        board = new int[]{0,1,0, 1,0,-1, 0,-1,0};
        ticState = new TicTacToeState(board);
        solution = Searcher.findBestPathForMax(ticState, 1);
        System.out.println("Solution: " + solution);
        System.out.println(Arrays.toString(solution.get(solution.size()-1).getBoard()));
        assert solution.get(solution.size()-1).xWon();
        
        // Tricky
        board = new int[]{0,-1,0, -1,0,1, 0,1,0};
        ticState = new TicTacToeState(board);
        solution = Searcher.findBestPathForMax(ticState, 1);
        System.out.println("Solution: " + solution);
        assert solution.get(solution.size()-1).xWon();
        
        // board = new int[]{0,0,0, 0,0,0, 0,0,0};
        // ticState = new TicTacToeState(board);
        // solution = Searcher.findBestPathForMax(ticState, 3);
        // assert solution.get(solution.size()-1).xWon();
        // System.out.println("Solution: " + solution);


    }


}

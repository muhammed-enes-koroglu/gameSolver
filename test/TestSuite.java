import java.util.List;

public class TestSuite{

    public static void main(String[] args){

        TestTicTacToeState.testTicTacToeState();
        testFindBestPathForMax();
        TestMangalaState.testMangalaState();
        
        System.out.println("\nALL TESTS SUCCESSFULL!");
    }
    
    public static void testFindBestPathForMax(){
        System.out.println("\nTesting findBestPathForMax");
        int[] board = new int[]{1,0,1, -1,-1,0, 0,0,0};
        TicTacToeState ticState = new TicTacToeState(board, true);
        List<TicTacToeState> solution = Searcher.findBestPathForMax(ticState, 1);
        assert solution.get(solution.size()-1).xWon();

        // Tricky
        board = new int[]{0,1,0, 1,0,-1, 0,-1,0};
        ticState = new TicTacToeState(board, true);
        solution = Searcher.findBestPathForMax(ticState, 1);
        assert solution.get(solution.size()-1).xWon();
        
        // Tricky
        board = new int[]{0,-1,0, -1,0,1, 0,1,0};
        ticState = new TicTacToeState(board, true);
        solution = Searcher.findBestPathForMax(ticState, 1);
        assert solution.get(solution.size()-1).xWon();
        
        // Will produce a more meaningful result when score() is improved.
        board = new int[]{0,0,0, 0,0,0, 0,0,0};
        ticState = new TicTacToeState(board, true);
        solution = Searcher.findBestPathForMax(ticState, 3);
        assert solution.get(solution.size()-1).score() == 0;

        board = new int[]{1,1,0, 1,0,-1, 0,-1,0};
        ticState = new TicTacToeState(board, false);
        solution = Searcher.findBestPathForMax(ticState, 1);
        assert solution.get(solution.size()-1).score() == 0;
        

        System.out.println("\nAll findBestPathForMax tests are Successful.");
    }


}

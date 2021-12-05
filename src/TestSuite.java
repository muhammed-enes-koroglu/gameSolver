import java.util.List;

public class TestSuite{

    public static void main(String[] args){
        TestTicTacToeState.testTicTacToeState();
        testBasicSearch();

        System.out.println("ALL TESTS SUCCESSFULL!");
    }
    
    public static void testBasicSearch(){
        // int[] board = new int[]{1,0,1, -1,-1,0, 0,0,0};
        int[] board = new int[]{0,1,0, 1,0,-1, 0,-1,0}; // Tricky
        // int[] board = new int[]{0,0,0, 0,0,0, 0,0,0};

        TwoPersonGameState ticState = new TicTacToeState(board);

        List<TwoPersonGameState> solution = Searcher.findBestPathForMax(ticState, 3);
        System.out.println(solution);

    }


}

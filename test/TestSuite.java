import java.util.Arrays;
import java.util.List;

public class TestSuite{

    public static void main(String[] args){

        // TestTicTacToeState.testTicTacToeState();
        TestGameSolver.testFindBestPathForMax();
        // TestMangalaState.testMangalaState();
        // testMangalaSearcher();

        System.out.println("\nALL TESTS SUCCESSFULL!");
    }

    public static void testMangalaSearcher(){
        boolean whitesTurn = false;
        boolean whiteIsMax = false;
        int searchFor = 30;
        // int[] board = new int[]{4,4,4, 4,4,1, 1, 5,5,4, 4,4,4, 0};
        int[] board = newStartingBoard();
        // int[] board = new int[]{1, 0, 2, 1, 0, 0, 15, 0, 0, 0, 0, 3, 1, 25};
        MangalaState mState = new MangalaState(board, whitesTurn, whiteIsMax);
        System.out.println("Search starting from: " + mState.toString());
        List<MangalaState> bestPath = GameSolver.findBestPathForMax(mState, searchFor);

        System.out.println("Best move is:\n" + bestPath.get(1));
        System.out.println(Arrays.toString(bestPath.get(1).getBoard()));
    }
    
    private static int[] newStartingBoard(){
        return new int[]{4,4,4, 4,4,4, 0, 4,4,4, 4,4,4, 0};
    }


}

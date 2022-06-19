import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class TestSuite{

    public static void main(String[] args){

        // TestTicTacToeState.testTicTacToeState();
        // TestGameSolver.testFindBestPathForMax();
        // TestMangalaState.testMangalaState();
        testMangalaSearcher();

        System.out.println("\nALL TESTS SUCCESSFULL!");
    }

    public static void testMangalaSearcher(){
        boolean whitesTurn = false;
        boolean whiteIsMax = false;
        int searchFor = 10;
        int[] board = new int[]{0, 0, 2, 3, 0, 0, 17, 0, 3, 1, 0, 0, 0, 22};

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

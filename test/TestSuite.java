public class TestSuite{

    public static void main(String[] args){

        // TestTicTacToeState.testTicTacToeState();
        // TestSearcher.testFindBestPathForMax();
        // TestMangalaState.testMangalaState();
        testMangalaSearcher();

        System.out.println("\nALL TESTS SUCCESSFULL!");
    }

    public static void testMangalaSearcher(){
        boolean whitesTurn = true;
        boolean whiteIsMax = true;
        // int[] board = new int[]{4,4,4, 4,4,4, 0, 4,4,4, 4,4,4, 0};
        int[] board = newStartingBoard();
        MangalaState mState = new MangalaState(board, whitesTurn, whiteIsMax);
        System.out.println(Searcher.findBestPathForMax(mState, 30));
    }
    
    private static int[] newStartingBoard(){
        return new int[]{4,4,4, 4,4,4, 0, 4,4,4, 4,4,4, 0};
    }


}

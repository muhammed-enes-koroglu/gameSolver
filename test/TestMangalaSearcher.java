
public abstract class TestMangalaSearcher {

    private TestMangalaSearcher() {
        throw new IllegalStateException("Utility class");
      }
    
    public static void testMangalaSearcher(){
        boolean whitesTurn = true;
        boolean whiteIsMax = true;
        int[] board = new int[]{0,0,0, 0,1,1, 0, 2,1,0, 0,0,0, 1};
        MangalaState mState = new MangalaState(board, whitesTurn, whiteIsMax);
        System.out.println(GameSolver.findBestPathForMax(mState, 1));
    }
    
}

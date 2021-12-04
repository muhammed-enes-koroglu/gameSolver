import java.util.Arrays;

public class TestSuite{

    public static void main(String[] args){
        testTicTacToeState();

        System.out.println("All tests succeeded!");
    }
    
    private static void testTicTacToeState(){
        int[] board = new int[]{0,0,0, 0,0,0, 0,0,0};
        boolean xTurn = true;
        TicTacToeState game = new TicTacToeState(board, xTurn);
        System.out.println(game);

        assert Arrays.stream(game.children()).allMatch(g -> !g.isXTurn());
        assert Arrays.stream(game.children()).allMatch(g -> countElement(g.getBoard(), 1) == 1);
        assert Arrays.stream(game.children()).allMatch(g -> countElement(g.getBoard(), -1) == 0);
        assert Arrays.stream(game.children()).allMatch(g -> countElement(g.getBoard(), 0) == 8);

    }

    private static int countElement(int[] arr, int elm){
        int count = 0;
        for(int e: arr)
            if(e == elm)
                count++;
        return count;
    }

}

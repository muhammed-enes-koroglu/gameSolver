import java.util.ArrayList;
import java.util.Arrays;

public abstract class TestTicTacToeState {
    
    public static void testTicTacToeState(){
        System.out.println("\n Testing TicTacToe..");

        testChildren();
        testXWonXLost();
        testHashCode();
        
        System.out.println("\nAll TicTacToe tests are Successful.");

    }

    private static void testChildren(){
        System.out.println("testChildren..");
        int[] board = new int[]{0,0,0, 0,0,0, 0,0,0};
        TicTacToeState game = new TicTacToeState(board);

        assert Arrays.stream(game.children()).allMatch(g -> !g.isXTurn());
        assert Arrays.stream(game.children()).allMatch(g -> countElement(g.getBoard(), 1) == 1);
        assert Arrays.stream(game.children()).allMatch(g -> countElement(g.getBoard(), -1) == 0);
        assert Arrays.stream(game.children()).allMatch(g -> countElement(g.getBoard(), 0) == 8);

        System.out.println("testChildren successful");
    }

    private static void testXWonXLost(){
        System.out.println("testXWon..");

        // X wins.
        int[] board = new int[]{0,0,0, 0,0,0, 0,0,0};
        int[][] boardMatrix = TicTacToeState.matrificise(board);
        assert(!TicTacToeState.xWon(boardMatrix) && !TicTacToeState.xLost(boardMatrix));

        board = new int[]{1,1,1, 0,0,0, 0,0,0}; // Win in column
        boardMatrix = TicTacToeState.matrificise(board);
        assert(TicTacToeState.xWon(boardMatrix) && !TicTacToeState.xLost(boardMatrix));

        board = new int[]{1,0,0, 1,0,0, 1,0,0}; // Win in row
        boardMatrix = TicTacToeState.matrificise(board);
        assert(TicTacToeState.xWon(boardMatrix) && !TicTacToeState.xLost(boardMatrix));
        
        board = new int[]{1,0,0, 0,1,0, 0,0,1}; // Win on main diagonal
        boardMatrix = TicTacToeState.matrificise(board);
        assert(TicTacToeState.xWon(boardMatrix) && !TicTacToeState.xLost(boardMatrix));
        
        board = new int[]{0,0,1, 0,1,0, 1,0,0}; // Win on second diagonal
        boardMatrix = TicTacToeState.matrificise(board);
        assert(TicTacToeState.xWon(boardMatrix) && !TicTacToeState.xLost(boardMatrix));
  
        // X loses.        
        board = new int[]{-1,-1,-1, 0,0,0, 0,0,0}; // Lose in column
        boardMatrix = TicTacToeState.matrificise(board);
        assert(!TicTacToeState.xWon(boardMatrix) && TicTacToeState.xLost(boardMatrix));

        board = new int[]{-1,0,0, -1,0,0, -1,0,0}; // Lose in row
        boardMatrix = TicTacToeState.matrificise(board);
        assert(!TicTacToeState.xWon(boardMatrix) && TicTacToeState.xLost(boardMatrix));
        
        board = new int[]{-1,0,0, 0,-1,0, 0,0,-1}; // Lose on main diagonal
        boardMatrix = TicTacToeState.matrificise(board);
        assert(!TicTacToeState.xWon(boardMatrix) && TicTacToeState.xLost(boardMatrix));
        
        board = new int[]{0,0,-1, 0,-1,0, -1,0,0}; // Lose on second diagonal
        boardMatrix = TicTacToeState.matrificise(board);
        assert(!TicTacToeState.xWon(boardMatrix) && TicTacToeState.xLost(boardMatrix));

        // Trickier
        board = new int[]{1,1,0, 1,0,-1, 0,-1,0};
        boardMatrix = TicTacToeState.matrificise(board);
        assert(!TicTacToeState.xWon(boardMatrix) && !TicTacToeState.xLost(boardMatrix));

        System.out.println("testXWonXLost successful");
    }

    private static void testHashCode(){
        System.out.println("testHashCode..");
        int[] board = new int[]{0,0,0, 0,0,0, 0,0,0};
        TicTacToeState game = new TicTacToeState(board);
        
        ArrayList<Integer> hashcodes = new ArrayList<>();
        Arrays.stream(game.children()).forEach(c -> {
            assert !hashcodes.contains(c.hashCode());
            hashcodes.add(c.hashCode());
        });

        System.out.println("testChildren successful");
    }
    
    private static int countElement(int[] arr, int elm){
        int count = 0;
        for(int e: arr)
            if(e == elm)
                count++;
        return count;
    }


}

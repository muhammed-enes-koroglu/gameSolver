package games.tictactoe;

import util.Board;
import util.Helper;

import java.util.ArrayList;
import java.util.Set;

public class TestTicTacToeState {

    private TestTicTacToeState(){
        throw new IllegalStateException("Utility class");
    }
    
    public static void testTicTacToeState(){
        System.out.println("[TESTING] TicTacToe");

        testChildren();
        testXWonXLost();
        testHashCode();
        testScore();

        System.out.println("[TESTING] TicTacToe - DONE\n");

    }

    private static void testChildren(){

        Board board = new Board( new int[]{0,0,0, 0,0,0, 0,0,0});
        TicTacToeState game = new TicTacToeState(board, true); // so it's max's turn.
        Set<TicTacToeState> children = game.children();
        assert children.stream().allMatch(g -> !g.isMaxPlayersTurn());
        assert children.stream().allMatch(g -> !g.isXTurn());
        assert children.stream().allMatch(g -> Helper.countElement(g.getBoard().getMatrix()[0], 1) == 1);
        assert children.stream().allMatch(g -> Helper.countElement(g.getBoard().getMatrix()[0], -1) == 0);
        assert children.stream().allMatch(g -> Helper.countElement(g.getBoard().getMatrix()[0], 0) == 8);

        game = new TicTacToeState(board, false); // so it's NOT max's turn.
        children = game.children();
        assert children.stream().allMatch(g -> g.isMaxPlayersTurn());
        assert children.stream().allMatch(g -> !g.isXTurn());
        assert children.stream().allMatch(g -> Helper.countElement(g.getBoard().getMatrix()[0], 1) == 1);
        assert children.stream().allMatch(g -> Helper.countElement(g.getBoard().getMatrix()[0], -1) == 0);
        assert children.stream().allMatch(g -> Helper.countElement(g.getBoard().getMatrix()[0], 0) == 8);
                
        System.out.println("    Children: OK");
    }

    private static void testXWonXLost(){

        // X wins.
        int[] board = new int[]{0,0,0, 0,0,0, 0,0,0};
        int[][] boardMatrix = TicTacToeState.matrificise(board);
        assert !TicTacToeState.xWon(boardMatrix) && !TicTacToeState.xLost(boardMatrix);

        board = new int[]{1,1,1, 0,0,0, 0,0,0}; // Win in column
        boardMatrix = TicTacToeState.matrificise(board);
        assert TicTacToeState.xWon(boardMatrix) && !TicTacToeState.xLost(boardMatrix);

        board = new int[]{1,0,0, 1,0,0, 1,0,0}; // Win in row
        boardMatrix = TicTacToeState.matrificise(board);
        assert TicTacToeState.xWon(boardMatrix) && !TicTacToeState.xLost(boardMatrix);
        
        board = new int[]{1,0,0, 0,1,0, 0,0,1}; // Win on main diagonal
        boardMatrix = TicTacToeState.matrificise(board);
        assert TicTacToeState.xWon(boardMatrix) && !TicTacToeState.xLost(boardMatrix);
        
        board = new int[]{0,0,1, 0,1,0, 1,0,0}; // Win on second diagonal
        boardMatrix = TicTacToeState.matrificise(board);
        assert TicTacToeState.xWon(boardMatrix) && !TicTacToeState.xLost(boardMatrix);
  
        // X loses.        
        board = new int[]{-1,-1,-1, 0,0,0, 0,0,0}; // Lose in column
        boardMatrix = TicTacToeState.matrificise(board);
        assert !TicTacToeState.xWon(boardMatrix) && TicTacToeState.xLost(boardMatrix);
        
        board = new int[]{-1,0,0, -1,0,0, -1,0,0}; // Lose in row
        boardMatrix = TicTacToeState.matrificise(board);
        assert !TicTacToeState.xWon(boardMatrix) && TicTacToeState.xLost(boardMatrix);
        
        board = new int[]{-1,0,0, 0,-1,0, 0,0,-1}; // Lose on main diagonal
        boardMatrix = TicTacToeState.matrificise(board);
        assert !TicTacToeState.xWon(boardMatrix) && TicTacToeState.xLost(boardMatrix);
        
        board = new int[]{0,0,-1, 0,-1,0, -1,0,0}; // Lose on second diagonal
        boardMatrix = TicTacToeState.matrificise(board);
        assert !TicTacToeState.xWon(boardMatrix) && TicTacToeState.xLost(boardMatrix);

        // Trickier
        board = new int[]{1,1,0, 1,0,-1, 0,-1,0};
        boardMatrix = TicTacToeState.matrificise(board);
        assert !TicTacToeState.xWon(boardMatrix) && !TicTacToeState.xLost(boardMatrix);

        System.out.println("    XWonXLost: OK");
    }

    private static void testHashCode(){

        Board board = new Board( new int[]{0,0,0, 0,0,0, 0,0,0});
        TicTacToeState game = new TicTacToeState(board, true);
        
        ArrayList<Integer> hashcodes = new ArrayList<>();
        game.children().stream().forEach(c -> {
            assert !hashcodes.contains(c.hashCode());
            hashcodes.add(c.hashCode());
        });

        System.out.println("    Hashcode: OK");
    }
    
    private static void testScore(){

        // X is Max
        Board board = new Board( new int[]{1,0,1, -1,-1,0, 0,0,0});
        TicTacToeState ticState = new TicTacToeState(board, true);

        assert ticState.score() == 0;
        assert ticState.children().stream().anyMatch(g -> g.score() == Float.MAX_VALUE);
        assert ticState.children().stream().anyMatch(g -> g.score() == 0);
        assert ticState.children().stream().noneMatch(g -> g.score() == -Float.MAX_VALUE);
        
        // O is Max
        board = new Board( new int[]{1,0,1, -1,-1,0, 0,0,0});
        ticState = new TicTacToeState(board, false);

        assert ticState.score() == 0;
        assert ticState.children().stream().anyMatch(g -> g.score() == -Float.MAX_VALUE);
        assert ticState.children().stream().anyMatch(g -> g.score() == 0);
        assert ticState.children().stream().noneMatch(g -> g.score() == Float.MAX_VALUE);
        
               
        System.out.println("    Score: OK");
    }

}

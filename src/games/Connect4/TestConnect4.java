package games.connect4;

import util.Board;
import util.TwoPersonGameState;
import util.GameSolver;

import java.util.List;
import java.util.Set;

public class TestConnect4 {

    public static final int BOARD_WIDTH = Connect4State.BOARD_WIDTH;
    public static final int BOARD_HEIGHT = Connect4State.BOARD_HEIGHT;
    public static final float MAX_SCORE = TwoPersonGameState.MAX_SCORE;
    public static final float MIN_SCORE = TwoPersonGameState.MIN_SCORE;

    private TestConnect4() {
        throw new IllegalStateException("Utility class");
      }

    public static void testConnect4(){

        System.out.println("[TESTING] Connect4 class");

        testEquals();
        testScore();
        testChildren();
        testPrivateMethods();
        
        System.out.println("[TESTING] Connect4 class - DONE\n");
    }

    private static void testEquals(){
        
        Board board1 = getInitialBoard();
        Board board2 = getInitialBoard();
        assert board1.equals(board2);

        board1.set(0, 0, 1);
        board2.set(0, 0, 1);
        assert board1.equals(board2);
        
        Connect4State game1 = new Connect4State(board1, true, true);
        Connect4State game2 = new Connect4State(board2, true, true);
        assert game1.equals(game2);

        System.out.println("    Equals: OK");
    }

    private static void testScore(){
        Board board1 = getInitialBoard();
        board1.set(0, 0, 1);
        board1.set(0, 1, 1);

        Connect4State state1 = new Connect4State(board1, true, true);

        Board board2 = board1.copy();
        board2.set(0, 2, 1);
        Connect4State state2 = new Connect4State(board2, true, true);
        assert state2.score() > state1.score();

        Board board3 = board2.copy();
        board3.set(0, 3, 1);
        Connect4State state3 = new Connect4State(board3, true, true);
        assert state3.score() > state2.score();



        int[][] array = {{1, -1, 0, -1, 0, -1, 0}, {1, 0, 0, 0, 0, 0, 0}, {1, 0, 0, 0, 0, 0, 0}, {1, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0}};
        board1 = new Board(array);
        state1 = new Connect4State(board1, false, false);
        assert state1.score() == MIN_SCORE;


        // Test the score of a board with a losing diagonal.
        array = new int[][]{{1, 1, 1, -1, 0, 0, 0}, {-1, 1, -1, -1, 0, 0, 0}, {1, -1, 0, 0, 0, 0, 0}, {-1, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0}};
        board1 = new Board(array);
        state1 = new Connect4State(board1, true, true);
        assert state1.score() == MIN_SCORE;

        // Same board, change the maxPlayer.
        state1 = new Connect4State(board1, true, false);
        assert state1.score() == MAX_SCORE;

        // Test that the advised state is 
        // the one state that prevents the certain lose.
        board1 = new Board(new int[][]{{1, 1, 1, -1, 0, 0, 0}, {-1, 1, 0, -1, 0, 0, 0}, {1, -1, 0, 0, 0, 0, 0}, {-1, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0}});
        state1 = new Connect4State(board1, true, true);
        List<Connect4State> bestPath = GameSolver.iterDeepeningMiniMax(state1, 0.1f);

        board2 = new Board(new int[][]{{1, 1, 1, -1, 0, 0, 0}, {-1, 1, 1, -1, 0, 0, 0}, {1, -1, 0, 0, 0, 0, 0}, {-1, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0}});
        Connect4State expectedState = new Connect4State(board2, false, true);
        Connect4State advisedState = bestPath.get(1);

        assert advisedState.equals(expectedState);

        System.out.println("    Score: OK");
    }

    private static void testChildren(){

        // Board with one element.
        Board board = getInitialBoard();
        board.set(0, 0, 1);

        Connect4State connect4;
        Set<Connect4State> children;

        // Board with white wins.
        board = getInitialBoard();
        board.set(0, 0, 1);
        board.set(0, 1, 1);
        board.set(0, 2, 1);
        board.set(0, 3, 1);

        connect4 = new Connect4State(board, true, true);
        children = connect4.children();
        assert children.isEmpty();

        // Board with black wins.
        board = getInitialBoard();
        board.set(0, 0, -1);
        board.set(0, 1, -1);
        board.set(0, 2, -1);
        board.set(0, 3, -1);

        connect4 = new Connect4State(board, true, true);
        children = connect4.children();
        assert children.isEmpty();

        System.out.println("    Children: OK");
        
    }

    private static Board getInitialBoard(){
        int[][] array = new int[BOARD_HEIGHT][BOARD_WIDTH];
        return new Board(array);
    }

    private static void testPrivateMethods(){
        
        Connect4State.testPrivateMethods();

    }

}

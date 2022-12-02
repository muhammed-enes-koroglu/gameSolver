package games.reversi;

import util.Board;
import util.ConsoleColors;
import util.TwoPersonGameState;

import java.util.Set;

public class TestReversi {

    public static final float MAX_SCORE = TwoPersonGameState.MAX_SCORE;
    public static final float MIN_SCORE = TwoPersonGameState.MIN_SCORE;
    private static final int BOARD_SIZE = ReversiState.BOARD_SIZE;
    private static final int WHITE = ReversiState.WHITE;
    private static final int BLACK = ReversiState.BLACK;

    private TestReversi() {
        throw new IllegalStateException("Utility class");
      }

    public static void testReversi(){

        System.out.println("[TESTING] Reversi class");

        testEquals();
        testScore();
        testChildren();
        // testPrivateMethods();
        
        System.out.println("[TESTING] Reversi class - DONE\n");
    }

    private static void testEquals(){
        
        Board board1 = getInitialBoard();
        Board board2 = getInitialBoard();
        assert board1.equals(board2);

        board1.set(0, 0, 1);
        board2.set(0, 0, 1);
        assert board1.equals(board2);
        
        ReversiState state1 = new ReversiState(board1, true, true);
        ReversiState state2 = new ReversiState(board2, true, true);
        assert state1.equals(state2);

        System.out.println("    Equals: OK");
    }

    private static void testScore(){
        Board board = getInitialBoard();
        ReversiState state = new ReversiState(board, true, true);
        assert state.score() == 0;
        
        // Test score simple.
        for(ReversiState child : state.children()){
            assert child.score() == 3;
        }

        // Test the score of a full board with equal pieces.
        int piece = WHITE;
        for(int i = 0; i < BOARD_SIZE; i++){
            for(int j = 0; j < BOARD_SIZE; j++){
                piece *= -1;
                board.set(i, j, piece);
            }
        }
        state = new ReversiState(board, true, true);
        assert state.score() == 0;

        // Test the score of a full board where one player has more pieces.
        board.set(0, 0, WHITE);
        state = new ReversiState(board, true, true);
        assert state.score() == MAX_SCORE;

        // Now test the score of a full board where the other player has more pieces.
        board.set(0, 0, BLACK);
        board.set(0, 1, BLACK);
        state = new ReversiState(board, true, true);
        assert state.score() == MIN_SCORE;


        System.out.println("    Score: OK");
    }

    private static void testChildren(){

        testChildrenHorizontallyVertically();
        testChildrenDiagonally();

        Board board = getInitialBoard();
        ReversiState state = new ReversiState(board, true, true);
        Set<ReversiState> children = state.children();

        // System.out.println(ConsoleColors.YELLOW_BACKGROUND + state.toString(ConsoleColors.YELLOW_BACKGROUND) + ConsoleColors.RESET);
        for(ReversiState child : children){

            // System.out.println(ConsoleColors.GREEN_BACKGROUND + child.toString(ConsoleColors.GREEN_BACKGROUND) + ConsoleColors.RESET);
            assert child.children().size() == 3;

            // for(ReversiState grandChild : child.children()){
                // System.out.println(ConsoleColors.CYAN_BACKGROUND_BRIGHT + grandChild.toString(ConsoleColors.CYAN_BACKGROUND_BRIGHT) + ConsoleColors.RESET);
            // }
        }

        System.out.println("    Children: OK");
    }

    private static void testChildrenHorizontallyVertically(){
 
        Board board = getInitialBoard();

        // Check if number of children is correct.
        ReversiState state = new ReversiState(board, true, true);
        Set<ReversiState> children = state.children();
        // System.out.println("ORIGINAL: \n" + state);
        // System.out.println("CHILDREN: \n" + children);
        assert children.size() == 4;

        // Check if cointains the correct children 
        // both horizontally and vertically.

        // Test child to the right.
        board = getInitialBoard();
        board.set(4, 4, WHITE);
        board.set(4, 5, WHITE);
        state = new ReversiState(board.copy(), false, true);
        assert children.contains(state);

        // Test child to the left.
        board = getInitialBoard();
        board.set(3, 2, WHITE);
        board.set(3, 3, WHITE);
        state = new ReversiState(board.copy(), false, true);
        assert children.contains(state);

        // Test child to the top.
        board = getInitialBoard();
        board.set(4, 4, WHITE);
        board.set(5, 4, WHITE);
        state = new ReversiState(board.copy(), false, true);
        assert children.contains(state);

        // Test child to the bottom.
        board = getInitialBoard();
        board.set(2, 3, WHITE);
        board.set(3, 3, WHITE);
        state = new ReversiState(board.copy(), false, true);
        assert children.contains(state);
        
    }

    private static void testChildrenDiagonally(){
        Board board = getInitialBoard();

        board.set(3, 2, WHITE);
        board.set(3, 3, WHITE);
        ReversiState state = new ReversiState(board.copy(), true, true);
        Set<ReversiState> children = state.children();
        // System.out.println("ORIGINAL: \n" + state);
        // System.out.println("CHILDREN: \n" + children);

        // Check if number of children is correct.
        assert children.size() == 3;


        // Test child to the top right.
        board.set(4, 4, WHITE);
        board.set(5, 5, WHITE);
        state = new ReversiState(board.copy(), false, true);
        assert children.contains(state);

        




    }

    private static Board getInitialBoard(){
        int[][] array = new int[ReversiState.BOARD_SIZE][ReversiState.BOARD_SIZE];

        // Get a random +1 or -1
        // int random = (int) (Math.random() * 2);
        // int sign = random == 0 ? 1 : -1;
        int sign = 1;
        // Set the initial board.
        array[4][3] = sign * 1;
        array[3][4] = sign * 1;
        array[3][3] = sign * -1;
        array[4][4] = sign * -1;

        return new Board(array);
    }

    private static void testPrivateMethods(){
        
        // ReversiState.testPrivateMethods();

    }

}

import java.beans.Statement;
import java.util.Set;

public class TestConnect4 {

    public static final int BOARD_WIDTH = Connect4.BOARD_WIDTH;
    public static final int BOARD_HEIGHT = Connect4.BOARD_HEIGHT;
    public static final int MAX_SCORE = Connect4.MAX_SCORE;

    public static void main(String[] args){

        // testToString();
        testChildren();
        // testPlusWithCeiling();
    }
    
    private static void testChildren(){

        // Board with one element.
        Board board = getInitialBoard();
        board.set(0, 0, 1);

        Connect4 align4 = new Connect4(board, true, true);
        Set<Connect4> children = align4.children();

        System.out.println(children);
        System.out.println(children.size());


        // Full board.
        board = getFullBoard();
        align4 = new Connect4(board, true, true);

        try{
            children = align4.children();
        } catch(IllegalArgumentException e){
            System.out.println("Full Board tests: OK");
        }
        
    }

    private static void testToString(){

        Board board = getInitialBoard();
        board.set(new Vector(2, 3), 1);
        Connect4 align4 = new Connect4(board, true, true);
        System.out.println(align4);
    }

    private static void testPlusWithCeiling(){
        assrt(Connect4.plusWithOverflow(1, 1) == 1 + 1);
        assrt(Connect4.plusWithOverflow(MAX_SCORE, 1) == MAX_SCORE);
        assrt(Connect4.plusWithOverflow(MAX_SCORE, MAX_SCORE) == MAX_SCORE);

        System.out.println("OK");
    }

    private static Board getInitialBoard(){
        int[][] array = new int[BOARD_HEIGHT][BOARD_WIDTH];
        return new Board(array);
    }

    private static Board getFullBoard(){
        int[][] array = new int[BOARD_HEIGHT][BOARD_WIDTH];
        int rand = -1;
        for(int rowNb=0; rowNb<BOARD_HEIGHT; rowNb++){
            for(int colNb=0; colNb<BOARD_WIDTH; colNb++){
                rand = -1 * rand;
                array[rowNb][colNb] = rand;
            }
        }

        return new Board(array);
    }

    private static void assrt(boolean statament){
        if(statament)
            return;
        throw new AssertionError();
    }

}

import java.util.Set;

public class TestConnect4 {

    public static final int BOARD_WIDTH = Connect4.BOARD_WIDTH;
    public static final int BOARD_HEIGHT = Connect4.BOARD_HEIGHT;
    public static final float MAX_SCORE = Connect4.MAX_SCORE;

    public static void main(String[] args){

        System.out.println("[TESTING] Connect4 class\n");

        // testToString();
        testPlusWithCeiling();
        testEquals();
        testScore();
        testChildren();
        
        System.out.println("\n[TESTING] Connect4 class - DONE");
    }

    private static void testPlusWithCeiling(){
        assrt(Connect4.addWithOverflow(1, 1) == 1 + 1);
        assrt(Connect4.addWithOverflow(MAX_SCORE, 1) == MAX_SCORE);
        assrt(Connect4.addWithOverflow(MAX_SCORE, MAX_SCORE) == MAX_SCORE);

        System.out.println("PlusWithCeiling: OK");
    }
    
    private static void testEquals(){
        
        Board board1 = getInitialBoard();
        Board board2 = getInitialBoard();
        assrt(board1.equals(board2));
        
        board1.set(0, 0, 1);
        board2.set(0, 0, 1);
        assrt(board1.equals(board2));
        
        Connect4 game1 = new Connect4(board1, true, true);
        Connect4 game2 = new Connect4(board2, true, true);
        assrt(game1.equals(game2));

        System.out.println("Equals: OK");
    }

    private static void testScore(){
        Board board1 = getInitialBoard();
        board1.set(0, 0, 1);
        board1.set(0, 1, 1);

        Connect4 state1 = new Connect4(board1, true, true);

        Board board2 = board1.copy();
        board2.set(0, 2, 1);
        Connect4 state2 = new Connect4(board2, true, true);
        assrt(state2.score() > state1.score());

        Board board3 = board2.copy();
        board3.set(0, 3, 1);
        Connect4 state3 = new Connect4(board3, true, true);
        assrt(state3.score() > state2.score());




        // System.out.println(connect4.score());
        System.out.println(state2);
        System.out.println(GameSolver.findBestPathForMax(state2, 1));


        System.out.println("Score: OK");
    }

    private static void testChildren(){

        // Board with one element.
        Board board = getInitialBoard();
        board.set(0, 0, 1);

        Connect4 connect4 = new Connect4(board, true, true);
        Set<Connect4> children = connect4.children();

        // Board with white wins.
        board = getInitialBoard();
        board.set(0, 0, 1);
        board.set(0, 1, 1);
        board.set(0, 2, 1);
        board.set(0, 3, 1);

        connect4 = new Connect4(board, true, true);
        children = connect4.children();
        assrt(children.isEmpty());

        // Board with black wins.
        board = getInitialBoard();
        board.set(0, 0, -1);
        board.set(0, 1, -1);
        board.set(0, 2, -1);
        board.set(0, 3, -1);

        connect4 = new Connect4(board, true, true);
        children = connect4.children();
        assrt(children.isEmpty());

        System.out.println("Children: OK");
        
    }

    private static void testToString(){

        Board board = getInitialBoard();
        board.set(new Vector(2, 3), 1);
        Connect4 align4 = new Connect4(board, true, true);
        System.out.println(align4);
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

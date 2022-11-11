package test;

import games.Connect4;
import util.Board;
import util.Helper;

import java.util.Set;

public class TestConnect4 {

    public static final int BOARD_WIDTH = Connect4.BOARD_WIDTH;
    public static final int BOARD_HEIGHT = Connect4.BOARD_HEIGHT;
    public static final float MAX_SCORE = Connect4.MAX_SCORE;

    public static void main(String[] args){

        System.out.println("[TESTING] Connect4 class\n");

        testEquals();
        testScore();
        testChildren();
        testPrivateMethods();
        
        System.out.println("\n[TESTING] Connect4 class - DONE");
    }

    private static void testEquals(){
        
        Board board1 = getInitialBoard();
        Board board2 = getInitialBoard();
        Helper.assrt(board1.equals(board2));
        
        board1.set(0, 0, 1);
        board2.set(0, 0, 1);
        Helper.assrt(board1.equals(board2));
        
        Connect4 game1 = new Connect4(board1, true, true);
        Connect4 game2 = new Connect4(board2, true, true);
        Helper.assrt(game1.equals(game2));

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
        Helper.assrt(state2.score() > state1.score());

        Board board3 = board2.copy();
        board3.set(0, 3, 1);
        Connect4 state3 = new Connect4(board3, true, true);
        Helper.assrt(state3.score() > state2.score());



        int[][] array = {{1, -1, 0, -1, 0, -1, 0}, {1, 0, 0, 0, 0, 0, 0}, {1, 0, 0, 0, 0, 0, 0}, {1, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0}};
        board1 = new Board(array);
        state1 = new Connect4(board1, false, false);
        Helper.assrt(state1.score() == -MAX_SCORE);

        // System.out.println(state1.score());
        // System.out.println(state1);
        // System.out.println(GameSolver.findBestPathForMax(state2, 1));


        System.out.println("Score: OK");
    }

    private static void testChildren(){

        // Board with one element.
        Board board = getInitialBoard();
        board.set(0, 0, 1);

        Connect4 connect4;
        Set<Connect4> children;

        // Board with white wins.
        board = getInitialBoard();
        board.set(0, 0, 1);
        board.set(0, 1, 1);
        board.set(0, 2, 1);
        board.set(0, 3, 1);

        connect4 = new Connect4(board, true, true);
        children = connect4.children();
        Helper.assrt(children.isEmpty());

        // Board with black wins.
        board = getInitialBoard();
        board.set(0, 0, -1);
        board.set(0, 1, -1);
        board.set(0, 2, -1);
        board.set(0, 3, -1);

        connect4 = new Connect4(board, true, true);
        children = connect4.children();
        Helper.assrt(children.isEmpty());

        System.out.println("Children: OK");
        
    }

    private static Board getInitialBoard(){
        int[][] array = new int[BOARD_HEIGHT][BOARD_WIDTH];
        return new Board(array);
    }

    private static void testPrivateMethods(){
        
        Connect4.testPrivateMethods();

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

}

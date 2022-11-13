package games.Connect4;

import util.TwoPersonPlay;
import util.Board;
import util.TwoPersonGameState;

import java.util.Scanner;


public class PlayConnect4 implements TwoPersonPlay<Connect4> {

    public static final int BOARD_WIDTH = Connect4.BOARD_WIDTH;
    public static final int BOARD_HEIGHT = Connect4.BOARD_HEIGHT;

    public static final int WHITE = Connect4.WHITE;
    public static final int BLACK = Connect4.BLACK;
    public static final int EMPTY = Connect4.EMPTY;

    private static final Scanner sc = new Scanner(System.in);

    @Override
    public boolean isGameOver(Connect4 state) {
        return Math.abs(state.score()) == TwoPersonGameState.MAX_SCORE;
    }

    @Override
    public Connect4 getInitialState(boolean maximizeForWhite){
        return new Connect4(getInitialBoard(), true, maximizeForWhite);
    }

    private static Board getInitialBoard(){
        int[][] array = new int[BOARD_HEIGHT][BOARD_WIDTH];
        return new Board(array);
    }

    @Override
    public Connect4 makeMove(Connect4 state, int moveNumber) {
        
        final int piece = state.whitesTurn ? WHITE : BLACK;

        Board board = state.getBoard();
        Connect4.appendToColumn(board, moveNumber, piece);

        return new Connect4(board, !state.whitesTurn, state.maximizeForWhite);
        
    }

    @Override
    public int scanMoveNumber(Connect4 state) {
        
        int colNumber = -1;
        boolean validInput = false;
        while(!validInput){
            validInput = true;

            // Ask for input.
            System.out.print("Enter column number: ");
            String line = sc.nextLine();

            // Parse the input.
            try{
                colNumber = Integer.parseInt(line) - 1;
            }catch(NumberFormatException e){
                System.out.println("[ERROR] Input must be an integer");
                validInput = false;
            }

            // colNumber should be in [1, BOARD_WIDTH]
            if(colNumber < 0 || colNumber >= BOARD_WIDTH ){
                System.out.println("[ERROR] Column number must be in [1, " + BOARD_WIDTH + "]");
                validInput = false;
            }
            // Trench to be played should not be full.
            else if(state.getBoard().get(BOARD_HEIGHT-1, colNumber) != EMPTY){
                System.out.println("[ERROR] Column may not be full");
                validInput = false;
            }
        }
        return colNumber;
    }

    @Override
    public String getWinnersName(Connect4 state) {

        if(state.score() == TwoPersonGameState.MAX_SCORE){ // Max player has won.
            return state.maximizeForWhite ? "Player 1" : "Player 2";
        } else { // Min player has won.
            return state.maximizeForWhite ? "Player 2" : "Player 1";
        }

    }
}

package games.connect4;

import util.TwoPersonPlay;
import util.Board;
import util.TwoPersonGameState;

import java.util.Arrays;
import java.util.Scanner;


public class PlayConnect4 implements TwoPersonPlay<Connect4State> {

    public static final int BOARD_WIDTH = Connect4State.BOARD_WIDTH;
    public static final int BOARD_HEIGHT = Connect4State.BOARD_HEIGHT;

    public static final int WHITE = Connect4State.WHITE;
    public static final int BLACK = Connect4State.BLACK;
    public static final int EMPTY = Connect4State.EMPTY;

    private static final Scanner sc = new Scanner(System.in);

    @Override
    public boolean isGameOver(Connect4State state) {
        return (Math.abs(state.score()) == TwoPersonGameState.MAX_SCORE) ||
                Arrays.stream(state.getBoard().getMatrix()).allMatch(row -> 
                Arrays.stream(row).allMatch(cell -> cell != EMPTY));
    }

    @Override
    public Connect4State getInitialState(boolean maximizeForWhite){
        return new Connect4State(getInitialBoard(), true, maximizeForWhite);
    }

    private static Board getInitialBoard(){
        int[][] array = new int[BOARD_HEIGHT][BOARD_WIDTH];
        return new Board(array);
    }

    @Override
    public Connect4State makeMove(Connect4State state, int[] action) {
        
        final int piece = state.whitesTurn ? WHITE : BLACK;

        Board board = state.getBoard();
        Connect4State.appendToColumn(board, action[0], piece);

        return new Connect4State(board, !state.whitesTurn, state.maximizeForWhite);
        
    }

    @Override
    public int[] scanMoveNumber(Connect4State state) {
        
        int colNumber = -1;
        boolean validInput = false;
        while(!validInput){
            validInput = true;

            // Ask for input.
            System.out.print("Enter column number: ");
            String line = sc.nextLine();
     
            // Return null if input is empty.
            if(line.equals("")){
                return new int[0];
            }

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
        return new int[]{colNumber};
    }

    @Override
    public String getWinnersName(Connect4State state) {

        if(state.score() == TwoPersonGameState.MAX_SCORE){ // Max player has won.
            return state.maximizeForWhite ? "X" : "O";
        } else if(state.score()== TwoPersonGameState.MIN_SCORE) { // Min player has won.
            return state.maximizeForWhite ? "O" : "X";
        } else { // Game is a draw.
            return "No one";
        }

    }
}

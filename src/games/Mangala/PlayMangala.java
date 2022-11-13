package games.Mangala;

import util.Board;
import util.TwoPersonPlay;

import java.util.HashSet;
import java.util.Scanner;

public class PlayMangala implements TwoPersonPlay<MangalaState> {

    public static final int BOARD_SIZE = MangalaState.BOARD_SIZE;
    public static final int WHITE_STARTING_TRENCH = MangalaState.WHITE_STARTING_TRENCH;
    public static final int BLACK_STARTING_TRENCH = MangalaState.BLACK_STARTING_TRENCH;
    
    private static final Scanner sc = new Scanner(System.in);

    // public void main() {

    //     MangalaState mangala = getInitialState(true);
    //     MangalaState advisedState;
    //     List<MangalaState> advisedPath;

    //     // The game loop
    //     while(!mangala.isGameOver()){
    //         System.out.println("[CURRENT]" + mangala);

    //         advisedPath = GameSolver.findBestPathForMax(mangala, 1);
    //         if(!advisedPath.isEmpty()){
    //             advisedState = advisedPath.get(1);
    //             System.out.println("[ADVISED]" + advisedState);    
    //         }

    //         // Get the user's input and update the state.
    //         int trenchNumber = inputTrenchNumber(mangala);
    //         mangala = mangala.makeMove(trenchNumber);
    //     }

    //     // Print the final state.
    //     System.out.println("[GAME OVER]" + mangala);
    //     // Print the winner.
    //     System.out.println("[WINNER] " + getWinnersName(mangala) + " player wins!");
    // }

    /** Scan, parse and validate the trench number player wants to play. */
    private static int inputTrenchNumber(MangalaState mangala){
        int offset = mangala.isWhitesTurn() ? WHITE_STARTING_TRENCH : BLACK_STARTING_TRENCH;
        
        int trenchNumber = -1;
        boolean validInput = false;
        while(!validInput){
            validInput = true;

            // Ask for input.
            System.out.print("Enter trench number: ");
            String line = sc.nextLine();

            // Parse the input.
            try{
                trenchNumber = Integer.parseInt(line) - 1;
            }catch(NumberFormatException e){
                System.out.println("[ERROR] Input must be an integer");
                validInput = false;
            }

            // trenchNumber should be in [0, BOARD_SIZE-1)
            if(!(trenchNumber >= 0 && trenchNumber < BOARD_SIZE-1 )){
                System.out.println("[ERROR] Trench number must be in [1, " + (BOARD_SIZE-1) + "]");
                validInput = false;
            }
            // Trench to be played should not be empty.
            else if(mangala.getBoard().get(0, trenchNumber + offset) == 0){
                System.out.println("[ERROR] Trench number must be occupied");
                validInput = false;
            }
        }
        return trenchNumber + offset;
    }

    @Override
    /** Returns winner's name. Assumes game is over. */
    public String getWinnersName(MangalaState mangala){
        Board board = mangala.getBoard();
        int scoreWhite = board.get(0, BOARD_SIZE-1);
        int scoreBlack = board.get(0, 2*BOARD_SIZE-1);

        // Player with more stones wins.
        if(scoreWhite > scoreBlack){
            return "First";
        }
        else{
            return "Second";
        }
    }

    @Override
    public boolean isGameOver(MangalaState state) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public MangalaState makeMove(MangalaState state, int trenchNumber) {

        return state.playTrench(new HashSet<>(), trenchNumber, state.getBoard());
    }

    @Override
    public MangalaState getInitialState(boolean maximizeForWhite) {
        boolean whitesTurn = true;
        boolean whiteIsMax = whitesTurn;
        Board board = new Board(new int[][]{ new int[]{4,4,4, 4,4,4, 0, 4,4,4, 4,4,4, 0}});

        return new MangalaState(board, whitesTurn, whiteIsMax);
    }

    @Override
    public int scanMoveNumber(MangalaState state) {
        
        int trenchNumber = -1;
        boolean validInput = false;
        while(!validInput){
            validInput = true;

            // Ask for input.
            System.out.print("Enter column number: ");
            String line = sc.nextLine();

            // Parse the input.
            try{
                trenchNumber = Integer.parseInt(line) - 1;
                // colNumber should be in [1, BOARD_SIZE-1]
                if(trenchNumber < 0 || trenchNumber > MangalaState.BOARD_SIZE-2){
                    System.out.println("[ERROR] Column number must be in [1, " + (MangalaState.BOARD_SIZE -1)+ "]");
                    validInput = false;
                }
                // Trench to be played should not be empty.
                else if(state.getBoard().get(0, trenchNumber) == 0){
                    System.out.println("[ERROR] Column may not be empty");
                    validInput = false;
                }
            } catch(NumberFormatException e){
                System.out.println("[ERROR] Input must be an integer");
                validInput = false;
            }

        }
        return trenchNumber + MangalaState.getOffsetForColor(state.isWhitesTurn());
    }

}

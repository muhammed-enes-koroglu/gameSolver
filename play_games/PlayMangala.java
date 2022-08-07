import java.util.List;
import java.util.Scanner;

public class PlayMangala {

    public static final int BOARD_SIZE = MangalaState.BOARD_SIZE;
    public static final int WHITE_STARTING_TRENCH = MangalaState.WHITE_STARTING_TRENCH;
    public static final int BLACK_STARTING_TRENCH = MangalaState.BLACK_STARTING_TRENCH;
    
    private static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {

        MangalaState mangala = getStartingState();
        MangalaState advisedState;
        List<MangalaState> advisedPath;

        // The game loop
        while(!mangala.isGameOver()){
            System.out.println("[CURRENT]" + mangala);

            advisedPath = GameSolver.findBestPathForMax(mangala, 1);
            if(!advisedPath.isEmpty()){
                advisedState = advisedPath.get(1);
                System.out.println("[ADVISED]" + advisedState);    
            }

            // Get the user's input and update the state.
            int trenchNumber = inputTrenchNumber(mangala);
            mangala = mangala.makeMove(trenchNumber);
        }

        // Print the final state.
        System.out.println("[GAME OVER]" + mangala);
        // Print the winner.
        System.out.println("[WINNER] " + getWinnersName(mangala) + " player wins!");
    }

    /** Returns the mangala state to start the game. */
    private static MangalaState getStartingState(){
        boolean whitesTurn = true;
        boolean whiteIsMax = whitesTurn;
        int[] board = new int[]{4,4,4, 4,4,4, 0, 4,4,4, 4,4,4, 0};
        // int[] board = new int[]{0,0,0, 0,0,1, 0, 4,4,4, 4,4,4, 0};

        return new MangalaState(board, whitesTurn, whiteIsMax);
    }


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
            else if(mangala.getBoard()[trenchNumber + offset] == 0){
                System.out.println("[ERROR] Trench number must be occupied");
                validInput = false;
            }
        }
        return trenchNumber + offset;
    }
    
    /** Scan, parse and validate whether the player wants to be player 1. */
    private static boolean inputWhiteIsMax(){
        
        int inputNumber = 1;
        boolean validInput = false;
        while(!validInput){
            validInput = true;

            // Ask for input.
            System.out.print("Enter `1` to be player 1, `2` to be player 2: ");
            String line = sc.nextLine();

            // Parse the input.
            try{
                inputNumber = Integer.parseInt(line);
            }
            catch(NumberFormatException e){
                System.out.println("[ERROR] Input must be an integer");
                validInput = false;
            }
            
            // inputNumber should be 1 or 2.
            if(!(inputNumber == 1 || inputNumber == 2)){
                System.out.println("[ERROR] Player number must be 1 or 2");
                validInput = false;
            }
        }
        return inputNumber == 1;
    }

    /** Returns winner's name. Assumes game is over. */
    private static String getWinnersName(MangalaState mangala){
        int[] board = mangala.getBoard();
        int scoreWhite = board[BOARD_SIZE-1];
        int scoreBlack = board[2*BOARD_SIZE-1];

        // Player with more stones wins.
        if(scoreWhite > scoreBlack){
            return "First";
        }
        else{
            return "Second";
        }
    }

}

package games.reversi;

import interfaces.TwoPersonGameState;
import interfaces.TwoPersonPlay;
import util.Board;
import util.Vector;

public class PlayReversi implements TwoPersonPlay<ReversiState>{

    @Override
    public boolean isGameOver(ReversiState state) {
        return state.isGameOver();
    }

    @Override
    public ReversiState makeMove(ReversiState state, int[] action) {
        int rowToPlay = action[0];
        int colToPlay = action[1];
        Vector positionToPlay = new Vector(rowToPlay, colToPlay);

        Board board = state.getBoard();
        int piece = state.whitesTurn ? ReversiState.WHITE : ReversiState.BLACK;
        board.set(positionToPlay, piece);
        ReversiState.flipPieces(board, positionToPlay);
        return new ReversiState(board, !state.whitesTurn, state.maximizeForWhite);
    }

    @Override
    public ReversiState getInitialState(boolean maximizeForWhite) {
        int[][] array = new int[ReversiState.BOARD_SIZE][ReversiState.BOARD_SIZE];

        // Get a random +1 or -1 so that the first player is random.
        // int random = (int) (Math.random() * 2);
        // int sign = random == 0 ? 1 : -1;
        int sign = 1;

        // Set the initial board.
        array[4][3] = sign * 1;
        array[3][4] = sign * 1;
        array[3][3] = sign * -1;
        array[4][4] = sign * -1;

        Board board = new Board(array);
        return new ReversiState(board, true, maximizeForWhite);
    }

    @Override
    public int[] scanMoveNumber(ReversiState state) {
    
        int col = -1;
        int row = -1;
        boolean validInput = false;
        while(!validInput){
            validInput = true;

            // Ask for input.
            System.out.print("Enter row: ");
            String lineOfRow = sc.nextLine();
            
            // Return null if input is empty.
            if(lineOfRow.equals("")){
                return new int[0];
            }

            System.out.print("Enter column: ");
            String lineOfCol = sc.nextLine();

            // Parse the input.
            try{
                row = Integer.parseInt(lineOfRow);
                col = Integer.parseInt(lineOfCol);

                // colNumber should be in [0, BOARD_SIZE-1]
                if(row < 0 || row > ReversiState.BOARD_SIZE-1){
                    System.out.println("[ERROR] Row number must be in [0, " + (ReversiState.BOARD_SIZE-1)+ "]");
                    validInput = false;
                }
                // colNumber should be in [0, BOARD_SIZE-1]
                else if(col < 0 || col > ReversiState.BOARD_SIZE-1){
                    System.out.println("[ERROR] Column number must be in [0, " + (ReversiState.BOARD_SIZE-1)+ "]");
                    validInput = false;
                }
                // Square to be played should be empty.
                else{
                    if(state.getBoard().get(row, col) != 0){
                        System.out.println("[ERROR] Entered position must be empty");
                        validInput = false;
                    }
                }
            } catch(NumberFormatException e){
                System.out.println("[ERROR] Input must be an integer");
                validInput = false;
            }
        }
        return new int[]{row, col};
    }
    
    @Override
    public String getWinnersName(ReversiState state) {

        if(state.score() == TwoPersonGameState.MAX_SCORE){ // Max player has won.
            return state.maximizeForWhite ? "X" : "O";
        } else if(state.score()== TwoPersonGameState.MIN_SCORE) { // Min player has won.
            return state.maximizeForWhite ? "O" : "X";
        } else { // Game is a draw.
            return "No one";
        }
    }
    
}

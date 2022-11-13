package games.TicTacToe;

import util.Board;
import util.TwoPersonPlay;

public class PlayTicTacToe implements TwoPersonPlay<TicTacToeState>{

    @Override
    public boolean isGameOver(TicTacToeState state) {
        return state.xWon() || state.xLost();
    }

    @Override
    public TicTacToeState makeMove(TicTacToeState state, int[] action) {

        int xCoordinate = action[0];
        int yCoordinate = action[1];
        Board board = state.getBoard();
        int piece = state.isXTurn() ? TicTacToeState.X : TicTacToeState.O;

        board.set(0, TicTacToeState.BOARD_SIZE * yCoordinate + xCoordinate, piece);
        return new TicTacToeState(board, state.xIsMaxPlayer);

    }

    @Override
    public TicTacToeState getInitialState(boolean maximizeForWhite) {
        Board board = new Board( new int[]{0,0,0, 0,0,0, 0,0,0});
        return new TicTacToeState(board, maximizeForWhite);
    }

    @Override
    public int[] scanMoveNumber(TicTacToeState state) {
        
        int xCoordinate = -1;
        int yCoordinate = -1;
        boolean validInput = false;
        while(!validInput){
            validInput = true;

            // Ask for input.
            System.out.print("First enter the row number: ");
            String line1 = sc.nextLine();
            System.out.print("Now enter the column number: ");
            String line2 = sc.nextLine();
            

            // Parse the input.
            try{
                yCoordinate = Integer.parseInt(line1) - 1;
                xCoordinate = Integer.parseInt(line2) - 1;

                // colNumber should be in [1, BOARD_SIZE-1]
                if(xCoordinate < 0 || xCoordinate > TicTacToeState.BOARD_SIZE-1){
                    System.out.println("[ERROR] Column number must be in [1, " + (TicTacToeState.BOARD_SIZE-1)+ "]");
                    validInput = false;
                }
                // colNumber should be in [1, BOARD_SIZE-1]
                else if(yCoordinate < 0 || yCoordinate > TicTacToeState.BOARD_SIZE-1){
                    System.out.println("[ERROR] Row number must be in [1, " + (TicTacToeState.BOARD_SIZE-1)+ "]");
                    validInput = false;
                }
                // Square to be played should be empty.
                else{
                    int[][] matrixBoard = TicTacToeState.matrificise(state.getBoard().getMatrix()[0]);
                    if(matrixBoard[yCoordinate][xCoordinate] != 0){
                    System.out.println("[ERROR] Column must be empty");
                    validInput = false;
                    }
                }
            } catch(NumberFormatException e){
                System.out.println("[ERROR] Input must be an integer");
                validInput = false;
            }
        }
        return new int[]{xCoordinate, yCoordinate};
    }

    @Override
    public String getWinnersName(TicTacToeState state) {
        return state.xWon() ? "X" : "O";
    }
    
}

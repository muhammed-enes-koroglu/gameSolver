package games.connect4;

import java.util.HashSet;
import java.util.Set;

import interfaces.TwoPersonGameState;
import util.ConsoleColors;
import util.Board;
import util.Helper;
import util.PrintBoardGame;
import util.Vector;


/** A working game of Connect4. 
 * To play, go to the Play.runGame() 
 * with the parameter an instance of PlayConnect4.
 * Then run Play.java */
public class Connect4State implements TwoPersonGameState<Connect4State> {

    private final Board board;
    protected final boolean whitesTurn;
    protected final boolean maximizeForWhite;
    public final float calculatedScore;

    public static final int BOARD_WIDTH = 7;
    public static final int BOARD_HEIGHT = 6;
    public static final int GOAL = 4;

    public static final int WHITE = 1;
    public static final int BLACK = -1;
    public static final int EMPTY = 0;

    private static final String BACKGROUND_X = ConsoleColors.WHITE_BACKGROUND;
    private static final String BACKGROUND_O = ConsoleColors.BLACK_BACKGROUND;
    

    @Override
    public Set<Connect4State> children() {

        if(isGameOver()) {
            return new HashSet<>();
        }

        Set<Connect4State> children = new HashSet<>();
        final int piece = this.whitesTurn ? WHITE : BLACK;
        boolean appendSuccesful;

        // For every column, try to append a piece.
        for (int col = 0; col < BOARD_WIDTH; col++) {
            Board chilBoard = board.copy();
            appendSuccesful = appendToColumn(chilBoard, col, piece);
            if(appendSuccesful){
                children.add(new Connect4State(chilBoard, !whitesTurn, maximizeForWhite));
            }
        }

        return children;
    }

    /** Appends the given piece to the top of the given column.
     * Returns true if succesful.
     */
    public static boolean appendToColumn(Board chilBoard, int col, int piece){

        // Top of the column == heighest row number.
        for(int row=0; row<BOARD_HEIGHT; row++){

            // Check if column is full.
            if(chilBoard.get(row, col) == EMPTY){
                chilBoard.set(row, col, piece);
                return true;
            }
        }
        return false;
    }

    @Override
    public float score() {
        return calculatedScore;
    }

    /** Calculates the score at creation to not have to do it a second time for the children(). */
    private float calculateScore() {
        float result = 0;
        int maxPiece = this.maximizeForWhite ? WHITE : BLACK;
        int minPiece = this.maximizeForWhite ? BLACK : WHITE;

        result += checkScoreVertically(board, maxPiece);
        result -= checkScoreVertically(board, minPiece);
        if(result == MAX_SCORE){
            return result;
        }

        result += checkScoreHorizontally(board, maxPiece);
        result -= checkScoreHorizontally(board, minPiece);
        if(result == MAX_SCORE){
            return result;
        }
        
        result += checkScoreAllUpRightDiagonals(board, maxPiece);
        result -= checkScoreAllUpRightDiagonals(board, minPiece);
        if(result == MAX_SCORE){
            return result;
        }
        
        result += checkScoreAllUpLeftDiagonals(board, maxPiece);
        result -= checkScoreAllUpLeftDiagonals(board, minPiece);
        return result;
    }

    @Override
    public boolean isMaxPlayersTurn() {
        return this.maximizeForWhite == this.whitesTurn;
    }
    
    @Override
    public boolean equals(Object o) {
        if(!(o instanceof Connect4State))
            return false;
        Connect4State other = (Connect4State) o;
        if(this.whitesTurn != other.whitesTurn)
            return false;
        return this.board.equals(other.board);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + this.board.hashCode();
        hash = 31 * hash + (whitesTurn ? 1 : 0);
        return hash;
    }

    public String toString(){
        return toString(ConsoleColors.RESET);
    }

    @Override
    public String toString(String backgroundColor){
        String[] pieceRepresentations = new String[]{ 
            "?", 
            BACKGROUND_O + "O" + backgroundColor,
            " ", 
            BACKGROUND_X + "X" + backgroundColor,  
            "?" };
        
        String turnMarker = this.whitesTurn ? BACKGROUND_X + "X" : BACKGROUND_O + "O";
        turnMarker += backgroundColor;
        return PrintBoardGame.toString(this.board, pieceRepresentations, turnMarker, backgroundColor);

    }

    // TODO: Change this to protected. And DELETE this method from TwoPersonGameState.
    public Board getBoard() {
        return board.copy();
    }
    
    public Connect4State(Board board, boolean whitesTurn, boolean maximizeForWhite){
        if(board.nbRows != BOARD_HEIGHT)
            throw new IllegalArgumentException("Board height must be " + BOARD_HEIGHT);
        if(board.nbCols != BOARD_WIDTH)
            throw new IllegalArgumentException("Board width must be " + BOARD_WIDTH);

        this.board = board;
        this.whitesTurn = whitesTurn;
        this.maximizeForWhite = maximizeForWhite;
        this.calculatedScore = calculateScore();
    }

    // HELP Methods

    /** Checks the score in all columns. */
    private float checkScoreVertically(Board board, int piece){
        float result = 0;
        float subResult;
        for(int col=0; col < BOARD_WIDTH; col++){
            subResult = checkScoreColumn(board, col, piece);
            if(subResult == MAX_SCORE)
                return subResult;
            result += subResult;
        }

        return result;
    }

    /** Checks the score in all rows. */
    private float checkScoreHorizontally(Board board, int piece){
        float result = 0;
        float subResult;
        for(int row=0; row < BOARD_HEIGHT; row++){
            subResult = checkScoreRow(board, row, piece);
            if(subResult == MAX_SCORE)
                return subResult;
            result += subResult;
        }

        return result;

    }

    /** Checks the score in all right-facing diagonals. */
    private float checkScoreAllUpRightDiagonals(Board board, int piece){
        float result = 0;
        float subResult;
        Vector startingVector;

        // Check all diagonals that start on the LEFT side of the board. Direction: UP-RIGHT.
        for(int row = BOARD_HEIGHT - GOAL; row >= 0; row--){// This row initiation makes it so that the 
                                                            // initial diagonals that are too short for 
                                                            // the goal don't unnecessarily get explored.
            startingVector = new Vector(row, 0);
            subResult = checkScoreDiagonal(board, startingVector, Vector.UP_RIGHT, piece);
            if(subResult == MAX_SCORE)
                return subResult;
            result += subResult;
        }

        // Check all diagonals that start on the BOTTOM of the board. Direction: UP_RIGHT
        for(int col=0; col < BOARD_WIDTH - GOAL; col++){// This row initiation makes it so that the 
                                                            // initial diagonals that are too short for 
                                                            // the goal don't unnecessarily get explored.
            startingVector = new Vector(0, col);
            subResult = checkScoreDiagonal(board, startingVector, Vector.UP_RIGHT, piece);
            if(subResult == MAX_SCORE)
                return subResult;
            result += subResult;
        }

        return result;
    }

    /** Checks the score in all right-facing diagonals. */
    private float checkScoreAllUpLeftDiagonals(Board board, int piece){
        float result = 0;
        float subResult;
        Vector startingVector;

        // Check all diagonals that start on the RIGHT side of the board. Direction: UP_LEFT
        for(int row = BOARD_HEIGHT - GOAL; row >= 0; row--){// This row initiation makes it so that the 
                                                            // initial diagonals that are too short for 
                                                            // the goal don't unnecessarily get explored.
            startingVector = new Vector(row, BOARD_WIDTH - 1);
            subResult = checkScoreDiagonal(board, startingVector, Vector.UP_LEFT, piece);
            if(subResult == MAX_SCORE)
                return subResult;
            result += subResult;
        }

        // Check all diagonals that start on the BOTTOM of the board. Direction: UP_LEFTS
        for(int col=BOARD_WIDTH-1; col >=0; col--){    // This col initiation makes it so that the 
                                                            // initial diagonals that are too short for 
                                                            // the goal don't unnecessarily get explored.
            startingVector = new Vector(0, col);
            subResult = checkScoreDiagonal(board, startingVector, Vector.UP_LEFT, piece);
            if(subResult == MAX_SCORE)
                return subResult;
            result += subResult;
        }

        return result;
    }

    /** Checks the score in the given column.
     * 
     * @param col to check.
     * @param piece to check for.
     * @return score of the column.
     */
    private float checkScoreColumn(Board board, int col, int piece){

        int chainLength = 0;
        int bestChainLength = 0;
        for(int row=0; row<BOARD_HEIGHT; row++){

            /* Check whether it is still possible to 
            complete the window with the remaining squares.
            Return 0 as the score if not. 
            (Because the score indicates
            how close to the solution this state is. 
            It is impossible to reach a solution 
            if the chain can not be completed.) */
            if(chainLength + (BOARD_HEIGHT - row) < GOAL){
                return streakToScore(bestChainLength);
            }

            // Check whether the chain keeps growing.
            if(board.get(row, col) == piece){
                chainLength++;
                bestChainLength = Math.max(bestChainLength, chainLength);

                // Check if game is over.
                if(chainLength == GOAL){
                    return streakToScore(chainLength);
                }
            } 
            // Back to searching from beginning.
            else{
                chainLength = 0;
                if(board.get(row,col) == EMPTY){
                    bestChainLength += 0.5;
                }
            }
        }

        return streakToScore(bestChainLength);
    }

    /** Checks the score in the given row.
     * 
     * @param row to check.
     * @param piece to check for.
     * @return score of the row.
    */
    private float checkScoreRow(Board board, int row, int piece){

        int bestChainLength = 0;
        int chainLength = 0;
        for(int col=0; col<BOARD_WIDTH; col++){

            /* Check whether it is still possible to 
            complete the window with the remaining squares.
            Return 0 as the score if not. 
            (Because the score indicates
            how close to the solution this state is. 
            It is impossible to reach a solution 
            if the chain can not be completed.) */
            if(chainLength + (BOARD_WIDTH - col) < GOAL){
                return streakToScore(bestChainLength);
            }

            // Check whether the chain keeps growing.
            if(board.get(row, col) == piece){
                chainLength++;
                bestChainLength = Math.max(bestChainLength, chainLength);

                // Check if game is over.
                if(chainLength == GOAL){
                    return streakToScore(chainLength);
                }
            } 
            // Back to searching from beginning.
            else{
                chainLength = 0;
                if(board.get(row,col) == EMPTY){
                    bestChainLength += 0.5;
                }
            }
        }

        return streakToScore(bestChainLength);
    }
       
    /** Checks the score of the right-facing diagonals.
     * 
     * @param startingVector Vector from which the diagonal starts.
     * @param direction of the diagonal. Either Vector.UP_RIGHT or Vector.DOWN_RIGHT.
     * @param piece to check for.
     * @return score of the diagonal.
     */
    private float checkScoreDiagonal(Board board, Vector startingVector, Vector direction, int piece){


        int bestChainLength = 0;
        int chainLength = 0;
        for(Vector position=startingVector; !board.locationIsOutOfBounds(position); position = position.plus(direction)){

            /* No checks made to see whether it is 
            still possible to complete the chain
            with the remaining squares. Because no 
            hard boundary has been defined for 
            the length of the diagonal.*/


            // Check whether the chain keeps growing.
            if(board.get(position) == piece){
                chainLength++;
                bestChainLength = Math.max(bestChainLength, chainLength);

                // Check if game is over.
                if(chainLength == GOAL){
                    return streakToScore(chainLength);
                }
            } 
            // Back to searching from beginning.
            else{
                chainLength = 0;
                if(board.get(position) == EMPTY){
                    bestChainLength += 0.5;
                }
            }
        }

        // 
        return streakToScore(bestChainLength);
    }
           
    /** Converts the length of the chain of same pieces to a score. */
    private static float streakToScore(int chainLength){
        if(chainLength == GOAL)
            return MAX_SCORE;
        if(chainLength == 0)
            return 0;
        return (float) Math.exp(chainLength);
    }

    @Override
    public boolean isGameOver(){
        return (this.calculatedScore == MAX_SCORE) || (this.calculatedScore == MIN_SCORE);
    }

    @Override
    public float getAvgGameLength() {
        return 42;
    }
}

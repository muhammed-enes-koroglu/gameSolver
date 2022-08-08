import java.util.HashSet;
import java.util.Set;

public class Connect4 implements TwoPersonGameState<Connect4> {

    private final Board board;
    private final boolean whitesTurn;
    private final boolean maximizeForWhite;

    public static final int BOARD_WIDTH = 7;
    public static final int BOARD_HEIGHT = 6;
    public static final int GOAL = 4;
    public static final int MAX_SCORE = Integer.MAX_VALUE;

    private static final int HORIZONTAL_LINE_LENGTH = 29;

    private static final int WHITE = 1;
    private static final int BLACK = -1;
    private static final int EMPTY = 0;

    @Override
    public Set<Connect4> children() {

        Set<Connect4> children = new HashSet<>();
        int piece = whitesTurn ? WHITE : BLACK;

        for (int col = 0; col < BOARD_WIDTH; col++) {
            Board chilBoard = board.copy();
            appendToColumn(chilBoard, col, piece);
            children.add(new Connect4(chilBoard, !whitesTurn, maximizeForWhite));
        }

        return children;
    }

    /** Appends the given piece to the top of the given column. */
    private void appendToColumn(Board chilBoard, int col, int piece){

        // Top of the column == heighest row number.
        for(int row=0; row<BOARD_HEIGHT; row++){
            if(chilBoard.get(row, col) == EMPTY){
                chilBoard.set(row, col, piece);
                return;
            }
        }
        throw new IllegalArgumentException("Column is full!");
    }

    @Override
    public float score() {
        float score = 0;
        int piece = whitesTurn ? WHITE : BLACK;

        score = plusWithOverflow(score, checkScoreAllColumns(board, piece));
        if(score == MAX_SCORE){
            return score;
        }

        score = plusWithOverflow(score, checkScoreAllRows(board, piece));
        if(score == MAX_SCORE){
            return score;
        }
        
        score = plusWithOverflow(score, checkScoreAllUpRightDiagonals(board, piece));
        if(score == MAX_SCORE){
            return score;
        }
        
        score = plusWithOverflow(score, checkScoreAllUpLeftDiagonals(board, piece));
        return score;
    }

    @Override
    public boolean isMaxPlayersTurn() {
        return !(this.maximizeForWhite ^ this.whitesTurn);
    }
    
    @Override
    public boolean equals(Object o) {
        if(!(o instanceof Connect4))
            return false;
        Connect4 other = (Connect4) o;
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

    @Override
    public String toString(){
        StringBuilder result = new StringBuilder();
        result.append("\n");

        // add board elements
        result.append(getHorizontalLine());
        for(int rowNb=BOARD_HEIGHT-1; rowNb>=0; rowNb--){
            int[] row = this.board.getRow(rowNb);
            result.append(rowToString(row));
            result.append("\n");
        }
        // add column numbers
        result.append(getHorizontalLine());
        
        return result.toString();
    }

    /** Returns a horizontal line of about the same size as a normal row. */
    private static String getHorizontalLine(){
        StringBuilder result = new StringBuilder();
        for(int i=0; i < HORIZONTAL_LINE_LENGTH; i++)
            result.append("_");

        result.append("\n");
        return result.toString();
    }

    /** Converts the given array of numbers to string. */
    private String rowToString(int[] row){
        StringBuilder result = new StringBuilder();

        result.append("| ");

        // Add the pieces, seperated by " | ".
        for(int piece: row){
            // add piece
            result.append(pieceToString(piece));
            result.append(" | ");
        }
        return result.toString();
    }


    /** Converts the piece number to its approppriate representation.
     * 
     * @param piece
     * @return String representation of the piece.
    */
    private static String pieceToString(int piece){

        switch(piece){
            case WHITE:
                return "X";
            case BLACK:
                return "O";
            case EMPTY:
                return " ";
            default:
                return "?";
        }
    }

    
    public Connect4(Board board, boolean whitesTurn, boolean maximizeForWhite){
        if(board.nbRows != BOARD_HEIGHT)
            throw new IllegalArgumentException("Board height must be " + BOARD_HEIGHT);
        if(board.nbCols != BOARD_WIDTH)
            throw new IllegalArgumentException("Board width must be " + BOARD_WIDTH);
            
        this.board = board;
        this.whitesTurn = whitesTurn;
        this.maximizeForWhite = maximizeForWhite;
    }

    // HELP Methods

    /** Checks the score in all columns. */
    private float checkScoreAllColumns(Board board, int piece){
        float result = 0;
        float subResult;
        for(int col=0; col < BOARD_WIDTH; col++){
            subResult = checkScoreColumn(board, col, piece);
            if(subResult == MAX_SCORE)
                return subResult;
            result = plusWithOverflow(result, subResult);
        }

        return result;
    }

    /** Checks the score in all rows. */
    private float checkScoreAllRows(Board board, int piece){
        float result = 0;
        float subResult;
        for(int row=0; row < BOARD_HEIGHT; row++){
            subResult = checkScoreRow(board, row, piece);
            if(subResult == MAX_SCORE)
                return subResult;
            result = plusWithOverflow(result, subResult);
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
            result = plusWithOverflow(result, subResult);
        }

        // Check all diagonals that start on the BOTTOM of the board. Direction: UP_RIGHT
        for(int col=0; col < BOARD_WIDTH - GOAL; col++){// This row initiation makes it so that the 
                                                            // initial diagonals that are too short for 
                                                            // the goal don't unnecessarily get explored.
            startingVector = new Vector(0, col);
            subResult = checkScoreDiagonal(board, startingVector, Vector.UP_RIGHT, piece);
            if(subResult == MAX_SCORE)
                return subResult;
            result = plusWithOverflow(result, subResult);
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
            result = plusWithOverflow(result, subResult);
        }

        // Check all diagonals that start on the BOTTOM of the board. Direction: UP_LEFTS
        for(int col=BOARD_WIDTH-1; col >=0; col--){    // This col initiation makes it so that the 
                                                            // initial diagonals that are too short for 
                                                            // the goal don't unnecessarily get explored.
            startingVector = new Vector(0, col);
            subResult = checkScoreDiagonal(board, startingVector, Vector.UP_LEFT, piece);
            if(subResult == MAX_SCORE)
                return subResult;
            result = plusWithOverflow(result, subResult);
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
        int maxChainLength = 0;
        for(int row=0; row<BOARD_HEIGHT; row++){

            /* Check whether it is still possible to 
            complete the window with the remaining squares.
            Return 0 as the score if not. 
            (Because the score indicates
            how close to the solution this state is. 
            It is impossible to reach a solution 
            if the chain can not be completed.) */
            if(chainLength + (BOARD_HEIGHT - row) < GOAL){
                return 0;
            }

            // Check whether the chain keeps growing.
            if(board.get(row, col) == piece){
                chainLength++;
                maxChainLength = Math.max(maxChainLength, chainLength);

                // Check if game is over.
                if(chainLength == GOAL){
                    return streakToScore(chainLength);
                }
            } 
            // Back to searching from beginning.
            else{
                chainLength = 0;
            }
        }

        // Normally next line should never be executed.
        throw new Error("Why is this line executed??");
        // return streakToScore(bestChainLength);
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
                return 0;
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
            }
        }

        // Normally next line should never be executed.
        throw new Error("Why is this line executed??");
        // return streakToScore(bestChainLength);
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
            }
        }

        // 
        return streakToScore(bestChainLength);
    }
           
    /** Converts the length of the chain of same pieces to a score. */
    private static float streakToScore(int chainLength){
        if(chainLength == GOAL)
            return Integer.MAX_VALUE;
        return (float) Math.exp(chainLength);
    }

    /** Tries to detect overflow.
     * @return MAX_SCORE-1 if overflow occurs. */
    public static float plusWithOverflow(float a, float b){
        if(a == MAX_SCORE || b == MAX_SCORE)
            return MAX_SCORE-1; // So the only way to reach MAX_SCORE is by winning the game.
        if(a > 0 && b > 0 && a + b < 0)
            return MAX_SCORE-1; // So the only way to reach MAX_SCORE is by winning the game.
        return a + b;
    }
}

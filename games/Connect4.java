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
        // TODO: Implement this method.
        return 0;
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

    // Checks the score in all columns.
    private float checkScoreAllColumns(Board board, int piece){
        float result = 0;
        float subResult;
        for(int col=0; col < BOARD_WIDTH; col++){
            subResult = checkScoreColumn(board, col, piece);
            if(subResult == MAX_SCORE)
                return subResult;
            result = plusWithCeiling(result, subResult);
        }

        return result;
    }

    // Checks the score in all rows.
    private float checkScoreAllRows(Board board, int piece){
        float result = 0;
        float subResult;
        for(int row=0; row < BOARD_HEIGHT; row++){
            subResult = checkScoreRow(board, row, piece);
            if(subResult == MAX_SCORE)
                return subResult;
            result = plusWithCeiling(result, subResult);
        }

        return result;

    }

    /** Checks the score in the given column. */
    private float checkScoreColumn(Board board, int col, int piece){

        int chainLength = 0;
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

            // Check whether to keep enlarging the window.
            if(board.get(row, col) == piece){
                chainLength++;

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
        // return streakToScore(chainLength);
    }

    /** Checks the score in the given row.*/
    private float checkScoreRow(Board board, int row, int piece){

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

            // Check whether to keep enlarging the window.
            if(board.get(row, col) == piece){
                chainLength++;

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
        // return streakToScore(chainLength);
    }
       
    /** Checks the score of the right-facing diagonals */
    private float checkScoreRightFacingDiagonals(Board board, int row, int piece){
        // TODO: Implement this method.
        return 0;
    }
       
    /** Checks the score of the right-facing diagonals */
    private float checkScoreLeftFacingDiagonals(Board board, int row, int piece){
        // TODO: Implement this method.
        return 0;
    }
    
    // Converts the length of the chain of same pieces to a score.
    private static float streakToScore(int chainLength){
        if(chainLength == GOAL)
            return Integer.MAX_VALUE;
        return (float) Math.exp(chainLength);
    }

    // Tries to detect overflow.
    // Return the maximum allowed value if overflow occurs.
    public static float plusWithCeiling(float result, float subResult){
        if(result == MAX_SCORE || subResult == MAX_SCORE)
            return MAX_SCORE;
        if(result > 0 && subResult > 0 && result + subResult < 0)
            return MAX_SCORE;
        return result + subResult;
    }
}

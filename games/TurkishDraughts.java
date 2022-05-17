import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


/* TODOs and PROBLEMS
1- 2 equal states produce different hashcodes due to their arrays returning different hashcodes 
because they are not identical. Check how we did it in MangalaState.
2- Promotion
3- Private Tests
4- addChildrenForKingAt()
5- score()
*/
public class TurkishDraughts implements TwoPersonGameState<TurkishDraughts>{
    public static final int BOARD_SIZE = 8;
    public static final int W_MAN = 1;
    public static final int B_MAN = -1;
    public static final int W_KING = 2;
    public static final int B_KING = -2;
    public static final int EMPTY_SQUARE = 0;
    public static final int TOP_ROW = BOARD_SIZE-1;
    public static final int BOTTOM_ROW = 0;
    public static final int RIGHTMOST_COL = BOARD_SIZE-1;
    public static final int LEFTMOST_COL = 0;
    private static final int[] UP = new int[]{1,0};
    private static final int[] DOWN = new int[]{-1,0};
    private static final int[] RIGHT = new int[]{0,1};
    private static final int[] LEFT = new int[]{0,-1};

    private int[][] board;
    private boolean whitesTurn;
    private boolean maximizeForWhite;
    private int nbWhitePieces;
    private int nbBlackPieces;
    private boolean hasTakenLastTurn;

    public static int[] up(){return UP.clone();}
    public static int[] down(){return DOWN.clone();}
    public static int[] right(){return RIGHT.clone();}
    public static int[] left(){return LEFT.clone();}
    public static int[][] getDirectionsList() { return new int[][]{up(), down(), right(), left()};}


    @Override
    public Set<TurkishDraughts> children() {
        HashSet<TurkishDraughts> childrenSet = new HashSet<>();
        boolean takePossible = false;

        for(int currentRow=0; currentRow<BOARD_SIZE; currentRow++){
            for(int currentCol=0; currentCol<BOARD_SIZE; currentCol++){
                int piece = this.board[currentRow][currentCol];
                if(piece == EMPTY_SQUARE || !piecesTurn(piece))
                    continue; // Skip empty squares and pieces whose turn it's not.
                
                int[][] childBoard = Helper.deepCopy(this.board);
                if(isKing(piece))
                    takePossible = addChildrenForKingAt(childrenSet, childBoard, currentRow, currentCol, takePossible) || takePossible;
                else
                    takePossible = addChildrenForManAt(childrenSet, childBoard, currentRow, currentCol, takePossible) || takePossible;
                }
        }

        if(takePossible)
            removeNonTakers(childrenSet, takePossible);

        return childrenSet;
    }

    private void removeNonTakers(HashSet<TurkishDraughts> childrenSet, boolean takePossible){
        for(TurkishDraughts child : childrenSet)
            if(!child.hasTakenLastTurn)
                childrenSet.remove(child);
    }

    private boolean addChildrenForKingAt(HashSet<TurkishDraughts> childrenSet, int[][] board, int currentRow, int currentCol, boolean takePossible) {
        
        // // Should not make a normal move if a take is possible.
        // int nbEnemyPieces = this.whitesTurn ? this.nbBlackPieces : this.nbWhitePieces;
        // if(manageTakesForKing(childrenSet, board, currentRow, currentCol, nbEnemyPieces)){
        //     return true;
        // }
        
        // // If a take is possible, but not for this piece -> this piece does not move. Saves us a TON of calculations.
        // if(takePossible){
        //     return false;
        // }

        // // No takes possible. Move simply.
        // for(int[] direction: getDirectionsList()){
        //     int rowDirection = direction[0];
        //     int colDirection = direction[1];
            
        //     if(!locationIsOutOfBounds(currentRow + rowDirection, currentCol + colDirection) &&
        //     board[currentRow + rowDirection][currentCol + colDirection] == EMPTY_SQUARE){
        //         int[][] childBoard = Helper.deepCopy(board);
        //         moveManInDirection(childBoard, currentRow, currentCol, rowDirection, colDirection);
        //         childrenSet.add(new TurkishDraughts(childBoard, !this.whitesTurn, this.maximizeForWhite, this.nbWhitePieces, this.nbBlackPieces, false));
        //     }
        // }
        return false;
    }    
    
    private boolean manageTakesForKing(HashSet<TurkishDraughts> childrenSet, int[][] board, int currentRow,
            int currentCol, int nbEnemyPieces) {
        throw new UnsupportedOperationException("Not yet implemented.");

        // boolean takePossible = false;
        // if(nbEnemyPieces == 0)
        //     return takePossible;

        // for(int[] direction: getDirectionsList()){
        //     int rowDirection = direction[0];
        //     int colDirection = direction[1];
            
        //     // Take if can take and try taking from there recursively.
        //     int[][] childBoard = Helper.deepCopy(board);
        //     takePossible = kingTryTakeInDirection(childBoard, currentRow, currentCol, rowDirection, colDirection, nbEnemyPieces);

        //     // Check if should enter survival mode.
        //     if(nbEnemyPieces <= 3)
        //         enterSurvivalMode(childBoard, !this.whitesTurn);

        //     // Update nbPieces and Add the new child.
        //     int newNbWhitePieces = this.whitesTurn ? this.nbWhitePieces : nbEnemyPieces;
        //     int newNbBlackPieces = !this.whitesTurn ? this.nbBlackPieces : nbEnemyPieces;
        //     childrenSet.add(new TurkishDraughts(childBoard, !this.whitesTurn, this.maximizeForWhite, newNbWhitePieces, newNbBlackPieces, true));

        //     // Note: This implementation enters survival mode mid-taking for performance reasons (see enterSurvivalMode())
        //     // Move the following line to before checking survival mode to enter it at the end of player's turn.
        //     manageTakesForKing(childrenSet, childBoard, currentRow + 2*rowDirection, currentCol + 2*colDirection, nbEnemyPieces);
        
        // }
        // return takePossible;
    }

    /** Adds all 
     * @return true if at least one take was possible.*/
    private boolean addChildrenForManAt(HashSet<TurkishDraughts> childrenSet, int[][] board, int currentRow, 
            int currentCol, boolean takePossible){
        
        // Should not make a normal move if a take is possible.
        int nbEnemyPieces = this.whitesTurn ? this.nbBlackPieces : this.nbWhitePieces;
        if(manageTakesForMan(childrenSet, board, currentRow, currentCol, nbEnemyPieces)){
            return true;
        }
        
        // If a take is possible, but not for this piece -> this piece does not move. Saves us a TON of calculations.
        if(takePossible){
            return false;
        }

        // No takes possible. Move simply.
        for(int[] direction: getDirectionsList()){
            int rowDirection = direction[0];
            int colDirection = direction[1];
            
            if(!locationIsOutOfBounds(currentRow + rowDirection, currentCol + colDirection) &&
            board[currentRow + rowDirection][currentCol + colDirection] == EMPTY_SQUARE){
                int[][] childBoard = Helper.deepCopy(board);
                moveManInDirection(childBoard, currentRow, currentCol, rowDirection, colDirection);
                childrenSet.add(new TurkishDraughts(childBoard, !this.whitesTurn, this.maximizeForWhite, this.nbWhitePieces, this.nbBlackPieces, false));
            }
        }
        return false;
    }

    /** Moves man in the given direction by one square.
     * Promotes if man reaches last row.
     * Assumes direction to be not blocked.
     */
    private static void moveManInDirection(int[][] board, int currentRow, int currentCol, int rowDirection,
            int colDirection){
        
        int man = board[currentRow][currentCol];
        board[currentRow][currentCol] = EMPTY_SQUARE;

        // Check for promotion.
        if(currentRow + rowDirection == getLastRowForPiece(man))
            board[currentRow + rowDirection][currentCol + colDirection] = getKingPieceForPiece(man);
        else
            board[currentRow + rowDirection][currentCol + colDirection] = man;
    }

    /** Returns true if at least one take was possible. */
    private boolean manageTakesForMan(HashSet<TurkishDraughts> childrenSet, int[][] board, int currentRow, 
            int currentCol, int nbEnemyPieces){
        boolean takePossible = false;
        if(nbEnemyPieces == 0)
            return takePossible;

        for(int[] direction: getDirectionsList()){
            int rowDirection = direction[0];
            int colDirection = direction[1];
            
            // Take if can take and try taking from there recursively.
            if(manCanTakeInDirection(board, currentRow, currentCol, rowDirection, colDirection)){
                takePossible = true;
                int[][] childBoard = Helper.deepCopy(board);
                letManTakeInDirection(childBoard, currentRow, currentCol, rowDirection, colDirection);
                nbEnemyPieces--;

                // Check if should enter survival mode.
                if(nbEnemyPieces <= 3)
                   enterSurvivalMode(childBoard, !this.whitesTurn);

                // Update nbPieces and Add the new child.
                int newNbWhitePieces = this.whitesTurn ? this.nbWhitePieces : nbEnemyPieces;
                int newNbBlackPieces = !this.whitesTurn ? this.nbBlackPieces : nbEnemyPieces;
                childrenSet.add(new TurkishDraughts(childBoard, !this.whitesTurn, this.maximizeForWhite, newNbWhitePieces, newNbBlackPieces, true));
            
                // Note: This implementation enters survival mode mid-taking for performance reasons (see enterSurvivalMode())
                // Move the following line to before checking survival mode to enter it at the end of player's turn.
                manageTakesForMan(childrenSet, childBoard, currentRow + 2*rowDirection, currentCol + 2*colDirection, nbEnemyPieces);
            }
        }
        return takePossible;
    }

    /** Returns also false if nextNext is out of bounds. */
    private static boolean manCanTakeInDirection(int[][] board,int currentRow, int currentCol, int rowDirection, 
            int colDirection){
        int nextRow = currentRow + rowDirection;
        int nextCol = currentCol + colDirection;


        // Can not take if next square doesn't exists.
        if(locationIsOutOfBounds(currentRow + rowDirection, currentCol + colDirection))
            return false;
        
        // Can only take if next is enemy == (next not empty) && (next not friendly)
        if(board[nextRow][nextCol] == EMPTY_SQUARE || areSameColor(board[currentRow][currentCol], board[nextRow][nextCol])){
            return false;
        }
        
        // Next is enemy piece. Check nextNext piece.
        int nextNextRow = nextRow + rowDirection;
        int nextNextCol = nextCol + colDirection;
        
        // Can not take if nextNext is out of bounds.
        if(locationIsOutOfBounds(nextNextRow, nextNextCol)){
            return false;
        }
        return board[nextNextRow][nextNextCol] == EMPTY_SQUARE; 
    }
    
    private static void letManTakeInDirection(int[][] board, int currentRow, int currentCol, int rowDirection, 
            int colDirection){

        int piece = board[currentRow][currentCol];
        board[currentRow][currentCol] = EMPTY_SQUARE;
        board[currentRow + rowDirection][currentCol + colDirection] = EMPTY_SQUARE;
        board[currentRow + 2 * rowDirection][currentCol + 2 * colDirection] = piece;
        
    }

    /** Converts all pieces of the right color to king pieces. 
     * Throws exception if number of converted pieces is more than 3. 
     */
    private static void enterSurvivalMode(int[][] childBoard, boolean doForWhite) {
        int count = 0;
        // Turn all pieces of the player to kings.
        for(int row=0; row<BOARD_SIZE; row++)
            for(int col=0; col<BOARD_SIZE; col++)
                if(isWhite(childBoard[row][col]) == doForWhite){
                    childBoard[row][col] = doForWhite ? W_KING : B_KING;
                    count++;
                }
        
        if(count > 3)
            throw new IllegalStateException("Can't enter survival mode if more than 3 pieces are present!");
    }
    
    @Override
    public float score() {
        // TODO: return nb pieces left on board of this player.
        throw new UnsupportedOperationException("Not yet implemented.");
    }

    @Override
    public boolean isMaxPlayersTurn() {
        return !(maximizeForWhite ^ whitesTurn);
    }

    @Override
    public boolean equals(Object o){
        if(!(o instanceof TurkishDraughts))
            return false;
        TurkishDraughts other = (TurkishDraughts) o;
        if(this.whitesTurn != other.whitesTurn)
            return false;
        return Arrays.deepEquals(this.board, other.board);
    }

    @Override
    public int hashCode(){
        int hash = 7;
        for(int[] row: board)
            hash = 31 * hash + (row == null ? 0 : Arrays.hashCode(row));
        hash = 31 * hash + (whitesTurn ? 1 : 0);
        return hash;
    }

    @Override
    public String toString(){
        StringBuilder str = new StringBuilder();

        // add board elements
        str.append(getHorizontalLine());
        for(int rowNb=BOARD_SIZE-1; rowNb>=0; rowNb--){
            int[] row = this.board[rowNb];
            str.append(rowToString(row, rowNb));
        }
        // add column numbers
        str.append(getHorizontalLine());
        str.append(rowToString(new int[]{0,1,2,3,4,5,6,7}, -1));
        
        return str.toString();
    }

    /** Returns a horizontal line of about the same size as a normal row. */
    private String getHorizontalLine(){
        StringBuilder result = new StringBuilder();
        for(int i=0; i < 34; i++)
            result.append("_");
        result.append("\n");
        return result.toString();
    }

    /** Converts the given array of numbers to string. */
    private String rowToString(int[] row, int rowNb){
        if(rowNb == -1)
            return lastRowToString(row) + "\n";
        else
            return normalRowToString(row, rowNb) + "\n";    
    }

    /** Converts the row with rowNb == -1 to string. */
    private String lastRowToString(int[] row){
        StringBuilder result = new StringBuilder();

        result.append(" | ");
        // Add elements, seperated by " | ".
        for(int element : row){
            result.append(element + " | ");
        }

         // Add whose turn it is
        String turnOfColor = this.whitesTurn ? "WHITE " : "BLACK ";
        result.append(turnOfColor + this.hashCode());

        return result.toString();
    }

    /** Converts a normal row to string. */
    private static String normalRowToString(int[] row, int rowNb){
        StringBuilder result = new StringBuilder();

        result.append(rowNb + "| ");

        // Add the pieces, seperated by " | ".
        for(int piece: row){
            // add row elements
            result.append(pieceToString(piece) + " | ");
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
            case W_KING:
                return "W";
            case W_MAN:
                return "w";
            case B_KING:
                return "B";
            case B_MAN:
                return "b";
            case EMPTY_SQUARE:
                return " ";
            default:
                return "?";
        }
    }
    
    /**
        @param board 2D array that contains values chosen from {-2, -1, 0, 1, 2}.
            0 : empty square
            1 : white man
            -1: black man
            2 : white king
            -2: black king
        @param whitesTurn whether it is the white player's turn.
        @param nbWhitePieces number of remaining white pieces on board. Max: 16, Min: 0. 
            If equal to -1 new exhaustive check is done.
        @param nbBlackPieces number of remaining black pieces on board. Max: 16, Min: 0. 
            If equal to -1 new exhaustive check is done.
    */
    public TurkishDraughts(int[][] board, boolean whitesTurn, boolean maximizeForWhite, int nbWhitePieces, 
            int nbBlackPieces, boolean hasTakenLastTurn){
        if(board.length != BOARD_SIZE)
            throw new IllegalArgumentException("`board` must have " + BOARD_SIZE + " rows.");
        if(board[0].length != BOARD_SIZE)
            throw new IllegalArgumentException("`board` must have " + BOARD_SIZE + " columns.");
        if(!Arrays.stream(board).allMatch(row -> 
        Arrays.stream(row).allMatch(i -> (i >= -2) && (i <= 2))))
            throw new IllegalArgumentException("pieces can only be man, king or empty.");

        // // Use these ONLY while testing. VERY calculation intensive!!
        // boolean checkForWhite = true;
        // if(nbPiecesLeftOnBoard(board, checkForWhite) == 0 && !whitesTurn)
        //     throw new IllegalArgumentException("It should be white's turn if white has lost.");
        // if(nbPiecesLeftOnBoard(board, !checkForWhite) == 0 && whitesTurn)
        //     throw new IllegalArgumentException("It should be black's turn if black has lost.");

        // if(playerLifeThreatened(board, checkForWhite) && !Arrays.stream(board).allMatch(row -> 
        // Arrays.stream(row).allMatch(i -> i == 2)))
        //     throw new IllegalArgumentException("white should be in survival mode.");
        // if(playerLifeThreatened(board, !checkForWhite) && !Arrays.stream(board).allMatch(row -> 
        // Arrays.stream(row).allMatch(i -> i == -2)))
        //     throw new IllegalArgumentException("black should be in survival mode.");
            
        this.board = board;
        this.whitesTurn = whitesTurn;
        this.maximizeForWhite = maximizeForWhite;
        this.hasTakenLastTurn = hasTakenLastTurn;

        if(nbWhitePieces == -1 || nbBlackPieces == -1){
            this.nbWhitePieces = nbPiecesLeftOnBoard(board, true);
            this.nbBlackPieces = nbPiecesLeftOnBoard(board, false);
        } else{
            this.nbWhitePieces = nbWhitePieces;
            this.nbBlackPieces = nbBlackPieces;
        }
    }

    //Help methods
    /** Returns a checkers board with 4 White pieces and 6 Black pieces on it. */
    public static int[][] getTestBoard(){
        int[][] board = new int[BOARD_SIZE][BOARD_SIZE];

        board[1][0] = W_MAN;
        board[3][3] = W_MAN;
        board[5][6] = W_MAN;
        board[5][4] = W_KING;

        board[2][0] = B_MAN;
        board[4][0] = B_KING;
        board[5][1] = B_MAN;

        board[1][1] = B_MAN;
        board[1][3] = B_MAN;
        board[2][4] = B_KING;
        
        return board.clone();
    }

    private static int nbPiecesLeftOnBoard(int[][] board, boolean checkForWhite){
        int nbPieces = 0;

        for(int[] row: board)
            for(int piece: row)
                if(pieceBelongsToPlayer(piece, checkForWhite))
                    nbPieces++;
        return nbPieces;
    }

    private static boolean pieceBelongsToPlayer(int piece, boolean checkForWhite){
        int playerSign = checkForWhite ? 1 : -1;
        return playerSign * piece > 0;
    }

    private static int getLastRowForPiece(int piece){
        return isWhite(piece) ? TOP_ROW : BOTTOM_ROW;
    }

    private static int getKingPieceForPiece(int piece){
        return isWhite(piece) ? W_KING : B_KING;
    }

    private static boolean isWhite(int piece){
        return piece == W_MAN || piece == W_KING;
    }
    
    private static boolean isBlack(int piece){
        return piece == B_MAN || piece == B_KING;
    }
    
    private static boolean isKing(int piece){
        return piece == W_KING || piece == B_KING;
    }

    private boolean piecesTurn(int piece){
        if(piece == EMPTY_SQUARE)
            throw new IllegalArgumentException("Empty square doesn't have a turn.");

        return (isWhite(piece) && this.whitesTurn) || (!isWhite(piece) && !this.whitesTurn);
    }

    private static boolean areSameColor(int piece1, int piece2){
        return piece1 * piece2 > 0;
    }

    private static boolean locationIsOutOfBounds(int nextRow, int nextCol) {
        return TOP_ROW < nextRow || nextRow < BOTTOM_ROW ||
            RIGHTMOST_COL < nextCol || nextCol < LEFTMOST_COL;
    }

    // TESTS
    public void testPrivateMethods(){
        System.out.println("  testPrivateMethods..");

        testManCanTakeInDirection();
        testManTakesInDirection();
        testManageTakes();

        System.out.println("  testPrivateMethods successful");
    }

    private static void testManCanTakeInDirection(){
        System.out.println("    testManCanTakeInDirection..");
        int[][] board = getTestBoard();

        assert manCanTakeInDirection(board, 1, 0, 1,  0);
        assert manCanTakeInDirection(board, 1, 0, -1, 0);
        assert manCanTakeInDirection(board, 1, 0, 0,  1);
        assert manCanTakeInDirection(board, 1, 0, 0, -1);
        System.out.println("    testManCanTakeInDirection successful");
    }

    private static void testManTakesInDirection(){
        System.out.println("    testManTakesInDirection..");
        int[][] board = getTestBoard();

        int[][] childBoard = Helper.deepCopy(board);
        letManTakeInDirection(childBoard, 1, 0, 1,  0);
        assert childBoard[1][0] == EMPTY_SQUARE;
        assert childBoard[2][0] == EMPTY_SQUARE;
        assert childBoard[3][0] == W_MAN;
        
        childBoard = Helper.deepCopy(board);
        letManTakeInDirection(childBoard, 1, 0, 0, 1);
        assert childBoard[1][0] == EMPTY_SQUARE;
        assert childBoard[1][1] == EMPTY_SQUARE;
        assert childBoard[1][2] == W_MAN;
        
        System.out.println("    testManTakesInDirection successful");
    }

    private void testManageTakes(){
        boolean whitesTurn = true;
        boolean maximizeForWhite= true;
        HashSet<TurkishDraughts> childrenSet = new HashSet<>();
        int[][] board = getTestBoard();

        manageTakesForMan(childrenSet, board, 1, 0, 6);

        for(TurkishDraughts child : childrenSet)
            System.out.println(child);
    }

}
package games.turkish_draughts;

import java.util.HashSet;
import java.util.Set;

import util.Board;
import util.ConsoleColors;
import util.TwoPersonGameState;
import util.Vector;


/* TODO's and PROBLEMS
- Predicts too many children: Somehow jumps over teammates.
- addChildrenForKingAt()
- Game should end if king vs man: 4 pieces -> king takes 3, the 1 staying is man -> doesn't get promoted, game ends immediately.
- Private Tests
- score()
*/
public class TurkishDraughts implements TwoPersonGameState<TurkishDraughts>{
    // Constants.
    public static final int EMPTY_SQUARE = 0;
    public static final int W_MAN = 1;
    public static final int B_MAN = -1;
    public static final int W_KING = 2;
    public static final int B_KING = -2;

    public static final int BOARD_SIZE = 8;
    public static final int TOP_ROW = BOARD_SIZE-1;
    public static final int BOTTOM_ROW = 0;
    public static final int RIGHTMOST_COL = BOARD_SIZE-1;
    public static final int LEFTMOST_COL = 0;

    public static final Vector UP = new Vector(1,0);
    public static final Vector DOWN = new Vector(-1,0);
    public static final Vector RIGHT = new Vector(0,1);
    public static final Vector LEFT = new Vector(0, -1);

    private final Board board;
    private final boolean whitesTurn;
    private final boolean maximizeForWhite;
    private final int nbWhitePieces;
    private final int nbBlackPieces;
    private final boolean hasTakenLastTurn;

    private static String backgroundW = ConsoleColors.WHITE_BACKGROUND;
    private static String backgroundB = ConsoleColors.BLACK_BACKGROUND;

    @Override
    public Set<TurkishDraughts> children() {
        HashSet<TurkishDraughts> childrenSet = new HashSet<>();
        boolean takePossible = false;

        for(int row=0; row<BOARD_SIZE; row++){
            for(int col=0; col<BOARD_SIZE; col++){
                Vector position = new Vector(row, col);

                int piece = this.board.get(position);
                if(piece == EMPTY_SQUARE || !piecesTurn(piece))
                    continue; // Skip empty squares and pieces whose turn it's not.
                
                Board childBoard = this.board.copy();
                if(isKing(piece))
                    takePossible = addChildrenForKingAt(childrenSet, childBoard, position, takePossible, false) || takePossible;
                else
                    takePossible = addChildrenForManAt(childrenSet, childBoard, position, takePossible) || takePossible;
                }
        }

        if(takePossible)
            removeNonTakers(childrenSet);

        return childrenSet;
    }

    private void removeNonTakers(HashSet<TurkishDraughts> childrenSet){
        childrenSet.removeIf((TurkishDraughts child) -> !child.hasTakenLastTurn);
    }

    private boolean addChildrenForKingAt(HashSet<TurkishDraughts> childrenSet, Board board, Vector position, boolean takePossible, boolean hasMadeATake) {
        // GENERAL IDEA IS KINDA RIGHT. CURRENTLY FAR FROM WORKING.

        int kingPiece = board.get(position);
        // Needed for simpler moving mechanics. Watch Out for unexpected behaviour though!!
        board.set(position, EMPTY_SQUARE);

        // Looks in all directions.
        // Adds children in every empty square in that direction until encountering a blockage.
        // If blockage is a takeable enemy piece, take and repeat action from there.
        for(Vector direction: getAllDirectionsList())
            takePossible = addChildrenAlongDirection(childrenSet, board, position, kingPiece, direction, takePossible, false, hasMadeATake) || takePossible;

        return takePossible;
    }

    /** Adds non-taker children of a king in the given direction until hitting a blockage. 
     * @param hasMadeATake */
    private boolean addChildrenAlongDirection(HashSet<TurkishDraughts> childrenSet, Board board, Vector startingPosition, int kingPiece,
            Vector direction, boolean takePossible, boolean justTook, boolean hasMadeATake) {
        boolean blocked = false;
        for(int i=1; !blocked; i++){
        
            Vector currentPosition = startingPosition.plus(direction.times(i));

            if(!takePossible || justTook){
                Board childBoard = addChildIfSquareIsMovable(childrenSet, board, kingPiece, blocked, currentPosition, hasMadeATake);
                blocked = (childBoard == null);
                if(!blocked && justTook){
                    addChildrenForKingAt(childrenSet, childBoard.copy(), currentPosition, takePossible, hasMadeATake);
                }
            }         
            takePossible = addChildrenIfSquareTakeable(childrenSet, board, kingPiece, currentPosition, direction, takePossible) || takePossible;
            if(locationIsOutOfBounds(currentPosition))
                blocked = true;
        }
        return takePossible;
    }

    private boolean addChildrenIfSquareTakeable(HashSet<TurkishDraughts> childrenSet, Board board, int kingPiece,
    Vector positionToMaybeTake, Vector direction, boolean takePossible) {
        if(isTakeableSquare(board, positionToMaybeTake, direction, kingPiece)){
            takePossible = true;

            Board childBoard = board.copy();
            childBoard.set(positionToMaybeTake, EMPTY_SQUARE);
            addChildrenAlongDirection(childrenSet, childBoard, positionToMaybeTake, kingPiece, direction, takePossible, true, true);
        }
        return takePossible;
    }

    // Returns the childBoard. Is equal to null if position blocked.
    private Board addChildIfSquareIsMovable(HashSet<TurkishDraughts> childrenSet, Board board, int kingPiece, boolean blocked,
            Vector currentPosition, boolean hasMadeATake) {
        Board childBoard = null;
        if(isMovableSquare(board, currentPosition)){
            // Set the empty space to the king.
            childBoard = board.copy();
            childBoard.set(currentPosition, kingPiece);
    
            // Add the child.
            TurkishDraughts child = new TurkishDraughts(childBoard, !this.whitesTurn, this.maximizeForWhite, nbWhitePieces, nbBlackPieces, hasMadeATake);
            System.out.println(child);
            childrenSet.add(child);        
        }
        return childBoard;
    }
  
    /** Adds all children of the given man.
     * @return true if at least one take was possible.*/
    private boolean addChildrenForManAt(HashSet<TurkishDraughts> childrenSet, Board board, Vector startingPosition, boolean takePossible){
        
        // Should not make a normal move if a take is possible.
        int nbEnemyPieces = this.whitesTurn ? this.nbBlackPieces : this.nbWhitePieces;
        if(manageTakesForMan(childrenSet, board, startingPosition, nbEnemyPieces)){
            return true;
        }
        
        // If a take is possible, but not for this piece -> this piece does not move. Saves us a TON of calculations.
        if(takePossible){
            return false;
        }

        // No takes possible. Move simply.
        for(Vector direction: getDirectionsNotBackwardForMan(board.get(startingPosition))){
            
            Vector positionToCheck = startingPosition.plus(direction);
            if(!locationIsOutOfBounds(positionToCheck) &&
            board.get(positionToCheck) == EMPTY_SQUARE){
                Board childBoard = board.copy();
                movePieceOneSquareInDirection(childBoard, startingPosition, direction);
                childrenSet.add(new TurkishDraughts(childBoard, !this.whitesTurn, this.maximizeForWhite, this.nbWhitePieces, this.nbBlackPieces, false));
            }
        }
        return false;
    }

    /** Moves man in the given direction by one square.
     * Promotes if man reaches last row.
     * Assumes direction to be not blocked.
     */
    private static void movePieceOneSquareInDirection(Board board, Vector position, Vector direction){
        
        int man = board.get(position);
        board.set(position, EMPTY_SQUARE);

        Vector positionToMoveTo = position.plus(direction);

        // Check for promotion.
        if(positionToMoveTo.row == getLastRowForPiece(man))
            board.set(positionToMoveTo, getKingPieceForPiece(man));
        else
            board.set(positionToMoveTo, man);
    }

    /** Returns true if at least one take was possible. */
    private boolean manageTakesForMan(HashSet<TurkishDraughts> childrenSet, Board board, Vector startingPosition, int nbEnemyPieces){
        boolean takePossible = false;
        if(nbEnemyPieces == 0)
            return takePossible;

        for(Vector direction: getDirectionsNotBackwardForMan(board.get(startingPosition))){            
            // Take if can take and try taking from there recursively.
            if(manCanTakeInDirection(board, startingPosition, direction)){
                takePossible = true;
                Board childBoard = board.copy();
                Vector positionLandedOn = letManTakeInDirection(childBoard, startingPosition, direction);
                int childNbEnemyPieces = nbEnemyPieces-1;

                // Check if should enter survival mode.
                if(nbEnemyPieces <= 3)
                   enterSurvivalMode(childBoard, !this.whitesTurn);

                // Update nbPieces and Add the new child.
                int newNbWhitePieces = this.whitesTurn ? this.nbWhitePieces : childNbEnemyPieces;
                int newNbBlackPieces = !this.whitesTurn ? this.nbBlackPieces : childNbEnemyPieces;

                TurkishDraughts child = new TurkishDraughts(childBoard, !this.whitesTurn, this.maximizeForWhite, newNbWhitePieces, newNbBlackPieces, true);
                // System.out.println(child);
                childrenSet.add(child);
            
                // Note: This implementation enters survival mode mid-taking for performance reasons (see enterSurvivalMode())
                // Move the following line to before checking survival mode to enter it at the end of player's turn.
                manageTakesForMan(childrenSet, childBoard, positionLandedOn, childNbEnemyPieces);
            }
        }
        return takePossible;
    }

    /** Returns also false if nextNext is out of bounds. */
    private static boolean manCanTakeInDirection(Board board, Vector position, Vector direction){
        Vector positionToMaybeTake = position.plus(direction);

        if(!isTakeableSquare(board, positionToMaybeTake, direction, board.get(position)))
            return false;
        
        // Next in direction is enemy piece. Check the position to land on.
        Vector positionToMaybeLandOn = positionToMaybeTake.plus(direction);
        return isMovableSquare(board, positionToMaybeLandOn); 
    }
    
    private static Vector letManTakeInDirection(Board board, Vector position, Vector direction){
        Vector positionToTake = position.plus(direction);
        Vector positionToLandOn = positionToTake.plus(direction);
        int man = board.get(position);

        board.set(position, EMPTY_SQUARE);
        board.set(positionToTake, EMPTY_SQUARE);

        // Check for promotion.
        if(positionToLandOn.row == getLastRowForPiece(man))
            board.set(positionToLandOn, getKingPieceForPiece(man));
        else
            board.set(positionToLandOn, man);
        return positionToLandOn;
    }

    /** Converts all pieces of the right color to king pieces. 
     * Throws exception if number of converted pieces is more than 3. 
     */
    private static void enterSurvivalMode(Board childBoard, boolean doForWhite) {
        int count = 0;
        int kingPiece = doForWhite ? W_KING : B_KING;

        // Turn all pieces of the player to kings.
        for(int row=0; row<BOARD_SIZE; row++)
            for(int col=0; col<BOARD_SIZE; col++){
                Vector position = new Vector(row, col);
                if(childBoard.get(position) != EMPTY_SQUARE && isWhite(childBoard.get(position)) == doForWhite){
                    childBoard.set(position, kingPiece);
                    count++;
                }
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
        return this.board.equals(other.board);
    }

    @Override
    public int hashCode(){
        int hash = 7;
        hash = 29 * hash + this.board.hashCode();
        hash = 31 * hash + (whitesTurn ? 1 : 0);
        return hash;
    }

    public String toString(){
        return this.toString(ConsoleColors.RESET);
    }

    @Override
    public String toString(String background){
        StringBuilder str = new StringBuilder();

        // add board elements
        str.append(getHorizontalLine());
        for(int rowNb=BOARD_SIZE-1; rowNb>=0; rowNb--){
            int[] row = this.board.getRow(rowNb);
            str.append(rowToString(row, rowNb, background));
        }
        // add column numbers
        str.append(getHorizontalLine());
        str.append(rowToString(new int[]{0,1,2,3,4,5,6,7}, -1, background));
        
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
    private String rowToString(int[] row, int rowNb, String background){
        if(rowNb == -1)
            return lastRowToString(row) + "\n";
        else
            return normalRowToString(row, rowNb, background) + "\n";    
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
    private static String normalRowToString(int[] row, int rowNb, String background){
        StringBuilder result = new StringBuilder();

        result.append(rowNb + "| ");

        // Add the pieces, seperated by " | ".
        for(int piece: row){
            // add row elements
            result.append(pieceToString(piece, background) + " | ");
        }
        return result.toString();
    }

    /** Converts the piece number to its approppriate representation.
     * 
     * @param piece
     * @return String representation of the piece.
    */
    private static String pieceToString(int piece, String background){

        switch(piece){
            case W_KING:
                return backgroundW + "W" + background;
            case W_MAN:
                return backgroundW + "w" + background;
            case B_KING:
                return backgroundB + "B" + background;
            case B_MAN:
                return backgroundB + "b" + background;
            case EMPTY_SQUARE:
                return " ";
            default:
                return "?";
        }
    }
    
    @Override
    public Board getBoard(){
        return this.board.copy();
    }

    /**
        @param board Board object that contains values chosen from {-2, -1, 0, 1, 2}.
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
    public TurkishDraughts(Board board, boolean whitesTurn, boolean maximizeForWhite, int nbWhitePieces, 
            int nbBlackPieces, boolean hasTakenLastTurn){
        if(board.nbRows != BOARD_SIZE)
            throw new IllegalArgumentException("`board` must have " + BOARD_SIZE + " rows.");
        if(board.nbCols != BOARD_SIZE)
            throw new IllegalArgumentException("`board` must have " + BOARD_SIZE + " columns.");
        // if(!Arrays.stream(board).allMatch(row -> 
        // Arrays.stream(row).allMatch(i -> (i >= -2) && (i <= 2))))
        //     throw new IllegalArgumentException("pieces can only be man, king or empty.");

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
    private static int nbPiecesLeftOnBoard(Board board, boolean checkForWhite){
        int nbPieces = 0;

        for(int[] row: board.getMatrix())
            for(int piece: row)
                if(pieceBelongsToPlayer(piece, checkForWhite))
                    nbPieces++;
        return nbPieces;
    }

    private static boolean pieceBelongsToPlayer(int piece, boolean checkForWhite){
        int playerSign = checkForWhite ? 1 : -1;
        return playerSign * piece > 0;
    }

    private static Vector[] getDirectionsNotBackwardForMan(int piece){
        if(piece == EMPTY_SQUARE)
            throw new IllegalArgumentException("Given piece may not be an empty square.");
        
        return isWhite(piece) ? new Vector[]{UP, RIGHT, LEFT} : new Vector[]{DOWN, RIGHT, LEFT};
    }

    private static Vector[] getAllDirectionsList(){
        return new Vector[]{UP, DOWN, RIGHT, LEFT};
    }

    /** Location is not out of bounds && location is empty. */
    private static boolean isMovableSquare(Board board, Vector position) {
        return !locationIsOutOfBounds(position) && board.get(position) == EMPTY_SQUARE;
    }
    
    /** Location is not out of bounds && is possessed by enemy. */
    private static boolean isTakeableSquare(Board board, Vector position, Vector direction, int piece){
        return !locationIsOutOfBounds(position) && 
        board.get(position) != EMPTY_SQUARE && 
        !areSameColor(piece, board.get(position)) &&
        board.get(position.plus(direction)) == EMPTY_SQUARE;
    }

    /** Returns the index of the promotion line for the given piece. */
    private static int getLastRowForPiece(int piece){
        return isWhite(piece) ? TOP_ROW : BOTTOM_ROW;
    }

    /** Returns a king of the same color. */
    private static int getKingPieceForPiece(int piece){
        return isWhite(piece) ? W_KING : B_KING;
    }

    private static boolean isWhite(int piece){
        return piece == W_MAN || piece == W_KING;
    }
        
    private static boolean isKing(int piece){
        return piece == W_KING || piece == B_KING;
    }

    private boolean piecesTurn(int piece){
        if(piece == EMPTY_SQUARE)
            throw new IllegalArgumentException("Empty square doesn't have a turn.");

        return (isWhite(piece) && this.whitesTurn) || (!isWhite(piece) && !this.whitesTurn);
    }

    /** Returns true if both white or both black. */
    private static boolean areSameColor(int piece1, int piece2){
        return piece1 * piece2 > 0;
    }

    private static boolean locationIsOutOfBounds(Vector position) {
        return TOP_ROW < position.row || position.row < BOTTOM_ROW ||
            RIGHTMOST_COL < position.col || position.col < LEFTMOST_COL;
    }

    // TESTS
    /** Returns a checkers board with 4 White pieces and 6 Black pieces on it. */
    public static Board getTestBoard(){
        Board board = new Board(new int[BOARD_SIZE][BOARD_SIZE]);

        Vector whiteManPos1 = new Vector(1, 0);
        Vector whiteManPos2 = new Vector(3, 3);
        Vector whiteManPos3 = new Vector(5, 6);
        Vector whiteKingPos1 = new Vector(5, 4);
        
        Vector blackManPos1 = new Vector(2, 0);
        Vector blackKingPos1 = new Vector(4, 0);
        Vector blackManPos2 = new Vector(5, 1);

        Vector blackManPos3 = new Vector(1, 1);
        Vector blackManPos4 = new Vector(1, 3);
        Vector blackKingPos2 = new Vector(2, 4);


        board.set(whiteManPos1, W_MAN);
        board.set(whiteManPos2, W_MAN);
        board.set(whiteManPos3, W_MAN);
        board.set(whiteKingPos1, W_KING);

        board.set(blackManPos1, B_MAN);
        board.set(blackKingPos1, B_KING);
        board.set(blackManPos2, B_MAN);

        board.set(blackManPos3, B_MAN);
        board.set(blackManPos4, B_MAN);
        board.set(blackKingPos2, B_KING);
        
        return board;
}
    
    public void testPrivateMethods(){
        System.out.println("  testPrivateMethods..");

        testManCanTakeInDirection();
        testManTakesInDirection();
        testManageTakes();

        System.out.println("  testPrivateMethods successful");
    }

    private static void testManCanTakeInDirection(){
        System.out.println("    testManCanTakeInDirection..");
        Board testBoard = getTestBoard();

        Vector positionOfMan = new Vector(1, 0);
        assert manCanTakeInDirection(testBoard, positionOfMan, UP);
        assert manCanTakeInDirection(testBoard, positionOfMan, DOWN);
        assert manCanTakeInDirection(testBoard, positionOfMan, RIGHT);
        assert manCanTakeInDirection(testBoard, positionOfMan, LEFT);
        System.out.println("    testManCanTakeInDirection successful");
    }

    private static void testManTakesInDirection(){
        System.out.println("    testManTakesInDirection..");
        Board testBoard = getTestBoard();
        Vector whiteManPos1 = new Vector(1, 0);

        Board childBoard = testBoard.copy();
        letManTakeInDirection(childBoard, whiteManPos1, UP);
        assert childBoard.get(new Vector(1, 0)) == EMPTY_SQUARE;
        assert childBoard.get(new Vector(2, 0)) == EMPTY_SQUARE;
        assert childBoard.get(new Vector(3, 0)) == W_MAN;
        
        childBoard = testBoard.copy();
        letManTakeInDirection(childBoard, whiteManPos1, RIGHT);
        assert childBoard.get(new Vector(1, 0)) == EMPTY_SQUARE;
        assert childBoard.get(new Vector(1, 1)) == EMPTY_SQUARE;
        assert childBoard.get(new Vector(1, 2)) == W_MAN;
        
        System.out.println("    testManTakesInDirection successful");
    }

    private void testManageTakes(){
        HashSet<TurkishDraughts> childrenSet = new HashSet<>();
        Board testBoard = getTestBoard();

        manageTakesForMan(childrenSet, testBoard, new Vector(1, 0), 6);

        for(TurkishDraughts child : childrenSet)
            System.out.println(child);
    }

}
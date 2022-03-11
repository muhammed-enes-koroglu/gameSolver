import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class TurkishDraughts implements TwoPersonGameState<TurkishDraughts>{
    public static final int BOARD_SIZE = 8;
    public static final int W_MAN = 1;
    public static final int B_MAN = -1;
    public static final int W_KING = 2;
    public static final int B_KING = -2;
    public static final int EMPTY_SQUARE = 0;
    public static final int TOP_ROW = BOARD_SIZE-1;
    public static final int BOTTOM_ROW = 0;
    public static final int RIGHTMOST_COL = TOP_ROW;
    public static final int LEFTMOST_COL = BOTTOM_ROW;
    private static final int[] UP = new int[]{1,0};
    private static final int[] DOWN = new int[]{-1,0};
    private static final int[] RIGHT = new int[]{0,1};
    private static final int[] LEFT = new int[]{0,-1};

    private int[][] board;
    private boolean whitesTurn;
    private boolean maximizeForWhite;

    public static int[] up(){return UP.clone();}
    public static int[] down(){return DOWN.clone();}
    public static int[] right(){return RIGHT.clone();}
    public static int[] left(){return LEFT.clone();}
    public static int[][] getDirectionsList() { return new int[][]{up(), down(), right(), left()};}


    @Override
    public Set<TurkishDraughts> children() {
        Set<TurkishDraughts> children = new HashSet<>();
        HashSet<int[][]> childBoardSet = new HashSet<>();
        boolean takePossible = false;

        for(int currentRow=0; currentRow<BOARD_SIZE; currentRow++){
            for(int currentCol=0; currentCol<BOARD_SIZE; currentCol++){
                int piece = this.board[currentRow][currentCol];
                if(piece == EMPTY_SQUARE || !piecesTurn(piece)) continue; // Skip empty spaces and pieces whose turn it is not.
                
                int[][] childBoard = Helper.deepCopy(this.board);
                if(isKing(piece))
                    takePossible = takePossible || addChildBoardsForKingAt(childBoardSet, childBoard, currentRow, currentCol, takePossible);
                else
                    takePossible = takePossible || addChildBoardsForManAt(childBoardSet, childBoard, currentRow, currentCol, takePossible);
                
                }
        for(int[][] childBoard: childBoardSet){
            children.add(new TurkishDraughts(childBoard, !this.whitesTurn, this.maximizeForWhite));
        }
        }


        return children;
    }

    private static boolean addChildBoardsForKingAt(HashSet<int[][]> childBoards, int[][] board, int currentRow, int currentCol, boolean takePossible) {
        //TODO
        throw new UnsupportedOperationException("Not yet implemented.");
    }

    // Returns true if at least one take was possible.
    private static boolean addChildBoardsForManAt(HashSet<int[][]> childBoards, int[][] board, int currentRow, int currentCol, boolean takePossible){
        
        // Should not make a normal move if a take is possible.
        if(manageTakes(childBoards, board, currentRow, currentCol)){ return true; }
        
        // If a take is possible, but this piece doesn't have any takes; this piece does not move.
        if(takePossible){ return false; }

        // No takes possible.
        for(int[] direction: getDirectionsList()){
            int rowDirection = direction[0];
            int colDirection = direction[1];
            
            // Take if can take and try taking from there recursively.
            if(!isLocationOutOfBounds(currentRow + rowDirection, currentCol + colDirection) &&
            board[currentRow + rowDirection][currentCol + colDirection] == EMPTY_SQUARE){
                int[][] childBoard = Helper.deepCopy(board);
                moveManInDirection(childBoard, currentRow, currentCol, rowDirection, colDirection);
                childBoards.add(childBoard);
            }
        }
        return false;
    }

    private static void moveManInDirection(int[][] board, int currentRow, int currentCol, int rowDirection, int colDirection){
        
        int man = board[currentRow][currentCol];
        board[currentRow][currentCol] = EMPTY_SQUARE;
        board[currentRow + rowDirection][currentCol + colDirection] = man;

    }

    // Returns true if at least one take was possible.
    private static boolean manageTakes(HashSet<int[][]> childBoards, int[][] board, int currentRow, int currentCol){
        boolean takePossible = false;

        for(int[] direction: getDirectionsList()){
            int rowDirection = direction[0];
            int colDirection = direction[1];
            
            // Take if can take and try taking from there recursively.
            if(!isLocationOutOfBounds(currentRow + rowDirection, currentCol + colDirection) && 
            manCanTakeInDirection(board, currentRow, currentCol, rowDirection, colDirection)){
                takePossible = true;
                int[][] childBoard = Helper.deepCopy(board);
                manTakesInDirection(childBoard, currentRow, currentCol, rowDirection, colDirection);
                childBoards.add(childBoard);
                manageTakes(childBoards, childBoard, currentRow + 2*rowDirection, currentCol + 2*colDirection);
            }
        }
        return takePossible;
    }

    // Assumes next square in direction is not out of bounds!!
    // Returns also false if nextNext is out of bounds.
    private static boolean manCanTakeInDirection(int[][] board,int currentRow, int currentCol, int rowDirection, int colDirection){
        int nextRow = currentRow + rowDirection;
        int nextCol = currentCol + colDirection;
        // Can only take if next is enemy == (next not empty) && (next not friendly)
        if(board[nextRow][nextCol] == EMPTY_SQUARE || areSameColor(board[currentRow][currentCol], board[nextRow][nextCol])){
            return false;
        }
        
        // Next is enemy piece. Check nextNext piece.
        int nextNextRow = nextRow + rowDirection;
        int nextNextCol = nextCol + colDirection;
        
        // Can not take if nextNext is out of bounds.
        if(isLocationOutOfBounds(nextNextRow, nextNextCol)){
            return false;
        }
        return board[nextNextRow][nextNextCol] == EMPTY_SQUARE; 
    }
    
    private static void manTakesInDirection(int[][] board, int currentRow, int currentCol, int rowDirection, int colDirection){

        int piece = board[currentRow][currentCol];
        board[currentRow][currentCol] = EMPTY_SQUARE;
        board[currentRow + rowDirection][currentCol + colDirection] = EMPTY_SQUARE;
        board[currentRow + 2 * rowDirection][currentCol + 2 * colDirection] = piece;
        
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
        hash = 31 * hash + (board == null ? 0 : Arrays.hashCode(board));
        hash = 31 * hash + (whitesTurn ? 1 : 0);
        return hash;
    }

    @Override
    public String toString(){
        StringBuilder str = new StringBuilder();
        
        // add board elements
        str.append(rowOfLines());
        for(int rowNb=BOARD_SIZE-1; rowNb>=0; rowNb--){
            int[] row = this.board[rowNb];
            str.append(rowToString(row, rowNb));
        }
        // add column numbers
        str.append(rowOfLines());
        str.append(rowToString(new int[]{0,1,2,3,4,5,6,7}, -1));
        
        return str.toString();
    }

    private static String rowOfLines(){
        StringBuilder str = new StringBuilder();
        for(int i=0; i < 34; i++)
            str.append("_");
        str.append("\n");
        return str.toString();
    }

    private static String rowToString(int[] row, int rowNb){
        StringBuilder result = new StringBuilder();

        // add row number
        if(rowNb == -1){
            result.append(" ");
        }
        else
            result.append(rowNb);
        result.append("| ");

        for(int piece: row){
            // add column number
            if(rowNb == -1)
                result.append(piece);
            // add row elements
            else
                result.append(pieceToString(piece));
            
            result.append(" | ");  // seperator
        }
        result.append("\n");
        return result.toString();
    }

    private static String pieceToString(int piece){

        if(isWhite(piece)){
            if(isKing(piece))
                return "W";
            else
                return "w";
        }
        else if(isBlack(piece)){
            if(isKing(piece))
                return "B";
            else
                return "b";
        }
        else
            return " ";
    }
    
    /*
        @param: int[][] board: contains values chosen from {-2, -1, 0, 1, 2}.
        0 : empty square
        1 : white man
        -1: black man
        2 : white king
        -2: black king
    */
    public TurkishDraughts(int[][] board, boolean whitesTurn, boolean maximizeForWhite){
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
    }

    //Help methods
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
        
        return board;
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
        if(piece == 0)
            throw new IllegalArgumentException("Empty square doesn't have a turn.");

        return (isWhite(piece) && this.whitesTurn) || (!isWhite(piece) && !this.whitesTurn);
    }

    private static boolean areSameColor(int piece1, int piece2){
        return piece1 * piece2 > 0;
    }

    private static boolean isLocationOutOfBounds(int nextRow, int nextCol) {
        return TOP_ROW < nextRow || nextRow < BOTTOM_ROW ||
            RIGHTMOST_COL < nextCol || nextCol < LEFTMOST_COL;
    }

    public static void testPrivateMethods(){
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
        manTakesInDirection(childBoard, 1, 0, 1,  0);
        assert childBoard[1][0] == EMPTY_SQUARE;
        assert childBoard[2][0] == EMPTY_SQUARE;
        assert childBoard[3][0] == W_MAN;
        
        childBoard = Helper.deepCopy(board);
        manTakesInDirection(childBoard, 1, 0, 0, 1);
        assert childBoard[1][0] == EMPTY_SQUARE;
        assert childBoard[1][1] == EMPTY_SQUARE;
        assert childBoard[1][2] == W_MAN;
        
        System.out.println("    testManTakesInDirection successful");
    }

    private static void testManageTakes(){
        boolean whitesTurn = true;
        boolean maximizeForWhite= true;
        HashSet<int[][]> childBoardSet = new HashSet<>();
        int[][] board = getTestBoard();

        manageTakes(childBoardSet, board, 1, 0);

        for(int[][] childBoard : childBoardSet){
            TurkishDraughts child = new TurkishDraughts(childBoard, !whitesTurn, maximizeForWhite);
            System.out.println(child);
        }
    }
}
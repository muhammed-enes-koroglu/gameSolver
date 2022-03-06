import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class TurkishCheckers implements TwoPersonGameState<TurkishCheckers>{
    public static final int BOARD_SIZE = 8;
    private int[][] board;
    private boolean whitesTurn;
    private boolean maximizeForWhite;

    @Override
    public Set<TurkishCheckers> children() {
        Set<TurkishCheckers> children = new HashSet<>();

        for(int row=0; row<BOARD_SIZE; row++){
            for(int col=0; col<BOARD_SIZE; col++){
                int piece = this.board[row][col];
                if(piece == 0 || !piecesTurn(piece)) continue; // look only at relevant pieces
                
                int[][] childBoard = this.board.clone();
                if(isKing(piece))
                    moveKingAt(childBoard, row, col);
                else
                    moveManAt(childBoard, row, col);
                
                children.add(new TurkishCheckers(childBoard, !this.whitesTurn, this.maximizeForWhite));

                }
        }


        return children;
    }

    private void moveKingAt(int[][] board, int row, int col) {
        //TODO
    }

    public void moveManAt(int[][] board, int row, int col){
        //TODO
    }

    @Override
    public float score() {
        // TODO
        return 0;
    }

    @Override
    public boolean isMaxPlayersTurn() {
        return !(maximizeForWhite ^ whitesTurn);
    }

    @Override
    public boolean equals(Object o){
        if(!(o instanceof TurkishCheckers))
            return false;
        TurkishCheckers other = (TurkishCheckers) o;
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
        
        str.append(horizontalLineString());
        for(int rowNb=BOARD_SIZE-1; rowNb>=0; rowNb--){
            int[] row = this.board[rowNb];
            str.append(rowString(row));
        }
        return str.toString();
    }

    private static String horizontalLineString(){
        StringBuilder str = new StringBuilder();
        for(int i=0; i < 30; i++)
            str.append("_");
        str.append("\n");
        return str.toString();
    }

    private static String rowString(int[] row){
        StringBuilder result = new StringBuilder();
        for(int piece: row){
            if(isWhite(piece)){
                if(isKing(piece))
                    result.append("W");
                else
                    result.append("w");
            }
            else if(isBlack(piece)){
                if(isKing(piece))
                    result.append("B");
                else
                    result.append("b");
            }
            else
                result.append(" ");
            result.append(" | ");  // seperator
        }
        result.append("\n");
        // result.append(horizontalLineString());
        return result.toString();
    }
    
    /*
        @param: int[][] board: contains values chosen from {-2, -1, 0, 1, 2}.
        0 : empty square
        1 : white man
        -1: black man
        2 : white king
        -2: black king
    */
    public TurkishCheckers(int[][] board, boolean whitesTurn, boolean maximizeForWhite){
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
        //     throw new IllegalArgumentException("whites should be in survival mode.");
        // if(playerLifeThreatened(board, !checkForWhite) && !Arrays.stream(board).allMatch(row -> 
        // Arrays.stream(row).allMatch(i -> i == -2)))
        //     throw new IllegalArgumentException("black should be in survival mode.");
            
        this.board = board;
        this.whitesTurn = whitesTurn;
        this.maximizeForWhite = maximizeForWhite;
    }

    //Help methods

    // Checks whether player has 3 or less pieces left on the board.
    private static boolean playerLifeThreatened(int[][] board, boolean checkForWhite){
        return nbPiecesLeftOnBoard(board, checkForWhite) <= 3;
    }
    
    private static int nbPiecesLeftOnBoard(int[][] board, boolean checkForWhite){
        int nbPieces = 0;

        for(int[] row: board)
            for(int piece: row)
                if(pieceBelongsToPlayer(piece, checkForWhite))
                    nbPieces++;
        return nbPieces;
    }

    private static boolean pieceBelongsToPlayer(int piece, boolean isWhitePlayer){
        int playerSign = isWhitePlayer ? 1 : -1;
        return playerSign * piece > 0;
    }

    private static boolean isWhite(int piece){
        return piece > 0;
    }
    
    private static boolean isBlack(int piece){
        return piece < 0;
    }
    
    private static boolean isKing(int piece){
        return piece == 2 || piece == -2;
    }

    private boolean piecesTurn(int piece){
        if(piece == 0)
            throw new IllegalArgumentException("Empty square doesn't have a turn.");

        boolean pieceIsWhite = piece > 0;
        return (pieceIsWhite && this.whitesTurn) || (!pieceIsWhite && !this.whitesTurn);
    }

}

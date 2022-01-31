import java.io.StringBufferInputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.swing.border.Border;

public class TurkishCheckers implements TwoPersonGameState<TurkishCheckers>{
    public static final int BOARD_SIZE = 8;
    private int[][] board;
    private boolean whitesTurn;
    private boolean whiteIsMax;

    @Override
    public Set<TurkishCheckers> children() {
        Set<TurkishCheckers> children = new HashSet<>();

        for(int row=0; row<BOARD_SIZE; row++){
            for(int col=0; col<BOARD_SIZE; col++){
                int piece = this.board[row][col];
                if(piece == 0 || !piecesTurn(piece)) continue; // implement compulsory takes.
                int[][] childboard = this.board.clone();
                if(!isSuper(piece))
                    moveFwdNormalPieceAt(board, row, col);
                // else
                    //implement children of super pieces.
            }
        }


        return children;
    }

    public void moveFwdNormalPieceAt(int[][] board, int row, int col){

    }

    @Override
    public float score() {
        // TODO
        return 0;
    }

    @Override
    public boolean isMaxPlayersTurn() {
        return !(whiteIsMax ^ whitesTurn);
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
        for(int[] row: this.board)
            str.append(rowString(row));
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
                if(isSuper(piece))
                    result.append("X");
                else
                    result.append("x");
            }
            else if(isBlack(piece)){
                if(isSuper(piece))
                    result.append("O");
                else
                    result.append("o");
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
        0: empty square
        1: normal white piece
        -1: normal black piece
        2: super white piece
        -2: super black piece
    */
    public TurkishCheckers(int[][] board, boolean whitesTurn, boolean maximizeForWhite){
        if(board.length != BOARD_SIZE)
            throw new IllegalArgumentException("`board` must have " + BOARD_SIZE + " rows.");
        if(board[0].length != BOARD_SIZE)
            throw new IllegalArgumentException("`board` must have " + BOARD_SIZE + " columns.");
        if(!Arrays.stream(board).allMatch(row -> 
        Arrays.stream(row).allMatch(i -> (i >= -2) && (i <= 2))))
            throw new IllegalArgumentException("pieces can only be normal, super or dead pieces.");
        boolean checkForWhite = true;
        if(playerLifeThreatened(board, checkForWhite) && !Arrays.stream(board).allMatch(row -> 
        Arrays.stream(row).allMatch(i -> i == 2)))
            throw new IllegalArgumentException("whites should be in survival mode.");
        if(playerLifeThreatened(board, !checkForWhite) && !Arrays.stream(board).allMatch(row -> 
        Arrays.stream(row).allMatch(i -> i == -2)))
            throw new IllegalArgumentException("black should be in survival mode.");
            
        this.board = board;
        this.whitesTurn = whitesTurn;
        this.whiteIsMax = maximizeForWhite;
    }

    //Help methods

    // Checks whether player has 3 or less pieces left on the board.
    private static boolean playerLifeThreatened(int[][] board, boolean checkForWhite){
        int nbPieces = 0;

        for(int[] row: board)
            for(int piece: row)
                if(pieceBelongsToPlayer(piece, checkForWhite))
                    nbPieces++;
        return nbPieces <= 3;
    }

    private static boolean isWhite(int piece){
        return piece > 0;
    }
    
    private static boolean isBlack(int piece){
        return piece < 0;
    }
    
    private static boolean isSuper(int piece){
        return piece == 2 || piece == -2;
    }

    private static boolean pieceBelongsToPlayer(int piece, boolean isWhitePlayer){
        int playerSign = isWhitePlayer ? 1 : -1;
        return playerSign * piece > 0;
    }

    private boolean piecesTurn(int piece){
        if(piece == 0)
            throw new IllegalArgumentException("Empty square doesn't have a turn.");

        boolean pieceIsWhite = piece > 0;
        return (pieceIsWhite && this.whitesTurn) || (!pieceIsWhite && !this.whitesTurn);
    }

}

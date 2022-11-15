package games.tictactoe;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import util.Board;
import util.TwoPersonGameState;

public class TicTacToeState implements TwoPersonGameState<TicTacToeState>{

    private final Board board;
    protected final boolean xTurn;
    protected final boolean xIsMaxPlayer;
    private final boolean xWon;
    private final boolean xLost;
    
    protected static final int BOARD_SIZE = 3;
    protected static final int X = 1;
    protected static final int O = -1;

    public boolean isXTurn() {
        return xTurn;
    }

    public boolean xWon(){
        return this.xWon;
    }

    public boolean xLost(){
        return this.xLost;
    }

    @Override
    public Set<TicTacToeState> children() {
        if(xWon || xLost)
            return new HashSet<>();
    
        HashSet<TicTacToeState> children = new HashSet<>();
        for(int i=0; i<board.nbCols; i++){
            Board copyBoard = board.copy();
            if(copyBoard.get(0, i) == 0){
                int newVal = xTurn ? 1 : -1;
                copyBoard.set(0, i, newVal);
                children.add(new TicTacToeState(copyBoard, this.xIsMaxPlayer));
            }
        }
        return children;
    }

    @Override // Should be improved.
    public float score() {
        int sign = this.xIsMaxPlayer ? +1 : -1;
        if(this.xWon)
            return sign * Float.MAX_VALUE;
        if(this.xLost)
            return -sign *  Float.MAX_VALUE;
        return 0;
    }

    @Override
    public boolean isMaxPlayersTurn() {
        return !(this.xIsMaxPlayer ^ this.xTurn);
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof TicTacToeState))
            return false;
        TicTacToeState other = (TicTacToeState) o;
        return other.board.equals(this.board);
    }
    
    @Override
    public int hashCode(){
        int hash = 7;
        hash = 31 * hash + (board == null ? 0 : board.hashCode());
        return hash;
    }

    @Override
    public String toString(){
        String str = "\n";
        for(int i=0; i<board.nbCols; i++){
            if(board.get(0, i) == 1)
                str += "X ";
            else if(board.get(0, i) == -1)
                str += "O ";
            else 
                str += "_ ";
            if(i%3 == 2)
                str += "\n";
        }
        str += "\n" + (xTurn ? "X" : "O");
        str += "\n" + "---------";
        return str;
    }

    @Override
    public Board getBoard() {
        return board.copy();
    }

    /**
     * @param board The board of the game. Must be a 1x9 board.
     * @param xTurn True if it is X's turn. 
     * 
     * 1 in `board` denotes an 'X', max player
     * -1 denotes an 'O', min player
     * 0 denotes blank space. */
    public TicTacToeState(Board board, boolean xIsMaxPlayer){
        if(board.nbRows != 1)
            throw new IllegalArgumentException("`board` must be a 1D array.");
        if(board.nbCols != 9)
            throw new IllegalArgumentException("`board` must be of length 9");
        if(!Arrays.stream(board.getMatrix()[0]
        ).allMatch(i -> (i==-1) || (i==0) || (i==1)))
            throw new IllegalArgumentException("`board` must only contain -1, 0 or 1");
        if(Arrays.stream(board.getMatrix()[0]).sum() == 0)
            this.xTurn = true;
        else if(Arrays.stream(board.getMatrix()[0]).sum() == 1)
            this.xTurn = false;
        else
            throw new IllegalArgumentException("impossible `board`: Not X's turn, not O's turn");

        int[][] boardMatrix = matrificise(board.getMatrix()[0]);
        boolean xHasWon = xWon(boardMatrix);
        boolean xHasLost = xLost(boardMatrix);
        if((xHasWon && this.xTurn)){
            System.out.println(board);
            throw new IllegalArgumentException("It must be O's turn if X has won.");
        }
        if(xHasLost && !this.xTurn)
            throw new IllegalArgumentException("It must be X's turn if O has won.");
    
        this.xWon = xHasWon;
        this.xLost = xHasLost;
        this.board = board;
        this.xIsMaxPlayer = xIsMaxPlayer;
    }

    public static boolean xWon(int[][] boardMatrix){
        return winInDiagonals(boardMatrix) || winInRows(boardMatrix) || winInColumns(boardMatrix);
    }

    public static boolean xLost(int[][] boardMatrix){
        int[][] reversedBoardMatrix = new int[3][3];
        for(int row=0; row<3; row++)
            for(int col=0; col<3; col++)
                reversedBoardMatrix[row][col] = -boardMatrix[row][col];
        return xWon(reversedBoardMatrix);
    }

    public static int[][] matrificise(int[] board){
        int[][] boardMatrix = new int[BOARD_SIZE][BOARD_SIZE];
        for(int i=0; i<BOARD_SIZE; i++)
            for(int j=0; j<BOARD_SIZE; j++)
                boardMatrix[i][j] = board[BOARD_SIZE*i + j];
        return boardMatrix;
    }
    
    private static boolean winInDiagonals(int[][] boardMatrix){
        boolean winInHeadDiagonal = true;
        boolean winInSecondDiagonal = true;
        for(int i=0; i<3; i++){
            if(boardMatrix[i][i] != 1)
                winInHeadDiagonal = false;
            if(boardMatrix[i][2-i] != 1)
                winInSecondDiagonal = false;
        }
        return winInHeadDiagonal || winInSecondDiagonal;
    }

    private static boolean winInRows(int[][] boardMatrix){
        for(int row=0; row<3; row++){
            boolean winInRow = true;
            for(int col=0; col<3; col++)
                if(boardMatrix[row][col] != 1)
                    winInRow = false;
            if(winInRow)
                return true;
        }
        return false;
    }

    private static boolean winInColumns(int[][] boardMatrix){
        for(int col=0; col<3; col++){
            boolean winInColumn = true;
            for(int row=0; row<3; row++)
                if(boardMatrix[row][col] != 1)
                    winInColumn = false;
            if(winInColumn)
                return true;
        }
        return false;
    }

}

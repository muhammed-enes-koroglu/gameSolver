import java.util.ArrayList;
import java.util.Arrays;

import javax.imageio.plugins.bmp.BMPImageWriteParam;

public class TicTacToeState implements TwoPersonGameState{

    private final int[] board;
    private final boolean xTurn;
    private final boolean xWon;
    private final boolean xLost;
    

    public int[] getBoard() {
        return board;
    }

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
    public TicTacToeState[] children() {
        if(xWon || xLost)
            return new TicTacToeState[0];
    
        ArrayList<TicTacToeState> children = new ArrayList<>();
        for(int i=0; i<board.length; i++){
            int[] copyBoard = board.clone();
            if(copyBoard[i] == 0){
                copyBoard[i] = xTurn ? 1 : -1;
                children.add(new TicTacToeState(copyBoard));
            }
        }
        return children.toArray(new TicTacToeState[0]);
    }

    @Override
    public float score() {
        if(this.xWon)
            return Float.MAX_VALUE;
        if(this.xLost)
            return -Float.MAX_VALUE;
        return 0;
    }

    @Override
    public boolean isMaxPlayer() {
        return this.xTurn;
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof TicTacToeState))
            return false;
        TicTacToeState other = (TicTacToeState) o;
        return other.board == this.board;
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
        for(int i=0; i<board.length; i++){
            if(board[i] == 1)
                str += "X ";
            else if(board[i] == -1)
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

    // 1 in `board` denotes an 'X', max player
    // -1 denotes an 'O', min player
    // 0 denotes blank space.
    public TicTacToeState(int[] board){
        if(board.length != 9)
            throw new IllegalArgumentException("`board` must be of length 9");
        if(!Arrays.stream(board).allMatch(i -> (i==-1) || (i==0) || (i==1)))
            throw new IllegalArgumentException("`board` must only contain -1, 0 or 1");
        if(Arrays.stream(board).sum() == 0)
            this.xTurn = true;
        else if(Arrays.stream(board).sum() == 1)
            this.xTurn = false;
        else
            throw new IllegalArgumentException("impossible `board`: Not X's turn, not O's turn");

        int[][] boardMatrix = matrificise(board);
        boolean xWon = xWon(boardMatrix);
        boolean xLost = xLost(boardMatrix);
        if((xWon && this.xTurn)){
            System.out.println(Arrays.toString(board));
            throw new IllegalArgumentException("It must be O's turn if X has won.");
        }
        if(xLost && !this.xTurn)
            throw new IllegalArgumentException("It must be X's turn if O has won.");
    
        this.xWon = xWon;
        this.xLost = xLost;
        this.board = board;
    }

    public static boolean xWon(int[][] boardMatrix){
        
        // Diagonals
        boolean winInHeadDiagonal = true;
        boolean winInSecondDiagonal = true;
        for(int i=0; i<3; i++){
            if(boardMatrix[i][i] != 1)
                winInHeadDiagonal = false;
            if(boardMatrix[i][2-i] != 1)
                winInSecondDiagonal = false;
        }
        if(winInHeadDiagonal || winInSecondDiagonal)
            return true;

        // Rows
        for(int row=0; row<3; row++){
            boolean winInRow = true;
            for(int col=0; col<3; col++)
                if(boardMatrix[row][col] != 1)
                    winInRow = false;
            if(winInRow)
                return true;
        }

        // Columns
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

    public static boolean xLost(int[][] boardMatrix){
        int[][] reversedBoardMatrix = new int[3][3];
        for(int row=0; row<3; row++)
            for(int col=0; col<3; col++)
                reversedBoardMatrix[row][col] = -boardMatrix[row][col];
        return xWon(reversedBoardMatrix);
    }

    public static int[][] matrificise(int[] board){
        int[][] boardMatrix = new int[3][3];
        for(int i=0; i<3; i++)
            for(int j=0; j<3; j++)
                boardMatrix[i][j] = board[3*i + j];
        return boardMatrix;
    }
}

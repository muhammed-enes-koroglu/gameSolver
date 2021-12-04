import java.util.ArrayList;
import java.util.Arrays;

public class TicTacToeState implements TwoPersonGameState{

    private final int[] board;
    private final boolean xTurn;

    public int[] getBoard() {
        return board;
    }

    public boolean isXTurn() {
        return xTurn;
    }

    @Override
    public TicTacToeState[] children() {
        ArrayList<TicTacToeState> children = new ArrayList<>();
        int j = 0;
        for(int i=0; i<board.length; i++){
            int[] copyBoard = board.clone();
            if(copyBoard[i] == 0){
                copyBoard[i] = xTurn ? 1 : -1;
                children.add(new TicTacToeState(copyBoard, !xTurn));
            }
        }
        return children.toArray(new TicTacToeState[0]);
    }

    @Override
    public float score() {

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
        return other.board == this.board && other.xTurn == this.xTurn;
    }
    
    @Override
    public int hashCode(){
        int hash = 7;
        hash = 31 * hash + (board == null ? 0 : board.hashCode());
        hash = 31 * hash + (xTurn ? 1 : 0);
        return hash;
    }

    @Override
    public String toString(){
        String str = "";
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
    public TicTacToeState(int[] board, boolean xTurn){
        if(board.length != 9)
            throw new IllegalArgumentException("`board` must be of length 9");
        if(!Arrays.stream(board).allMatch(i -> (i==-1) || (i==0) || (i==1)))
            throw new IllegalArgumentException("`board` must only contain -1, 0 or 1");
        if(xTurn && Arrays.stream(board).sum() != 0)
            throw new IllegalArgumentException("`board` inconsistent with it being X's turn");
        if(!xTurn && Arrays.stream(board).sum() != 1)
            throw new IllegalArgumentException("`board` inconsistent with it being O's turn");
        
        this.board = board;
        this.xTurn = xTurn;
    }

}

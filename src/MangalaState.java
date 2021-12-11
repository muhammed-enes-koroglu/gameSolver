import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.ComboBoxEditor;


public class MangalaState implements TwoPersonGameState<MangalaState>{

    private int[] blackTrenches;
    private int[] whiteTrenches;
    private final int[] board;
    private boolean whiteIsMax;
    private boolean whitesTurn;
    private static final int BOARD_SIZE = 7;

    @Override
    public MangalaState[] children() {
        
        ArrayList<MangalaState> children = new ArrayList<>();
        int turnBias = whiteIsMax ? 0 : BOARD_SIZE;
        boolean childWhitesTurn;
        for(int trench = turnBias; trench < BOARD_SIZE + turnBias; trench++){
            int[] childBoard = this.board.clone();
            int endedAt = playTrench(childBoard, trench);
            childWhitesTurn = !this.whitesTurn;

            if(endedAt == BOARD_SIZE-1 + turnBias) // Ended at treasury
                childWhitesTurn = this.whitesTurn;
            else if(turnBias <= endedAt && endedAt < BOARD_SIZE + turnBias){ // Ended at own trench
                if(childBoard[endedAt] == 1){ // Trench is empty
                    childBoard[BOARD_SIZE -1 + turnBias] = childBoard[endedAt] + childBoard[oppositeTrenchOf(endedAt)];
                    childBoard[endedAt] = 0;
                    childBoard[oppositeTrenchOf(endedAt)] = 0;
                }
            }
            else if(childBoard[endedAt] % 2 == 0){ // Ended at enemy trench and is even
                childBoard[BOARD_SIZE -1 + turnBias] = childBoard[endedAt];
                childBoard[endedAt] = 0;
            }
            if(Arrays.stream(Arrays.copyOfRange(childBoard, 0, BOARD_SIZE -2)).sum() == 0) // White's side is empty
                for(int i=BOARD_SIZE; i<= 2*BOARD_SIZE-2; i++){ // Black's side gets slurped
                    childBoard[BOARD_SIZE-1] = childBoard[i];
                    childBoard[i] = 0;
                }
            else if(Arrays.stream(Arrays.copyOfRange(childBoard, BOARD_SIZE, 2 * BOARD_SIZE -2 + turnBias)).sum() == 0) // Black's side is empty
                for(int i=0; i<= BOARD_SIZE-2; i++){ // White's side gets slurped
                    childBoard[2*BOARD_SIZE-1] = childBoard[i];
                    childBoard[i] = 0;
                }
            children.add(new MangalaState(childBoard, childWhitesTurn, this.whiteIsMax));            
        }

        return children.toArray(new MangalaState[0]);
    }

    @Override
    public float score() {
        // TODO Auto-generated method stub
        return 0;
    }
    
    @Override
    public boolean equals(Object o) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public int hashCode(){
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public boolean isMaxPlayersTurn() {
        return (whiteIsMax ^ whitesTurn);
    }

    @Override
    public String toString(){
        String str = "\n  ";
        for(int i= 2*BOARD_SIZE-2; i>BOARD_SIZE-1; i--)
            str += " " + this.board[i];
        str += "\n" + this.board[2*BOARD_SIZE-1] + "              " + this.board[BOARD_SIZE-1] + "\n ";
        for(int i=0; i<BOARD_SIZE-1; i++)
            str += " " + this.board[i];
        return str;        
    }


    public MangalaState(int[] board, boolean whitesTurn, boolean whiteIsMax){
        if(board.length != 2 * BOARD_SIZE)
            throw new IllegalArgumentException("`board` must contain 14 trenches");
        if(!Arrays.stream(board).allMatch(i -> i >= 0))
            throw new IllegalArgumentException("number of stones in trenches must be positive");
        if((Arrays.stream(Arrays.copyOfRange(board, 0, BOARD_SIZE-2)).sum() == 0) ^ 
        (Arrays.stream(Arrays.copyOfRange(board, BOARD_SIZE, 2 * BOARD_SIZE - 2)).sum() == 0))
            throw new IllegalArgumentException("If one side is empty, so should the other be");
        int[] whitesTrenches = Arrays.copyOfRange(board, 0, BOARD_SIZE-1);
        int[] blacksTrenches = Arrays.copyOfRange(board, BOARD_SIZE, 2 * BOARD_SIZE - 1);
        
        this.whiteTrenches = whitesTrenches;
        this.blackTrenches = blacksTrenches;
        this.board = board;
        this.whitesTurn = whitesTurn;
        this.whiteIsMax = whiteIsMax;
    }

    // Help Methods..

    private static int playTrench(int[] aBoard, int trench){
        int opponentTreasury = trench < BOARD_SIZE ? 2 * BOARD_SIZE -1 : BOARD_SIZE -1;
        int stones = aBoard[trench] - 1;
        aBoard[trench] = 1;
        trench++;
        for(; stones > 0; trench++)
            if(trench % (2 * BOARD_SIZE) != opponentTreasury){
                stones--;
                aBoard[trench % aBoard.length]++;
            }
        return (--trench) % (2 * BOARD_SIZE);
    }

    private static int oppositeTrenchOf(int trench){
        return 2 * BOARD_SIZE -1 - trench;
    }

    public static void testPrivateMethods(){

        testPlayTrench();

    }

    private static void testPlayTrench(){
        System.out.println("testPlayTrench..");

        int[] board = new int[]{0,5,0, 0,0,0, 0, 0,0,0, 0,0,0, 0};
        int endedAt = playTrench(board, 1);
        assert Arrays.equals(board, new int[]{0,1,1, 1,1,1, 0, 0,0,0, 0,0,0, 0});
        assert endedAt == 5;

        board = new int[]{0,6,2, 3,0,0, 0, 0,0,0, 0,0,0, 0};
        endedAt = playTrench(board, 1);
        assert Arrays.equals(board, new int[]{0,1,3, 4,1,1, 1, 0,0,0, 0,0,0, 0});
        assert endedAt == 6;

        board = new int[]{0,6,2, 3,0,0, 0, 0,0,0, 0,0,0, 0};
        endedAt = playTrench(board, 1);
        assert Arrays.equals(board, new int[]{0,1,3, 4,1,1, 1, 0,0,0, 0,0,0, 0});
        assert endedAt == 6;

        // Doesn't put stone to opponent's treasury on pass
        board = new int[]{0,0,0, 0,0,10, 0, 0,0,0, 0,0,0, 0};
        endedAt = playTrench(board, 5);
        assert Arrays.equals(board, new int[]{1,1,0, 0,0,1, 1, 1,1,1, 1,1,1, 0});
        assert endedAt == 1;
                
        System.out.println("testPlayTrench successful");
    }

}

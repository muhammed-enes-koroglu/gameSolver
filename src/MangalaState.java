import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.ComboBoxEditor;


public class MangalaState implements TwoPersonGameState<MangalaState>{

    private int[] blackTrenches;
    private int[] whiteTrenches;
    private final int[] board;
    private boolean whiteIsMax;
    private boolean whitesTurn;
    private int sideSize;

    @Override
    public MangalaState[] children() {
        
        ArrayList<MangalaState> children = new ArrayList<>();
        int turnBias = whiteIsMax ? 0 : sideSize;
        for(int trench = turnBias; trench < sideSize + turnBias; trench++){
            int[] childBoard = this.board.clone();
            int endedAt = playTrench(childBoard, trench);

            if(endedAt == this.sideSize-1 + turnBias) // Ended at treasury
                children.add(new MangalaState(childBoard, this.whitesTurn, this.whiteIsMax));
            if(childBoard[endedAt] == 1)
                ;
            if()
            
        }

    }

    @Override
    public float score() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public boolean isMaxPlayersTurn() {
        // TODO Auto-generated method stub
        return (whiteIsMax ^ whitesTurn);
    }
    
    public MangalaState(int[] board, boolean whitesTurn, boolean whiteIsMax){
        if(board.length != 14)
            throw new IllegalArgumentException("`board` must contain 14 trenches");
        if(!Arrays.stream(board).allMatch(i -> i >= 0))
            throw new IllegalArgumentException("number of stones in trenches must be positive");
        if((Arrays.stream(Arrays.copyOfRange(board, 0, 5)).sum() == 0) ^ 
        (Arrays.stream(Arrays.copyOfRange(board, 7, 12)).sum() == 0))
            throw new IllegalArgumentException("If one side is empty, so should the other be");
        int[] whitesTrenches = Arrays.copyOfRange(board, 0, 6);
        int[] blacksTrenches = Arrays.copyOfRange(board, 7, 13);
        
        this.whiteTrenches = whitesTrenches;
        this.blackTrenches = blacksTrenches;
        this.board = board;
        this.whitesTurn = whitesTurn;
        this.whiteIsMax = whiteIsMax;
        this.sideSize = this.board.length/2;

    }

    // Help Methods
    private static int playTrench(int[] aBoard, int trench){
        int stones = aBoard[trench] - 1;
        aBoard[trench] = 1;
        trench++;
        for(; stones > 0; trench++, stones--)
            aBoard[trench % aBoard.length]++;
        return --trench;
    }

    private static <T> boolean arrayContains(T[] arr, T value){
        for(T elm: arr)
            if(elm.equals(value))
                return true;
        return false;
    }

    public static void testMangalaState(){
        System.out.println("\nTesting MangalaState");

        testPlayTrench();

        System.out.println("\nAll MangalaState tests are Successful.");
    }

    private static void testPlayTrench(){
        System.out.println("testPlayTrench..");

        int[] board = new int[]{0, 5, 0, 0, 0, 0};
        int endedAt = playTrench(board, 1);
        assert Arrays.equals(board, new int[]{0,1,1, 1,1,1});
        assert endedAt == 5;

        board = new int[]{0,6,2, 3,0,0, 0,0};
        endedAt = playTrench(board, 1);
        assert Arrays.equals(board, new int[]{0,1,3, 4,1,1, 1,0});
        assert endedAt == 6;

        System.out.println("testPlayTrench successful");
    }

    private static void testChildren(){
        /* Rules:
            -Play again if ended at own treasury.
            -Take own and opposite trench if ended at own empty trench.
            -Take enemy trench if ended at enemy trench and made it even.
            -Take enemy's all trenches if your side empty.
        */
        // One simple move
        int[] board = new int[]{1,0,0, 0,0,0, 0, 0,0,0, 0,0,0, 0};
        boolean whitesTurn = true;
        boolean whiteIsMax = true;
        MangalaState mState = new MangalaState(board, whitesTurn, whiteIsMax);
        int[] childBoard = new int[]{0,1,0, 0,0,0, 0, 0,0,0, 0,0,0, 0};
        MangalaState child = new MangalaState(childBoard, !whitesTurn, whiteIsMax); // lso tests if turn changed.
        assert Arrays.equals(mState.children(), new MangalaState[]{child}); // not deepEquals ?

        // Doesn't matter who we want to maximize
        MangalaState m2 = new MangalaState(board, whitesTurn, !whiteIsMax);
        assert Arrays.equals(mState.children(), m2.children());

        // Play again if ended at treasury
        board = new int[]{0,0,0, 0,0,1, 0, 0,0,0, 0,0,0, 0};
        mState = new MangalaState(board, whitesTurn, whiteIsMax);
        childBoard = new int[]{0,0,0, 0,0,0, 1, 0,0,0, 0,0,0, 0};
        child = new MangalaState(childBoard, whitesTurn, whiteIsMax); // AGAIN whitesTurn
        assert Arrays.equals(mState.children(), new MangalaState[]{child}); // not deepEquals ?

        board = new int[]{0,0,0, 4,0,0, 0, 0,0,0, 0,0,0, 0};
        mState = new MangalaState(board, whitesTurn, whiteIsMax);
        childBoard = new int[]{0,0,0, 1,1,1, 1, 0,0,0, 0,0,0, 0};
        child = new MangalaState(childBoard, whitesTurn, whiteIsMax); // AGAIN whitesTurn
        assert Arrays.equals(mState.children(), new MangalaState[]{child}); // not deepEquals ?

        // Takes opposite trench if ended at empty at own side
        board = new int[]{1,0,0, 0,0,0, 0, 0,0,0, 0,7,0, 0};
        mState = new MangalaState(board, whitesTurn, whiteIsMax);
        childBoard = new int[]{0,0,0, 0,0,0, 8, 0,0,0, 0,0,0, 0};
        child = new MangalaState(childBoard, !whitesTurn, whiteIsMax);
        assert Arrays.equals(mState.children(), new MangalaState[]{child}); // not deepEquals ?

        // Doesn't take opposite trench if ended at empty at other side
        board = new int[]{0,0,0, 0,7,4, 0, 0,0,0, 0,0,0, 0};
        mState = new MangalaState(board, whitesTurn, whiteIsMax);
        childBoard = new int[]{0,0,0, 0,7,1, 1, 1,1,0, 0,0,0, 0};
        child = new MangalaState(childBoard, !whitesTurn, whiteIsMax); // possibility 1
        childBoard = new int[]{0,0,0, 0,1,5, 1, 1,1,1, 1,0,0};
        MangalaState child2 = new MangalaState(childBoard, !whitesTurn, whiteIsMax); // possibility 2
        assert Arrays.equals(mState.children(), new MangalaState[]{child, child2}); // not deepEquals ?

        // Takes enemy's trench if ended there and made it even
        board = new int[]{0,0,0, 0,4,0, 0, 3,0,0, 0,7,0, 0};
        mState = new MangalaState(board, whitesTurn, whiteIsMax);
        childBoard = new int[]{0,0,0, 0,1,1, 5, 0,0,0, 0,7,0, 0};
        child = new MangalaState(childBoard, !whitesTurn, whiteIsMax);
        assert Arrays.equals(mState.children(), new MangalaState[]{child}); // not deepEquals ?

        // Doesn't take own trench if ended there and made it even
        board = new int[]{0,0,3, 0,1,0, 0, 0,0,0, 0,0,0, 0};
        mState = new MangalaState(board, whitesTurn, whiteIsMax);
        childBoard = new int[]{0,0,1, 1,0,0, 2, 0,0,0, 0,0,0, 0};
        child = new MangalaState(childBoard, !whitesTurn, whiteIsMax); // possibility 1
        childBoard = new int[]{0,0,3, 0,1,0, 0, 0,0,0, 0,0,0};
        child2 = new MangalaState(childBoard, !whitesTurn, whiteIsMax); // possibility 2
        assert Arrays.equals(mState.children(), new MangalaState[]{child, child2}); // not deepEquals ?

        // Take all enemy trenches if own side empty
        board = new int[]{0,0,0, 0,0,1, 0, 3,0,0, 0,7,0, 8};
        mState = new MangalaState(board, whitesTurn, whiteIsMax);
        childBoard= new int[]{0,0,0, 0,0,0, 11, 0,0,0, 0,0,0, 8};
        child = new MangalaState(childBoard, whitesTurn, whiteIsMax);
        assert Arrays.equals(mState.children(), new MangalaState[]{child}); // not deepEquals ?

        // Take all enemy trenches if own side empty, even if enemy ends it
        board = new int[]{2,0,0, 0,0,0, 3, 3,0,0, 7,1,0, 8};
        mState = new MangalaState(board, !whitesTurn, whiteIsMax);
        childBoard= new int[]{0,0,0, 0,0,0, 13, 0,0,0, 0,0,0, 11};
        child = new MangalaState(childBoard, whitesTurn, whiteIsMax);
        assert arrayContains(mState.children(), child);
        
    }

}

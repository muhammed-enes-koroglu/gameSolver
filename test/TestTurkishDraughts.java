import java.util.HashSet;

public class TestTurkishDraughts {
    private static final int BOARD_SIZE = TurkishDraughts.BOARD_SIZE;
    private static final int W_MAN = TurkishDraughts.W_MAN;
    private static final int B_MAN = TurkishDraughts.B_MAN;
    private static final int W_KING = TurkishDraughts.W_KING;
    private static final int B_KING = TurkishDraughts.B_KING;
    private static final int EMPTY_SQUARE = TurkishDraughts.EMPTY_SQUARE;

    private TestTurkishDraughts(){
        throw new IllegalStateException("Utility class");
    }

    public static void main(String[] args) {
        System.out.println("\nTesting TurkishDraughts");

        // testInitialization();
        TurkishDraughts.testPrivateMethods();
        // testChildren();
        
        System.out.println("\nAll TurkishDraughts tests are Successful.");
    }

    private static void testInitialization(){
        System.out.println("testInitialization..");

        boolean whitesTurn = true;
        boolean whiteIsMax = true;
        int[][] board = getInitialBoard();
        new TurkishDraughts(board, whitesTurn, whiteIsMax);

        System.out.println("testInitialization successful");
    }

    private static void testChildren(){
        System.out.println("testChildren..");

        // testChildrenBasicMoving();
        testChildrenTaking();
        testChildrenPromotion();

        testChildrenEnteringSurvivalMode();
        testChildrenGameOver();

        System.out.println("testChildren successful");
    }

    // Tests easiest case with no kings, no to be kings and no takes.
    // Adds white men in some squares. Adds each of their children to `expectedChildren`. 
    private static void testChildrenBasicMoving(){
        boolean whitesTurn = true;
        boolean maximizeForWhite = true;
        int[][] board = new int[BOARD_SIZE][BOARD_SIZE];
        int[][] childBoard;
        HashSet<TurkishDraughts> expectedChildren = new HashSet<>();
        HashSet<int[][]> expectedChildBoards = new HashSet<>();
        
        board[1][0] = W_MAN;
        board[3][3] = W_MAN;
        board[5][6] = W_MAN;

        // children of (1,0)
        childBoard = Helper.deepCopy(board);
        childBoard[1][0] = EMPTY_SQUARE;

        expectedChildBoards.add(Helper.modifiedCloneOf(childBoard, 2, 0, W_MAN));
        expectedChildBoards.add(Helper.modifiedCloneOf(childBoard, 1, 1, W_MAN));
        // children of (3,3)
        childBoard = Helper.deepCopy(board);
        childBoard[3][3] = EMPTY_SQUARE;
        expectedChildBoards.add(Helper.modifiedCloneOf(childBoard, 4, 3, W_MAN));
        expectedChildBoards.add(Helper.modifiedCloneOf(childBoard, 3, 4, W_MAN));
        expectedChildBoards.add(Helper.modifiedCloneOf(childBoard, 3, 2, W_MAN));
        // children of (5,6)
        childBoard = Helper.deepCopy(board);
        childBoard[5][6] = EMPTY_SQUARE;
        expectedChildBoards.add(Helper.modifiedCloneOf(childBoard, 6, 6, W_MAN));
        expectedChildBoards.add(Helper.modifiedCloneOf(childBoard, 5, 7, W_MAN));
        expectedChildBoards.add(Helper.modifiedCloneOf(childBoard, 5, 5, W_MAN));
        
        for(int[][] childBrd : expectedChildBoards){
            TurkishDraughts child = new TurkishDraughts(childBrd, !whitesTurn, maximizeForWhite);
            expectedChildren.add(child);
         }

        TurkishDraughts checkers = new TurkishDraughts(board, whitesTurn, maximizeForWhite);
        assert checkers.children().equals(expectedChildren);
    }
    
    // Tests taking.
    // Piece that can take MUST take. It can stop whenever it wants after taking one.
    // Player can choose with which piece to take if multiple takes are possible.
    private static void testChildrenTaking(){
        boolean whitesTurn = true;
        boolean maximizeForWhite = true;
        int[][] board = new int[BOARD_SIZE][BOARD_SIZE];
        int[][] childBoard;
        HashSet<TurkishDraughts> expectedChildren = new HashSet<>();
        HashSet<int[][]> expectedChildBoards = new HashSet<>();

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

        board[4][3] = B_MAN;

        System.out.println(new TurkishDraughts(board, whitesTurn, maximizeForWhite));

        //children of man at (1,0) going up 
        childBoard = Helper.deepCopy(board);
        childBoard[1][0] = EMPTY_SQUARE;
        childBoard[2][0] = EMPTY_SQUARE;
        expectedChildBoards.add(Helper.modifiedCloneOf(childBoard, 3, 0, W_MAN));

        childBoard[4][0] = EMPTY_SQUARE;
        expectedChildBoards.add(Helper.modifiedCloneOf(childBoard, 5, 0, W_MAN));
        
        childBoard[5][1] = EMPTY_SQUARE;
        expectedChildBoards.add(Helper.modifiedCloneOf(childBoard, 5, 2, W_MAN));

        //children of man at (1,0) going right
        childBoard = Helper.deepCopy(board);
        childBoard[1][0] = EMPTY_SQUARE;
        childBoard[1][1] = EMPTY_SQUARE;
        expectedChildBoards.add(Helper.modifiedCloneOf(childBoard, 1, 2, W_MAN));

        childBoard[1][3] = EMPTY_SQUARE;
        expectedChildBoards.add(Helper.modifiedCloneOf(childBoard, 1, 4, W_MAN));
        
        childBoard[2][4] = EMPTY_SQUARE;
        expectedChildBoards.add(Helper.modifiedCloneOf(childBoard, 3, 4, W_MAN));
        
        // child of man at (3,3)
        childBoard = Helper.deepCopy(board);
        childBoard[3][3] = EMPTY_SQUARE;
        childBoard[4][3] = EMPTY_SQUARE;
        expectedChildBoards.add(Helper.modifiedCloneOf(childBoard, 5, 3, W_MAN));

        //children of king at (5,4)
        childBoard = Helper.deepCopy(board);
        childBoard[5][4] = EMPTY_SQUARE;
        childBoard[5][1] = EMPTY_SQUARE;
        expectedChildBoards.add(Helper.modifiedCloneOf(childBoard, 5, 0, W_MAN));

        childBoard[4][0] = EMPTY_SQUARE;
        expectedChildBoards.add(Helper.modifiedCloneOf(childBoard, 3, 0, W_MAN));
        
        // children of (5,6)
        // (1,0) or (3,3) MUST take! Can't play anything else.
        
        for(int[][] childBrd : expectedChildBoards){
            TurkishDraughts child = new TurkishDraughts(childBrd, !whitesTurn, maximizeForWhite);
            expectedChildren.add(child);
        }

        TurkishDraughts checkers = new TurkishDraughts(board, whitesTurn, maximizeForWhite);
        assert checkers.children().equals(expectedChildren);
    }

    // Tests promotion when reaching the last row. Can keep taking after getting promoted.
    private static void testChildrenPromotion(){
        boolean whitesTurn = true;
        boolean maximizeForWhite = true;
        int[][] board = new int[BOARD_SIZE][BOARD_SIZE];
        int[][] childBoard;
        HashSet<TurkishDraughts> expectedChildren = new HashSet<>();
        HashSet<int[][]> expectedChildBoards= new HashSet<>();
        
        TurkishDraughts checkers;

        // Move forward to promote.
        board[6][3] = W_MAN;

        childBoard = Helper.deepCopy(board);
        childBoard[6][3] = EMPTY_SQUARE;
        childBoard[7][3] = W_KING;
        expectedChildren.add(new TurkishDraughts(childBoard, !whitesTurn, maximizeForWhite));

        checkers = new TurkishDraughts(board, whitesTurn, maximizeForWhite);
        assert checkers.children().equals(expectedChildren);

        // Takes even if promotion seems possible. Can keep taking after getting promoted.
        board[2][2] = W_MAN;
        board[3][2] = B_MAN;
        board[4][6] = B_KING;

        childBoard = Helper.deepCopy(board);
        childBoard[2][2] = EMPTY_SQUARE;
        childBoard[3][2] = EMPTY_SQUARE;
        expectedChildBoards.add(Helper.modifiedCloneOf(childBoard, 4, 2, W_KING));

        childBoard[4][6] = EMPTY_SQUARE;
        expectedChildBoards.add(Helper.modifiedCloneOf(childBoard, 4, 7, W_KING));        

        for(int[][] childBrd : expectedChildBoards){
            TurkishDraughts child = new TurkishDraughts(childBrd, !whitesTurn, maximizeForWhite);
            expectedChildren.add(child);
        }

        checkers = new TurkishDraughts(board, whitesTurn, maximizeForWhite);
        assert checkers.children().equals(expectedChildren);       

    }

    private static void testChildrenEnteringSurvivalMode(){

        // TODO
        throw new UnsupportedOperationException("Test not yet implemented.");

    }

    private static void testChildrenGameOver(){

        // TODO
        throw new UnsupportedOperationException("Test not yet implemented.");

    }

    // Helper methods
    private static int[][] getInitialBoard(){

        int[][] board = new int[8][8];
        for(int row = 0; row < TurkishDraughts.BOARD_SIZE; row++){
            if(row == 1 || row == 2)
                board[row] = new int[]{-1,-1,-1,-1, -1,-1,-1,-1};
            else if(row == 5 || row == 6)
                board[row] = new int[]{1,1,1,1, 1,1,1,1};
            else
                board[row] = new int[]{0,0,0,0, 0,0,0,0};
        }
        return board;
    }

}

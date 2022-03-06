import java.util.HashSet;

public class TestTurkishCheckers {
    private static final int BOARD_SIZE = TurkishCheckers.BOARD_SIZE;
    private static final int W_MAN = 1;
    private static final int B_MAN = -1;
    private static final int W_KING = 2;
    private static final int B_KING = -2;
    private static final int EMPTY_SQUARE = 0;

    private TestTurkishCheckers(){
        throw new IllegalStateException("Utility class");
    }

    public static void main(String[] args) {
        System.out.println("\nTesting TurkishCheckers");

        testInitialization();
        testChildren();

        System.out.println("\nAll TurkishCheckers tests are Successful.");
    }

    private static void testInitialization(){
        System.out.println("testInitialization..");

        boolean whitesTurn = true;
        boolean whiteIsMax = true;
        int[][] board = getInitialBoard();
        TurkishCheckers checkers = new TurkishCheckers(board, whitesTurn, whiteIsMax);
        // System.out.println(checkers);

        System.out.println("testInitialization successful");
    }

    private static void testChildren(){
        System.out.println("testChildren..");

        // testChildrenSimplestCase();
        testChildrenTaking();

        System.out.println("testChildren successful");
    }

    // Tests easiest case with no kings, no to be kings and no takes.
    // Adds white men in some squares. Adds each of their children to `expectedChildren`. 
    private static void testChildrenSimplestCase(){
        boolean whitesTurn = true;
        boolean maximizeForWhite = true;
        int[][] board = new int[BOARD_SIZE][BOARD_SIZE];
        int[][] childBoard;
        HashSet<TurkishCheckers> expectedChildren = new HashSet<>();
        HashSet<int[][]> expectedChildBoards = new HashSet<>();
        
        board[1][0] = W_MAN;
        board[3][3] = W_MAN;
        board[5][6] = W_MAN;

        // children of (1,0)
        childBoard = deepCopy(board);
        childBoard[1][0] = EMPTY_SQUARE;

        expectedChildBoards.add(modifiedCloneOf(childBoard, 2, 0, W_MAN));
        expectedChildBoards.add(modifiedCloneOf(childBoard, 1, 1, W_MAN));
        // children of (3,3)
        childBoard = deepCopy(board);
        childBoard[3][3] = EMPTY_SQUARE;
        expectedChildBoards.add(modifiedCloneOf(childBoard, 4, 3, W_MAN));
        expectedChildBoards.add(modifiedCloneOf(childBoard, 3, 4, W_MAN));
        expectedChildBoards.add(modifiedCloneOf(childBoard, 3, 2, W_MAN));
        // children of (5,6)
        childBoard = deepCopy(board);
        childBoard[5][6] = EMPTY_SQUARE;
        expectedChildBoards.add(modifiedCloneOf(childBoard, 6, 6, W_MAN));
        expectedChildBoards.add(modifiedCloneOf(childBoard, 5, 7, W_MAN));
        expectedChildBoards.add(modifiedCloneOf(childBoard, 5, 5, W_MAN));
        
        for(int[][] childBrd : expectedChildBoards){
            TurkishCheckers child = new TurkishCheckers(childBrd, !whitesTurn, maximizeForWhite);
            expectedChildren.add(child);
         }

        TurkishCheckers checkers = new TurkishCheckers(board, whitesTurn, maximizeForWhite);
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
        HashSet<TurkishCheckers> expectedChildren = new HashSet<>();
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

        System.out.println(new TurkishCheckers(board, whitesTurn, maximizeForWhite));

        //children of man at (1,0) going up 
        childBoard = deepCopy(board);
        childBoard[1][0] = EMPTY_SQUARE;
        childBoard[2][0] = EMPTY_SQUARE;
        expectedChildBoards.add(modifiedCloneOf(childBoard, 3, 0, W_MAN));

        childBoard[4][0] = EMPTY_SQUARE;
        expectedChildBoards.add(modifiedCloneOf(childBoard, 5, 0, W_MAN));
        
        childBoard[5][1] = EMPTY_SQUARE;
        expectedChildBoards.add(modifiedCloneOf(childBoard, 5, 2, W_MAN));

        //children of man at (1,0) going right
        childBoard = deepCopy(board);
        childBoard[1][0] = EMPTY_SQUARE;
        childBoard[1][1] = EMPTY_SQUARE;
        expectedChildBoards.add(modifiedCloneOf(childBoard, 1, 2, W_MAN));

        childBoard[1][3] = EMPTY_SQUARE;
        expectedChildBoards.add(modifiedCloneOf(childBoard, 1, 4, W_MAN));
        
        childBoard[2][4] = EMPTY_SQUARE;
        expectedChildBoards.add(modifiedCloneOf(childBoard, 3, 4, W_MAN));
        
        // child of man at (3,3)
        childBoard = deepCopy(board);
        childBoard[3][3] = EMPTY_SQUARE;
        childBoard[4][3] = EMPTY_SQUARE;
        expectedChildBoards.add(modifiedCloneOf(childBoard, 5, 3, W_MAN));

        //children of king at (5,4)
        childBoard = deepCopy(board);
        childBoard[5][4] = EMPTY_SQUARE;
        childBoard[5][1] = EMPTY_SQUARE;
        expectedChildBoards.add(modifiedCloneOf(childBoard, 5, 0, W_MAN));

        childBoard[4][0] = EMPTY_SQUARE;
        expectedChildBoards.add(modifiedCloneOf(childBoard, 3, 0, W_MAN));
        
        // children of (5,6)
        // (1,0) or (3,3) MUST take! Can't play anything else.
        
        for(int[][] childBrd : expectedChildBoards){
            TurkishCheckers child = new TurkishCheckers(childBrd, !whitesTurn, maximizeForWhite);
            expectedChildren.add(child);
        }

        TurkishCheckers checkers = new TurkishCheckers(board, whitesTurn, maximizeForWhite);
        assert checkers.children().equals(expectedChildren);
    }
    
    // Helper methods
    private static int[][] getInitialBoard(){
        // int[][] board = new int[][]]{1,1,1,1, 1,1,1,1};


        int[][] board = new int[8][8];
        for(int row = 0; row < TurkishCheckers.BOARD_SIZE; row++){
            if(row == 1 || row == 2)
                board[row] = new int[]{-1,-1,-1,-1, -1,-1,-1,-1};
            else if(row == 5 || row == 6)
                board[row] = new int[]{1,1,1,1, 1,1,1,1};
            else
                board[row] = new int[]{0,0,0,0, 0,0,0,0};
        }
        return board;
    }

    private static int[][] modifiedCloneOf(int[][] board, int row, int col, int val){
        int[][] nBoard = deepCopy(board);
        nBoard[row][col] = val;
        return nBoard; 
    }

    private static int[][] deepCopy(int[][] arr){
        int[][] newArray = new int[arr.length][arr[0].length];

        for(int row=0; row<arr.length; row++){
            newArray[row] = arr[row].clone();
        }

        return newArray;
    }

}

import java.util.HashSet;
import java.util.Set;

public abstract class TestTurkishDraughts {

    private static final int BOARD_SIZE = TurkishDraughts.BOARD_SIZE;
    private static final int W_MAN = TurkishDraughts.W_MAN;
    private static final int B_MAN = TurkishDraughts.B_MAN;
    private static final int W_KING = TurkishDraughts.W_KING;
    private static final int B_KING = TurkishDraughts.B_KING;
    private static final int EMPTY_SQUARE = TurkishDraughts.EMPTY_SQUARE;
    private static final int INITITAL_NB_WHITE_PIECES = 16;
    private static final int INITITAL_NB_BLACK_PIECES = 16;

    public static void main(String[] args) {
        System.out.println("\nTesting TurkishDraughts\n");

        testInitialization();
        // TurkishDraughts.testPrivateMethods();
        testChildren();
        
        System.out.println("\nAll TurkishDraughts tests are Successful.");
    }

    private static void testInitialization(){
        System.out.println("testInitialization..");

        boolean whitesTurn = true;
        boolean whiteIsMax = true;
        int[][] board = getInitialBoard();
        new TurkishDraughts(new Board(board), whitesTurn, whiteIsMax, INITITAL_NB_WHITE_PIECES, INITITAL_NB_BLACK_PIECES, false);

        System.out.println("testInitialization successful");
    }

    private static void testChildren(){
        System.out.println("testChildren..");

        testChildrenBasicMoving();
        testChildrenManTaking();
        testChildrenPromotion();
        testChildrenKingTaking();
        // testChildrenKingWhenPromotionPossible();
        // testChildrenEnteringSurvivalMode();
        // testChildrenGameOver();

        System.out.println("testChildren OK.");
    }

    /** Tests easiest case with no kings, no promotions and no takes.
    *   Adds white men in some squares. Adds each of their children to `expectedChildren`.  
    */
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

        expectedChildBoards.add(Helper.cloneAndSet(childBoard, 2, 0, W_MAN));
        expectedChildBoards.add(Helper.cloneAndSet(childBoard, 1, 1, W_MAN));
        
        // children of (3,3)
        childBoard = Helper.deepCopy(board);
        childBoard[3][3] = EMPTY_SQUARE;

        expectedChildBoards.add(Helper.cloneAndSet(childBoard, 4, 3, W_MAN));
        expectedChildBoards.add(Helper.cloneAndSet(childBoard, 3, 4, W_MAN));
        expectedChildBoards.add(Helper.cloneAndSet(childBoard, 3, 2, W_MAN));
        
        // children of (5,6)
        childBoard = Helper.deepCopy(board);
        childBoard[5][6] = EMPTY_SQUARE;
        
        expectedChildBoards.add(Helper.cloneAndSet(childBoard, 6, 6, W_MAN));
        expectedChildBoards.add(Helper.cloneAndSet(childBoard, 5, 7, W_MAN));
        expectedChildBoards.add(Helper.cloneAndSet(childBoard, 5, 5, W_MAN));
        
        for(int[][] childBrd : expectedChildBoards){
            TurkishDraughts child = new TurkishDraughts(new Board(childBrd), !whitesTurn, maximizeForWhite, INITITAL_NB_WHITE_PIECES, INITITAL_NB_BLACK_PIECES, false);
            expectedChildren.add(child);
         }

         TurkishDraughts checkers = new TurkishDraughts(new Board(board), whitesTurn, maximizeForWhite, INITITAL_NB_WHITE_PIECES, INITITAL_NB_BLACK_PIECES, false);
        assert checkers.children().equals(expectedChildren);
    }
    
    // Tests taking.
    // Piece that can take MUST take. It can stop whenever it wants after taking one.
    // Player can choose with which piece to take if multiple takes are possible.
    private static void testChildrenManTaking(){
        boolean whitesTurn = true;
        boolean maximizeForWhite = true;
        int[][] board = new int[BOARD_SIZE][BOARD_SIZE];
        int[][] childBoard;
        HashSet<TurkishDraughts> expectedChildren = new HashSet<>();

        board[1][0] = W_MAN;
        board[3][3] = W_MAN;
        board[5][6] = W_MAN;

        board[2][0] = B_MAN;
        board[4][0] = B_KING;
        board[5][1] = B_MAN;

        board[1][1] = B_MAN;
        board[1][3] = B_MAN;
        board[2][4] = B_KING;

        board[4][3] = B_MAN;

        int initialWhitePieces = 3;
        int initialBlackPieces = 7;

        TurkishDraughts child;
        int[][] childBrd;

        //children of man at (1,0) going up 
        childBoard = Helper.deepCopy(board);
        childBoard[1][0] = EMPTY_SQUARE;

        // #1
        childBoard[2][0] = EMPTY_SQUARE;
        childBrd = Helper.cloneAndSet(childBoard, 3, 0, W_MAN);

        child = new TurkishDraughts(new Board(childBrd), !whitesTurn, maximizeForWhite, initialWhitePieces, initialBlackPieces-1, true);
        expectedChildren.add(child);
        
        // #2
        childBoard[4][0] = EMPTY_SQUARE;
        childBrd = Helper.cloneAndSet(childBoard, 5, 0, W_MAN);
        
        child = new TurkishDraughts(new Board(childBrd), !whitesTurn, maximizeForWhite, initialWhitePieces, initialBlackPieces-2, true);
        expectedChildren.add(child);

        // #3
        childBoard[5][1] = EMPTY_SQUARE;
        childBrd = Helper.cloneAndSet(childBoard, 5, 2, W_MAN);

        child = new TurkishDraughts(new Board(childBrd), !whitesTurn, maximizeForWhite, initialWhitePieces, initialBlackPieces-3, true);
        expectedChildren.add(child);
        

        //children of man at (1,0) going right
        childBoard = Helper.deepCopy(board);
        childBoard[1][0] = EMPTY_SQUARE;

        // #1
        childBoard[1][1] = EMPTY_SQUARE;
        childBrd = Helper.cloneAndSet(childBoard, 1, 2, W_MAN);

        child = new TurkishDraughts(new Board(childBrd), !whitesTurn, maximizeForWhite, initialWhitePieces, initialBlackPieces-1, true);
        expectedChildren.add(child);

        // #2
        childBoard[1][3] = EMPTY_SQUARE;
        childBrd = Helper.cloneAndSet(childBoard, 1, 4, W_MAN);
        
        child = new TurkishDraughts(new Board(childBrd), !whitesTurn, maximizeForWhite, initialWhitePieces, initialBlackPieces-2, true);
        expectedChildren.add(child);

        // #3
        childBoard[2][4] = EMPTY_SQUARE;
        childBrd = Helper.cloneAndSet(childBoard, 3, 4, W_MAN);
        
        child = new TurkishDraughts(new Board(childBrd), !whitesTurn, maximizeForWhite, initialWhitePieces, initialBlackPieces-3, true);
        expectedChildren.add(child);

        // child of man at (3,3)
        childBoard = Helper.deepCopy(board);
        childBoard[3][3] = EMPTY_SQUARE;
        childBoard[4][3] = EMPTY_SQUARE;
        childBrd = Helper.cloneAndSet(childBoard, 5, 3, W_MAN);

        child = new TurkishDraughts(new Board(childBrd), !whitesTurn, maximizeForWhite, initialWhitePieces, initialBlackPieces-1, true);
        expectedChildren.add(child);

        // children of (5,6)
        // (1,0) or (3,3) MUST take! Can't play anything else.
        
        TurkishDraughts checkers = new TurkishDraughts(new Board(board), whitesTurn, maximizeForWhite, initialWhitePieces, initialBlackPieces, false);
        assert checkers.children().equals(expectedChildren);
    }

    // Tests promotion when reaching the last row. Can keep taking after getting promoted.
    private static void testChildrenPromotion(){
        boolean whitesTurn = true;
        boolean maximizeForWhite = true;
        int[][] board = new int[BOARD_SIZE][BOARD_SIZE];
        int[][] childBoard;
        
        TurkishDraughts checkers;

        // Move forward to promote.
        board[6][3] = W_MAN;

        childBoard = Helper.deepCopy(board);
        childBoard[6][3] = EMPTY_SQUARE;
        childBoard[7][3] = W_KING;
        TurkishDraughts child = new TurkishDraughts(new Board(childBoard), !whitesTurn, maximizeForWhite, INITITAL_NB_WHITE_PIECES, INITITAL_NB_BLACK_PIECES, false);

        checkers = new TurkishDraughts(new Board(board), whitesTurn, maximizeForWhite, INITITAL_NB_WHITE_PIECES, INITITAL_NB_BLACK_PIECES, false);

        assert checkers.children().contains(child);
    }

    // Tests taking.
    // Piece that can take MUST take. It can stop whenever it wants after taking one.
    // Player can choose with which piece to take if multiple takes are possible.
    private static void testChildrenKingTaking(){
        boolean whitesTurn = true;
        boolean maximizeForWhite = true;
        int[][] board = new int[BOARD_SIZE][BOARD_SIZE];
        int[][] childBoard;
        HashSet<TurkishDraughts> expectedChildren = new HashSet<>();

        board[1][0] = W_KING;
        board[3][3] = W_MAN;
        board[5][6] = W_MAN;
        board[6][4] = W_KING;

        board[2][0] = B_MAN;
        board[4][0] = B_KING;
        board[5][1] = B_MAN;

        board[1][3] = B_MAN;
        board[4][5] = B_KING;

        board[4][3] = B_MAN;

        int initialWhitePieces = 4;
        int initialBlackPieces = 6;

        TurkishDraughts child;
        int[][] childBrd;

        TurkishDraughts checkers = new TurkishDraughts(new Board(board), whitesTurn, maximizeForWhite, INITITAL_NB_WHITE_PIECES, INITITAL_NB_BLACK_PIECES, false);
        System.out.println(checkers);

        //children of king at (1,0) going up.
        childBoard = Helper.deepCopy(board);
        childBoard[1][0] = EMPTY_SQUARE;

        // Take #1
        childBoard[2][0] = EMPTY_SQUARE;
        childBrd = Helper.cloneAndSet(childBoard, 3, 0, W_KING);

        child = new TurkishDraughts(new Board(childBrd), !whitesTurn, maximizeForWhite, initialWhitePieces, initialBlackPieces-1, true);
        expectedChildren.add(child);
        

        // Take #2
        childBoard[4][0] = EMPTY_SQUARE;

        childBrd = Helper.cloneAndSet(childBoard, 5, 0, W_KING);
        child = new TurkishDraughts(new Board(childBrd), !whitesTurn, maximizeForWhite, initialWhitePieces, initialBlackPieces-2, true);
        expectedChildren.add(child);

        childBrd = Helper.cloneAndSet(childBoard, 6, 0, W_KING);
        child = new TurkishDraughts(new Board(childBrd), !whitesTurn, maximizeForWhite, initialWhitePieces, initialBlackPieces-2, true);
        expectedChildren.add(child);

        childBrd = Helper.cloneAndSet(childBoard, 7, 0, W_KING);
        child = new TurkishDraughts(new Board(childBrd), !whitesTurn, maximizeForWhite, initialWhitePieces, initialBlackPieces-2, true);
        expectedChildren.add(child);

        // Take #3
        childBoard[5][1] = EMPTY_SQUARE;
        
        childBrd = Helper.cloneAndSet(childBoard, 5, 2, W_KING);
        child = new TurkishDraughts(new Board(childBrd), !whitesTurn, maximizeForWhite, initialWhitePieces, initialBlackPieces-3, true);
        expectedChildren.add(child);

        childBrd = Helper.cloneAndSet(childBoard, 5, 3, W_KING);
        child = new TurkishDraughts(new Board(childBrd), !whitesTurn, maximizeForWhite, initialWhitePieces, initialBlackPieces-3, true);
        expectedChildren.add(child);

        childBrd = Helper.cloneAndSet(childBoard, 5, 4, W_KING);
        child = new TurkishDraughts(new Board(childBrd), !whitesTurn, maximizeForWhite, initialWhitePieces, initialBlackPieces-3, true);
        expectedChildren.add(child);

        childBrd = Helper.cloneAndSet(childBoard, 5, 5, W_KING);
        child = new TurkishDraughts(new Board(childBrd), !whitesTurn, maximizeForWhite, initialWhitePieces, initialBlackPieces-3, true);
        expectedChildren.add(child);


        //children of king at (1,0) going right
        childBoard = Helper.deepCopy(board);
        childBoard[1][0] = EMPTY_SQUARE;
        
        // Take #1
        childBoard[1][3] = EMPTY_SQUARE;
        
        childBrd = Helper.cloneAndSet(childBoard, 1, 4, W_KING);
        child = new TurkishDraughts(new Board(childBrd), !whitesTurn, maximizeForWhite, initialWhitePieces, initialBlackPieces-1, true);
        expectedChildren.add(child);

        childBrd = Helper.cloneAndSet(childBoard, 1, 5, W_KING);
        child = new TurkishDraughts(new Board(childBrd), !whitesTurn, maximizeForWhite, initialWhitePieces, initialBlackPieces-1, true);
        expectedChildren.add(child);

        childBrd = Helper.cloneAndSet(childBoard, 1, 6, W_KING);
        child = new TurkishDraughts(new Board(childBrd), !whitesTurn, maximizeForWhite, initialWhitePieces, initialBlackPieces-1, true);
        expectedChildren.add(child);

        childBrd = Helper.cloneAndSet(childBoard, 1, 7, W_KING);
        child = new TurkishDraughts(new Board(childBrd), !whitesTurn, maximizeForWhite, initialWhitePieces, initialBlackPieces-1, true);
        expectedChildren.add(child);

        // Take #2
        childBoard[4][5] = EMPTY_SQUARE;
        
        childBrd = Helper.cloneAndSet(childBoard, 5, 5, W_KING);
        child = new TurkishDraughts(new Board(childBrd), !whitesTurn, maximizeForWhite, initialWhitePieces, initialBlackPieces-2, true);
        expectedChildren.add(child);

        childBrd = Helper.cloneAndSet(childBoard, 6, 5, W_KING);
        child = new TurkishDraughts(new Board(childBrd), !whitesTurn, maximizeForWhite, initialWhitePieces, initialBlackPieces-2, true);
        expectedChildren.add(child);

        childBrd = Helper.cloneAndSet(childBoard, 7, 5, W_KING);
        child = new TurkishDraughts(new Board(childBrd), !whitesTurn, maximizeForWhite, initialWhitePieces, initialBlackPieces-2, true);
        expectedChildren.add(child);
        
        // child of man at (3,3)
        childBoard = Helper.deepCopy(board);
        childBoard[3][3] = EMPTY_SQUARE;
        childBoard[4][3] = EMPTY_SQUARE;
        childBrd = Helper.cloneAndSet(childBoard, 5, 3, W_MAN);
        child = new TurkishDraughts(new Board(childBrd), !whitesTurn, maximizeForWhite, initialWhitePieces, initialBlackPieces-1, true);
        expectedChildren.add(child);


        // children of king at (5,4)
        // (1,0) or (3,3) MUST take! Can't play anything else.
        
        // children of man at (5,6)
        // (1,0) or (3,3) MUST take! Can't play anything else.

        System.out.println("###########\n###########");
        System.out.println("Expected Children: " + expectedChildren.size());
        System.out.println(expectedChildren);
        System.out.println("###########\n###########");

        Set<TurkishDraughts> children = checkers.children();
        System.out.println("\nCalculated Children: " + children.size());
        // System.out.println(children);

        // System.out.println("###########\n###########");
        // children.removeAll(expectedChildren);
        // System.out.println(children);

        assert children.equals(expectedChildren);
    }

    // Piece should take even if a promotion to being a king is possible.
    private static void testChildrenKingWhenPromotionPossible(){
        boolean whitesTurn = true;
        boolean maximizeForWhite = true;
        int[][] boardMatrix = new int[BOARD_SIZE][BOARD_SIZE];
        int[][] childBoardMatrix;
        HashSet<TurkishDraughts> expectedChildren = new HashSet<>();
        HashSet<int[][]> expectedChildBoards= new HashSet<>();
        
        TurkishDraughts checkers;

        // Takes even if promotion seems possible. Can keep taking after getting promoted.
        boardMatrix[2][2] = W_KING;
        boardMatrix[3][2] = B_MAN;
        boardMatrix[4][6] = B_KING;

        // Men added so survival mode isn't activated. 
        boardMatrix[1][0] = W_MAN;
        boardMatrix[2][0] = W_MAN;
        boardMatrix[3][0] = B_MAN;
        boardMatrix[4][0] = B_MAN;
        boardMatrix[5][0] = B_MAN;
        boardMatrix[6][0] = B_MAN;
        

        childBoardMatrix = Helper.deepCopy(boardMatrix);
        childBoardMatrix[2][2] = EMPTY_SQUARE;
        childBoardMatrix[3][2] = EMPTY_SQUARE;
        expectedChildBoards.add(Helper.cloneAndSet(childBoardMatrix, 4, 2, W_KING));

        childBoardMatrix[4][6] = EMPTY_SQUARE;
        expectedChildBoards.add(Helper.cloneAndSet(childBoardMatrix, 4, 7, W_KING));        
        TurkishDraughts child;

        for(int[][] childBrdMatrix : expectedChildBoards){
            child = new TurkishDraughts(new Board(childBrdMatrix), !whitesTurn, maximizeForWhite, INITITAL_NB_WHITE_PIECES, INITITAL_NB_BLACK_PIECES, false);
            expectedChildren.add(child);
        }

        checkers = new TurkishDraughts(new Board(boardMatrix), whitesTurn, maximizeForWhite, INITITAL_NB_WHITE_PIECES, INITITAL_NB_BLACK_PIECES, false);
        System.out.println(checkers);
        System.out.println("###########\n###########");
        System.out.println("Expected Children: " + expectedChildren.size());
        System.out.println(expectedChildren);
        System.out.println("###########\n###########");
        System.out.println("\nCalculated Children: " + checkers.children().size());
        System.out.println(checkers.children());

        assert checkers.children().equals(expectedChildren);       


    }

    // Player should enter Survival Mode after getting down to 3 pieces.
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

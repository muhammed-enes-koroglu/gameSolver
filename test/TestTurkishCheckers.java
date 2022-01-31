public class TestTurkishCheckers {
 
    private TestTurkishCheckers(){
        throw new IllegalStateException("Utility class");
    }

    public static void main(String[] args) {
        System.out.println("\nTesting TurkishCheckers");

        // MangalaState.testPrivateMethods();
        testInitialization();
        // testChildren();

        System.out.println("\nAll TurkishCheckers tests are Successful.");
    }

    private static void testInitialization(){
        boolean whitesTurn = true;
        boolean whiteIsMax = true;
        int[][] board = initialBoard();
        TurkishCheckers checkers = new TurkishCheckers(board, whitesTurn, whiteIsMax);
        System.out.println(checkers);

    }
    private static void testChildren(){
        boolean whitesTurn = true;
        boolean whiteIsMax = true;
        int[][] board = initialBoard();
        TurkishCheckers checkers = new TurkishCheckers(board, whitesTurn, whiteIsMax);
    }
    
    private static int[][] initialBoard(){
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


}

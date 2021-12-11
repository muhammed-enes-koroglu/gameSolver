import java.util.Arrays;

public class TestMangalaState {
    
    public static void testMangalaState(){
        System.out.println("\nTesting MangalaState");

        MangalaState.testPrivateMethods();
        testChildren();

        System.out.println("\nAll MangalaState tests are Successful.");
    }
    
    private static void testChildren(){
        /* Rules:
            -Play again if ended at own treasury.
            -Put stone to your treasury on pass, but not to enemy's treasury.
            -Take own and opposite trench if ended at own empty trench.
            -Take enemy trench if ended at enemy trench and made it even.
            -Take enemy's all trenches if your side empty.
        */
        // One simple move
        int[] board = new int[]{1,0,0, 0,0,0, 0, 1,0,0, 0,0,0, 0};
        boolean whitesTurn = true;
        boolean whiteIsMax = true;
        MangalaState mState = new MangalaState(board, whitesTurn, whiteIsMax);
        int[] childBoard = new int[]{0,1,0, 0,0,0, 0, 1,0,0, 0,0,0, 0};
        MangalaState child = new MangalaState(childBoard, !whitesTurn, whiteIsMax); // lso tests if turn changed.
        System.out.println("Original: " + mState.toString());
        System.out.println("\nChildren: " + Arrays.toString(mState.children()));
        System.out.println("\nExpected: " + Arrays.toString(new MangalaState[]{child}));
        assert Arrays.equals(mState.children(), new MangalaState[]{child}); // not deepEquals ?

        // Doesn't matter who we want to maximize
        MangalaState m2 = new MangalaState(board, whitesTurn, !whiteIsMax);
        assert Arrays.equals(mState.children(), m2.children());

        // Play again if ended at treasury
        board = new int[]{0,0,0, 0,0,1, 0, 1,0,0, 0,0,0, 0};
        mState = new MangalaState(board, whitesTurn, whiteIsMax);
        childBoard = new int[]{0,0,0, 0,0,0, 1, 1,0,0, 0,0,0, 0};
        child = new MangalaState(childBoard, whitesTurn, whiteIsMax); // AGAIN whitesTurn
        assert Arrays.equals(mState.children(), new MangalaState[]{child}); // not deepEquals ?
            // play further trench
        board = new int[]{0,0,0, 4,0,0, 0, 0,0,0, 0,0,0, 1}; // JUST TRYING!! REMOVE THIS!!
        // board = new int[]{0,0,0, 4,0,0, 0, 0,0,0, 0,0,1, 0};
        mState = new MangalaState(board, whitesTurn, whiteIsMax);
        childBoard = new int[]{0,0,0, 1,1,1, 1, 0,0,0, 0,0,0, 1}; // JUST TRYING!! REMOVE THIS!!
        // childBoard = new int[]{0,0,0, 1,1,1, 1, 0,0,0, 0,0,1, 0};
        child = new MangalaState(childBoard, whitesTurn, whiteIsMax); // AGAIN whitesTurn
        assert Arrays.equals(mState.children(), new MangalaState[]{child}); // not deepEquals ?

        // Doesn't put stone to opponent's treasury on pass
        board = new int[]{0,0,0, 0,0,10, 0, 0,0,0, 0,0,0, 0};
        mState = new MangalaState(board, whitesTurn, whiteIsMax);
        childBoard = new int[]{1,1,0, 0,0,1, 1, 1,1,1, 1,1,1, 0};
        child = new MangalaState(childBoard, whitesTurn, whiteIsMax);
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
        assert Helper.arrayContains(mState.children(), child);
    }

}

package games.mangala;

import util.Board;
import util.Helper;
import static util.Helper.assrt;

public class TestMangala {
    
    private TestMangala(){
        throw new IllegalStateException("Utility class");
    }

    public static void testMangalaState(){
        System.out.println("[TESTING] Mangala");

        MangalaState.testPrivateMethods();
        testChildren();

        System.out.println("[TESTING] Mangala - DONE\n");
    }
    
    private static void testChildren(){
        /* Possibly make array comparisons into set comparisons.
        Rules:
            -Play again if ended at own treasury.
            -Put stone to your treasury on pass, but not to enemy's treasury.
            -Take own and opposite trench if ended at own empty trench.
            -Take enemy trench if ended at enemy trench and made it even.
            -Take enemy's all trenches if your side empty.
        */

        // One simple move
        MangalaState child2;
        Board board = new Board( new int[]{1,0,0, 0,0,0, 0, 1,0,0, 0,0,0, 0});
        boolean whitesTurn = true;
        boolean whiteIsMax = true;
        MangalaState mState = new MangalaState(board, whitesTurn, whiteIsMax);
        Board childBoard = new Board( new int[]{0,1,0, 0,0,0, 0, 1,0,0, 0,0,0, 0});
        MangalaState child = new MangalaState(childBoard, !whitesTurn, whiteIsMax); // lso tests if turn changed.
        assrt(mState.children().equals(Helper.newHashSet(child))); // not deepEquals ?

        // Doesn't matter who we want to maximize
        MangalaState m2 = new MangalaState(board, whitesTurn, !whiteIsMax);
        assrt(mState.children().equals(m2.children()));

        // Play again if ended at treasury
        board = new Board( new int[]{1,0,0, 0,0,1, 0, 1,0,0, 0,0,0, 0});
        mState = new MangalaState(board, whitesTurn, whiteIsMax);
        childBoard = new Board( new int[]{1,0,0, 0,0,0, 1, 1,0,0, 0,0,0, 0});
        child = new MangalaState(childBoard, whitesTurn, whiteIsMax); // AGAIN whitesTurn
        childBoard = new Board( new int[]{0,1,0, 0,0,1, 0, 1,0,0, 0,0,0, 0});
        child2 = new MangalaState(childBoard, !whitesTurn, whiteIsMax); // AGAIN whitesTurn
        assrt(mState.children().equals(Helper.newHashSet(child2, child))); // not deepEquals ?
        
        // play further trench
        board = new Board( new int[]{0,0,0, 4,0,0, 0, 0,0,0, 0,0,1, 0});
        mState = new MangalaState(board, whitesTurn, whiteIsMax);
        childBoard = new Board( new int[]{0,0,0, 1,1,1, 1, 0,0,0, 0,0,1, 0});
        child = new MangalaState(childBoard, whitesTurn, whiteIsMax); // AGAIN whitesTurn
        assrt(mState.children().equals(Helper.newHashSet(child))); // not deepEquals ?

        // Doesn't put stone to opponent's treasury on pass
        board = new Board( new int[]{0,0,0, 0,0,10, 0, 1,0,0, 0,0,0, 0});
        mState = new MangalaState(board, whitesTurn, whiteIsMax);
        childBoard = new Board( new int[]{1,0,0, 0,0,1, 3, 2,1,1, 1,0,1, 0});
        child = new MangalaState(childBoard, !whitesTurn, whiteIsMax);
        assrt(mState.children().equals(Helper.newHashSet(child))); // not deepEquals ?

        // Takes opposite trench if ended at empty at own side
        board = new Board( new int[]{1,0,0, 0,0,0, 0, 0,0,0, 0,7,0, 0});
        mState = new MangalaState(board, whitesTurn, whiteIsMax);
        childBoard = new Board( new int[]{0,0,0, 0,0,0, 8, 0,0,0, 0,0,0, 0});
        child = new MangalaState(childBoard, !whitesTurn, whiteIsMax);
        assrt(mState.children().equals(Helper.newHashSet(child))); // not deepEquals ?

        // Doesn't take opposite trench if ended at empty at other side
        board = new Board( new int[]{0,0,0, 0,7,4, 0, 1,0,0, 0,0,0, 0});
        mState = new MangalaState(board, whitesTurn, whiteIsMax);
        childBoard = new Board( new int[]{0,0,0, 0,7,1, 1, 2,1,0, 0,0,0, 0});
        child = new MangalaState(childBoard, !whitesTurn, whiteIsMax); // possibility 1
        childBoard = new Board( new int[]{0,0,0, 0,1,5, 1, 2,1,1, 1,0,0, 0});
        child2 = new MangalaState(childBoard, !whitesTurn, whiteIsMax); // possibility 2
        assrt(mState.children().equals(Helper.newHashSet(child2, child))); // not deepEquals ?

        // Takes enemy's trench if ended there and made it even
        board = new Board( new int[]{0,0,0, 0,4,0, 0, 3,0,0, 0,7,0, 0});
        mState = new MangalaState(board, whitesTurn, whiteIsMax);
        childBoard = new Board( new int[]{0,0,0, 0,1,1, 5, 0,0,0, 0,7,0, 0});
        child = new MangalaState(childBoard, !whitesTurn, whiteIsMax);
        assrt(mState.children().equals(Helper.newHashSet(child))); // not deepEquals ?

        // Doesn't take own trench if ended there and made it even
        board = new Board( new int[]{0,0,3, 0,1,0, 0, 0,0,0, 1,0,0, 0});
        mState = new MangalaState(board, whitesTurn, whiteIsMax);
        childBoard = new Board( new int[]{0,0,1, 1,2,0, 0, 0,0,0, 1,0,0, 0});
        child = new MangalaState(childBoard, !whitesTurn, whiteIsMax); // possibility 1
        childBoard = new Board( new int[]{0,0,3, 0,0,1, 0, 0,0,0, 1,0,0, 0});
        child2 = new MangalaState(childBoard, !whitesTurn, whiteIsMax); // possibility 2
        assrt(mState.children().equals(Helper.newHashSet(child, child2))); // not deepEquals ?

        // Take all enemy trenches if own side empty
        board = new Board( new int[]{0,0,0, 0,0,1, 0, 3,0,0, 0,7,0, 8});
        mState = new MangalaState(board, whitesTurn, whiteIsMax);
        childBoard= new Board( new int[]{0,0,0, 0,0,0, 11, 0,0,0, 0,0,0, 8});
        child = new MangalaState(childBoard, whitesTurn, whiteIsMax);
        assrt(mState.children().equals(Helper.newHashSet(child))); // not deepEquals ?

        // Take all enemy trenches if own side empty, even if enemy ends it
        board = new Board( new int[]{3,0,0, 0,0,0, 3, 3,0,0, 5,1,0, 8});
        mState = new MangalaState(board, !whitesTurn, whiteIsMax);
        childBoard= new Board( new int[]{0,0,0, 0,0,0, 10, 0,0,0, 0,0,0, 13});
        child = new MangalaState(childBoard, whitesTurn, whiteIsMax);
        assrt(mState.children().contains(child));

        System.out.println("    Children: OK");
    }

}

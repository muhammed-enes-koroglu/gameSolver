import java.util.HashSet;
import java.util.Set;
import java.util.Collections;

public abstract class Helper {

    private Helper(){
        throw new IllegalStateException("Utility class");
    }
    
    public static <T> boolean arrayContains(T[] arr, T value){
        for(T elm: arr)
            if(elm.equals(value))
                return true;
        return false;
    }

    public static int countElement(int[] arr, int elm){
        int count = 0;
        for(int e: arr)
            if(e == elm)
                count++;
        return count;
    }

    @SafeVarargs
    public static final <T> Set<T> newHashSet(T... objs) {
        Set<T> set = new HashSet<>();
        Collections.addAll(set, objs);
        return set;
    }

    /** Deep copies board, modifies the specified square in it and returns it. */
    public static int[][] modifiedCloneOf(int[][] board, int row, int col, int val){
        // Corrects for overshoots.
        if(col<0) {
            col = 0;
            System.out.println("Col reset!");
        } else
        if(col>7) {
            col = 7;
            System.out.println("Col reset!");
        }

        int[][] nBoard = deepCopy(board);
        nBoard[row][col] = val;
        return nBoard; 
    }

    public static int[][] deepCopy(int[][] arr){
        int[][] newArray = new int[arr.length][arr[0].length];

        for(int row=0; row<arr.length; row++){
            newArray[row] = arr[row].clone();
        }

        return newArray;
    }

}

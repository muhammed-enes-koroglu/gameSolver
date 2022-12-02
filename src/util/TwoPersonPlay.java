package util;

import java.util.Scanner;

public interface TwoPersonPlay <C>{
    public static final Scanner sc = new Scanner(System.in);

    public boolean isGameOver(C state);

    public C makeMove(C state, int[] action);
    
    public C getInitialState(boolean maximizeForWhite);

    /**
     * 
     * @param state
     * @return null if input is empty.
     */
    public int[] scanMoveNumber(C state);
    
    /** Returns winner's name. Assumes game is over. */
    public String getWinnersName(C state);
}

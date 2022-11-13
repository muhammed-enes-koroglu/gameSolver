package util;

import java.util.Set;

public interface TwoPersonGameState<S>{
    public static final float MAX_SCORE = Integer.MAX_VALUE;
    public static final float MIN_SCORE = -MAX_SCORE;

    /** @return A set of all possible child states that follow from this one. */
    public Set<S> children();

    /** @return A number indicating how close the maxPlayer is to winning. 
     * The higher the number, the closer maxPlayer in this state is to winning. 
     * Returns Integer.MAX_VALUE if the maxPlayer has won.
     * Returns -Integer.MAX_VALUE if the minPlayer has won.
     * !! NOT Integer.MIN_VALUE !! */
    public float score();

    /** @return Whether it is the turn of the player for whom we try to maximize. */
    public boolean isMaxPlayersTurn();

    /** @return Whether the given object is equal to this game state. */
    public boolean equals(Object o);

    /** @return A printable form of the game state. */
    public String toString();

    /** @return The board that represents this game. */
    public Board getBoard();
}

package util;

import java.util.Set;

public interface TwoPersonGameState<C>{

    /** @return A set of all possible child states that follow from this one. */
    public Set<C> children();

    /** @return A number indicating how close the maxPlayer is to winning. 
     * The higher the number, the closer this state is to winning. 
     * Returns Integer.MAX_VALUE if the maxPlayer has won.
     * Returns -Integer.MAX_VALUE if the minPlayer has won.
     * (!!NOT Integer.MIN_VALUE) */
    public float score();

    /** @return Whether it is the of the player for whom we try to maximize. */
    public boolean isMaxPlayersTurn();

    /** @return Whether given object is equal to this game state. */
    public boolean equals(Object o);

    /** @return A printable form of the game state. */
    public String toString();

    public Object getBoard();
}

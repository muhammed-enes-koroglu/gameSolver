import java.util.Set;

public interface TwoPersonGameState<C>{

    /** @return A set of all possible child states that follow from this one. */
    public Set<C> children();

    /** @return A number indicating how close this state is to winning. The higher the number, the closer this state is to winning. */
    public float score();

    /** @return Whether it is the of the player for whom we try to maximize. */
    public boolean isMaxPlayersTurn();

    /** @return Whether given object is equal to this game state. */
    public boolean equals(Object o);

    /** @return A printable form of the game state. */
    public String toString();
}

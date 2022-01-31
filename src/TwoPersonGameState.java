import java.util.Set;

public interface TwoPersonGameState<C>{
    public Set<C> children();
    public float score();
    public boolean isMaxPlayersTurn();
    public boolean equals(Object o);
    public String toString();
}

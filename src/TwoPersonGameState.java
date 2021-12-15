public interface TwoPersonGameState<C>{
    public C[] children();
    public float score();
    public boolean isMaxPlayersTurn();
    public boolean equals(Object o);
    public String toString();
}

public interface TwoPersonGameState{
    public TwoPersonGameState[] children();
    public float score();
    public boolean isMaxPlayersTurn();
    public boolean equals(Object o);
}

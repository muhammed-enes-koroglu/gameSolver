public interface TwoPersonGameState{
    public TwoPersonGameState[] children();
    public float score();
    public boolean isMaxPlayer();
    public boolean equals(Object o);
}

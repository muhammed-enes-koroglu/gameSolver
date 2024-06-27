package interfaces;

import java.util.Set;

import util.Board;
import util.SigmoidScaler;

public interface TwoPersonGameState<S>{
    public static final float MAX_SCORE = (float) 1E3;
    public static final float MIN_SCORE = -MAX_SCORE;
    public static final SigmoidScaler sigmoidScaler = new SigmoidScaler(0, 1, 10f, 1.0f);


    /** @return A set of all possible child states that follow from this one. */
    public Set<S> children();

    /** Checks if the game is over.
     * 
     * @return true if the game is over, false otherwise.
     */
    public boolean isGameOver();

    /** @return A number indicating how close the maxPlayer is to winning. 
     * The higher the number, the closer maxPlayer in this state is to winning. 
     * Returns MAX_SCORE if the maxPlayer has won.
     * Returns MIN_SCORE if the minPlayer has won.
     * 
     * The score should get progressively better
     * (or stay the same) for each
     * progressive move towards a win.
     * */
    public float score();

    public default float heuristic(){
        float score = this.score();
        float h = 1 - (score - MIN_SCORE) / (MAX_SCORE - MIN_SCORE);
        float scaled = sigmoidScaler.scale(h);
        return scaled * getAvgGameLength();
    }

    /** Returns the average length of the game.
     *
     * @return the average length of the game
     */
    public float getAvgGameLength();

    /** @return Whether it is the turn of the player for whom we try to maximize. */
    public boolean isMaxPlayersTurn();

    /** @return Whether the given object is equal to this game state. */
    public boolean equals(Object o);

    /** @return A printable form of the game state. */
    public String toString(String backgroundColor);

    /** @return The board that represents this game. */
    public Board getBoard();
}

package util;

import java.util.Scanner;

/**
 * The TwoPersonPlay interface represents a two-person game.
 * It defines methods for checking if the game is over, making a move, getting the initial state,
 * scanning the move number, and getting the winner's name.
 *
 * @param <C> the type of the game state
 */
public interface TwoPersonPlay<C> {
    public static final Scanner sc = new Scanner(System.in);

    /**
     * Checks if the game is over.
     *
     * @param state the current game state
     * @return true if the game is over, false otherwise
     */
    public boolean isGameOver(C state);

    /**
     * Makes a move in the game.
     *
     * @param state  the current game state
     * @param action the action to be performed
     * @return the new game state after the move is made
     */
    public C makeMove(C state, int[] action);

    /**
     * Gets the initial game state.
     *
     * @param maximizeForWhite true if the game is to be maximized for white player, false otherwise
     * @return the initial game state
     */
    public C getInitialState(boolean maximizeForWhite);

    /**
     * Scans the move number from the user.
     *
     * @param state the current game state
     * @return the move number as an array of integers, or null if the input is empty
     */
    public int[] scanMoveNumber(C state);

    /**
     * Gets the winner's name.
     * Assumes that the game is over.
     *
     * @param state the current game state
     * @return the winner's name
     */
    public String getWinnersName(C state);
}
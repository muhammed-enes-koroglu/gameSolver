package test;

import static util.ConsoleColors.pprint;

import java.util.List;

import games.mangala.MangalaState;
import games.mangala.PlayMangala;
import games.reversi.PlayReversi;
import games.reversi.ReversiState;
import games.tictactoe.PlayTicTacToe;
import games.tictactoe.TicTacToeState;
import interfaces.TwoPersonGameState;
import interfaces.TwoPersonPlay;
import search_algorithms.AStarSearch;
import search_algorithms.MiniMaxSearch;
import util.ConsoleColors;

public class AIShowdown {

    private AIShowdown() {
        // Private constructor to hide the implicit public one
    }

    public static void main(String[] args) {
        tournament(10);
    }

    public static void tournament(int nbGames) {
        pprint(ConsoleColors.GREEN, "### AI Showdown ###");
        final float minSearchTime = 10.0f;
        // Simulate a game between Minimax and AStarMinimax
        PlayMangala game = new PlayMangala();
        for (int i = 0; i < nbGames; i++) {
            MangalaState finalState = playGame(game.getInitialState(true), minSearchTime, true);
            System.out.println("Final state of the game: " + finalState);
            System.out.println("Winner: " + game.getWinnersName(finalState));
            System.out.println("###");
            }

        pprint(ConsoleColors.GREEN, "### AI Showdown - DONE ###");
    }

    /** Simulates a game where two AI strategies take turns playing from a given state.
     * @param initialState The initial state of the game.
     * @param minSearchTime The minimum time each strategy should consider its move.
     * @param isAStarMax Boolean flag to determine if AStarMinimax is the max player.
     * @return The winning state of the game.
     */
    public static <S extends TwoPersonGameState<S>> S playGame(S initialState, float minSearchTime, boolean isAStarMax) {

        S currentState = initialState;
        int turn = 0;
        while (!currentState.children().isEmpty()) { // Continue until no more moves are possible
            turn++;
            System.out.println("Turn " + turn + ": " + currentState.toString());
            if (currentState.isMaxPlayersTurn() != isAStarMax) {
                // Minimax takes the turn
                // System.out.println("Minimax is playing...");
                List<S> minimaxResult = MiniMaxSearch.iterativeDeepeningMiniMax(currentState, minSearchTime);
                currentState = minimaxResult.get(1); // Assume the method returns the path to the best move
            } else {
                // AStarMinimax takes the turn
                // System.out.println("AStarMinimax is playing...");
                List<S> aStarResult = AStarSearch.aStarMinimaxDepth(currentState, 4);
                currentState = aStarResult.get(1); // Assume the method returns the path to the best move
            }
            System.out.println("###\n");
        }

        return currentState;
    }
}

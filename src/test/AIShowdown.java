package test;

import static util.ConsoleColors.pprint;

import java.util.List;

import games.mangala.MangalaState;
import games.mangala.PlayMangala;
import interfaces.TwoPersonGameState;
import search_algorithms.AStarSearch;
import search_algorithms.MiniMaxSearch;
import util.ConsoleColors;
import util.Node;

public class AIShowdown {
    private static final int MAX_DEPTH = 8;


    private AIShowdown() {
        // Private constructor to hide the implicit public one
    }

    public static void main(String[] args) {
        tournament(1);
    }

    public static void tournament(int nbGames) {
        pprint(ConsoleColors.GREEN, "### AI Showdown ###");
        final float minSearchTime = 10.0f;
        // Simulate a game between Minimax and AStarMinimax
        PlayMangala game = new PlayMangala();
        int aStarMinimaxWon = 0;
        int miniMaxWon = 0;
        for (int i = 0; i < nbGames; i++) {
            MangalaState initialState = game.getInitialState(true);
            MangalaState finalState = playGame(initialState, minSearchTime, true);
            System.out.println("Final state of the game: " + finalState);
            System.out.println("Winner: " + game.getWinnersName(finalState));
            System.out.println("###");
            if (game.getWinnersName(finalState).equals("First Player")) {
                aStarMinimaxWon++;
            } else if(game.getWinnersName(finalState).equals("Second Player")) {
                miniMaxWon++;
            }
        }

        pprint(ConsoleColors.GREEN, "### AI Showdown - DONE ###");
        pprint(ConsoleColors.GREEN, "### Results ###");
        pprint(ConsoleColors.GREEN, "AStarMinimax won " + aStarMinimaxWon + " games.");
        pprint(ConsoleColors.GREEN, "Minimax won " + miniMaxWon + " games.");
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

        pprint(ConsoleColors.GREEN, "minSearchTime: " + minSearchTime);
        pprint(ConsoleColors.GREEN, "### Playing a game ###");
        while (!currentState.children().isEmpty()) { // Continue until no more moves are possible
            turn++;
            pprint(ConsoleColors.YELLOW, "Current Algorithm: " + getCurrentAlgorithm(currentState, isAStarMax));
            System.out.println("Turn " + turn + ": " + currentState.toString());
            if (!isAStarTurn(currentState, isAStarMax)) {
                List<S> minimaxResult = MiniMaxSearch.iterativeDeepeningMiniMax(currentState, MAX_DEPTH);
                currentState = minimaxResult.get(1); // Assume the method returns the path to the best move
            } else {
                Node<S> aStarResult = AStarSearch.search(currentState, MAX_DEPTH);
                currentState = aStarResult.getPath().get(1).getState(); // Assume the method returns the path to the best move
            }
            System.out.println("###\n");
        }

        return currentState;
    }
    private static <S extends TwoPersonGameState<S>> boolean isAStarTurn(S currentState, boolean isAStarMax) {
        return currentState.isMaxPlayersTurn() == isAStarMax;
    }

    private static <S extends TwoPersonGameState<S>> String getCurrentAlgorithm(S currentState, boolean isAStarMax) {
        return isAStarTurn(currentState, isAStarMax) ? "AStarMinimax" : "Minimax";
    }
    
}

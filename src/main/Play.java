package main;

import util.GameSolver;
import util.TwoPersonGameState;
import util.TwoPersonPlay;

import java.util.List;

public class Play {
    
    public static void main(String[] args) {

        final float minSearchTime = 0.1f;

        /* Change which TwoPersonPlay class is 
        assigned to `game` below to choose 
        which game to play. */
        TwoPersonPlay game = new PlayConnect4();
        runGame(game, minSearchTime);

    }


    /**
     * Runs a game G that uses states S.
     * @param <S> a TwoPersonGameState.
     * @param <G> a wrapper for the states S.
     * @param game an instance of G.
     * @param minSearchTime the minimum time to search for a move.
     */
    public static <S extends TwoPersonGameState<S>, G extends TwoPersonPlay<S>> void runGame(G game, float minSearchTime){
        
        S state = game.getInitialState(TwoPersonPlay.inputWhiteIsMax());
        S advisedState;
        List<S> advisedPath;
        
        // The game loop
        while(!game.isGameOver(state)){

            System.out.println("\n[ANALYSING...]");
            advisedPath = GameSolver.findBestPathForMax(state, minSearchTime);
            if(!advisedPath.isEmpty()){
                advisedState = advisedPath.get(1);
                System.out.println("[ADVISED]" + advisedState);    
            }

            // System.out.println(state.getBoard());

            // Get the user's input and update the state.
            int moveNumber = game.scanMoveNumber(state);
            state = game.makeMove(state, moveNumber);
            System.out.println("\n[CURRENT]" + state);
            System.out.println("Score: " + state.score());


        }

        // Print the final state.
        System.out.println("[GAME OVER]" + state);
        // Print the winner.
        System.out.println("[WINNER] " + game.getWinnersName(state) + " player wins!");

    }
}

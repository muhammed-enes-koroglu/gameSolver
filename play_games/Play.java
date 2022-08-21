import java.util.List;

public class Play {
    
    public static void main(String[] args) {

        float minSearchTime = 0.1f;
        runGame(new PlayConnect4(), minSearchTime);

    }


    /**
     * Runs a game G that uses states S.
     * @param <S> a TwoPersonGameState.
     * @param <G> a wrapper for the states S.
     * @param game an instance of G.
     * @param minSearchTime the minimum time to search for a move.
     */
    public static <S extends TwoPersonGameState<S>, G extends TwoPersonPlay<S>> void runGame(G game, float minSearchTime){
        
        S currentState = game.getInitialState(TwoPersonPlay.inputWhiteIsMax());
        S advisedState;
        List<S> advisedPath;
        
        // The game loop
        while(!game.isGameOver(currentState)){
            System.out.println("\n[CURRENT STATE]" + currentState);

            System.out.println("[ANALYSING...]");
            advisedPath = GameSolver.findBestPathForMax(currentState, minSearchTime);
            if(!advisedPath.isEmpty()){
                advisedState = advisedPath.get(1);
                System.out.println("[ADVISED STATE]" + advisedState);    
            }

            // Get the user's input and update the state.
            int moveNumber = game.scanMoveNumber(currentState);
            currentState = game.makeMove(currentState, moveNumber);

        }

        // Print the final state.
        System.out.println("[GAME OVER]" + currentState);
        // Print the winner.
        System.out.println("[WINNER] " + game.getWinnersName(currentState) + " player wins!");

    }
}

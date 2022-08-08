import java.util.List;

public class Play {
    
    public static void main(String[] args) {

        runGame(new PlayConnect4());

    }


    public static <S extends TwoPersonGameState<S>, P extends TwoPersonPlay<S>> void runGame(P game){
        
        S currentState = game.getInitialState(TwoPersonPlay.inputWhiteIsMax());
        S advisedState;
        List<S> advisedPath;
        

        // The game loop
        while(!game.isGameOver(currentState)){
            System.out.println("\n[CURRENT STATE]" + currentState);

            System.out.println("[ANALYSING...]");
            advisedPath = GameSolver.findBestPathForMax(currentState, 1);
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

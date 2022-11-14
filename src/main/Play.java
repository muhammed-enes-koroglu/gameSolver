package main;

import util.GameSolver;
import util.TwoPersonGameState;
import util.TwoPersonPlay;

import java.util.List;
import java.util.Scanner;

import games.Mangala.PlayMangala;
import games.TicTacToe.PlayTicTacToe;
import games.Connect4.PlayConnect4;

public class Play {
    static final String[] gameStrings = new String[]{"TicTacToe", "Mangala", "Connect4"}; 

    public static void main(String[] args) {

        final float minSearchTime = 1f;
        chooseNRunGame(minSearchTime);

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
        System.out.println(state);
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
            int[] moveNumber = game.scanMoveNumber(state);
            state = game.makeMove(state, moveNumber);
            System.out.println("\n[CURRENT]" + state);
            System.out.println("Score: " + state.score());


        }

        // Print the final state.
        System.out.println("[GAME OVER]" + state);
        // Print the winner.
        System.out.println("[WINNER] " + game.getWinnersName(state) + " player wins!");

    }

    private static void chooseNRunGame(float minSearchTime){

        // Show the games.
        System.out.println("Choose a game:");
        for(int i=1; i<=gameStrings.length; i++)
            System.out.println(i + ". " + gameStrings[i-1]);

        // Scan the choice and run the appropriate game.
        int gameNumber = scanGameChoice(gameStrings);
        switch(gameNumber){
            case 1:
                runGame(new PlayTicTacToe(), minSearchTime);
                break;
            case 2:
                runGame(new PlayMangala(), minSearchTime);
                break;
            case 3:
                runGame(new PlayConnect4(), minSearchTime);
                break;
            default:
                System.out.println("Error in scanning the game choice.");
        }
    }

    private static int scanGameChoice(String[] gameStrings){
        Scanner sc = new Scanner(System.in);
            
        int gameNumber = -1;
        boolean validInput = false;
        
        // Loop until valid input.
        while(!validInput){
            validInput = true;

            // Ask for input.
            System.out.print("Enter the number for your game of choice: ");
            String line = sc.nextLine();

            // Parse the input.
            try{
                gameNumber = Integer.parseInt(line);

                // gameNumber should be in [1, BOARD_SIZE-1]
                if(gameNumber <= 0 || gameNumber > gameStrings.length){
                    System.out.println("[ERROR] Game number must be in [1, " + (gameStrings.length)+ "]");
                    validInput = false;
                }

            } catch(NumberFormatException e){
                System.out.println("[ERROR] Input must be an integer");
                validInput = false;
            }
        }
        return gameNumber;
    }

}

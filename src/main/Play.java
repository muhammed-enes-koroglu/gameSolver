package main;

import util.ConsoleColors;
import util.GameSolver;
import util.TwoPersonGameState;
import util.TwoPersonPlay;

import java.util.List;
import java.util.Scanner;

import games.tictactoe.PlayTicTacToe;
import games.connect4.PlayConnect4;
import games.mangala.PlayMangala;

public class Play {

    public static final String BACKGROUND_ADVISED = ConsoleColors.GREEN_BACKGROUND;
    public static final String BACKGROUND_CURRENT = ConsoleColors.CYAN_BACKGROUND;
    
    static final String[] gameStrings = new String[]{"TicTacToe", "Mangala", "Connect4"}; 
    static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {

        final float minSearchTime = .1f;
        chooseNRunGame(minSearchTime);

    }

    /**
     * Runs a game G that uses states S.
     * @param <S> a TwoPersonGameState.
     * @param <G> a wrapper for the states S.
     * @param game an instance of G.
     * @param minSearchTime the minimum time to search for a move.
     */
    public static <S extends TwoPersonGameState<S>, G extends TwoPersonPlay<S>> void runGame(G game, float minSearchTime, boolean showAdvised){
        
        // Initialize the game.
        S state = game.getInitialState(TwoPersonPlay.inputWhiteIsMax());
        System.out.println(BACKGROUND_CURRENT + "[CURRENT]" + state.toString(BACKGROUND_CURRENT) + ConsoleColors.RESET);
        S advisedState = state;
        List<S> advisedPath;
        
        // The game loop
        while(!game.isGameOver(state)){

            // Get the best move.
            advisedPath = GameSolver.iterDeepeningMiniMax(state, minSearchTime);
            if(advisedPath.size() >= 2){
                advisedState = advisedPath.get(1);
            } else if(advisedPath.size() == 1){
                advisedState = advisedPath.get(0);
            }

            // Print the advised move.
            if(showAdvised){
                System.out.println(BACKGROUND_ADVISED + "\n[ADVISED]" + advisedState.toString(BACKGROUND_ADVISED) + ConsoleColors.RESET);    
                System.out.println("Depth: " + advisedPath.size());
                System.out.println("Score: " + advisedPath.get(advisedPath.size()-1).score() + "\n");
            }

            // Get the next state.
            if(state.isMaxPlayersTurn()){ // MaxPlayer == User ==> User's turn.

                // Get the user's input and update the state.
                int[] moveNumber = game.scanMoveNumber(state);

                // If the user's input is empty, 
                // then the user wants to play as advised.
                if(moveNumber.length == 0){
                    state = advisedState;
                } else{
                    state = game.makeMove(state, moveNumber);
                }
            } else {
                // Get the computer's move and update the state.
                state = advisedState;
                System.out.println(BACKGROUND_CURRENT + "[CURRENT]" + state.toString(BACKGROUND_CURRENT) + ConsoleColors.RESET);
            }

        }

        // Print the winner.
        System.out.println("[GAME OVER]");
            System.out.println("[WINNER] " + game.getWinnersName(state) + " wins!\n");
        System.out.println(ConsoleColors.RESET);
    }

    private static void chooseNRunGame(float minSearchTime){

        // Choose whether to show the advised move.
        boolean showAdvised = scanShowAdvised();

        // Show the games.
        System.out.println("Choose a game:");
        for(int i=1; i<=gameStrings.length; i++)
            System.out.println(i + ". " + gameStrings[i-1]);

        // Scan the choice and run the appropriate game.
        int gameNumber = scanGameChoice(gameStrings);
        switch(gameNumber){
            case 1:
                runGame(new PlayTicTacToe(), minSearchTime, showAdvised);
                break;
            case 2:
                runGame(new PlayMangala(), minSearchTime, showAdvised);
                break;
            case 3:
                runGame(new PlayConnect4(), minSearchTime, showAdvised);
                break;
            default:
                System.out.println("Error in scanning the game choice.");
        }
    }

    private static int scanGameChoice(String[] gameStrings){
            
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

    /** Choose whether to show the advised move. */
    private static boolean scanShowAdvised(){

        boolean showAdvised = false;
        System.out.println("Show advised move? (y/n)");
        String input = sc.nextLine();
        switch(input){
            case "y":
                showAdvised = true;
                break;
            case "n":
                showAdvised = false;
                break;
            default:
                System.out.println("Invalid input. Defaulting to 'n'.");
                showAdvised = false;
                break;
        }
        return showAdvised;
    }
}

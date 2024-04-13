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
import games.reversi.PlayReversi;

public class Play {

    public static final String BACKGROUND_ADVISED = ConsoleColors.GREEN_BACKGROUND;
    public static final String BACKGROUND_CURRENT = ConsoleColors.CYAN_BACKGROUND;
    
    static final String[] gameStrings = new String[]{"TicTacToe", "Mangala", "Connect4", "Reversi"}; 
    static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {

        final float minSearchTime = 1;
        chooseNRunGame(minSearchTime);

    }

    /** Runs the game loop for a two-person game.
     *
     * @param <S>            the type of the game state
     * @param <G>            the type of the game play
     * @param game           the game play instance
     * @param minSearchTime  the minimum search time for the AI player
     * @param showAdvised    flag indicating whether to show the advised move
     */
    public static <S extends TwoPersonGameState<S>, G extends TwoPersonPlay<S>> void runGame(G game, float minSearchTime, boolean showAdvised){
        
        // Initialize the game.
        S state = game.getInitialState(scanWhiteIsMax());
        S advisedState = state;
        List<S> advisedPath;

        int turn = 0;
        int totalDepth = 0;

        final long startTime = System.currentTimeMillis();

        // The game loop
        while(!game.isGameOver(state)){
            turn++;
            System.out.println("#########################\n#########################");
            System.out.println("[TURN] " + (turn+1)/2 + "\n");
            System.out.println(BACKGROUND_CURRENT + "[CURRENT]" + state.toString(BACKGROUND_CURRENT) + ConsoleColors.RESET);

            // Get the best move.
            advisedPath = GameSolver.iterativeDeepeningMiniMax(state, minSearchTime);
            if(advisedPath.size() >= 2){
                advisedState = advisedPath.get(1);
            } else if(advisedPath.size() == 1){
                advisedState = advisedPath.get(0);
            }
            System.out.println("Depth: " + advisedPath.size());
            totalDepth += advisedPath.size();

            if(state.isMaxPlayersTurn()){ // MaxPlayer == User ==> User's turn.

                // Print the advised move.
                if(showAdvised){
                    System.out.println(BACKGROUND_ADVISED + "\n[ADVISED]" + advisedState.toString(BACKGROUND_ADVISED) + ConsoleColors.RESET);    
                    System.out.println("Score: " + advisedPath.get(advisedPath.size()-1).score() + "\n");
                }
                
                // Get the user's input and update the state.
                int[] move = game.scanMoveNumber(state);
                // int[] move = new int[0];

                // Get the next state.
                // If the user's input is empty, 
                // then the user wants to play as advised.
                if(move.length == 0){
                    state = advisedState;
                } else{
                    state = game.makeMove(state, move);
                }
            } else {
                // Get the computer's move and update the state.
                state = advisedState;
            }

        }

        // Print the winner.
        System.out.println("[GAME OVER]");
        System.out.println("[WINNER] " + game.getWinnersName(state) + " wins!\n");
        System.out.println(ConsoleColors.RESET);

        System.out.println("Average depth: " + (float) (totalDepth) / turn);
        final long endTime = System.currentTimeMillis();
        System.out.println("Time: " + (endTime - startTime) / 1000f + "s");
    }

    private static void chooseNRunGame(float minSearchTime){

        // Choose whether to show the advised move.
        boolean showAdvised = scanShowAdvised();

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
            case 4:
                runGame(new PlayReversi(), minSearchTime, showAdvised);
                break;
            default:
                System.out.println("Error in scanning the game choice.");
        }
    }

    private static int scanGameChoice(String[] gameStrings){
        
        // Show the games.
        System.out.println("Choose a game:");
        for(int i=1; i<=gameStrings.length; i++)
            System.out.println(i + ". " + gameStrings[i-1]);


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

    /** Scan, parse and validate whether the player wants to be player 1. */
    public static boolean scanWhiteIsMax(){

        int inputNumber = 1;
        boolean validInput = false;
        while(!validInput){
            validInput = true;

            // Ask for input.
            System.out.print("Enter `1` to be player 1, `2` to be player 2: ");
            String line = sc.nextLine();

            // Parse the input.
            try{
                inputNumber = Integer.parseInt(line);
            }
            catch(NumberFormatException e){
                System.out.println("[ERROR] Input must be 1 of 2");
                validInput = false;
            }
            
            // inputNumber should be 1 or 2.
            if(!(inputNumber == 1 || inputNumber == 2)){
                System.out.println("[ERROR] Player number must be 1 or 2");
                validInput = false;
            }
        }
        return inputNumber == 1;
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

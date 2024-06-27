package test;

import java.util.List;

import games.connect4.TestConnect4;
import games.mangala.MangalaState;
import games.mangala.PlayMangala;
import games.mangala.TestMangala;
import games.reversi.TestReversi;
import games.tictactoe.TestTicTacToeState;
import interfaces.TwoPersonGameState;
import util.Board;
import util.ConsoleColors;
import util.SigmoidScaler;

import static util.Helper.assrt;
import static util.ConsoleColors.pprint;


public class RunTests{

    public static void main(String[] args){

        System.out.println("[TESTING]\n");

        // TestTicTacToeState.testTicTacToeState();
        // TestGameAI.testAStarMinimax();
        // TestGameSolver.testIterDeepeningMiniMax();
        // TestMangala.testMangalaState();
        // TestConnect4.testConnect4();
        // TestReversi.testReversi();


        System.out.println("\nALL TESTS SUCCESSFULL!\n");

        // testHeuristic();

        printAverageChildrenCounts();
    }


    public static void testHeuristic(){
        // store the max and min scores in variables
        float maxScore = 1;
        float minScore = 0;

        SigmoidScaler scaler = TwoPersonGameState.sigmoidScaler;
        

        // print the unscaled values
        pprint(ConsoleColors.GREEN_BRIGHT, "Unscaled: ");
        pprint(ConsoleColors.GREEN_BRIGHT, "MAX_SCORE: " + maxScore);
        pprint(ConsoleColors.GREEN_BRIGHT, "MIN_SCORE: " + minScore);
        pprint(ConsoleColors.GREEN_BRIGHT, "MID_SCORE: " + ((maxScore + minScore) / 2));
        pprint(ConsoleColors.GREEN_BRIGHT, "QUARTER_SCORE: " + ((maxScore + minScore) / 4));
        pprint(ConsoleColors.GREEN_BRIGHT, "3 QUARTER_SCORE: " + ((maxScore + minScore) * 3 / 4));

        // print the scaled values
        pprint(ConsoleColors.GREEN_BRIGHT, "\nSigmoidScaled: ");
        pprint(ConsoleColors.GREEN_BRIGHT, "MAX_SCORE: " + scaler.scale(maxScore));
        pprint(ConsoleColors.GREEN_BRIGHT, "MIN_SCORE: " + scaler.scale(minScore));
        pprint(ConsoleColors.GREEN_BRIGHT, "MID_SCORE: " + scaler.scale((maxScore + minScore) / 2));
        pprint(ConsoleColors.GREEN_BRIGHT, "QUARTER_SCORE: " + scaler.scale((maxScore + minScore) / 4));
        pprint(ConsoleColors.GREEN_BRIGHT, "3 QUARTER_SCORE: " + scaler.scale((maxScore + minScore) * 3 / 4));

        System.out.println("\n\n");
        MangalaState initialState = new PlayMangala().getInitialState(true);
        pprint(ConsoleColors.CYAN_BRIGHT, "Initial State: " + Float.toString(initialState.heuristic()));

        initialState = new PlayMangala().getInitialState(false);
        pprint(ConsoleColors.CYAN_BRIGHT, "Initial State but maximizing for black: " + Float.toString(initialState.heuristic()));

        // State with a winning board
        MangalaState winningState = new MangalaState(new Board(new int[][]{new int[]{0,0,0, 0,0,0, 20, 0,0,0, 0,0,0, 0}}), true, true);
        assrt(winningState.isGameOver());
        pprint(ConsoleColors.CYAN_BRIGHT, "Winning State: " + Float.toString(winningState.heuristic()));

        // State with a winning board
        MangalaState almostWinningState = new MangalaState(new Board(new int[][]{new int[]{0,10,10, 0,0,0, 10, 1,0,0, 0,0,0, 0}}), true, true);
        pprint(ConsoleColors.CYAN_BRIGHT, "Almost Winning State: " + Float.toString(almostWinningState.heuristic()));

    }

    /**
     * This method performs an analysis of the average children count for each generation.
     * It initializes the game state, sets the number of generations and iterations,
     * and calculates the average children count for each generation.
     * Finally, it prints the average children count to the console.
     */
    public static void printAverageChildrenCounts(){
        MangalaState initialState = new PlayMangala().getInitialState(true);
        int nbGenerations = 150;
        int numIterations = 1000;
        
        List<Float> avgChildrenCount = Statistics.getAverageChildrenCounts(numIterations, nbGenerations, initialState);

        // Print the average children count
        pprint(ConsoleColors.CYAN_BRIGHT, "Average children count for each generation:");
        pprint(ConsoleColors.CYAN_BRIGHT, avgChildrenCount.toString());
        for (int i = 0; i < avgChildrenCount.size(); i++) {
            if (avgChildrenCount.get(i) < 1) {
                System.out.println("First index whose value drops below 1: " + i);
                break;
            }
        }
    }

}

package test;

import java.util.List;

import games.connect4.TestConnect4;
import games.mangala.MangalaState;
import games.mangala.PlayMangala;
import games.mangala.TestMangala;
import games.reversi.TestReversi;
import games.tictactoe.TestTicTacToeState;
import util.ConsoleColors;
import static util.ConsoleColors.pprint;


public class RunTests{

    public static void main(String[] args){

        System.out.println("[TESTING]\n");
        TestTicTacToeState.testTicTacToeState();
        TestGameAI.testAStarMinimax();
        TestGameSolver.testIterDeepeningMiniMax();
        TestMangala.testMangalaState();
        TestConnect4.testConnect4();
        TestReversi.testReversi();


        System.out.println("\nALL TESTS SUCCESSFULL!\n");

        someTest();
    }


    /**
     * This method performs a test on the Mangala game solver.
     * It initializes the game state, sets the number of generations and iterations,
     * and calculates the average children count for each generation.
     * Finally, it prints the average children count to the console.
     */
    public static void someTest(){
        MangalaState initialState = new PlayMangala().getInitialState(true);
        int nbGenerations = 150;
        int numIterations = 1000;
        
        List<Float> avgChildrenCount = Statistics.getAverageChildrenCounts(numIterations, nbGenerations, initialState);

        // Print the average children count
        pprint(ConsoleColors.CYAN_BRIGHT, "Average children count for each generation:");
        pprint(ConsoleColors.CYAN_BRIGHT, avgChildrenCount.toString());
    }

}

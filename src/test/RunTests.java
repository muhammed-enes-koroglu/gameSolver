package test;

import games.connect4.TestConnect4;
import games.mangala.TestMangala;
import games.reversi.TestReversi;
import games.tictactoe.TestTicTacToeState;

public class RunTests{

    public static void main(String[] args){

        System.out.println("[TESTING]\n");
        TestTicTacToeState.testTicTacToeState();
        TestGameSolver.testIterDeepeningMiniMax();
        TestMangala.testMangalaState();
        TestConnect4.testConnect4();
        TestReversi.testReversi();

        System.out.println("\nALL TESTS SUCCESSFULL!\n");
    }

}

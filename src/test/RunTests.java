package test;

import util.Board;
import util.GameSolver;

import java.util.Arrays;
import java.util.List;

import games.Mangala.MangalaState;

public class RunTests{

    public static void main(String[] args){

        System.out.println("[TESTING]\n");
        TestTicTacToeState.testTicTacToeState();
        TestGameSolver.testIterDeepeningMiniMax();
        TestMangala.testMangalaState();
        TestConnect4.testConnect4();

        System.out.println("\nALL TESTS SUCCESSFULL!\n");
    }

}

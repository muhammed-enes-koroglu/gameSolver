package test;

import util.Board;
import util.GameSolver;

import java.util.Arrays;
import java.util.List;

import games.Mangala.MangalaState;

public class RunTests{

    public static void main(String[] args){

        TestTicTacToeState.testTicTacToeState();
        TestGameSolver.testFindBestPathForMax();
        TestMangalaState.testMangalaState();

        System.out.println("\nALL TESTS SUCCESSFULL!");
    }

}

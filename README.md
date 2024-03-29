# GameSolver
This project offers a class `GameSolver` implementing iterative deepening minimax search with alpha-beta pruning. The algorithm works for any game implementing the given interface `TwoPersonGameState`. The algorithm finds the path to the most desirable game state after searching for some time. This is achieved by giving `GameSolver.findBestPathForMax()` an initial game state conform the `TwoPersonGameState` interface. 

## Adding new game to Play
1. Implement a new game conform the `TwoPersonGameState` interface.
2. Implement the play class confrom the `TwoPersonPlay` interface.
3. Append the game's name to the `gameStrings` variable in `src/main/Play.java`,
4. Append to the switch-case in the `chooseNRunGame` method in `src/main/Play.java`.

Check out the interfaces `TwoPersonGameState` and `TwoPersonPlay` to add your own games.

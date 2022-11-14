# GameSolver
This project offers a class `GameSolver` implementing iterative deepening minimax search with alpha-beta pruning. The algorithms works for any game implementing the given interface `TwoPersonGameState`. The algorithm finds the path to the most desirable game state after searching for some time. This is achieved by giving `GameSolver.findBestPathForMax()` an initial game state conform the `TwoPersonGameState` interface. 

## Adding new game to Play
1. Append to the `gameStrings` variable in `src/main/Play.java`,
2. Append to the switch-case in the `chooseNRunGame` method in `src/main/Play.java`.

Check out the interfaces `TwoPersonGameState` and `TwoPersonPlay` to add your own games.
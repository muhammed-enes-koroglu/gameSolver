# GameSolver
This project offers a class `GameSolver` implementing iterative deepening minimax search with alpha-beta pruning. The algorithms works for any game implementing the given interface `TwoPersonGameState`. The algorithm finds the path to the most desirable game state after searching for some time. This is achieved by giving `GameSolver.findBestPathForMax()` an initial game state conform the `TwoPersonGameState` interface. 

The folder `src` contains the files essential to the working of `GameSolver`.
The folder `test` contains some tests which can be run from `TestSuite`.
The folder `games` contains two possible games `TicTacToeState` and `MangalaState`. `TicTacToeState` is used to test `GameSolver`.

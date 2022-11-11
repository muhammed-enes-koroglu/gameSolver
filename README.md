# GameSolver
This project offers a class `GameSolver` implementing iterative deepening minimax search with alpha-beta pruning. The algorithms works for any game implementing the given interface `TwoPersonGameState`. The algorithm finds the path to the most desirable game state after searching for some time. This is achieved by giving `GameSolver.findBestPathForMax()` an initial game state conform the `TwoPersonGameState` interface. 

Edit the `game` variable in the `main` method of src/main/Play.java and run the file to play your chosen game.

Check out the interfaces `TwoPersonGameState` and `TwoPersonPlay` to add your own games to the repo.
# 0. Multithreading
We have made an attempt at starting to implement multithreading to our project, however it has not gone very favorably. Creation of the threads takes too much time. This time propagates in the search algorithm and makes our algorithm run exponentially slower. 

Solution --> Reuse existing threads? 
--> Didn't work. Using better children() algorithm advised instead of multithreading by experts.

# 1. Action Space
Change the `GameSolver` algorithm so it asks not for the child states, but for the action space. Do this so the return type of the algorithm can be an action to achieve the desired state instead of the new state itself.

# 2. A* and IDA*

# 3. Admissible and Consistent Heuristics
https://en.wikipedia.org/wiki/Admissible_heuristic
## Admissible
Never overestimates the cost of reaching the goal.
## Consistent
The estimate is always less than or equal to the estimated distance from any neighbouring vertex to the goal, plus the cost of reaching that neighbour.

# 4. Write `TurkishDraughts`

# 5. Play- class for `TurkishDraughts`

# 6. Abalone
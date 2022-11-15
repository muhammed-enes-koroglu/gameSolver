package util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public abstract class GameSolver{

    private GameSolver(){
        throw new IllegalStateException("Utility class");
    }

    /** Return a path with the best score for maxPlayer after searching at least for `minSearchTime` seconds.
     * 
     * @param minSearchTime in seconds.
    */
    public static <S extends TwoPersonGameState<S>> List<S> iterDeepeningMiniMax(S startState, float minSearchTime){
        long minSearchTimeMilliSeconds = (long) (minSearchTime *  1000);
        long startTime = System.currentTimeMillis();
        long now;
        long timePassed = 0;

        ArrayList<S> bestPath = new ArrayList<>();
        int depth;

        /** Iterative Deepening Loop. 
         * 
         * Doesn't need to remember best of 
         * the previous iteration, because
         * the prediction of the current 
         * generation is guaranteed to be
         * better than the previous. */
        for(depth=1; timePassed < minSearchTimeMilliSeconds; depth++){

            bestPath = miniMax(
                new ArrayList<S>(Arrays.asList(startState)), depth, -Float.MAX_VALUE, Float.MAX_VALUE);

            if(bestPath.get(bestPath.size()-1).children().isEmpty()){
                break;
            }

            now = System.currentTimeMillis();
            timePassed = now - startTime;   
        }
        
        // Print searched depth.
        System.out.println("Depth: " + depth);
        return bestPath;
    }

    
    /** Return a path with the best score for maxPlayer 
     * after searching for a depth of `maxDepth`.
     * 
     * @param maxDepth The maximum depth to search.
    */
    public static <S extends TwoPersonGameState<S>> List<S> iterDeepeningMiniMax(S startState, int maxDepth){
        ArrayList<S> bestPath = new ArrayList<>();
        int depth;

        /** Iterative Deepening Loop. 
         * 
         * Doesn't need to remember best of 
         * the previous iteration, because
         * the prediction of the current 
         * generation is guaranteed to be
         * better than the previous. */
        for(depth=1; depth <= maxDepth; depth++){

            bestPath = miniMax(
                new ArrayList<S>(Arrays.asList(startState)), depth, -Float.MAX_VALUE, Float.MAX_VALUE);

            if(bestPath.get(bestPath.size()-1).children().isEmpty()){
                break;
            }

        }
        
        // Print searched depth.
        System.out.println("Depth: " + depth);
        return bestPath;
    }

    // Return a path with the best score for maxPlayer within given maxDepth.
    private static <S extends TwoPersonGameState<S>> ArrayList<S> miniMax(ArrayList<S> path, int maxDepth, float alpha, float beta){
        if(maxDepth <= 0) // Leaf node.
            return path;

        ArrayList<S> bestPath = path; // So `path` is returned if no child nodes left.
        S currentState = path.get(path.size()-1);
        Set<S> children = currentState.children();

        if(currentState.isMaxPlayersTurn()){
            return searchIsMaxPlayer(path, maxDepth, alpha, beta, bestPath, children);
        }
        else{
            return searchIsMinPlayer(path, maxDepth, alpha, beta, bestPath, children);
        }        
    }

    private static <S extends TwoPersonGameState<S>> ArrayList<S> searchIsMaxPlayer(ArrayList<S> path, int maxDepth, float alpha,
            float beta, ArrayList<S> bestPath, Set<S> children) {
        float bestValue = -Float.MAX_VALUE;
        for(S child: children){
            if(!path.contains(child)){
                ArrayList<S> childPath = new ArrayList<>(path);
                childPath.add(child);

                ArrayList<S> resultPath = miniMax(childPath, maxDepth-1, alpha, beta);
                float resultScore = resultPath.get(resultPath.size()-1).score(); // Score of the last state
                
                if(bestValue < resultScore || bestPath.equals(path)){ // if bestPath is the default or we have a better value
                    bestPath = resultPath;
                    bestValue = resultScore;
                }
                if(alpha < bestValue)
                    alpha = bestValue;
                
                if(beta <= alpha)
                    break;
            }
        }
        return bestPath;
    }
    
    private static <S extends TwoPersonGameState<S>> ArrayList<S> searchIsMinPlayer(ArrayList<S> path, int maxDepth, float alpha,
            float beta, ArrayList<S> bestPath, Set<S> children) {
        float bestValue = Float.MAX_VALUE;
        for(S child: children){
            if(!path.contains(child)){
                ArrayList<S> childPath = new ArrayList<>(path);
                childPath.add(child);

                ArrayList<S> resultPath = miniMax(childPath, maxDepth-1, alpha, beta);
                float resultScore = resultPath.get(resultPath.size()-1).score(); // Score of the last state
                
                if(bestValue > resultScore || bestPath.equals(path)){ // if bestPath is the default or we have a better value
                    bestPath = resultPath;
                    bestValue = resultScore;
                }
                if(beta > bestValue)
                    beta = bestValue;
                
                if(beta <= alpha)
                    break;
            }
        }
        return bestPath;
    }
}

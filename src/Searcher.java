import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Searcher{

    private Searcher(){
        throw new IllegalStateException("Utility class");
    }

    // Return a path with the best score for maxPlayer. 
    public static <S extends TwoPersonGameState> List<S> findBestPathForMax(S startState, int maxSearchTime){
        long maxSearchTimeMilli = maxSearchTime * (long) 1000;
        long startTime = System.currentTimeMillis();
        long now;
        long timePassed = 0;

        float bestScore = -Float.MAX_VALUE;
        ArrayList<S> bestPath = new ArrayList<>();
        ArrayList<S> resultPath;

        for(int depth=1; timePassed < maxSearchTimeMilli; depth++){

            resultPath = miniMax(
                new ArrayList<S>(Arrays.asList(startState)), depth, -Float.MAX_VALUE, Float.MAX_VALUE);
            float resultScore = resultPath.get(resultPath.size()-1).score();
            if(bestScore < resultScore){
                bestScore = resultScore;
                bestPath = resultPath;
            }

            now = System.currentTimeMillis();
            timePassed = now - startTime;    
        }
        return bestPath;
    }

    // Return a path with the best score for maxPlayer within given maxDepth.
    private static <S extends TwoPersonGameState> ArrayList<S> miniMax(ArrayList<S> path, int maxDepth, float alpha, float beta){
        // System.out.println("\n" + maxDepth + "\n" + path.toString());
        if(maxDepth <= 0) // Leaf node.
            return path;

        ArrayList<S> bestPath = path; // So `path` is returned if no child nodes left.
        S currentState = path.get(path.size()-1);
        S[] children = (S[]) currentState.children();

        if(currentState.isMaxPlayer()){
            return searchIsMaxPlayer(path, maxDepth, alpha, beta, bestPath, children);
        }
        else{
            return searchIsMinPlayer(path, maxDepth, alpha, beta, bestPath, children);
        }        
    }

    private static <S extends TwoPersonGameState> ArrayList<S> searchIsMaxPlayer(ArrayList<S> path, int maxDepth, float alpha,
            float beta, ArrayList<S> bestPath, S[] children) {
        float bestValue = -Float.MAX_VALUE;
        for(S child: children){
            if(!path.contains(child)){
                ArrayList<S> childPath = (ArrayList<S>) path.clone();
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
    
    private static <S extends TwoPersonGameState> ArrayList<S> searchIsMinPlayer(ArrayList<S> path, int maxDepth, float alpha,
            float beta, ArrayList<S> bestPath, S[] children) {
        float bestValue = Float.MAX_VALUE;
        for(S child: children){
            if(!path.contains(child)){
                ArrayList<S> childPath = (ArrayList<S>) path.clone();
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

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Searcher{

    private Searcher(){
        throw new IllegalStateException("Utility class");
    }

    // Return the path with best score for maxPlayer. 
    public static <S extends TwoPersonGameState> List<S> findBestPathForMax(S startState, int maxSearchTimeMilli){
        Instant startTime = Instant.now();
        Instant now;
        long timePassed = 0;

        float bestScore = -Float.MAX_VALUE;
        ArrayList<S> bestPath = new ArrayList<>();
        ArrayList<S> resultPath;

        for(int depth=1; timePassed < maxSearchTimeMilli; depth++){
            resultPath = miniMax(
                new ArrayList<S>(Arrays.asList(startState)), depth, -Float.MAX_VALUE, Float.MAX_VALUE);
            if(resultPath.get(resultPath.size()-1).score() > bestScore){
                bestScore = resultPath.get(resultPath.size()-1).score();
                bestPath = resultPath;
            }

            now = Instant.now();
            timePassed = Duration.between(startTime, now).toMillis();    
        }
        return bestPath;
    }

    // Return the path with best score for maxPlayer within given maxDepth.
    private static <S extends TwoPersonGameState> ArrayList<S> miniMax(ArrayList<S> path, int maxDepth, float alpha, float beta){
        if(maxDepth <= 0) // Leaf node.
            return path;

        S currentState = path.get(path.size()-1);
        ArrayList<S> bestPath = path; // So `path` is returned if no child nodes left.
        S[] children = (S[]) currentState.children();

        if(currentState.isMaxPlayer()){
            return searchIsMaxPlayer(path, maxDepth, alpha, beta, bestPath, children);
        }
        else{
            return searchIsMinPlayer(path, maxDepth, alpha, beta, bestPath, children);
        }        
    }

    private static <S extends TwoPersonGameState> ArrayList<S> searchIsMinPlayer(ArrayList<S> path, int maxDepth, float alpha,
            float beta, ArrayList<S> bestPath, S[] children) {
        float bestValue = Float.MAX_VALUE;
        for(S child: children){
            if(!path.contains(child)){
                ArrayList<S> childPath = (ArrayList<S>) path.clone();
                childPath.add(child);

                ArrayList<S> resultPath = miniMax(childPath, maxDepth-1, alpha, beta);
                S resultState = resultPath.get(resultPath.size()-1);
                if(resultState.score() < bestValue){
                    bestValue = resultState.score();
                    bestPath = resultPath;
                }

                alpha = alpha < bestValue ? alpha : bestValue;
                if(beta <= alpha)
                    break;
            }
        }
        return bestPath;
    }

    private static <S extends TwoPersonGameState> ArrayList<S> searchIsMaxPlayer(ArrayList<S> path, int maxDepth, float alpha,
            float beta, ArrayList<S> bestPath, S[] children) {
        float bestValue = -Float.MAX_VALUE;
        for(S child: children){
            if(!path.contains(child)){
                ArrayList<S> childPath = (ArrayList<S>) path.clone();
                childPath.add(child);

                ArrayList<S> resultPath = miniMax(childPath, maxDepth-1, alpha, beta);
                S resultState = resultPath.get(resultPath.size()-1);
                if(resultState.score() > bestValue){
                    bestValue = resultState.score();
                    bestPath = resultPath;
                }

                alpha = alpha > bestValue ? alpha : bestValue;
                if(beta <= alpha)
                    break;
            }
        }
        return bestPath;
    }
}

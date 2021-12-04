import java.lang.reflect.Method;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.stream.Stream;

public abstract class Searcher{

    private Searcher(){
        throw new IllegalStateException("Utility class");
    }

    // Return the (shortest?) path to goal, 
    // Path is empty if no solution exists within given timeframe.
    public static <S> S[] miniMax(S startState, int maxSearchTimeMilli) throws NoSuchMethodException{
        // S must contain
        if(Arrays.stream(startState.getClass().getMethods()).
            noneMatch(m -> "getChildren".equals(m.getName())))
            throw new NoSuchMethodException("S must contain method 'getChildren'.");
        if(Arrays.stream(startState.getClass().getMethods())
            .noneMatch(m -> "isGoal".equals(m.getName())))
            throw new NoSuchMethodException("S must contain method 'isGoal'.");
        if(Arrays.stream(startState.getClass().getMethods())
            .noneMatch(m -> "estimatedCost".equals(m.getName())))
            throw new NoSuchMethodException("S must contain method 'estimatedCost'.");

        Instant startTime = Instant.now();
        Instant now = Instant.now();
        long timePassed = Duration.between(startTime, now).toSeconds();    
        for(int depth=1; timePassed < maxSearchTimeMilli; depth++){
            S[] resultPath = depthFirstSearch(startState, depth);
            if(resultPath != null) 
                return resultPath;

            now = Instant.now();
            timePassed = Duration.between(startTime, now).toMillis();    
        }
        return (S[]) new Object[0];
    }

    // Return the (shortest?) path to goal, 
    // Path is empty if no solution exists within given maxDepth.
    private static <S> S[] depthFirstSearch(S startState, int maxDepth){
        
    }
}

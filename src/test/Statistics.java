package test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import interfaces.TwoPersonGameState;

public class Statistics {

    private Statistics(){
        throw new IllegalStateException("Utility class");
    }
    
    /**
     * Calculates the average children count for a given number of iterations and generations, starting from the initial state.
     *
     * @param numIterations  The number of iterations to perform.
     * @param nbGenerations  The number of generations to consider.
     * @param initialState   The initial state of the game.
     * @param <S>            The type of the game state.
     * @return               A list of average children counts for each generation.
     */
    public static <S extends TwoPersonGameState<S>> List<Float> getAverageChildrenCounts(int numIterations, int nbGenerations, S initialState){
        List<Integer> totalChildrenCount = new ArrayList<>(Collections.nCopies(nbGenerations, 0));

        // Get the children count of the lineage for each iteration
        for (int i = 0; i < numIterations; i++) {
            List<S> lineage = getLineage(nbGenerations, initialState);
            List<Integer> childrenCount = getChildrenCount(lineage);
            
            // Add the children count of this lineage to the total children count
            addChildrenCount(totalChildrenCount, childrenCount);
        }

        // Calculate the average children count by dividing the total children count by the number of iterations
        List<Float> avgChildrenCount = new ArrayList<>();
        for (int i = 0; i < nbGenerations; i++) {
            avgChildrenCount.add((float) totalChildrenCount.get(i) / numIterations);
        }

        return avgChildrenCount;
    }

    /**
     * Adds the elements of the 'childrenCount' list to the corresponding elements of the 'totalChildrenCount' list.
     * 
     * @param totalChildrenCount The list containing the total children count.
     * @param childrenCount The list containing the children count to be added.
     */
    public static void addChildrenCount(List<Integer> totalChildrenCount, List<Integer> childrenCount){
        for (int i = 0; i < childrenCount.size(); i++) {
            totalChildrenCount.set(i, totalChildrenCount.get(i) + childrenCount.get(i));
        }
    }

    /**
     * Returns a list of game states representing the lineage of a given start state.
     * The lineage includes the start state and its descendants up to a specified number of generations.
     *
     * @param <S> the type of the game state
     * @param nbGenerations the number of generations to include in the lineage
     * @param startState the start state from which to generate the lineage
     * @return a list of game states representing the lineage
     */
    public static <S extends TwoPersonGameState<S>> List<S> getLineage(int nbGenerations, S startState){
        List<S> lineage = new ArrayList<>();
        S currentState = startState;
        lineage.add(currentState);
        for(int i=0; i<nbGenerations; i++){
            try { // Try to get a random child of the current state
                List<S> children = new ArrayList<>(currentState.children());
                currentState = children.get(new Random().nextInt(children.size()));
                lineage.add(currentState);
                    
            } catch (Exception e) { // If the current state has no children, break the loop
                break;
            }
        }
        return lineage;
    }

    /**
     * Returns a list of the number of children for each state in the given lineage.
     *
     * @param lineage the lineage of states
     * @param <S> the type of the game state
     * @return a list of the number of children for each state in the lineage
     */
    public static <S extends TwoPersonGameState<S>> List<Integer> getChildrenCount(List<S> lineage){
        List<Integer> childrenCount = new ArrayList<>();
        for(S state : lineage){
            childrenCount.add(state.children().size());
        }
        return childrenCount;        
    }

}

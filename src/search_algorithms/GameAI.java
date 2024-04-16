package search_algorithms;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

import interfaces.TwoPersonGameState;

public class GameAI {

    private static final int MAX_DEPTH_LIMIT = 1000;

    private GameAI() {
        // Private constructor to hide the implicit public one
    }

    public static <S extends TwoPersonGameState<S>> List<S> aStarMinimax(S startState, float minSearchTime) {
        long minSearchTimeMilliseconds = (long) (minSearchTime * 1000);
        long startTime = System.currentTimeMillis();

        PriorityQueue<PathNode<S>> openSet = new PriorityQueue<>();
        openSet.add(new PathNode<>(null, startState, 0, heuristic(startState)));

        PathNode<S> bestNode = null;

        // while (!openSet.isEmpty() && (System.currentTimeMillis() - startTime) < minSearchTimeMilliseconds) {
        while (!openSet.isEmpty()) {
            PathNode<S> currentNode = openSet.poll();
            Set<S> children = currentNode.state.children();

            if (children.isEmpty() || currentNode.depth >= MAX_DEPTH_LIMIT) {  // Depth limit for example
                if (bestNode == null || currentNode.compareTo(bestNode) > 0) {
                    bestNode = currentNode;
                }
                continue;
            }

            for (S child : children) {
                if (!currentNode.containsState(child)) {
                    float score = child.score();
                    PathNode<S> newNode = new PathNode<>(currentNode, child, score, heuristic(child));
                    openSet.add(newNode);
                }
            }
        }

        return bestNode != null ? bestNode.constructPath() : new ArrayList<>();
    }

    private static <S extends TwoPersonGameState<S>> float heuristic(S state) {
        // Define heuristic that estimates the score from state to end of the game
        // This is a placeholder: real heuristic should be based on game-specific knowledge
        return state.score();
    }

    private static class PathNode<S extends TwoPersonGameState<S>> implements Comparable<PathNode<S>> {
        PathNode<S> parent;
        S state;
        float score;
        float estimatedTotalScore;
        int depth;

        public PathNode(PathNode<S> parent, S state, float score, float heuristic) {
            this.parent = parent;
            this.state = state;
            this.score = score;
            this.depth = (parent == null) ? 0 : parent.depth + 1;
            this.estimatedTotalScore = this.score + heuristic;  // f = g + h
        }

        public List<S> constructPath() {
            List<S> path = new ArrayList<>();
            PathNode<S> current = this;
            while (current != null) {
                path.add(0, current.state);  // Prepend to path
                current = current.parent;
            }
            return path;
        }

        public boolean containsState(S state) {
            PathNode<S> current = this;
            while (current != null) {
                if (current.state.equals(state)) {
                    return true;
                }
                current = current.parent;
            }
            return false;
        }

        @Override
        public int compareTo(PathNode<S> o) {
            return Float.compare(this.estimatedTotalScore, o.estimatedTotalScore);
        }
    }
}

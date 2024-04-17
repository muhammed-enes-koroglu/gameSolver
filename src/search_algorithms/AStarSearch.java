package search_algorithms;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

import games.mangala.MangalaState;
import interfaces.TwoPersonGameState;
import interfaces.TwoPersonPlay;
import util.SigmoidScaler;

public class AStarSearch {

    private static final int MAX_DEPTH_LIMIT = 1000;

    private AStarSearch() {
        throw new IllegalStateException("Utility class");
    }

    public static <S extends TwoPersonGameState<S>> List<S> aStarMinimax(S startState, float minSearchTime) {
        long minSearchTimeMilliseconds = (long) (minSearchTime * 1000);
        long startTime = System.currentTimeMillis();

        PriorityQueue<PathNode<S>> openSet = new PriorityQueue<>();
        openSet.add(new PathNode<>(null, startState, 0));

        PathNode<S> bestNode = null;

        while (!openSet.isEmpty() && (System.currentTimeMillis() - startTime) < minSearchTimeMilliseconds) {
            bestNode = getChildren(openSet, bestNode);
        }

        return bestNode != null ? bestNode.constructPath() : new ArrayList<>();
    }

    public static <S extends TwoPersonGameState<S>> List<S> aStarMinimaxDepth(S startState, int depthLimit) {
        PriorityQueue<PathNode<S>> openSet = new PriorityQueue<>();
        openSet.add(new PathNode<>(null, startState, 0));

        PathNode<S> bestNode = null;

        int depth = 0;
        while (!openSet.isEmpty() && depth < depthLimit) {
            bestNode = getChildren(openSet, bestNode);
            depth++;
        }

        return bestNode != null ? bestNode.constructPath() : new ArrayList<>();
    }


    private static <S extends TwoPersonGameState<S>> PathNode<S> getChildren(PriorityQueue<PathNode<S>> openSet,
            PathNode<S> bestNode) {
        PathNode<S> currentNode = openSet.poll();
        Set<S> children = currentNode.state.children();

        if (children.isEmpty() || currentNode.depth > MAX_DEPTH_LIMIT) {  // Depth limit for example
            if (bestNode == null || currentNode.compareTo(bestNode) > 0) {
                bestNode = currentNode;
            }
            return bestNode;
        }

        for (S child : children) {
            if (!currentNode.containsState(child)) {
                float childScore = child.score();
                float currentScore = currentNode.state.score();
                float childHeuristic = child.heuristic();
                float cost = currentNode.cost + (currentNode.heuristic - childHeuristic);
                PathNode<S> newNode = new PathNode<>(currentNode, child, cost);
                openSet.add(newNode);
            }
        }
        return bestNode;
    }

    private static class PathNode<S extends TwoPersonGameState<S>> implements Comparable<PathNode<S>> {
        PathNode<S> parent;
        S state;
        float cost;
        float heuristic;
        float estimatedTotalScore;
        int depth;

        public PathNode(PathNode<S> parent, S state, float cost) {
            this.parent = parent;
            this.state = state;
            this.cost = cost;
            this.depth = (parent == null) ? 0 : parent.depth + 1;
            this.heuristic = state.heuristic();
            this.estimatedTotalScore = this.cost + this.heuristic;  // f = g + h
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

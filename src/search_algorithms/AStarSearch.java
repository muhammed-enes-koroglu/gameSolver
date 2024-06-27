package search_algorithms;

import java.util.Set;

import interfaces.TwoPersonGameState;
import util.Node;

public class AStarSearch {

    private AStarSearch() {
        throw new IllegalStateException("Utility class");
    }

    public static <S extends TwoPersonGameState<S>> Node<S> search(S startState, int maxDepth) {
        
        Node<S> startNode = new Node<>(null, startState, 0);
        Node<S> bestNode;
        if(startState.isMaxPlayersTurn()){
            bestNode = search4Max(startNode, maxDepth, -Float.MAX_VALUE, Float.MAX_VALUE);
        } else {
            bestNode = search4Min(startNode, maxDepth, -Float.MAX_VALUE, Float.MAX_VALUE);
        }

        if(bestNode == null){
            throw new IllegalStateException("Best node is null");
        }
        System.out.println("Number of nodes expanded: " + bestNode.getNbExpansions());

        return bestNode;
    }


    private static <S extends TwoPersonGameState<S>> Node<S> search4Max(Node<S> node, int maxDepth, float alpha, float beta) {
        // Base case
        if(maxDepth == 0 || node.getState().isGameOver()){
            return node;
        }

        // For each child node, search for the best node
        Set<Node<S>> children = node.getChildren();
        Node<S> bestNode = null;
        Node<S> current;
        for(Node<S> child : children){
            current = search4Min(child, maxDepth - 1, alpha, beta);
            if(current == null){
                throw new IllegalStateException("Current node is null");
            }

            // Update the best node
            if(bestNode == null || current.getEvaluation() > bestNode.getEvaluation()){
                bestNode = current;
            }

            // Alpha-beta pruning
            if(bestNode.getEvaluation() > alpha){
                alpha = bestNode.getEvaluation();
            }
            if(beta <= alpha){
                break;
            }
        }

        // Return the best node
        return bestNode;
        
    }

    private static <S extends TwoPersonGameState<S>> Node<S> search4Min(Node<S> node, int maxDepth, float alpha, float beta) {
        // Base case
        if(maxDepth == 0 || node.getState().isGameOver()){
            return node;
        }

        // For each child node, search for the best node
        Set<Node<S>> children = node.getChildren();
        Node<S> bestNode = null;
        
        for(Node<S> child : children){
            Node<S> current = search4Max(child, maxDepth - 1, alpha, beta);

            if(current == null){
                throw new IllegalStateException("Current node is null");
            }
            // Update the best node
            if(bestNode == null || current.getEvaluation() < bestNode.getEvaluation()){
                bestNode = current;
            }

            // Alpha-beta pruning
            if(bestNode.getEvaluation() < beta){
                beta = bestNode.getEvaluation();
            }
            if(beta <= alpha){
                break;
            }
        }

        // Return the best node
        return bestNode;
    }
        
        
}

package util;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import interfaces.TwoPersonGameState;
import java.util.ArrayList;

public class Node<S extends TwoPersonGameState<S>> implements Comparable<Node<S>>{
    
    private Node<S> parent;
    private Set<Node<S>> children=null;
    private S state;
    private float cost;
    private float heuristic;
    private static int nbExpansions;

    public Node(Node<S> parent, S gameState, float cost){
        this.parent = parent;
        this.state = gameState;
        this.cost = cost;
        this.heuristic = gameState.heuristic();
    }

    /** Returns a set of child nodes.
     * If the children have already been computed, returns the cached result.
     * Otherwise, computes the child nodes based on the current state and 
     * caches the result for future use.
     *
     * @return a set of child nodes
     */
    public Set<Node<S>> getChildren(){
        if(children != null){
            return children;
        }

        nbExpansions++;
        Set<S> childStates = state.children();
        Set<Node<S>> childNodes = new HashSet<>();
        for(S childState : childStates){
            Node<S> node = new Node<>(this, childState, cost+1);
            childNodes.add(node);
        }
        children = childNodes;
        return children;
    }

    public Node<S> getParent(){
        return parent;
    }

    public S getState(){
        return state;
    }

    public List<Node<S>> getPath(){
        if (parent == null){
            return new ArrayList<>(Collections.singletonList(this));
        }
        List<Node<S>> path = parent.getPath();
        path.add(this);
        return path;
    }

    @Override
    public int compareTo(Node<S> o) {
        return Float.compare(this.cost + this.heuristic, o.cost + o.heuristic);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Node<?> node = (Node<?>) obj;
        return Objects.equals(state, node.state);
    }

    @Override
    public int hashCode() {
        return state.hashCode();
    }

    public boolean isMaximizing(){
        return state.isMaxPlayersTurn();
    }

    public Integer getNbExpansions(){
        return nbExpansions;
    }

    public float getEvaluation(){
        return cost + heuristic;
    }
}

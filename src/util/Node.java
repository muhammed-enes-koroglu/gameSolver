package util;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import interfaces.TwoPersonGameState;

public class Node<S extends TwoPersonGameState<S>> {
    
    private Node<S> parent;
    private Set<Node<S>> children=null;
    private S state;

    public Node(Node<S> parent, S childState){
        this.parent = parent;
        this.state = childState;
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

        Set<S> childStates = state.children();
        Set<Node<S>> childNodes = new HashSet<>();
        for(S childState : childStates){
            Node<S> node = new Node<>(this, childState);
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
            return Collections.singletonList(this);
        }
        List<Node<S>> path = parent.getPath();
        path.add(this);
        return path;
    }

}

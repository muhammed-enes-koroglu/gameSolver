package games.quoridor;

import util.Vector;

public class Node {
    protected final Vector position;
    protected final Node parent;
    protected final int h;
    protected final int g;
    protected final int f;

    @Override
    public boolean equals(Object obj) {
        if(obj == null){
            return false;
        }
        if(!(obj instanceof Node)){
            return false;
        }
        Node other = (Node) obj;
        return this.position.equals(other.position);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + this.position.hashCode();
        return hash;
    }

    public Node(Vector position, Node parent, int h, int g){
        this.position = position;
        this.parent = parent;
        this.h = h;
        this.g = g;
        this.f = h + g;
    }
}

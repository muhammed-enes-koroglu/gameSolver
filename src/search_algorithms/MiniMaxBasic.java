package search_algorithms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MiniMaxBasic {
    private MiniMaxBasic(){
    }
    
    public static List<Node> search(Node node, boolean isMax){
        Node resultNode = searchNode(node, isMax);
        return resultNode.getPath();
    }

    private static LeafNode searchNode(Node node, boolean isMax){
        // Base case
        if(node instanceof LeafNode){
            return (LeafNode) node;
        }
        if(!(node instanceof InternalNode)){
            throw new IllegalArgumentException("Node must be either a LeafNode or an InternalNode");
        }

        // Recursive case
        InternalNode currentNode = (InternalNode) node;
        LeafNode leftResultNode = searchNode(currentNode.getLeftChild(), !isMax);
        LeafNode rightResultNode = searchNode(currentNode.getRightChild(), !isMax);
         
        if(isMax){
            return maxNode(leftResultNode, rightResultNode);
        }
        return minNode(leftResultNode, rightResultNode);
    }

    public static LeafNode maxNode(LeafNode node1, LeafNode node2){
        return node1.getValue() > node2.getValue() ? node1 : node2;
    }

    public static LeafNode minNode(LeafNode node1, LeafNode node2){
        return node1.getValue() < node2.getValue() ? node1 : node2;
    }
    
    private Node createTree(int[] values){
        int height = (int) (Math.log(values.length) / Math.log(2) + 1);

        if (height == 1) {
            return new LeafNode(null, values[0]);
        }
        return createTree(values, null, height);
    }

    private Node createTree(int[] values, Node parent, int height){

        if(height == 1) {
            return new LeafNode(parent, values[0]);
        }

        InternalNode node = new InternalNode(parent);
        int[] leftValues = new int[values.length / 2];
        int[] rightValues = new int[values.length / 2];

        // Split the values array into two arrays
        System.arraycopy(values, 0, leftValues, 0, values.length / 2);
        System.arraycopy(values, values.length / 2, rightValues, 0, values.length / 2);

        // Recursively create the left and right children
        Node leftChild = createTree(leftValues, node, height - 1);
        Node rightChild = createTree(rightValues, node, height - 1);

        node.setLeftChild(leftChild);
        node.setRightChild(rightChild);

        return node;
    }

    private class Node {
        private final Node parent;

        public Node(Node parent){
            this.parent = parent;
        }

        public Node getParent(){
            return parent;
        }

        public List<Node> getPath(){
            if (parent == null) {
                return new ArrayList<>(Collections.singletonList(this));
            }
            List<Node> path = parent.getPath();
            path.add(this);
            return path;
        }

        public int getValue(){
            throw new UnsupportedOperationException();
        }
    }

    private class LeafNode extends Node {
        private final int value;
    
        public LeafNode(Node parent, int value) {
            super(parent);
            this.value = value;
        }

        public int getValue(){
            return value;
        }

        @Override
        public String toString(){
            return "LeafNode{" +
                    "value=" + value +
                    '}';
        }
    }

    private class InternalNode extends Node {
        private Node leftChild;
        private Node rightChild;
    
        public InternalNode(Node parent) {
            super(parent);
        }
        
        public void setLeftChild(Node leftChild){
            this.leftChild = leftChild;
        }

        public void setRightChild(Node rightChild){
            this.rightChild = rightChild;
        }

        public Node getLeftChild(){
            return leftChild;
        }

        public Node getRightChild(){
            return rightChild;
        }

        @Override
        public String toString(){
            return "InternalNode{" +
                    "\nleftChild=" + leftChild +
                    ", \nrightChild=" + rightChild +
                    '}';
        }
    }
    
     public static void main(String[] args) {

        MiniMaxBasic miniMax = new MiniMaxBasic();
        int[] values = {3, 6, 2, 9, 1, 8, 5, 7};
        Node root = miniMax.createTree(values);
        List<Node> result = MiniMaxBasic.search(root, true);
        int lastElement = result.get(result.size() - 1).getValue();
        System.out.println(lastElement);
        System.out.println(result.size());
        System.out.println(result.get(0));
        
    }
}

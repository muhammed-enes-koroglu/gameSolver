package search_algorithms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;

import util.Board;
import util.Vector;

public class AStarBasic {
    private AStarBasic(){
    }
    
    public static List<Node> search(Node start, Node goal, Board board){

        PriorityQueue<Node> openSet = new PriorityQueue<>((n1, n2) -> n1.getF() - n2.getF());
        openSet.add(start);

        // Rest of the A* search algorithm implementation
        Node currentNode = start;
        while(currentNode != null && !currentNode.equals(goal)) {
            openSet.addAll(currentNode.getNeighbors(board));
            currentNode = openSet.poll();
        }

        if (currentNode == null) {
            return Collections.emptyList();
        }
        return currentNode.getPath(); // Placeholder return statement
    }
    
    private class Node {
        private Vector position;
        private int g;
        private int h;
        private int f;
        private Node goal;
        private Node parent;

        public Node(Vector position, int g, Node goal, Node parent){
            this.position = position;
            this.goal = goal;
            this.parent = parent;
            this.g = g;
            this.h = goal == null ? 0 : manhattanDistance();
            this.f = g + h;
        }

        public Vector getPosition(){
            return position;
        }

        public int getF(){
            return f;
        }

        public Node getParent(){
            return parent;
        }

        @Override
        public boolean equals(Object obj){
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            Node node = (Node) obj;
            return this.position.equals(node.getPosition());
        }
        
        @Override
        public int hashCode(){
            return position.hashCode();
        }

        public int manhattanDistance(){
            return Math.abs(this.position.getX() - this.goal.getPosition().getX()) + Math.abs(this.position.getY() - goal.getPosition().getY());
        }

        public List<Node> getNeighbors(Board board) {

            ArrayList<Node> neighbors = new ArrayList<>();
            Vector[] directions = {Vector.UP, Vector.DOWN, Vector.LEFT, Vector.RIGHT};

            for (Vector direction : directions) {
                Vector neighborPosition = this.position.plus(direction);
                if (board.locationIsOutOfBounds(neighborPosition)) {
                    continue;
                }
                int neighborG = this.g + board.get(neighborPosition);
                Node neighbor = new Node(neighborPosition, neighborG, this.goal, this);
                neighbors.add(neighbor);
            }
            return neighbors;
        }

        public List<Node> getPath() {
            ArrayList<Node> path = new ArrayList<>();
            Node current = this;
            while (current != null) {
                path.add(current);
                current = current.getParent();
            }
            Collections.reverse(path);
            return path;
        }
    }

    public static void testAStarPath(){
        System.out.println("[TESTING] AStarPath");
    
        Board board = new Board(new int[][]{
            {1, 1, 1, 9},
            {9, 9, 1, 9},
            {1, 1, 1, 9},
            {1, 9, 9, 9},
            {1, 1, 1, 1}
        });
    
        // Create start and goal nodes
        AStarBasic pathFinder = new AStarBasic();
        AStarBasic.Node goal = pathFinder.new Node(new Vector(board.nbRows-1, board.nbCols-1), 0, null, null);
        AStarBasic.Node start = pathFinder.new Node(new Vector(0, 0), 0, goal, null);
    
        // Search for path
        List<AStarBasic.Node> path = AStarBasic.search(start, goal, board);

        // Print path
        System.out.println("Path: ");
        for (AStarBasic.Node node : path) { 
            System.out.println(node.getPosition());
        }

        System.out.println("[TESTING] AStarPath COMPLETED");
    }

    public static void main(String[] args){
        testAStarPath();
    }

}

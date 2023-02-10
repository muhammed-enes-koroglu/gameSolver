package games.quoridor;

import java.util.ArrayList;
import java.util.PriorityQueue;

import util.Board;
import util.Vector;

/** Class to find *a* path from the player's position
 *  to the respective finish line. */
public abstract class QuoridorPathFinder {
    

    public boolean pathExists(Board board, Vector startingVector, int targetRow){

        Node start = new Node(startingVector, null, heuristic(startingVector, targetRow), 0);

        PriorityQueue<Node> openList = new PriorityQueue<>();
        ArrayList<Node> closedList = new ArrayList<>();
        openList.add(start);

        while(!openList.isEmpty()){
            Node current = openList.poll();

            if(isTarget(current.position, targetRow)){
                return true;
            }

            // Cyce through all adjacent nodes
            for(Vector child : getAdjacent(board, current.position)){
                Node childNode = new Node(child, current, heuristic(child, targetRow), current.g + 1);

                if(!openList.contains(childNode) && !closedList.contains(childNode)){
                    openList.add(childNode);
                }
            }

            closedList.add(current);
        }
        return false;
    }

    /** Return the heuristic for the given vector. */
    private int heuristic(Vector current, int targetRow){
        return Math.abs(current.row - targetRow);
    }

    /** Return true if the given vector is on the target row. */
    private boolean isTarget(Vector current, int targetRow){
        return current.row == targetRow;
    }

    /** Return all adjacent empty squares. */
    private ArrayList<Vector> getAdjacent(Board board, Vector current){

        ArrayList<Vector> adjacent = new ArrayList<>();
        for(int i = 0; i < 4; i++){
            Vector v = current.plus(Vector.CARDINAL_DIRECTIONS[i].times(2));
            if(!board.locationIsOutOfBounds(v) && board.get(v) == QuoridorState.EMPTY){
                adjacent.add(v);
            }
        }
        
        return adjacent;
    }

}

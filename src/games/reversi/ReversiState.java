package games.reversi;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import util.Vector;
import util.Board;
import util.ConsoleColors;
import util.PrintBoardGame;
import util.TwoPersonGameState;

public class ReversiState implements TwoPersonGameState<ReversiState>{

    private final Board board;
    protected final boolean whitesTurn;
    protected final boolean maximizeForWhite;
    protected final int nbWhitesOnBoard;
    protected final int nbBlacksOnBoard;
    protected final boolean whitesPlayedLastTurn;
    protected final boolean blacksPlayedLastTurn;

    public static final int BOARD_SIZE = 8;
    public static final String BACKGROUND_WHITE = ConsoleColors.WHITE_BACKGROUND;
    public static final String BACKGROUND_BLACK = ConsoleColors.BLACK_BACKGROUND;
    public static final int WHITE = 1;
    public static final int BLACK = -1;
    public static final int EMPTY = 0;

    private static final int MULTITHREADING_THRESHOLD = BOARD_SIZE * BOARD_SIZE / 2;
    private static final int MULTITHREADED_GROUP_SIZE = 16;
    @Override
    public Set<ReversiState> children() {

        if(isGameOver()) return new HashSet<>();

        Set<ReversiState> children = getChildren();

        if(children.isEmpty()){
            Set<ReversiState> passingState = new HashSet<>();
            passingState.add(new ReversiState(this.board, true, true));
            return passingState;
        }

        return children;

    }

    private Set<ReversiState> getChildren() {

        Set<ReversiState> children;
        if(nbWhitesOnBoard + nbBlacksOnBoard > MULTITHREADING_THRESHOLD){
            children = getChildrenInParallel();
        } else{
            children = getChildrenInSerial();
        }
        return children;
    }

    private Set<ReversiState> getChildrenInSerial() {

        int playersPiece = whitesTurn ? WHITE : BLACK;
        HashSet<ReversiState> childrenSet = new HashSet<>();
        for(int row=0; row<BOARD_SIZE; row++){
            for(int col=0; col<BOARD_SIZE; col++){
                if(board.get(row, col) == playersPiece){
                    addChildrenToTheRight(row, col, childrenSet);
                    addChildrenToTheLeft(row, col, childrenSet);
                    addChildrenToTheTop(row, col, childrenSet);
                    addChildrenToTheBottom(row, col, childrenSet);
                    addChildrenToTheTopRight(row, col, childrenSet);
                    addChildrenToTheTopLeft(row, col, childrenSet);
                    addChildrenToTheBottomRight(row, col, childrenSet);
                    addChildrenToTheBottomLeft(row, col, childrenSet);
                }
            }
            }
        return childrenSet;
    }


    private Set<ReversiState> getChildrenInParallel() {

        int playersPiece = whitesTurn ? WHITE : BLACK;

        // Initialize the sets of children.
        ArrayList<HashSet<ReversiState>> childrenSets = new ArrayList<>();
        for(int i = 0; i < BOARD_SIZE; i++){
            childrenSets.add(new HashSet<>());
        }

        // Find own positions.
        ArrayList<Vector> playablePositions = findPlayablePositions(playersPiece);

        // Group positions into sets of 8.
        // Each position is put into only one grouping.
        ArrayList<HashSet<Vector>> groupingsOfPositions = new ArrayList<>();
        createGroups(playablePositions, groupingsOfPositions, MULTITHREADED_GROUP_SIZE);

        ArrayList<Thread> threads = initializeThreads(playersPiece, childrenSets, groupingsOfPositions);

        return joinThreads(childrenSets, threads);
    }

    
    /** Wait for all threads to finish.
     * Combine all children sets into one.
     * @throws InterruptedException
     */
    private HashSet<ReversiState> joinThreads(ArrayList<HashSet<ReversiState>> childrenSets, ArrayList<Thread> threads) {
        HashSet<ReversiState> children = new HashSet<>();
        for(int i = 0; i < threads.size(); i++){

            // Wait for the thread to finish.
            try {
                threads.get(i).join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            HashSet<ReversiState> childrenSet = childrenSets.get(i);
            children.addAll(childrenSet);
        }
        return children;
    }

    /** For each piece on the board, get the children of the piece.
     * Do this in parallel.
     */
    private ArrayList<Thread> initializeThreads(int playersPiece, ArrayList<HashSet<ReversiState>> childrenSets,
            ArrayList<HashSet<Vector>> groupingsOfPositions) {

        // Keep track of the threads.
        ArrayList<Thread> threads = new ArrayList<>();
        for(int groupNumber=0; groupNumber<groupingsOfPositions.size(); groupNumber++){

            // We need a final variable for the lambda expression.
            final Integer groupNumberFinal = groupNumber;

            // Create a thread for this group.
            // The thread will get the children of the pieces in this group.
            threads.add(new Thread(
                () -> getChildrenOfGroup(groupingsOfPositions.get(groupNumberFinal), childrenSets.get(groupNumberFinal), playersPiece)
            ));
            threads.get(groupNumber).start();
        }
        return threads;
    }

    /** Find positions with pieces belonging to whomever's turn it is. */
    private ArrayList<Vector> findPlayablePositions(int playersPiece) {
        ArrayList<Vector> playablePositions = new ArrayList<>();
        for(int row=0; row<BOARD_SIZE; row++){
            for(int col=0; col<BOARD_SIZE; col++){
                if(board.get(row, col) == playersPiece){
                    playablePositions.add(new Vector(row, col));
                }
            }
        }
        return playablePositions;
    }

    /** Group positions into sets of 8.
     * Each position is put into only one grouping.
     */
    private void createGroups(ArrayList<Vector> playablePositions, ArrayList<HashSet<Vector>> groupingsOfPositions,
            int groupSize) {
        for(int i = 0; i < playablePositions.size(); i += groupSize){
            HashSet<Vector> grouping = new HashSet<>();
            for(int j = 0; j < groupSize; j++){
                if(i + j < playablePositions.size()){
                    grouping.add(playablePositions.get(i + j));
                }
            }
            groupingsOfPositions.add(grouping);
        }
    }

    private void getChildrenOfGroup(HashSet<Vector> group, HashSet<ReversiState> childrenSet, int playersPiece) {

        // For each piece in the group, 
        // add the children of the piece to `childrenSet`.
        for(Vector position : group){
            int row = position.row;
            int col = position.col;

            // Add the children of the piece.
            if(board.get(row, col) == playersPiece){
                addChildrenToTheRight(row, col, childrenSet);
                addChildrenToTheLeft(row, col, childrenSet);
                addChildrenToTheTop(row, col, childrenSet);
                addChildrenToTheBottom(row, col, childrenSet);
                addChildrenToTheTopRight(row, col, childrenSet);
                addChildrenToTheTopLeft(row, col, childrenSet);
                addChildrenToTheBottomRight(row, col, childrenSet);
                addChildrenToTheBottomLeft(row, col, childrenSet);
            }
        }

    }

    private void addChildrenToTheRight(int initialRow, int initialCol, HashSet<ReversiState> children) {

        // Check if there is a piece of the opposite color to the right.
        // If there is, then check if there is an empty space after that
        // If there is, add a child with a new piece of the same color in the empty space
        // And flip all the pieces in between
        
        Vector initialVector = new Vector(initialRow, initialCol);
        Vector currentVector = initialVector.plus(Vector.RIGHT);
        boolean foundOppositeColor = false;

        // Check if there is a piece of the opposite color to the left.
        while(!board.locationIsOutOfBounds(currentVector) && board.get(currentVector) == -board.get(initialRow, initialCol)){
            currentVector = currentVector.plus(Vector.RIGHT);
            foundOppositeColor = true;
        }
        // If there is, then check if there is an empty space after that
        if(!board.locationIsOutOfBounds(currentVector) && board.get(currentVector) == EMPTY && foundOppositeColor){
            Board newBoard = board.copy();

            // Add a child with a new piece of the same color in the empty space
            newBoard.set(currentVector, board.get(initialRow, initialCol));

            // And flip all the pieces in between
            int nbFlippedPieces = flipPieces(newBoard, currentVector);
            addChild(children, newBoard, nbFlippedPieces);
        }

    }

    private void addChildrenToTheLeft(int initialRow, int initialCol, HashSet<ReversiState> children) {

        // Check if there is a piece of the opposite color to the left.
        // If there is, then check if there is an empty space after that
        // If there is, add a child with a new piece of the same color in the empty space
        // And flip all the pieces in between
        
        Vector initialVector = new Vector(initialRow, initialCol);
        Vector currentVector = initialVector.plus(Vector.LEFT);
        boolean foundOppositeColor = false;

        // Check if there is a piece of the opposite color to the left.
        while(!board.locationIsOutOfBounds(currentVector) && board.get(currentVector) == -board.get(initialRow, initialCol)){
            currentVector = currentVector.plus(Vector.LEFT);
            foundOppositeColor = true;
        }
        // If there is, then check if there is an empty space after that
        if(!board.locationIsOutOfBounds(currentVector) && board.get(currentVector) == EMPTY && foundOppositeColor){
            Board newBoard = board.copy();

            // Add a child with a new piece of the same color in the empty space
            newBoard.set(currentVector, board.get(initialRow, initialCol));

            // And flip all the pieces in between
            int nbFlippedPieces = flipPieces(newBoard, currentVector);
            addChild(children, newBoard, nbFlippedPieces);
        }

    }

    private void addChildrenToTheTop(int initialRow, int initialCol, HashSet<ReversiState> children) {

        // Check if there is a piece of the opposite color to the top.
        // If there is, then check if there is an empty space after that
        // If there is, add a child with a new piece of the same color in the empty space
        // And flip all the pieces in between
        
        Vector initialVector = new Vector(initialRow, initialCol);
        Vector currentVector = initialVector.plus(Vector.UP);
        boolean foundOppositeColor = false;

        // Check if there is a piece of the opposite color to the left.
        while(!board.locationIsOutOfBounds(currentVector) && board.get(currentVector) == -board.get(initialRow, initialCol)){
            currentVector = currentVector.plus(Vector.UP);
            foundOppositeColor = true;
        }
        // If there is, then check if there is an empty space after that
        if(!board.locationIsOutOfBounds(currentVector) && board.get(currentVector) == EMPTY && foundOppositeColor){
            Board newBoard = board.copy();

            // Add a child with a new piece of the same color in the empty space
            newBoard.set(currentVector, board.get(initialRow, initialCol));

            // And flip all the pieces in between
            int nbFlippedPieces = flipPieces(newBoard, currentVector);
            addChild(children, newBoard, nbFlippedPieces);
        }

    }

    private void addChildrenToTheBottom(int initialRow, int initialCol, HashSet<ReversiState> children) {

        // Check if there is a piece of the opposite color to the bottom.
        // If there is, then check if there is an empty space after that
        // If there is, add a child with a new piece of the same color in the empty space
        // And flip all the pieces in between
        
        Vector initialVector = new Vector(initialRow, initialCol);
        Vector currentVector = initialVector.plus(Vector.DOWN);
        boolean foundOppositeColor = false;

        // Check if there is a piece of the opposite color to the left.
        while(!board.locationIsOutOfBounds(currentVector) && board.get(currentVector) == -board.get(initialRow, initialCol)){
            currentVector = currentVector.plus(Vector.DOWN);
            foundOppositeColor = true;
        }
        // If there is, then check if there is an empty space after that
        if(!board.locationIsOutOfBounds(currentVector) && board.get(currentVector) == EMPTY && foundOppositeColor){
            Board newBoard = board.copy();

            // Add a child with a new piece of the same color in the empty space
            newBoard.set(currentVector, board.get(initialRow, initialCol));

            // And flip all the pieces in between
            int nbFlippedPieces = flipPieces(newBoard, currentVector);
            addChild(children, newBoard, nbFlippedPieces);
        }

    }

    private void addChildrenToTheTopRight(int initialRow, int initialCol, HashSet<ReversiState> children) {

        // Check if there is a piece of the opposite color to the top right.
        // If there is, then check if there is an empty space after that
        // If there is, add a child with a new piece of the same color in the empty space
        // And flip all the pieces in between
        Vector initialVector = new Vector(initialRow, initialCol);
        Vector currentVector = initialVector.plus(Vector.UP_RIGHT);
        boolean foundOppositeColor = false;

        // Check if there is a piece of the opposite color to the left.
        while(!board.locationIsOutOfBounds(currentVector) && board.get(currentVector) == -board.get(initialVector)){
            currentVector = currentVector.plus(Vector.UP_RIGHT);
            foundOppositeColor = true;
        }
        // If there is, then check if there is an empty space after that
        if(!board.locationIsOutOfBounds(currentVector) && board.get(currentVector) == EMPTY && foundOppositeColor){
            Board newBoard = board.copy();

            // Add a child with a new piece of the same color in the empty space
            newBoard.set(currentVector, board.get(initialVector));

            // And flip all the pieces in between
            int nbFlippedPieces = flipPieces(newBoard, currentVector);
            addChild(children, newBoard, nbFlippedPieces);
        }

    }

    private void addChildrenToTheTopLeft(int initialRow, int initialCol, HashSet<ReversiState> children) {

        // Check if there is a piece of the opposite color to the top left.
        // If there is, then check if there is an empty space after that
        // If there is, add a child with a new piece of the same color in the empty space
        // And flip all the pieces in between
        Vector initialVector = new Vector(initialRow, initialCol);
        Vector currentVector = initialVector.plus(Vector.UP_LEFT);
        boolean foundOppositeColor = false;

        // Check if there is a piece of the opposite color to the left.
        while(!board.locationIsOutOfBounds(currentVector) && board.get(currentVector) == -board.get(initialVector)){
            currentVector = currentVector.plus(Vector.UP_LEFT);
            foundOppositeColor = true;
        }
        // If there is, then check if there is an empty space after that
        if(!board.locationIsOutOfBounds(currentVector) && board.get(currentVector) == EMPTY && foundOppositeColor){
            Board newBoard = board.copy();

            // Add a child with a new piece of the same color in the empty space
            newBoard.set(currentVector, board.get(initialVector));

            // And flip all the pieces in between
            int nbFlippedPieces = flipPieces(newBoard, currentVector);
            addChild(children, newBoard, nbFlippedPieces);
        }

    }

    private void addChildrenToTheBottomRight(int initialRow, int initialCol, HashSet<ReversiState> children) {

        // Check if there is a piece of the opposite color to the top right.
        // If there is, then check if there is an empty space after that
        // If there is, add a child with a new piece of the same color in the empty space
        // And flip all the pieces in between
        Vector initialVector = new Vector(initialRow, initialCol);
        Vector currentVector = initialVector.plus(Vector.DOWN_RIGHT);
        boolean foundOppositeColor = false;

        // Check if there is a piece of the opposite color to the left.
        while(!board.locationIsOutOfBounds(currentVector) && board.get(currentVector) == -board.get(initialVector)){
            currentVector = currentVector.plus(Vector.DOWN_RIGHT);
            foundOppositeColor = true;
        }
        // If there is, then check if there is an empty space after that
        if(!board.locationIsOutOfBounds(currentVector) && board.get(currentVector) == EMPTY && foundOppositeColor){
            Board newBoard = board.copy();

            // Add a child with a new piece of the same color in the empty space
            newBoard.set(currentVector, board.get(initialVector));

            // And flip all the pieces in between
            int nbFlippedPieces = flipPieces(newBoard, currentVector);
            addChild(children, newBoard, nbFlippedPieces);
        }

    }

    private void addChildrenToTheBottomLeft(int initialRow, int initialCol, HashSet<ReversiState> children) {

        // Check if there is a piece of the opposite color to the top left.
        // If there is, then check if there is an empty space after that
        // If there is, add a child with a new piece of the same color in the empty space
        // And flip all the pieces in between
        Vector initialVector = new Vector(initialRow, initialCol);
        Vector currentVector = initialVector.plus(Vector.DOWN_LEFT);
        boolean foundOppositeColor = false;

        // Check if there is a piece of the opposite color to the left.
        while(!board.locationIsOutOfBounds(currentVector) && board.get(currentVector) == -board.get(initialVector)){
            currentVector = currentVector.plus(Vector.DOWN_LEFT);
            foundOppositeColor = true;
        }
        // If there is, then check if there is an empty space after that
        if(!board.locationIsOutOfBounds(currentVector) && board.get(currentVector) == EMPTY && foundOppositeColor){
            Board newBoard = board.copy();

            // Add a child with a new piece of the same color in the empty space
            newBoard.set(currentVector, board.get(initialVector));

            // And flip all the pieces in between
            int nbFlippedPieces = flipPieces(newBoard, currentVector);
            addChild(children, newBoard, nbFlippedPieces);
        }

    }

    /**
     * @param children  The set of children to add to
     * @param newBoard  The board to add to the children
     * @param nbAddedPieces The number of pieces added to the board
     */
    private void addChild(HashSet<ReversiState> children, Board newBoard, int nbAddedPieces) {
        if(whitesTurn){
            children.add(new ReversiState(
                newBoard, 
                false, 
                maximizeForWhite, 
                nbWhitesOnBoard + nbAddedPieces + 1, 
                nbBlacksOnBoard - nbAddedPieces, 
                true, 
                this.blacksPlayedLastTurn));
        } else{
            children.add(new ReversiState(
                newBoard, 
                true, 
                maximizeForWhite, 
                nbWhitesOnBoard - nbAddedPieces, 
                nbBlacksOnBoard + nbAddedPieces + 1, 
                this.whitesPlayedLastTurn, 
                true));
        }
    }

    // Flippers
    protected static int flipPieces(Board newBoard, Vector finalVector) {
        int nbFlippedPieces = 0;
        nbFlippedPieces += flipPiecesToTheTop(newBoard, finalVector);
        nbFlippedPieces += flipPiecesToTheBottom(newBoard, finalVector);
        nbFlippedPieces += flipPiecesToTheLeft(newBoard, finalVector);
        nbFlippedPieces += flipPiecesToTheRight(newBoard, finalVector);
        nbFlippedPieces += flipPiecesToTheTopLeft(newBoard, finalVector);
        nbFlippedPieces += flipPiecesToTheTopRight(newBoard, finalVector);
        nbFlippedPieces += flipPiecesToTheBottomLeft(newBoard, finalVector);
        nbFlippedPieces += flipPiecesToTheBottomRight(newBoard, finalVector);

        return nbFlippedPieces;
    }

    private static int flipPiecesToTheTop(Board newBoard, Vector finalVector) {
        Vector currentVector = finalVector.plus(Vector.UP);
        boolean foundOppositeColor = false;
        int nbFlippedPieces = 0;

        // Check if there is a piece of the opposite color to the top.
        while(!newBoard.locationIsOutOfBounds(currentVector) && newBoard.get(currentVector) == -newBoard.get(finalVector)){
            currentVector = currentVector.plus(Vector.UP);
            foundOppositeColor = true;
        }
        // If there is, then check if there is a friendly piece after that
        if(!newBoard.locationIsOutOfBounds(currentVector) && newBoard.get(currentVector) == newBoard.get(finalVector) && foundOppositeColor){
            // And flip all the pieces in between
            for(Vector v=finalVector.plus(Vector.UP); v.row<currentVector.row; v=v.plus(Vector.UP)){
                newBoard.set(v, newBoard.get(finalVector));
                nbFlippedPieces++;
            }
        }
        return nbFlippedPieces;
    }

    private static int flipPiecesToTheBottom(Board newBoard, Vector finalVector) {
        Vector currentVector = finalVector.plus(Vector.DOWN);
        boolean foundOppositeColor = false;
        int nbFlippedPieces = 0;

        // Check if there is a piece of the opposite color to the bottom.
        while(!newBoard.locationIsOutOfBounds(currentVector) && newBoard.get(currentVector) == -newBoard.get(finalVector)){
            currentVector = currentVector.plus(Vector.DOWN);
            foundOppositeColor = true;
        }
        // If there is, then check if there is a friendly piece after that
        if(!newBoard.locationIsOutOfBounds(currentVector) && newBoard.get(currentVector) == newBoard.get(finalVector) && foundOppositeColor){
            // And flip all the pieces in between
            for(Vector v=finalVector.plus(Vector.DOWN); v.row>currentVector.row; v=v.plus(Vector.DOWN)){
                newBoard.set(v, newBoard.get(finalVector));
                nbFlippedPieces++;
            }
        }
        return nbFlippedPieces;
    }

    private static int flipPiecesToTheLeft(Board newBoard, Vector finalVector) {
        Vector currentVector = finalVector.plus(Vector.LEFT);
        boolean foundOppositeColor = false;
        int nbFlippedPieces = 0;

        // Check if there is a piece of the opposite color to the left.
        while(!newBoard.locationIsOutOfBounds(currentVector) && newBoard.get(currentVector) == -newBoard.get(finalVector)){
            currentVector = currentVector.plus(Vector.LEFT);
            foundOppositeColor = true;
        }
        // If there is, then check if there is a friendly piece after that
        if(!newBoard.locationIsOutOfBounds(currentVector) && newBoard.get(currentVector) == newBoard.get(finalVector) && foundOppositeColor){
            // And flip all the pieces in between
            for(Vector v=finalVector.plus(Vector.LEFT); v.col>currentVector.col; v=v.plus(Vector.LEFT)){
                newBoard.set(v, newBoard.get(finalVector));
                nbFlippedPieces++;
            }
        }
        return nbFlippedPieces;
    }

    private static int flipPiecesToTheRight(Board newBoard, Vector finalVector) {
        Vector currentVector = finalVector.plus(Vector.RIGHT);
        boolean foundOppositeColor = false;
        int nbFlippedPieces = 0;

        // Check if there is a piece of the opposite color to the right.
        while(!newBoard.locationIsOutOfBounds(currentVector) && newBoard.get(currentVector) == -newBoard.get(finalVector)){
            currentVector = currentVector.plus(Vector.RIGHT);
            foundOppositeColor = true;
        }
        // If there is, then check if there is a friendly piece after that
        if(!newBoard.locationIsOutOfBounds(currentVector) && newBoard.get(currentVector) == newBoard.get(finalVector) && foundOppositeColor){
            // And flip all the pieces in between
            for(Vector v=finalVector.plus(Vector.RIGHT); v.col<currentVector.col; v=v.plus(Vector.RIGHT)){
                newBoard.set(v, newBoard.get(finalVector));
                nbFlippedPieces++;
            }
        }
        return nbFlippedPieces;
    }

    private static int flipPiecesToTheTopLeft(Board newBoard, Vector finalVector) {
        Vector currentVector = finalVector.plus(Vector.UP_LEFT);
        boolean foundOppositeColor = false;
        int nbFlippedPieces = 0;

        // Check if there is a piece of the opposite color to the top left.
        while(!newBoard.locationIsOutOfBounds(currentVector) && newBoard.get(currentVector) == -newBoard.get(finalVector)){
            currentVector = currentVector.plus(Vector.UP_LEFT);
            foundOppositeColor = true;
        }
        // If there is, then check if there is a friendly piece after that
        if(!newBoard.locationIsOutOfBounds(currentVector) && newBoard.get(currentVector) == newBoard.get(finalVector) && foundOppositeColor){
            // And flip all the pieces in between
            for(Vector v=finalVector.plus(Vector.UP_LEFT); v.row<currentVector.row && v.col>currentVector.col; v=v.plus(Vector.UP_LEFT)){
                newBoard.set(v, newBoard.get(finalVector));
                nbFlippedPieces++;
            }
        }
        return nbFlippedPieces;
    }

    private static int flipPiecesToTheTopRight(Board newBoard, Vector finalVector) {
        Vector currentVector = finalVector.plus(Vector.UP_RIGHT);
        boolean foundOppositeColor = false;
        int nbFlippedPieces = 0;

        // Check if there is a piece of the opposite color to the top right.
        while(!newBoard.locationIsOutOfBounds(currentVector) && newBoard.get(currentVector) == -newBoard.get(finalVector)){
            currentVector = currentVector.plus(Vector.UP_RIGHT);
            foundOppositeColor = true;
        }
        // If there is, then check if there is a friendly piece after that
        if(!newBoard.locationIsOutOfBounds(currentVector) && newBoard.get(currentVector) == newBoard.get(finalVector) && foundOppositeColor){
            // And flip all the pieces in between
            for(Vector v=finalVector.plus(Vector.UP_RIGHT); v.row<currentVector.row && v.col<currentVector.col; v=v.plus(Vector.UP_RIGHT)){
                newBoard.set(v, newBoard.get(finalVector));
                nbFlippedPieces++;
            }
        }
        return nbFlippedPieces;
    }

    private static int flipPiecesToTheBottomLeft(Board newBoard, Vector finalVector) {
        Vector currentVector = finalVector.plus(Vector.DOWN_LEFT);
        boolean foundOppositeColor = false;
        int nbFlippedPieces = 0;

        // Check if there is a piece of the opposite color to the bottom left.
        while(!newBoard.locationIsOutOfBounds(currentVector) && newBoard.get(currentVector) == -newBoard.get(finalVector)){
            currentVector = currentVector.plus(Vector.DOWN_LEFT);
            foundOppositeColor = true;
        }
        // If there is, then check if there is a friendly piece after that
        if(!newBoard.locationIsOutOfBounds(currentVector) && newBoard.get(currentVector) == newBoard.get(finalVector) && foundOppositeColor){
            // And flip all the pieces in between
            for(Vector v=finalVector.plus(Vector.DOWN_LEFT); v.row>currentVector.row && v.col>currentVector.col; v=v.plus(Vector.DOWN_LEFT)){
                newBoard.set(v, newBoard.get(finalVector));
                nbFlippedPieces++;
            }
        }
        return nbFlippedPieces;
    }

    private static int flipPiecesToTheBottomRight(Board newBoard, Vector finalVector) {
        Vector currentVector = finalVector.plus(Vector.DOWN_RIGHT);
        boolean foundOppositeColor = false;
        int nbFlippedPieces = 0;

        // Check if there is a piece of the opposite color to the bottom right.
        while(!newBoard.locationIsOutOfBounds(currentVector) && newBoard.get(currentVector) == -newBoard.get(finalVector)){
            currentVector = currentVector.plus(Vector.DOWN_RIGHT);
            foundOppositeColor = true;
        }
        // If there is, then check if there is a friendly piece after that
        if(!newBoard.locationIsOutOfBounds(currentVector) && newBoard.get(currentVector) == newBoard.get(finalVector) && foundOppositeColor){
            // And flip all the pieces in between
            for(Vector v=finalVector.plus(Vector.DOWN_RIGHT); v.row>currentVector.row && v.col<currentVector.col; v=v.plus(Vector.DOWN_RIGHT)){
                newBoard.set(v, newBoard.get(finalVector));
                nbFlippedPieces++;
            }
        }
        return nbFlippedPieces;
    }

    @Override
    public float score() {

        int nbOurPieces = maximizeForWhite ? nbWhitesOnBoard : nbBlacksOnBoard;
        int nbOpponentPieces = maximizeForWhite ? nbBlacksOnBoard : nbWhitesOnBoard;

        // If game ended, and we have more
        // pieces than the opponent, we win.
        if(isGameOver()){
            if(nbOurPieces > nbOpponentPieces){
                return TwoPersonGameState.MAX_SCORE;
            } else if(nbOurPieces < nbOpponentPieces){
                return TwoPersonGameState.MIN_SCORE;
            } else {
                return 0;
            }
        }else{
            return (float) (nbOurPieces - nbOpponentPieces);
        }
    }

    protected boolean isGameOver() {
        // If there are no more empty spaces, 
        // or if both players have not been able
        //  to make a move the last turn, 
        // the game is over

        if(nbWhitesOnBoard + nbBlacksOnBoard == BOARD_SIZE * BOARD_SIZE){
            return true;
        } else {
            return !whitesPlayedLastTurn && !blacksPlayedLastTurn;
        }
    }

    @Override
    public boolean isMaxPlayersTurn() {
        return !(this.maximizeForWhite ^ this.whitesTurn);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null){
            return false;
        }
        if(obj == this){
            return true;
        }
        if(!(obj instanceof ReversiState)){
            return false;
        }
        ReversiState other = (ReversiState) obj;
        return this.board.equals(other.board) && this.whitesTurn == other.whitesTurn;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + this.board.hashCode();
        hash = 29 * hash + (this.whitesTurn ? 1 : 0);
        return hash;
    }


    public String toString(){
        return toString(ConsoleColors.RESET);
    }

    @Override
    public String toString(String backgroundColor){

        // Print the board using the PrintBoardGame class.
        String[] pieceRepresentations = new String[]{ 
            "?", 
            BACKGROUND_BLACK + "O " + backgroundColor,
            "  ", 
            BACKGROUND_WHITE + "X " + backgroundColor,  
            "?" };
        
        String turnMarker = this.whitesTurn ? BACKGROUND_WHITE + "X" : BACKGROUND_BLACK + "O";
        turnMarker += backgroundColor;
        return PrintBoardGame.toString(this.board, pieceRepresentations, turnMarker, backgroundColor);

    }

    @Override
    public Board getBoard() {
        return board.copy();
    }
    
    public ReversiState(Board board, boolean whitesTurn, boolean maximizeForWhite, int nbWhitesOnBoard, int nbBlacksOnBoard, boolean whitesPlayedLastTurn, boolean blacksPlayedLastTurn){
        if(board.nbRows != BOARD_SIZE)
            throw new IllegalArgumentException("Board height must be " + BOARD_SIZE);
        if(board.nbCols != BOARD_SIZE)
            throw new IllegalArgumentException("Board width must be " + BOARD_SIZE);

        this.board = board;
        this.whitesTurn = whitesTurn;
        this.maximizeForWhite = maximizeForWhite;
        this.nbWhitesOnBoard = nbWhitesOnBoard;
        this.nbBlacksOnBoard = nbBlacksOnBoard;
        this.whitesPlayedLastTurn = whitesPlayedLastTurn;
        this.blacksPlayedLastTurn = blacksPlayedLastTurn;
    }

    public ReversiState(Board board, boolean whitesTurn, boolean maximizeForWhite){
        if(board.nbRows != BOARD_SIZE)
            throw new IllegalArgumentException("Board height must be " + BOARD_SIZE);
        if(board.nbCols != BOARD_SIZE)
            throw new IllegalArgumentException("Board width must be " + BOARD_SIZE);

        int nbWhites = 0;
        int nbBlacks = 0;
        for(int row=0; row<BOARD_SIZE; row++){
            for(int col=0; col<BOARD_SIZE; col++){
                if(board.get(row, col) == WHITE){
                    nbWhites++;
                }
                else if(board.get(row, col) == BLACK){
                    nbBlacks++;
                }
            }
        }

        this.board = board;
        this.whitesTurn = whitesTurn;
        this.maximizeForWhite = maximizeForWhite;
        this.nbWhitesOnBoard = nbWhites;
        this.nbBlacksOnBoard = nbBlacks;
        this.whitesPlayedLastTurn = true;
        this.blacksPlayedLastTurn = true;

    }
}

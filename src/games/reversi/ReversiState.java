package games.reversi;

import java.util.HashSet;
import java.util.Set;

import interfaces.TwoPersonGameState;
import util.Vector;
import util.Board;
import util.ConsoleColors;
import util.PrintBoardGame;

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

    @Override
    /**
     * Serial: 7.27 depth
     * Parallel 4 Threads: 6.16 depth
     * Parallel 8 Threads: 6.24 depth
     * Parallel 12 Threads: 6.30 depth
     */
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

        int playersPiece = whitesTurn ? WHITE : BLACK;
        HashSet<ReversiState> childrenSet = new HashSet<>();

        // For each piece of the player, add the children.
        for(int row=0; row<BOARD_SIZE; row++){
            for(int col=0; col<BOARD_SIZE; col++){
                if(board.get(row, col) == playersPiece){
                    addChildrenForPosition(childrenSet, row, col);
                }
            }
        }

        return childrenSet;
    }

    /** Add all children for a given position */
    private void addChildrenForPosition(Set<ReversiState> childrenSet, int row, int col) {
        Vector initialVector = new Vector(row, col);
        Vector[] directions = Vector.getAllDirections();
        for(Vector direction : directions){
            addChildrenInDirection(initialVector, direction, childrenSet);
        }
    }

    private void addChildrenInDirection(Vector initialVector, Vector direction, Set<ReversiState> childrenSet) {

        // Check if there is a piece of the opposite color in the given direction.
        // If there is, then check if there is an empty space after that
        // If there is, add a child with a new piece of the same color in the empty space
        // And flip all the pieces in between
        
        Vector currentVector = initialVector.plus(direction);
        boolean foundOppositeColor = false;

        // Check if there is a piece of the opposite color in the given direction.
        while(!board.locationIsOutOfBounds(currentVector) && board.get(currentVector) == -board.get(initialVector)){
            currentVector = currentVector.plus(direction);
            foundOppositeColor = true;
        }
        // If there is, then check if there is an empty space after that
        if(!board.locationIsOutOfBounds(currentVector) && board.get(currentVector) == EMPTY && foundOppositeColor){
            Board newBoard = board.copy();

            // Add a child with a new piece of the same color in the empty space
            newBoard.set(currentVector, board.get(initialVector));

            // And flip all the pieces in between
            int nbFlippedPieces = flipPieces(newBoard, currentVector);
            addChild(childrenSet, newBoard, nbFlippedPieces);
        }

    }


    /**
     * @param childrenSet  The set of children to add to
     * @param newBoard  The board to add to the children
     * @param nbAddedPieces The number of pieces added to the board
     */
    private void addChild(Set<ReversiState> childrenSet, Board newBoard, int nbAddedPieces) {
        if(whitesTurn){
            childrenSet.add(new ReversiState(
                newBoard, 
                false, 
                maximizeForWhite, 
                nbWhitesOnBoard + nbAddedPieces + 1, 
                nbBlacksOnBoard - nbAddedPieces, 
                true, 
                this.blacksPlayedLastTurn));
        } else{
            childrenSet.add(new ReversiState(
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
    protected static int flipPieces(Board newBoard, Vector squareToMakeMoveOn) {
        int nbFlippedPieces = 0;
        for (Vector direction : Vector.getAllDirections()) {
            nbFlippedPieces += flipPiecesInDirection(newBoard, squareToMakeMoveOn, direction);
        }

        return nbFlippedPieces;
    }


    /** Generalized method to flip pieces in a given direction on the board.
     * @param newBoard The game board on which pieces will be flipped.
     * @param squareToMakeMoveOn The starting vector position from where to begin flipping.
     * @param direction The direction vector in which to flip pieces.
     * @return The number of pieces flipped in this operation.
     */
    public static int flipPiecesInDirection(Board newBoard, Vector squareToMakeMoveOn, Vector direction) {
        Vector currentSquare = squareToMakeMoveOn.plus(direction);
        boolean foundOppositeColor = false;
        int nbFlippedPieces = 0;

        // Check if there is a piece of the opposite color in the given direction.
        while (!newBoard.locationIsOutOfBounds(currentSquare) && newBoard.get(currentSquare) == -newBoard.get(squareToMakeMoveOn)) {
            currentSquare = currentSquare.plus(direction);
            foundOppositeColor = true;
        }

        // Check if there is a friendly piece after the line of opposite pieces.
        if (foundOppositeColor &&
        !newBoard.locationIsOutOfBounds(currentSquare) && 
        newBoard.get(currentSquare) == newBoard.get(squareToMakeMoveOn)) {
            // Flip all the pieces in between.
            for (Vector v = squareToMakeMoveOn.plus(direction); 
            !v.equals(currentSquare); 
            v = v.plus(direction)) {
                newBoard.set(v, newBoard.get(squareToMakeMoveOn));
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
                return MAX_SCORE;
            } else if(nbOurPieces < nbOpponentPieces){
                return MIN_SCORE;
            } else {
                return 0;
            }
        }else{
            return (nbOurPieces - nbOpponentPieces);
        }
    }

    @Override
    public boolean isGameOver() {
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
        return maximizeForWhite == whitesTurn;
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

    public String toInitString() {
        StringBuilder sb = new StringBuilder();
        sb.append("new ReversiState(new Board(new int[][]{");
        for (int i = 0; i < board.nbRows; i++) {
            sb.append("{");
            for (int j = 0; j < board.nbCols; j++) {
                sb.append(board.get(i, j));
                if (j < board.nbCols - 1) {
                    sb.append(", ");
                }
            }
            sb.append("}");
            if (i < board.nbRows - 1) {
                sb.append(", ");
            }
        }
        sb.append("}), ");
        sb.append(whitesTurn ? "true" : "false");
        sb.append(", ");
        sb.append(maximizeForWhite ? "true" : "false");
        sb.append(");");
        return sb.toString();
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

    @Override
    public float getAvgGameLength() {
        return 59;
    }
}

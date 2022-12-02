package games.reversi;

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

    @Override
    public Set<ReversiState> children() {

        if(gameOver()) return new HashSet<>();

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


    private void addChildrenToTheRight(int initialRow, int initialCol, HashSet<ReversiState> children) {

        // Check if there is a piece of the opposite color to the right.
        // If there is, then check if there is an empty space after that
        // If there is, add a child with a new piece of the same color in the empty space
        // And flip all the pieces in between
        
        int row = initialRow;
        int col = initialCol + 1;
        boolean foundOppositeColor = false;

        // Check if there is a piece of the opposite color to the left.
        while(col < BOARD_SIZE && board.get(row, col) == -board.get(initialRow, initialCol)){
            col++;
            foundOppositeColor = true;
        }
        // If there is, then check if there is an empty space after that
        if(col < BOARD_SIZE && board.get(row, col) == EMPTY && foundOppositeColor){
            Board newBoard = board.copy();

            // Add a child with a new piece of the same color in the empty space
            newBoard.set(row, col, board.get(initialRow, initialCol));
            int nbAddedPieces = 0;
            // And flip all the pieces in between
            for(int c=initialCol+1; c<col; c++){
                newBoard.set(row, c, board.get(initialRow, initialCol));
                nbAddedPieces++;
            }
            addChild(children, newBoard, nbAddedPieces);
        }

    }

    private void addChildrenToTheLeft(int initialRow, int initialCol, HashSet<ReversiState> children) {

        // Check if there is a piece of the opposite color to the left.
        // If there is, then check if there is an empty space after that
        // If there is, add a child with a new piece of the same color in the empty space
        // And flip all the pieces in between
        
        int row = initialRow;
        int col = initialCol - 1;
        boolean foundOppositeColor = false;

        // Check if there is a piece of the opposite color to the left.
        while(col >= 0 && board.get(row, col) == -board.get(initialRow, initialCol)){
            col--;
            foundOppositeColor = true;
        }
        // If there is, then check if there is an empty space after that
        if(col >= 0 && board.get(row, col) == EMPTY && foundOppositeColor){
            Board newBoard = board.copy();

            // Add a child with a new piece of the same color in the empty space
            newBoard.set(row, col, board.get(initialRow, initialCol));
            int nbAddedPieces = 0;

            // And flip all the pieces in between
            for(int c=initialCol-1; c>col; c--){
                newBoard.set(row, c, board.get(initialRow, initialCol));
                nbAddedPieces++;
            }
            addChild(children, newBoard, nbAddedPieces);

        }

    }

    private void addChildrenToTheTop(int initialRow, int initialCol, HashSet<ReversiState> children) {

        // Check if there is a piece of the opposite color to the left.
        // If there is, then check if there is an empty space after that
        // If there is, add a child with a new piece of the same color in the empty space
        // And flip all the pieces in between
        
        int row = initialRow - 1;
        int col = initialCol;
        boolean foundOppositeColor = false;

        // Check if there is a piece of the opposite color to the left.
        while(row >= 0 && board.get(row, col) == -board.get(initialRow, initialCol)){
            row--;
            foundOppositeColor = true;
        }
        // If there is, then check if there is an empty space after that
        if(row >= 0 && board.get(row, col) == EMPTY && foundOppositeColor){
            Board newBoard = board.copy();

            // Add a child with a new piece of the same color in the empty space
            newBoard.set(row, col, board.get(initialRow, initialCol));
            int nbAddedPieces = 0;

            // And flip all the pieces in between
            for(int r=initialRow-1; r>row; r--){
                newBoard.set(r, col, board.get(initialRow, initialCol));
                nbAddedPieces++;
            }
            addChild(children, newBoard, nbAddedPieces);
        }

    }

    private void addChildrenToTheBottom(int initialRow, int initialCol, HashSet<ReversiState> children) {

        // Check if there is a piece of the opposite color to the left.
        // If there is, then check if there is an empty space after that
        // If there is, add a child with a new piece of the same color in the empty space
        // And flip all the pieces in between
        
        int row = initialRow + 1;
        int col = initialCol;
        boolean foundOppositeColor = false;

        // Check if there is a piece of the opposite color to the left.
        while(row < BOARD_SIZE && board.get(row, col) == -board.get(initialRow, initialCol)){
            row++;
            foundOppositeColor = true;
        }
        // If there is, then check if there is an empty space after that
        if(row < BOARD_SIZE && board.get(row, col) == EMPTY && foundOppositeColor){
            Board newBoard = board.copy();

            // Add a child with a new piece of the same color in the empty space
            newBoard.set(row, col, board.get(initialRow, initialCol));
            int nbAddedPieces = 0;

            // And flip all the pieces in between
            for(int r=initialRow+1; r<row; r++){
                newBoard.set(r, col, board.get(initialRow, initialCol));
                nbAddedPieces++;
            }
            addChild(children, newBoard, nbAddedPieces);
        }

    }

    private void addChildrenToTheTopRight(int initialRow, int initialCol, HashSet<ReversiState> children) {

        // Check if there is a piece of the opposite color to the left.
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
            int nbAddedPieces = 0;

            // And flip all the pieces in between
            for(Vector v=initialVector.plus(Vector.UP_RIGHT); 
                    v.row<currentVector.row && v.col<currentVector.col; 
                    v=v.plus(Vector.UP_RIGHT)){
                newBoard.set(v, board.get(initialVector));
                nbAddedPieces++;
            }
            addChild(children, newBoard, nbAddedPieces);
        }

    }

    private void addChildrenToTheTopLeft(int initialRow, int initialCol, HashSet<ReversiState> children) {

        // Check if there is a piece of the opposite color to the left.
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
            int nbAddedPieces = 0;

            // And flip all the pieces in between
            for(Vector v=initialVector.plus(Vector.UP_LEFT); 
                    v.row<currentVector.row && v.col>currentVector.col; 
                    v=v.plus(Vector.UP_LEFT)){
                newBoard.set(v, board.get(initialVector));
                nbAddedPieces++;
            }
            addChild(children, newBoard, nbAddedPieces);
        }

    }

    private void addChildrenToTheBottomRight(int initialRow, int initialCol, HashSet<ReversiState> children) {

        // Check if there is a piece of the opposite color to the left.
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
            int nbAddedPieces = 0;

            // And flip all the pieces in between
            for(Vector v=initialVector.plus(Vector.DOWN_RIGHT); 
                    v.row>currentVector.row && v.col<currentVector.col; 
                    v=v.plus(Vector.DOWN_RIGHT)){
                newBoard.set(v, board.get(initialVector));
                nbAddedPieces++;
            }
            addChild(children, newBoard, nbAddedPieces);
        }

    }

    private void addChildrenToTheBottomLeft(int initialRow, int initialCol, HashSet<ReversiState> children) {

        // Check if there is a piece of the opposite color to the left.
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
            int nbAddedPieces = 0;

            // And flip all the pieces in between
            for(Vector v=initialVector.plus(Vector.DOWN_LEFT); 
                    v.row>currentVector.row && v.col>currentVector.col; 
                    v=v.plus(Vector.DOWN_LEFT)){
                newBoard.set(v, board.get(initialVector));
                nbAddedPieces++;
            }
            addChild(children, newBoard, nbAddedPieces);
        }

    }

    /**
     * 
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

    @Override
    public float score() {

        int nbOurPieces = maximizeForWhite ? nbWhitesOnBoard : nbBlacksOnBoard;
        int nbOpponentPieces = maximizeForWhite ? nbBlacksOnBoard : nbWhitesOnBoard;

        // If game ended, and we have more
        // pieces than the opponent, we win.
        if(gameOver()){
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

    private boolean gameOver() {
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
            BACKGROUND_BLACK + "O" + backgroundColor,
            " ", 
            BACKGROUND_WHITE + "X" + backgroundColor,  
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

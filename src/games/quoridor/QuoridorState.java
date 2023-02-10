package games.quoridor;


import java.util.Map;
import java.util.Set;

import util.Board;
import util.ConsoleColors;
import util.PrintBoardGame;
import util.Vector;
import util.TwoPersonGameState;

public class QuoridorState implements TwoPersonGameState<QuoridorState> {

    protected static final int PAWN_MOVE_UNIT_SIZE = 2;
    protected static final int WALL_SIZE = 3;
    protected static final int EFFECTIVE_BOARD_SIZE = 9;
    protected static final int BOARD_SIZE = 2 * EFFECTIVE_BOARD_SIZE - 1;
    protected static final int TARGET_ROW_WHITE = BOARD_SIZE - 1;
    protected static final int TARGET_ROW_BLACK = 0;

    protected static final int EMPTY = 0;
    protected static final int WALL = 1;
    protected static final int WALLABLE = -1;
    protected static final int WHITE = 2;
    protected static final int BLACK = -2;

    protected static final String BACKGROUND_WHITE = ConsoleColors.WHITE_BACKGROUND;
    protected static final String BACKGROUND_BLACK = ConsoleColors.BLACK_BACKGROUND;

    protected final int whitesRemainingWalls;
    protected final int blacksRemainingWalls;
    protected final Vector whitePawnPosition;
    protected final Vector blackPawnPosition;
    protected final Board board;

    protected final boolean whitesTurn;
    protected final boolean maximizeForWhite;
    
    
    @Override
    public Set<QuoridorState> children() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public float score() {
        // TODO Auto-generated method stub
        return 0;
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
        if(!(obj instanceof QuoridorState)){
            return false;
        }
        QuoridorState other = (QuoridorState) obj;
        return this.board.equals(other.board) && 
            this.whitePawnPosition.equals(other.whitePawnPosition) && 
            this.blackPawnPosition.equals(other.blackPawnPosition) && 
            this.whitesRemainingWalls == other.whitesRemainingWalls && 
            this.blacksRemainingWalls == other.blacksRemainingWalls;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + this.board.hashCode();
        hash = 29 * hash + this.whitePawnPosition.hashCode();
        hash = 29 * hash + this.blackPawnPosition.hashCode();
        hash = 29 * hash + this.whitesRemainingWalls;
        hash = 29 * hash + this.blacksRemainingWalls;
        return hash;
    }

    public String toString(){
        return toString(ConsoleColors.RESET);
    }

    @Override
    public String toString(String backgroundColor){

        // Print the board using the PrintBoardGame class.
        Map<Integer, String> pieceRepresentations = Map.of(
            BLACK, BACKGROUND_BLACK + "O" + backgroundColor,
            WALLABLE, ConsoleColors.BLUE_BACKGROUND + " " + backgroundColor,
            EMPTY, " ",
            WALL, ConsoleColors.RED_BACKGROUND + " " + backgroundColor,
            WHITE, BACKGROUND_WHITE + "X" + backgroundColor
        );
        
        String turnMarker = this.whitesTurn ? BACKGROUND_WHITE + "X" : BACKGROUND_BLACK + "O";
        turnMarker += backgroundColor;
        return PrintBoardGame.toString(this.board, pieceRepresentations, turnMarker, backgroundColor, false);

    }

    @Override
    public Board getBoard() {
        return board.copy();
    }
    
    //* Empty constructor. Returns the initial state. */
    public QuoridorState() {
        this.whitesRemainingWalls = 10;
        this.blacksRemainingWalls = 10;

        // Pawns start in the middle of the board, on opposite sides.
        this.whitePawnPosition = new Vector(0, (BOARD_SIZE - 1) / 2);
        this.blackPawnPosition = new Vector(BOARD_SIZE-1, (BOARD_SIZE - 1) / 2);

        this.board = getInitialBoard();
        this.board.set(whitePawnPosition, WHITE);
        this.board.set(blackPawnPosition, BLACK);

        this.whitesTurn = true;
        this.maximizeForWhite = true;
    }

    public QuoridorState(int whitesRemainingWalls, int blacksRemainingWalls, Vector whitePawnPosition, Vector blackPawnPosition, Board board, boolean whitesTurn, boolean maximizeForWhite) {
        if(board.nbCols != BOARD_SIZE || board.nbRows != BOARD_SIZE) {
            throw new IllegalArgumentException("`board` must be of size " + BOARD_SIZE + "x" + BOARD_SIZE);
        }
        if(whitePawnPosition.row < 0 || whitePawnPosition.row >= BOARD_SIZE || whitePawnPosition.col < 0 || whitePawnPosition.col >= BOARD_SIZE) {
            throw new IllegalArgumentException("`whitePawnPosition` must be within the board");
        }
        if(blackPawnPosition.row < 0 || blackPawnPosition.row >= BOARD_SIZE || blackPawnPosition.col < 0 || blackPawnPosition.col >= BOARD_SIZE) {
            throw new IllegalArgumentException("`blackPawnPosition` must be within the board");
        }
        if(whitesRemainingWalls < 0 || blacksRemainingWalls < 0) {
            throw new IllegalArgumentException("Remaining walls must be non-negative");
        }
        if(whitePawnPosition.row == blackPawnPosition.row && whitePawnPosition.col == blackPawnPosition.col) {
            throw new IllegalArgumentException("Pawns must not be on the same position");
        }
        if(whitePawnPosition.row % PAWN_MOVE_UNIT_SIZE != 0 || whitePawnPosition.col % PAWN_MOVE_UNIT_SIZE != 0) {
            throw new IllegalArgumentException("whites pawn must be on a movable position");
        }
        if(board.get(whitePawnPosition) != WHITE) {
            throw new IllegalArgumentException("white's pawn must be on `whitePawnPosition`");
        }
        if(board.get(blackPawnPosition) != BLACK) {
            throw new IllegalArgumentException("black's pawn must be on `blackPawnPosition`");
        }

        this.whitesRemainingWalls = whitesRemainingWalls;
        this.blacksRemainingWalls = blacksRemainingWalls;
        this.whitePawnPosition = whitePawnPosition;
        this.blackPawnPosition = blackPawnPosition;
        this.board = board;

        this.whitesTurn = whitesTurn;
        this.maximizeForWhite = maximizeForWhite;
    }

    /** Returns a new board with the initial configuration. */
    protected static Board getInitialBoard() {
        Board initialBoard = new Board(new int[BOARD_SIZE][BOARD_SIZE]);
        for(int row = 0; row < BOARD_SIZE; row++) {
            for(int col = 0; col < BOARD_SIZE; col++) {
                if(row % 2 != 0 || col % 2 != 0) {
                    initialBoard.set(row, col, WALLABLE);
                }
            }
        }

        return initialBoard;
    }

    public Vector getWhitePawnPosition() {
        return whitePawnPosition;
    }

    public Vector getBlackPawnPosition() {
        return blackPawnPosition;
    }

}

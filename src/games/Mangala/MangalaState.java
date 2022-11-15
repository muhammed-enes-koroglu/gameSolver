package games.Mangala;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import util.Board;
import util.TwoPersonGameState;


/*  #MangalaState
Mangala, from game family mancala, is a two person game featuring 
6 trenches and 1 treasury on each side. Game starts with all trenches 
filled with 4 stones and follows the rules stated below. The game ends 
when a player's trenches are all empty. Player with most stones in 
their treasury wins.

    1-The game is turn based.
    2-Each player must play one trench at their turn. 
    3-It is then the other player's turn. Except for rule 5.
    4-To play a trench, one takes ALL the stones in that trench and 
        starts placing them in trenches one by one in counter-clockwise
        rotation, starting from the trench they are taken from (Yes, 
        this means that the starting trench will have 1 stone left in 
        it after being played.). It is important where the last stone is placed.
    
    5-Play again if ended at own treasury.
    6-Place stone in your treasury on pass, but not in enemy's treasury.
    7-Take own and trench opposite to it if ended at own empty trench.
    8-Take enemy trench if ended at enemy trench and made it even.
    9-Take enemy's all trenches if your side is empty.

The rules seem complicated, but the game clears itself after a few games.

A playing interface is not yet implemented. This class only describes 
how the game after any given turn can look like.  
*/
public class MangalaState implements TwoPersonGameState<MangalaState>{

    private final Board board;
    protected final boolean whiteIsMax;
    protected final boolean whitesTurn;
    public static final int BOARD_SIZE = 7;
    public static final int WHITE_STARTING_TRENCH = 0;
    public static final int BLACK_STARTING_TRENCH = BOARD_SIZE;
    

    @Override
    public Set<MangalaState> children() {
        
        HashSet<MangalaState> children = new HashSet<>();
        int colorOffset = getOffsetForColor(this.whitesTurn);

        for(int trench = colorOffset; trench < BOARD_SIZE + colorOffset-1; trench++){
            if(this.board.get(0, trench) != 0){ // Don't play empty trench
                Board childBoard = this.board.copy();
                playTrench(children, trench, childBoard);
            }
        }

        return children;
    }

    /** Plays the given trench and adds the created child to `children`.
     * 
     * @param children The set of children to add the created child to.
     * @param trench The trench to play.
     * @param childBoard The board to play the trench on.
    */
    protected MangalaState playTrench(HashSet<MangalaState> children, int trench, Board childBoard) {
        boolean nextIsWhitesTurn;
        int colorOffset = getOffsetForColor(this.whitesTurn);

        int endedAt = spreadTrench(childBoard, trench);
        nextIsWhitesTurn = !this.whitesTurn;

        if(endedAtTreasury(endedAt, colorOffset)){ // Ended at treasury
            nextIsWhitesTurn = this.whitesTurn;
            emptyBoardToFinisherIfGameOver(childBoard);
            children.add(new MangalaState(childBoard, nextIsWhitesTurn, this.whiteIsMax));
        }
        else 
            adjustBoardAfterSpreading(endedAt, colorOffset, nextIsWhitesTurn, childBoard, children);

        return new MangalaState(childBoard, nextIsWhitesTurn, nextIsWhitesTurn);

}

    @Override
    public float score() {
        float score = 0;
        score += this.board.get(0, ownTreasury(getOffsetForColor(this.whiteIsMax)));
        score -= this.board.get(0, ownTreasury(getOffsetForColor(!this.whiteIsMax)));
        
        float ownPosition = (float) (0.1 * positionalScore(this.whiteIsMax));
        float opponentPosition = (float) (0.1 * positionalScore(!this.whiteIsMax));
        
        if(ownPosition + opponentPosition == 0)
            return Math.signum(score) * Float.MAX_VALUE; 
        score += ownPosition;
        score -= opponentPosition;
        // score += 
        return score;
    }
    
    @Override
    public boolean equals(Object o) {
        if(!(o instanceof MangalaState))
            return false;
        MangalaState other = (MangalaState) o;
        return (this.board.equals(other.board)) && 
            (other.whitesTurn == this.whitesTurn);
    }

    @Override
    public int hashCode(){
        int hash = 7;
        hash = 31 * hash + (board == null ? 0 : board.hashCode());
        hash = 31 * hash + (whitesTurn ? 1 : 0);
        return hash;
    }

    @Override
    public boolean isMaxPlayersTurn() {
        return !(whiteIsMax ^ whitesTurn);
    }

    @Override
    public String toString(){
        String str = "\n ";
        String turn = "   <<";
        for(int i= 2*BOARD_SIZE-2; i>BOARD_SIZE-1; i--)
            str += " " + this.board.get(0, i);
        str += !this.whitesTurn ? turn : "";
        str += "\n" + this.board.get(0, 2*BOARD_SIZE-1) + "             " + this.board.get(0, BOARD_SIZE-1) + "\n ";
        for(int i=0; i<BOARD_SIZE-1; i++)
            str += " " + this.board.get(0, i);
        str += this.whitesTurn ? turn : "";
        return str + "\n";        
    }

    @Override
    public Board getBoard(){
        return this.board.copy();
    }

    /**
     * 
     * @param board 1D board object with 1 row and 2*BOARD_SIZE columns.
     * @param whitesTurn
     * @param whiteIsMax
     */
    public MangalaState(Board board, boolean whitesTurn, boolean whiteIsMax){
        if(board.nbCols != 2 * BOARD_SIZE)
            throw new IllegalArgumentException("`board` must contain 14 trenches");
        if(!Arrays.stream(board.getMatrix()[0]).allMatch(i -> i >= 0))
            throw new IllegalArgumentException("number of stones in trenches must be positive");
        if(isWhiteSideEmpty(board) ^ isBlackSideEmpty(board))
            throw new IllegalArgumentException("If one side is empty, so should the other be");

        this.board = board.copy();
        this.whitesTurn = whitesTurn;
        this.whiteIsMax = whiteIsMax;
    }

    // Help Methods..

    /** Spreads the stones in the given trench 
     * in every trench going forward from the 
     * given one, one by one. This except if the trench
     * contains only one stone, in which case it is
     * given to the next trench.
     * 
     * @return the index of the last stone placed.
     * @param trench the index of the trench to play.
     * @param childBoard the board to play on.
     */
    private static int spreadTrench(Board childBoard, int trench){
        if(childBoard.get(0, trench) == 0)
            return trench;
        
        if(childBoard.get(0, trench) == 1){
            childBoard.set(0, trench, 0);
            childBoard.increment(0, trench + 1, 1);
            return trench + 1;
        }

        int opponentTreasury = trench < BOARD_SIZE ? 2 * BOARD_SIZE -1 : BOARD_SIZE -1;
        int stones = childBoard.get(0, trench) - 1;
        childBoard.set(0, trench, 1);
        trench++;
        for(; stones > 0; trench++)
            if(trench % (2 * BOARD_SIZE) != opponentTreasury){
                stones--;
                childBoard.increment(0, trench % childBoard.nbCols, 1);
            }
        return (--trench) % (2 * BOARD_SIZE);
    }

    /** 
     * @return Index of the trench directly 
     * opposite to the given trench when looked 
     * from above. 
     * @param trench Index of the trench to get the opposite of.
    */
    private static int oppositeTrenchOf(int trench){
        return 2 * BOARD_SIZE -2 - trench;
    }

    /** 
     * @return Whether the given treasury is color's own treasury. 
     * @param colorOffset getOffsetForColor(isWhite)
     * @param endedAt Index of the last stone placed.
    */
    private static boolean endedAtTreasury(int endedAt, int colorOffset){
        return endedAt == ownTreasury(colorOffset);
    }

    /** 
     * @return Index of own treasury. 
     * @param colorOffset getOffsetForColor(isWhite)
    */
    private static int ownTreasury(int colorOffset) {
        return BOARD_SIZE -1 + colorOffset;
    }
    
    /**
     * @return Whether the trench is one 
     * that belongs to the color.
     * @param colorOffset getOffsetForColor(isWhite)
     */
    private static boolean endedAtOwnTrench(int endedAt, int colorOffset){
        return colorOffset <= endedAt && endedAt < BOARD_SIZE + colorOffset;
    }
    
    /** Does one of the following 2 things if applicable:
     * 1. If the last stone was placed in an empty trench
     *    on the player's side, the stones in the opposite
     *    trench are taken and added to the player's treasury.
     * 2. If the last stone was placed in a trench on the
     *    opponent's side, and that trench now contains an even
     *    number of stones, the stones in that trench are taken.
     * 
     * @param endedAt Index of the last stone placed.
     * @param colorOffset getOffsetForColor(isWhite)
     * @param childWhitesTurn Whether it is whites turn in the child state.
     * @param childBoard The board to play on.
     * @param children The set of children to add the created child to.
     */
    private void adjustBoardAfterSpreading(int endedAt, int colorOffset, boolean childWhitesTurn, Board childBoard, HashSet<MangalaState> children){
        
        // Checks for Situation 1.
        if(endedAtOwnTrench(endedAt, colorOffset)){ // Ended at own trench
            if((childBoard.get(0, endedAt) == 1) && (childBoard.get(0, oppositeTrenchOf(endedAt)) > 0)){ // Trench is empty
                childBoard.increment(0, 
                    ownTreasury(colorOffset), 
                    childBoard.get(0, endedAt) + childBoard.get(0, oppositeTrenchOf(endedAt)));
                childBoard.set(0, endedAt, 0);
                childBoard.set(0, oppositeTrenchOf(endedAt), 0);
            }
        }
        // Checks for Situation 2.
        else if(childBoard.get(0, endedAt) % 2 == 0){ // Ended at enemy trench and is even
            childBoard.increment(0, ownTreasury(colorOffset), childBoard.get(0, endedAt));
            childBoard.set(0, endedAt, 0);
        }

        emptyBoardToFinisherIfGameOver(childBoard);
        
        children.add(new MangalaState(childBoard, childWhitesTurn, this.whiteIsMax));            
    }

    /** Adds the remaining stones to the treasury of the player 
     * who has finished the first.
     * 
     * @param childBoard The board to play on.
     */
    private void emptyBoardToFinisherIfGameOver(Board childBoard) {
        if(isWhiteSideEmpty(childBoard)) // White's side is empty
            for(int i=BOARD_SIZE; i<= 2*BOARD_SIZE-2; i++){ // Black's side gets slurped
                childBoard.increment(0, BOARD_SIZE-1, childBoard.get(0, i));
                childBoard.set(0, i, 0);
            }
        else if(isBlackSideEmpty(childBoard)) // Black's side is empty
            for(int i=0; i<= BOARD_SIZE-2; i++){ // White's side gets slurped
                childBoard.increment(0, 2*BOARD_SIZE-1, childBoard.get(0, i));
                childBoard.set(0, i, 0);
            }
    }

    /**
     * @param aBoard The board to check.
     * @return Whether the black side (excluding the treasury) is empty.
     */
    private boolean isBlackSideEmpty(Board aBoard) {

        for(int i=BOARD_SIZE; i< 2*BOARD_SIZE-1; i++)
            if(aBoard.get(0, i) > 0)
                return false;

        return true;
    }

    /**
     * @param aBoard The board to check.
     * @return Whether the white side (excluding the treasury) is empty.
     */
    private boolean isWhiteSideEmpty(Board aBoard) {

        for(int i=0; i<BOARD_SIZE-1; i++)
            if(aBoard.get(0, i) > 0)
                return false;

        return true;
    }

    /** Returns the starting trench of the player.
     * 
     * @param isWhite Whether the player is white.
     * @return 0 if black, BOARD_SIZE if white.
     */
    protected static int getOffsetForColor(boolean isWhite) {
        return isWhite ? 0 : BOARD_SIZE;
    }

    /**
     * @param isWhite Whether the player is white.
     * @return A score representing how good the 
     * current state is for the given player.
     */
    private int positionalScore(boolean isWhite){
        int score = 0;
        int colorOffset = getOffsetForColor(isWhite);
        for(int i=0; i<BOARD_SIZE-1; i++){
            score += this.board.get(0, i + colorOffset) * (i+1);
        }
        return score;
    }

    public static void testPrivateMethods(){

        testPlayTrench();

    }

    private static void testPlayTrench(){

        Board board = new Board(new int[][]{ new int[]{0,5,0, 0,0,0, 0, 0,0,0, 0,0,0, 0}});
        int endedAt = spreadTrench(board, 1);
        assert Arrays.equals(board.getMatrix()[0], new int[]{0,1,1, 1,1,1, 0, 0,0,0, 0,0,0, 0});
        assert endedAt == 5;

        board = new Board(new int[][]{ new int[]{0,6,2, 3,0,0, 0, 0,0,0, 0,0,0, 0}});
        endedAt = spreadTrench(board, 1);
        assert Arrays.equals(board.getMatrix()[0], new int[]{0,1,3, 4,1,1, 1, 0,0,0, 0,0,0, 0});
        assert endedAt == 6;

        board = new Board(new int[][]{ new int[]{0,6,2, 3,0,0, 0, 0,0,0, 0,0,0, 0}});
        endedAt = spreadTrench(board, 1);
        assert Arrays.equals(board.getMatrix()[0], new int[]{0,1,3, 4,1,1, 1, 0,0,0, 0,0,0, 0});
        assert endedAt == 6;

        // Doesn't put stone to opponent's treasury on pass
        board = new Board(new int[][]{ new int[]{0,0,0, 0,0,10, 0, 0,0,0, 0,0,0, 0}});
        endedAt = spreadTrench(board, 5);
        assert Arrays.equals(board.getMatrix()[0], new int[]{1,1,0, 0,0,1, 1, 1,1,1, 1,1,1, 0});
        assert endedAt == 1;
                
        System.out.println("    PlayTrench: OK");
    }

    public boolean isGameOver() {
        return isWhiteSideEmpty(this.board) || isBlackSideEmpty(this.board);
    }

    public boolean isWhitesTurn() {
        return this.whitesTurn;
    }
}

import java.util.ArrayList;
import java.util.Arrays;


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

    private final int[] board;
    private boolean whiteIsMax;
    private boolean whitesTurn;
    private static final int BOARD_SIZE = 7;

    @Override
    public MangalaState[] children() {
        
        ArrayList<MangalaState> children = new ArrayList<>();
        int turnBias = getTurnBias(this.whitesTurn);
        boolean childWhitesTurn;
        for(int trench = turnBias; trench < BOARD_SIZE + turnBias-1; trench++){
            if(this.board[trench] != 0){ // Don't play empty trench
                int[] childBoard = this.board.clone();
                int endedAt = playTrench(childBoard, trench);
                childWhitesTurn = !this.whitesTurn;

                if(endedAtTreasury(endedAt, turnBias)){ // Ended at treasury
                    childWhitesTurn = this.whitesTurn;
                    checkGameOver(childBoard);
                    children.add(new MangalaState(childBoard, childWhitesTurn, this.whiteIsMax));
                }
                else 
                    resultEndingInTrench(endedAt, turnBias, childWhitesTurn, childBoard, children);
            }
        }

        return children.toArray(new MangalaState[0]);
    }

    @Override
    public float score() {
        float score = 0;
        score += this.board[ownTreasury(getTurnBias(this.whiteIsMax))];
        score -= this.board[ownTreasury(getTurnBias(!this.whiteIsMax))];
        
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
        return (Arrays.equals(other.board, this.board)) && 
            other.whitesTurn == this.whitesTurn;
    }

    @Override
    public int hashCode(){
        int hash = 7;
        hash = 31 * hash + (board == null ? 0 : Arrays.hashCode(board));
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
        String turn = "   <";
        for(int i= 2*BOARD_SIZE-2; i>BOARD_SIZE-1; i--)
            str += " " + this.board[i];
        str += !this.whitesTurn ? turn : "";
        str += "\n" + this.board[2*BOARD_SIZE-1] + "             " + this.board[BOARD_SIZE-1] + "\n ";
        for(int i=0; i<BOARD_SIZE-1; i++)
            str += " " + this.board[i];
        str += this.whitesTurn ? turn : "";
        return str + "\n";        
    }

    public int[] getBoard(){
        return this.board.clone();
    }

    public MangalaState(int[] board, boolean whitesTurn, boolean whiteIsMax){
        if(board.length != 2 * BOARD_SIZE)
            throw new IllegalArgumentException("`board` must contain 14 trenches");
        if(!Arrays.stream(board).allMatch(i -> i >= 0))
            throw new IllegalArgumentException("number of stones in trenches must be positive");
        if(isWhiteSideEmpty(board) ^ isBlackSideEmpty(board))
            throw new IllegalArgumentException("If one side is empty, so should the other be");

        this.board = board.clone();
        this.whitesTurn = whitesTurn;
        this.whiteIsMax = whiteIsMax;
    }

    // Help Methods..

    private static int playTrench(int[] aBoard, int trench){
        if(aBoard[trench] == 0)
            return trench;
        
        if(aBoard[trench] == 1){
            aBoard[trench] = 0;
            aBoard[trench + 1] += 1;
            return trench + 1;
        }

        int opponentTreasury = trench < BOARD_SIZE ? 2 * BOARD_SIZE -1 : BOARD_SIZE -1;
        int stones = aBoard[trench] - 1;
        aBoard[trench] = 1;
        trench++;
        for(; stones > 0; trench++)
            if(trench % (2 * BOARD_SIZE) != opponentTreasury){
                stones--;
                aBoard[trench % aBoard.length]++;
            }
        return (--trench) % (2 * BOARD_SIZE);
    }

    private static int oppositeTrenchOf(int trench){
        return 2 * BOARD_SIZE -2 - trench;
    }

    private static boolean endedAtTreasury(int endedAt, int turnBias){
        return endedAt == ownTreasury(turnBias);
    }

    private static int ownTreasury(int turnBias) {
        return BOARD_SIZE -1 + turnBias;
    }
    
    private static boolean endedAtOwnTrench(int endedAt, int turnBias){
        return turnBias <= endedAt && endedAt < BOARD_SIZE + turnBias;
    }
    
    private void resultEndingInTrench(int endedAt, int turnBias, boolean childWhitesTurn, int[] childBoard, ArrayList<MangalaState> children){
        if(endedAtOwnTrench(endedAt, turnBias)){ // Ended at own trench
            if((childBoard[endedAt] == 1) && (childBoard[oppositeTrenchOf(endedAt)] > 0)){ // Trench is empty
                childBoard[ownTreasury(turnBias)] += childBoard[endedAt] + childBoard[oppositeTrenchOf(endedAt)];
                childBoard[endedAt] = 0;
                childBoard[oppositeTrenchOf(endedAt)] = 0;
            }
        }
        else if(childBoard[endedAt] % 2 == 0){ // Ended at enemy trench and is even
            childBoard[ownTreasury(turnBias)] += childBoard[endedAt];
            childBoard[endedAt] = 0;
        }

        checkGameOver(childBoard);
        
        children.add(new MangalaState(childBoard, childWhitesTurn, this.whiteIsMax));            
    }

    private void checkGameOver(int[] childBoard) {
        if(isWhiteSideEmpty(childBoard)) // White's side is empty
            for(int i=BOARD_SIZE; i<= 2*BOARD_SIZE-2; i++){ // Black's side gets slurped
                childBoard[BOARD_SIZE-1] += childBoard[i];
                childBoard[i] = 0;
            }
        else if(isBlackSideEmpty(childBoard)) // Black's side is empty
            for(int i=0; i<= BOARD_SIZE-2; i++){ // White's side gets slurped
                childBoard[2*BOARD_SIZE-1] += childBoard[i];
                childBoard[i] = 0;
            }
    }

    private boolean isBlackSideEmpty(int[] board) {
        return Arrays.stream(Arrays.copyOfRange(board, BOARD_SIZE, 2 * BOARD_SIZE -1)).sum() == 0;
    }

    private boolean isWhiteSideEmpty(int[] board) {
        return Arrays.stream(Arrays.copyOfRange(board, 0, BOARD_SIZE -1)).sum() == 0;
    }

    private int getTurnBias(boolean whitesTurn) {
        return whitesTurn ? 0 : BOARD_SIZE;
    }

    private int positionalScore(boolean isWhite){
        int score = 0;
        int turnBias = getTurnBias(isWhite);
        for(int i=0; i<BOARD_SIZE-1; i++){
            score += this.board[i + turnBias] * (i+1);
        }
        return score;
    }

    public static void testPrivateMethods(){

        testPlayTrench();

    }

    private static void testPlayTrench(){
        System.out.println("testPlayTrench..");

        int[] board = new int[]{0,5,0, 0,0,0, 0, 0,0,0, 0,0,0, 0};
        int endedAt = playTrench(board, 1);
        assert Arrays.equals(board, new int[]{0,1,1, 1,1,1, 0, 0,0,0, 0,0,0, 0});
        assert endedAt == 5;

        board = new int[]{0,6,2, 3,0,0, 0, 0,0,0, 0,0,0, 0};
        endedAt = playTrench(board, 1);
        assert Arrays.equals(board, new int[]{0,1,3, 4,1,1, 1, 0,0,0, 0,0,0, 0});
        assert endedAt == 6;

        board = new int[]{0,6,2, 3,0,0, 0, 0,0,0, 0,0,0, 0};
        endedAt = playTrench(board, 1);
        assert Arrays.equals(board, new int[]{0,1,3, 4,1,1, 1, 0,0,0, 0,0,0, 0});
        assert endedAt == 6;

        // Doesn't put stone to opponent's treasury on pass
        board = new int[]{0,0,0, 0,0,10, 0, 0,0,0, 0,0,0, 0};
        endedAt = playTrench(board, 5);
        assert Arrays.equals(board, new int[]{1,1,0, 0,0,1, 1, 1,1,1, 1,1,1, 0});
        assert endedAt == 1;
                
        System.out.println("testPlayTrench successful");
    }

}

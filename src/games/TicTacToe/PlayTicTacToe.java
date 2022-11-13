package games.TicTacToe;

import util.Board;
import util.TwoPersonPlay;

public class PlayTicTacToe implements TwoPersonPlay<TicTacToeState>{

    @Override
    public boolean isGameOver(TicTacToeState state) {
        return state.xWon() || state.xLost();
    }

    @Override
    public TicTacToeState makeMove(TicTacToeState state, int moveNumber) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public TicTacToeState getInitialState(boolean maximizeForWhite) {
        Board board = new Board( new int[]{0,0,0, 0,0,0, 0,0,0});
        return new TicTacToeState(board, maximizeForWhite);
    }

    @Override
    public int scanMoveNumber(TicTacToeState state) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public String getWinnersName(TicTacToeState state) {
        // TODO Auto-generated method stub
        return null;
    }
    
}

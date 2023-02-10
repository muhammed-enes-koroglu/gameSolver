package util;

import java.util.Map;
import java.util.stream.IntStream;

public class PrintBoardGame {

    private PrintBoardGame(){
        throw new IllegalStateException("Utility class");
    }

    /** Returns a string representation of the given board. 
     * @param board The board to print.
     * @param pieceRepresentation The representation of the pieces. 
     * Index 0 holds what is printed for the number -2, index 1 holds what is printed for the number -1,
     * index 2 holds what is printed for the number 0, index 3 holds what is printed for the number 1,
     * index 4 holds what is printed for the number 2 on the board.
    */
    public static String toString(Board board, Map<Integer, String> pieceRepresentation, String turnMarker, String background, boolean printCoordinates){
        if(pieceRepresentation.size() != 5){
            throw new IllegalArgumentException("The number of piece representations must be equal to 5");
        }
        StringBuilder str = new StringBuilder();

        // add board elements
        str.append("\n");
        str.append(getHorizontalLine(board.nbCols));
        for(int rowNb=board.nbRows-1; rowNb>=0; rowNb--){
            int[] row = board.getRow(rowNb);
            str.append(normalRowToString(row, pieceRepresentation, rowNb, printCoordinates));
        }

        // add column numbers
        if(printCoordinates){
            str.append(getHorizontalLine(board.nbCols));
            str.append(lastRowToString(IntStream.range(0, board.nbCols).toArray()));
        }
        
        // Add whose turn it is
        str.append("Turn of: " + turnMarker);

        return str.toString();
    }

    /** Returns a horizontal line of about the same size as a normal row. */
    private static String getHorizontalLine(int nbCols){
        StringBuilder result = new StringBuilder();
        int horizontalLineLength = 3*nbCols + 2;
        for(int i=0; i < horizontalLineLength; i++)
            result.append("_");
        result.append("\n");
        return result.toString();
    }

    /** Converts the row with rowNb == -1 to string. */
    private static String lastRowToString(int[] row){
        StringBuilder result = new StringBuilder();

        result.append(" | ");
        // Add elements, seperated by " | ".
        for(int element : row){
            result.append(element + "| ");
        }
        result.append("\n");

        return result.toString();
    }

    /** Converts a normal row to string. */
    private static String normalRowToString(int[] row, Map<Integer, String> pieceRepresentation, int rowNb, boolean printCoordinates){
        StringBuilder result = new StringBuilder();

        if(printCoordinates)
            result.append(rowNb + "|");
        else
            result.append(" |");
        // Add the pieces, seperated by " | ".
        for(int piece: row){
            // add row elements
            result.append(pieceRepresentation.get(piece) + "|");
        }
        return result.toString();
    }

}

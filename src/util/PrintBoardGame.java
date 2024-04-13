package util;

import java.util.stream.IntStream;

public class PrintBoardGame {

    private PrintBoardGame(){
        throw new IllegalStateException("Utility class");
    }

    public static String toString(Board board, String[] pieceRepresentation, String turnMarker, String background){
        if(pieceRepresentation.length != 5){
            throw new IllegalArgumentException("The number of piece representations must be equal to 5");
        }
        StringBuilder str = new StringBuilder();

        // add board elements
        str.append("\n");
        str.append(getHorizontalLine(board.nbCols));
        for(int rowNb=board.nbRows-1; rowNb>=0; rowNb--){
        int[] row = board.getRow(rowNb);
            str.append(rowToString(row, pieceRepresentation, rowNb, turnMarker, background));
        }
        // add column numbers
        str.append(getHorizontalLine(board.nbCols));
        str.append(rowToString(IntStream.range(0, board.nbCols).toArray(), pieceRepresentation, -1, turnMarker, background));
        
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

    /** Converts the given array of numbers to string. */
    private static String rowToString(int[] row, String[] pieceRepresentation, int rowNb, String turnMarker, String background){
        if(rowNb == -1)
            return lastRowToString(row, turnMarker) + "\n";
        else
            return normalRowToString(row, pieceRepresentation, rowNb) + "\n";    
    }

    /** Converts the row with rowNb == -1 to string. */
    private static String lastRowToString(int[] row, String turnMarker){
        StringBuilder result = new StringBuilder();

        result.append(" |");
        // Add elements, seperated by " | ".
        for(int element : row){
            result.append(element + "|");
        }
        result.append("\n");
         // Add whose turn it is
        result.append("Turn of: " + turnMarker);

        return result.toString();
    }

    /** Converts a normal row to string. */
    private static String normalRowToString(int[] row, String[] pieceRepresentation, int rowNb){
        StringBuilder result = new StringBuilder();

        result.append(rowNb + "|");

        // Add the pieces, seperated by " | ".
        for(int piece: row){
            // add row elements
            result.append(pieceRepresentation[piece + 2] + "|");
        }
        return result.toString();
    }

}

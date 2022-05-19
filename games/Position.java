public class Position {
    public final int row;
    public final int col;

    public Position(int row, int col){
        if(row < 0 || col < 0)
            throw new IllegalArgumentException("Row and Col must be non-negative.");
        
            this.row = row;
            this.col = col;
    }
}

import java.util.Arrays;

public class Board {
    private final int[][] matrix;
    public final int nbRows;
    public final int nbCols;


    public int get(int row, int col){
        return matrix[row][col];
    }

    public int get(Vector position){
        return matrix[position.row][position.col];
    }

    public int[][] getMatrix(){
        return Helper.deepCopy(this.matrix);
    }

    public int[] getRow(int row){
        return this.matrix[row].clone();
    }

    public Board copy(){
        return new Board(this.getMatrix());
    }

    @Override
    public String toString() {
        return Arrays.deepToString(matrix);
    }

    @Override
    public boolean equals(Object o){
        if(!(o instanceof Board))
            return false;
        Board other = (Board) o;
        return Arrays.deepEquals(this.matrix, other.matrix);
    }

    @Override
    public int hashCode(){
        int hash = 13;
        for(int[] row: this.matrix)
            hash = 31 * hash + (row == null ? 0 : Arrays.hashCode(row));
        return hash;
    }

    public Board(int[][] matrix){
        if(matrix == null)
            throw new IllegalArgumentException("Given matrix may not be null!");
        this.nbRows = matrix.length;
        this.nbCols = matrix[0].length;
        if(this.nbRows == 0)
            throw new IllegalArgumentException("Number of rows may not be 0.");
        if(this.nbCols == 0)
            throw new IllegalArgumentException("Number of columns may not be 0.");
        

        this.matrix = Helper.deepCopy(matrix);
    }

    public void set(int row, int col, int value){
        matrix[row][col] = value;
    }

    public void set(Vector position, int value){
        matrix[position.row][position.col] = value;
    }

    public boolean locationIsOutOfBounds(Vector position) {
        return this.nbRows <= position.row || position.row < 0 ||
            this.nbCols <= position.col || position.col < 0;
    }


}

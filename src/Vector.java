public class Vector {
    public final int row;
    public final int col;

    public Vector plus(Vector other){
        return new Vector(this.row + other.row, this.col + other.col);
    }

    public Vector times(int factor){
        return new Vector(this.row * factor, this.col * factor);
    }
    public Vector(int row, int col){
            this.row = row;
            this.col = col;
    }
}

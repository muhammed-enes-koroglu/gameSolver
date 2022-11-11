package util;

public class Vector {
    public final int row;
    public final int col;

    public static final Vector UP = new Vector(1,0);
    public static final Vector DOWN = new Vector(-1,0);
    public static final Vector RIGHT = new Vector(0,1);
    public static final Vector LEFT = new Vector(0, -1);

    public static final Vector UP_RIGHT = new Vector(1,1);
    public static final Vector UP_LEFT = new Vector(-1,1);
    public static final Vector DOWN_RIGHT = new Vector(1,-1);
    public static final Vector DOWN_LEFT = new Vector(-1, -1);



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

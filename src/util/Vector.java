package util;

public class Vector {
    public final int row;
    public final int col;


    // Directions according to normal x-y plane
    // NOT according to matrix indexing (Board class uses matrix indexing)
    public static final Vector UP = new Vector(1,0);
    public static final Vector DOWN = new Vector(-1,0);
    public static final Vector RIGHT = new Vector(0,1);
    public static final Vector LEFT = new Vector(0, -1);

    public static final Vector UP_RIGHT = new Vector(1,1);
    public static final Vector UP_LEFT = new Vector(1,-1);
    public static final Vector DOWN_RIGHT = new Vector(-1,1);
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
    public static Vector[] getAllDirections() {
        return new Vector[]{UP, DOWN, RIGHT, LEFT, UP_RIGHT, UP_LEFT, DOWN_RIGHT, DOWN_LEFT};
    }

    public int getX(){
        return col;
    }

    public int getY(){
        return row;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Vector other = (Vector) obj;
        return this.row == other.row && this.col == other.col;
    }

    @Override
    public int hashCode() {
        return 31 * row + col;
    }

    @Override
    public String toString(){
        return "(" + row + ", " + col + ")";
    }
}

/********************Starter Code
 *
 * This represents the coordinate data structure (row, column)
 * and prints the required output
 *
 *
 * @author at258
 *
 */

public class Coord {
    private int r;//row
    private int c;//column

    public Coord(int row,int column) {
        r=row;
        c=column;
    }

    public String toString() {
        return "("+r+","+c+")";
    }

    public int getR() {
        return r;
    }
    public int getC() {
        return c;
    }

    @Override
    public boolean equals(Object o) {

        Coord coord=(Coord) o;
        if(coord.r==r && coord.c==c) {
            return true;
        }
        return false;

    }


    /**
     * Get if a triangle is upwards or downwards facing.
     *
     * @return 1 if triangle is downwards pointing, 0 otherwise.
     */
    public int getTriangleDirection() {
        int downwards; // flag to determine if triangle faces upwards or downwards.

        // if row and column have modulo of 0 with 2, then the arrow is upwards facing.
        if ((r % 2 == 0 && c % 2 == 0) || (r % 2 == 1 && c % 2 == 1)) {
            downwards = 0;
        } else {
            downwards = 1;
        }

        return downwards;
    }

}

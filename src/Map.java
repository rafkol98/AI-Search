/********************Starter Code
 *
 * This class contains the maps to be used for evaluation
 *
 * @author at258
 *
 */

public enum Map {


    //************************TEST MAPS as discussed in lectures ********************

    JMAP00(new int[][]{ //JMAP00 is the map in the spec
            {0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0},
            {0, 0, 1, 1, 0, 0},
            {0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0}

    }),


    JMAP01(new int[][]{ //JMAP01 is used in the given tests
            {0, 0, 0},
            {0, 0, 0},
            {0, 0, 0}
    }),


    JMAP02(new int[][]{ //JMAP03 is used in the given tests
            {0, 0, 0},
            {0, 0, 0},
            {0, 1, 0}
    }),

    //************************MAPS for evaluation ********************
    MAP0(new int[][]{
            {0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0}
    }),
    MAP1(new int[][]{
            {0, 0, 0, 0, 0},
            {0, 0, 1, 0, 0},
            {0, 0, 1, 0, 0},
            {0, 0, 1, 0, 0},
            {0, 0, 0, 0, 0},
    }),
    MAP2(new int[][]{
            {0, 0, 0, 0, 0, 0},
            {0, 0, 1, 1, 0, 0},
            {0, 0, 1, 0, 1, 0},
            {0, 0, 1, 1, 1, 0},
            {0, 0, 0, 0, 0, 0},
            {0, 0, 1, 1, 1, 0}
    }),
    MAP3(new int[][]{
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 1, 0, 0, 1, 0},
            {0, 0, 0, 1, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 1, 1, 0, 0},
            {0, 0, 1, 1, 0, 1, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},

    }),
    MAP4(new int[][]{
            {0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 1, 0, 0},
            {1, 0, 0, 1, 1, 0, 0, 0},
            {0, 0, 0, 1, 0, 0, 0, 0},
            {0, 0, 1, 0, 0, 0, 1, 0},
            {0, 0, 1, 1, 0, 0, 0, 0},
            {0, 0, 1, 1, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0}
    });

    public final int[][] map;

    Map(int[][] map) {
        this.map = map;

    }

    /**
     * Set a tide on the map (value 2) at the given coordinates.
     *
     * @param row the row that the tide will be added.
     * @param col the column that the tide will be added.
     */
    public void setTide(int row, int col) {
        map[row][col] = 2;
    }

    /**
     * Count number of islands in the map. Number of 1's.
     *
     * @return the number of islands.
     */
    public int countIslands() {
        int rows = map.length;
        int columns = map[0].length;

        int count = 0;
        for(int r=0; r< rows; r++) {
            for (int c=0; c<columns; c++) {
                if (map[r][c]==1) {
                    count++;
                }
            }
        }
        return count;
    }

    public int[][] getMap() {
        return map;
    }


}

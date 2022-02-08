import java.util.ArrayList;
import java.util.Random;

/********************Starter Code
 *
 * This class contains some examples on how to handle the required inputs and outputs
 * and other debugging options
 *
 * @author at258
 *
 * run with
 * java A1main <Algo> <ConfID>
 *
 */


public class A1main {

    public static void main(String[] args) {
        //Example: java A1main BFS JCONF03 M

        try {
            Conf conf = Conf.valueOf(args[1]);
            Map map = conf.getMap();

            String heuristic;
            // Check if heuristic is passed in.
            if (args.length >= 3) {
                heuristic = args[2];

                if (args.length == 4 && args[3].charAt(0) == 'Y') {
                    generateHighTides(map, conf.getS(), conf.getG(), 5); // change map.
                } else {
                    System.out.println("To generate waves please type Y as the fourth argument e.g.: java A1main <Alg> <Conf> <H> Y");
                    System.exit(-1);
                }
            } else {
                heuristic = "M";
            }

            if (heuristic.charAt(0) == 'M' || heuristic.charAt(0) == 'E' || heuristic.charAt(0) == 'C' || heuristic.charAt(0) == 'T') {
                printMap(map, conf.getS(), conf.getG());
                // Run search algorithm.
                runSearch(args[0], map, conf.getS(), conf.getG(), heuristic.charAt(0));
            } else {
                System.out.println("Accepted heuristics: M, T, E, C");
                System.exit(-1);
            }


        } catch (Exception e) {
            e.printStackTrace();

            System.out.println("There was a problem. Please run the program like this java A1main BFS JCONF03 M");
        }


    }

    private static void runSearch(String algo, Map map, Coord start, Coord goal, char heuristic) {
        Search uninformed = new UninformedSearch(map, start, goal, heuristic);
        Search informed = new InformedSearch(map, start, goal, heuristic);
        BidirectionalSearch bidirectional = new BidirectionalSearch(map, start, goal, heuristic);

        switch (algo) {
            case "BFS": //run BFS
                uninformed.treeSearch("BFS");
                break;
            case "DFS": //run DFS
                uninformed.treeSearch("DFS");
                break;
            case "BestF": //run BestF
                informed.treeSearch("BestF");
                break;
            case "AStar": //run AStar
                informed.treeSearch("AStar");
                break;
            case "Bidirectional": //run Bidirectional
                bidirectional.treeSearch("Bidirectional");
                break;
        }

    }


    private static void printMap(Map m, Coord init, Coord goal) {

        int[][] map = m.getMap();

        System.out.println();
        int rows = map.length;
        int columns = map[0].length;

        //top row
        System.out.print("  ");
        for (int c = 0; c < columns; c++) {
            System.out.print(" " + c);
        }
        System.out.println();
        System.out.print("  ");
        for (int c = 0; c < columns; c++) {
            System.out.print(" -");
        }
        System.out.println();

        //print rows
        for (int r = 0; r < rows; r++) {
            boolean right;
            System.out.print(r + "|");
            if (r % 2 == 0) { //even row, starts right [=starts left & flip right]
                right = false;
            } else { //odd row, starts left [=starts right & flip left]
                right = true;
            }
            for (int c = 0; c < columns; c++) {
                System.out.print(flip(right));
                if (isCoord(init, r, c)) {
                    System.out.print("S");
                } else {
                    if (isCoord(goal, r, c)) {
                        System.out.print("G");
                    } else {
                        if (map[r][c] == 0) {
                            System.out.print(".");
                        } else {
                            System.out.print(map[r][c]);
                        }
                    }
                }
                //ALTERNATES!
                right = !right;
            }
            System.out.println(flip(right));
        }
        System.out.println();


    }

    private static boolean isCoord(Coord coord, int r, int c) {
        //check if coordinates are the same as current (r,c)
        if (coord.getR() == r && coord.getC() == c) {
            return true;
        }
        return false;
    }


    public static String flip(boolean right) {
        //prints triangle edges
        if (right) {
            return "\\"; //right return left
        } else {
            return "/"; //left return right
        }

    }

    /**
     * Randomly generates high tides on different coordinates of the map.
     *
     * @param m    map of the configuration.
     * @param init initial coordinates.
     * @param goal goal coordinates.
     * @return the updated map.
     */
    private static Map generateHighTides(Map m, Coord init, Coord goal, int numberOfTides) {
        // Get rows and columns of the map.
        int rows = m.getMap().length;
        int columns = m.getMap()[0].length;

        int tidesAssigned = 0;
        if (numberOfTides < ((rows * columns)-m.countIslands())) {
           while(tidesAssigned < numberOfTides) {
                int randomRow = new Random().nextInt(rows-1);
                int randomColumn = new Random().nextInt(columns-1);

               System.out.println("here "+randomRow +" , "+randomColumn);
                if (m.getMap()[randomRow][randomColumn] != 1 && m.getMap()[randomRow][randomColumn] != 2 && !isCoord(init, randomRow, randomColumn) && !isCoord(goal,randomRow, randomColumn)) {
                    m.setTide(randomRow, randomColumn);
                    tidesAssigned++;
                }
            }
        } else {
            System.out.println("Please select smaller number of tides, between 0 and "+((rows * columns)-m.countIslands()));
            System.exit(0);
        }

        return m;
    }

}

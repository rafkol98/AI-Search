import java.util.Random;

public class TestingTides {

    Map map;
    Coord start;
    Coord goal;
    char heuristic;

    public TestingTides(Map map, Coord start, Coord goal, char heuristic) {
        this.map = map;
        this.start = start;
        this.goal = goal;
        this.heuristic = heuristic;

    }

    /**
     * Initialise algorithms and run them with the same heuristic.
     */
    public void runAlgorithms(int maxTides) {
        Map tempMap = map;
        int tides = 0;
        while(tides <= maxTides) {
            tempMap = generateHighTides(tempMap,2);

            System.out.println("TIDES: "+tides);
            System.out.println("MAP:");
            A1main.printMap(tempMap,start,goal);

            // Run searches and print output.
            runSearches(tempMap);
            tides = tides + 1;
        }

    }

    private void runSearches(Map map) {
        Search bfs = new UninformedSearch(map, start, goal);
        Search dfs = new UninformedSearch(map, start, goal);
        Search bestF = new InformedSearch(map, start, goal, heuristic);
        Search aStar = new InformedSearch(map, start, goal, heuristic);
        BidirectionalSearch bidirectional = new BidirectionalSearch(map, start, goal);

        bfs.treeSearch("BFS",false);
        dfs.treeSearch("DFS", false);
        bestF.treeSearch("BestF", false);
        aStar.treeSearch("AStar", false);
        bidirectional.treeSearch("Bidirectional", false);

        System.out.print("BFS: "+bfs.nodesExplored + " , "+bfs.pathCost);
        System.out.print(" | DFS: "+dfs.nodesExplored + " , "+dfs.pathCost);
        System.out.print(" | BestF: "+bestF.nodesExplored + " , "+bestF.pathCost);
        System.out.print(" | AStar: "+aStar.nodesExplored + " , "+aStar.pathCost);
        System.out.print(" | Bidirectional: "+aStar.nodesExplored + " , "+aStar.pathCost+"\n\n");
    }

    /**
     * Randomly generates high tides on different coordinates of the map.
     *
     * @return the updated map.
     */
    private Map generateHighTides(Map map, int numberOfTides) {
        // Get rows and columns of the map.
        int rows = map.getMap().length;
        int columns = map.getMap()[0].length;

        int tidesAssigned = 0;
        if (numberOfTides < ((rows * columns)-map.countIslands())) {
            while(tidesAssigned < numberOfTides) {
                int randomRow = new Random().nextInt(rows-1);
                int randomColumn = new Random().nextInt(columns-1);

                if (map.getMap()[randomRow][randomColumn] != 1 && map.getMap()[randomRow][randomColumn] != 2 && !A1main.isCoord(start, randomRow, randomColumn) && !A1main.isCoord(goal,randomRow, randomColumn)) {
                    map.setTide(randomRow, randomColumn);
                    tidesAssigned++;
                }
            }
        } else {
            System.out.println("Please select smaller number of tides, between 0 and "+ ((rows * columns)-map.countIslands()-2));
            System.exit(0);
        }

        return map;
    }



}

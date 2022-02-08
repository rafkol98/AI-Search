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
    public void runAlgorithms() {
        Search uninformed = new UninformedSearch(map, start, goal);
        Search uninformed2 = new UninformedSearch(map, start, goal);
        Search informed = new InformedSearch(map, start, goal, heuristic);
        Search informed2 = new InformedSearch(map, start, goal, heuristic);
        BidirectionalSearch bidirectional = new BidirectionalSearch(map, start, goal);

        System.out.println("\n TESTING ALL THE ALGORITHMS ON THE FOLLOWING MAP - WHERE THERE IS 2 ARE THE TIDES GENERATED ");
        A1main.printMap(map,start,goal);

        System.out.println("\n BREADTH-FIRST SEARCH: ");
        uninformed.treeSearch("BFS");
        System.out.println("\n DEPTH-FIRST SEARCH: ");
        uninformed2.treeSearch("DFS");
        System.out.println("\n BEST-FIRST SEARCH: ");
        informed.treeSearch("BestF");
        System.out.println("\n ASTAR SEARCH: ");
        informed2.treeSearch("AStar");
        System.out.println("\n BIDIRECTIONAL SEARCH: ");
        bidirectional.treeSearch("Bidirectional");
    }


}

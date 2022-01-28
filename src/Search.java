import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;
import java.util.stream.Collectors;

public abstract class Search {

    private int[][] map;
    private Coord start;
    private Coord goal;
    private ArrayList<Node> explored = new ArrayList<>();
    private String algo;

    /**
     * @param map
     * @param start
     * @param goal
     */
    public Search(Map map, Coord start, Coord goal) {
        this.map = map.getMap();
        this.start = start;
        this.goal = goal;
    }

    public String getAlgo() {
        return algo;
    }

    public void setAlgo(String algo) {
        this.algo = algo;
    }

    /**
     * Get states of nodes that are included in the frontier.
     *
     * @return
     */
    public abstract ArrayList<Coord> getFrontierStates();

    /**
     * @return
     */
    public int[][] getMap() {
        return map;
    }

    /**
     * @return
     */
    public Coord getStart() {
        return start;
    }

    /**
     * @return
     */
    public Coord getGoal() {
        return goal;
    }


    /**
     * @return
     */
    public ArrayList<Node> getExplored() {
        return explored;
    }

    /**
     * @param node
     */
    public void addExplored(Node node) {
        explored.add(node);
    }

    /**
     * Print elements that are currently in the frontier.
     */
    public abstract void printFrontier();

    /**
     * @param algo
     * @return
     */
    public void treeSearch(String algo) {
        setAlgo(algo);

        Node initialNode = new Node(null, getStart()); // Create initial node.

        insert(initialNode); // Insert initial node to the frontier.

        loopFrontier();

        failure(); // if path was not found -> print failure.

    }


    public abstract void loopFrontier();

    /**
     * @param node
     * @return
     */
    public ArrayList<Node> expand(Node node) {

        ArrayList<Coord> nextStates = successor(node.getState()); // Assign all the next legal states to an ArrayList.

        ArrayList<Node> successors = new ArrayList<>(); // ArrayList to hold the successor nodes.

        // Iterate through the next states.
        for (Coord state : nextStates) {
            addSuitableSuccessors(state, successors, node);
        }
        return successors;

    }

    public abstract void addSuitableSuccessors(Coord state, ArrayList<Node> successors, Node node);


    /**
     * @param successors
     */
    public abstract void insertAll(ArrayList<Node> successors);

    /**
     * Insert the first node to frontier.
     * The node is added for all the algorithms in the same order as this method is only used for the first node.
     */
    public abstract void insert(Node node);

    /**
     * @return
     */
    public abstract Node removeFromFrontier();

    /**
     * Get states of nodes that are included in the explored list.
     *
     * @return
     */
    public ArrayList<Coord> getExploredStates() {
        ArrayList<Coord> exploredStates = new ArrayList<>();
        // Iterate through the nodes of the frontier, add state of all the nodes to
        // the frontierStates ArrayList.
        for (Node node : explored) {
            exploredStates.add(node.getState());
        }
        return exploredStates;
    }

    /**
     * @param state
     * @param goal
     * @return
     */
    public boolean goalTest(Coord state, Coord goal) {
        return state.equals(goal);
    }

    /**
     * @param state
     * @return
     */
    public ArrayList<Coord> successor(Coord state) {
        boolean upwards = isTriangleUpwards(state); // Find if triangle points upwards (direction).

        return getMoves(state, upwards);
    }

    //TODO: maybe you can add a new property 'Direction'  in the NODE class.

    /**
     * Get if a triangle is upwards or downwards facing.
     *
     * @param state
     * @return if triangle is upwards or downwards pointing.
     */
    public boolean isTriangleUpwards(Coord state) {
        int row = state.getR(); // Get row of state passed in.
        int col = state.getC(); // Get column of state passed in.

        boolean upwards; // flag to determine if triangle faces upwards or downwards.

        // if row and column have modulo of 0 with 2, then the arrow is upwards facing.
        if ((row % 2 == 0 && col % 2 == 0) || (row % 2 == 1 && col % 2 == 1)) {
            upwards = true;
        } else {
            upwards = false;
        }

        return upwards;
    }

    /**
     * @param state
     * @param upwardsTriangleDirection
     * @return
     */
    public ArrayList<Coord> getMoves(Coord state, boolean upwardsTriangleDirection) {
        ArrayList<Coord> successorStates = new ArrayList<>(); // Initialise ArrayList to hold all the successor states.

        int row = state.getR(); // Get row of state passed in.
        int col = state.getC(); // Get column of state passed in.

        Coord stateLeft = new Coord(row, col - 1); // Initialise the left state.
        Coord stateRight = new Coord(row, col + 1); // Initialise the right state.
        Coord stateVertical;

        boolean down;

        if (upwardsTriangleDirection) {
            stateVertical = new Coord(row + 1, col); // Assign the downward state in the stateVertical.
            down = true;
        } else {
            stateVertical = new Coord(row - 1, col); // Assign the upwards state in the stateVertical.
            down = false;
        }

        // Tie breaking.
        successorStates.add(stateRight); // Add the right position (1st priority).

        // If the vertical state is down, then add it second (2nd priority)
        if (down) {
            successorStates.add(stateVertical);
        }

        successorStates.add(stateLeft);  // Add the left position (3rd priority)

        // If the vertical state is upwards, then add it last (4th priority)
        if (!down) {
            successorStates.add(stateVertical);
        }

        return keepOnlyLegalStates(successorStates); // Keep and return only the legal states out of the ones added.
    }


    /**
     * This method ensures that only legal states are kept.
     * It ensures that both row and column are greater or equal than 0 (we cannot have negative coordinates) and less
     * or equal than the map's row and column boundaries.
     * It finally checks that the state is not an island i.e. has a value of 1 on the map.
     *
     * @param states
     * @return
     */
    public ArrayList<Coord> keepOnlyLegalStates(ArrayList<Coord> states) {

        ArrayList<Coord> legalStates = new ArrayList<>();

        // Iterate through the states passed in, keep only the legal states.
        for (Coord state : states) {

            int row = state.getR(); // Get row of current state.
            int col = state.getC(); // Get column of current state.

            int rows = map.length - 1;
            int columns = map[0].length - 1;

            // Check that the current state is legal.
            if ((row >= 0 && col >= 0) && (row <= rows && col <= columns) && (map[row][col] != 1)) {
                legalStates.add(state); // add state to the legal states ArrayList.
            }
        }

        return legalStates;
    }

    /**
     * Print final output (when goal node is reached).
     *
     * @param node
     */
    public void printGoal(Node node) {
        Stack<Coord> pathStates = node.getPath(getStart());

        // Print path, path cost, and number of nodes explored.
        while (!pathStates.isEmpty()) {
            System.out.print(pathStates.pop());
        }
        System.out.println("\n" + node.getPathCost(getStart())); // Print path cost.
        System.out.println(getExplored().size()); // Print nodes explored.

        System.exit(0); // Exit system.
    }

    public void failure() {
        System.out.println("fail");
        System.out.println(getExplored().size());
        System.exit(0); // Exit system.
    }

}

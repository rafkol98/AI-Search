import java.util.*;
import java.util.stream.Collectors;

/**
 * The Search class containing common methods used by both the UninformedSearch and InformedSearch classes.
 *
 * @author 210017984.
 */
public abstract class Search {

    // Initialise variables.
    private int[][] map;
    private Coord start;
    private Coord goal;
    private ArrayList<Node> explored = new ArrayList<>();
    private String algo;
    private char heuristic;

    /**
     * @param map
     * @param start
     * @param goal
     */
    public Search(Map map, Coord start, Coord goal, char heuristic) {
        this.map = map.getMap();
        this.start = start;
        this.goal = goal;
        this.heuristic = heuristic;
    }

    /**
     * Get coordinates of the starting position.
     *
     * @return the coordinates of the starting position.
     */
    public Coord getStart() {
        return start;
    }

    /**
     * Get coordinates of the goal position.
     *
     * @return the coordinates of the goal position.
     */
    public Coord getGoal() {
        return goal;
    }

    /**
     * Get algorithm selected.
     *
     * @return the algorithm selected.
     */
    public String getAlgo() {
        return algo;
    }

    /**
     * Set algorithm selected by the user.
     */
    public void setAlgo(String algo) {
        this.algo = algo;
    }

    /**
     * Get heuristic selected by the user.
     * @return heuristic selected.
     */
    public char getHeuristic() {
        return heuristic;
    }

    /**
     * Iterate through the nodes of the frontier, add the state of each node to the frontierStates ArrayList.
     *
     * @return an ArrayList containing all the states of the frontier.
     */
    public ArrayList<Coord> getFrontierStates(Collection<Node> frontier) {
        ArrayList<Coord> frontierStates = new ArrayList<>();

        // Iterate through the frontier, adding each node's state to the frontierStates ArrayList.
        frontier.stream().forEach(node -> frontierStates.add(node.getState()));

        return frontierStates;
    }

    /**
     * Get a node that has the same state as the one passed in and is included in the frontier.
     *
     * @param frontier the frontier to be iterated.
     * @param state the state that we are looking.
     * @return the node that is included in the frontier.
     */
    public Node getNodeInFrontier(Collection<Node> frontier, Coord state) {
        for (Node node : frontier) {
            if (node.getState().equals(state)) {
                return node;
            }
        }
        return null;
    }

    /**
     * Get nodes explored.
     *
     * @return ArrayList containing the nodes explored.
     */
    public ArrayList<Node> getExplored() {
        return explored;
    }

    /**
     * Add a node to the explored ArrayList.
     *
     * @param node the node to be added.
     */
    public void addExplored(Node node) {
        explored.add(node);
    }

    /**
     * Print all the states currently in the frontier.
     *
     */
    public abstract void printFrontier(Collection<Node> frontier);

    /**
     * Construct the search tree to find the goal.
     *
     * @param algo the algorithm used.
     */
    public void treeSearch(String algo) {
        setAlgo(algo); // Set algorithm selected.
        Node initialNode;

        // Create initial node.
        if ((getAlgo() == "BestF") && (getAlgo() == "AStar")) {
            initialNode = new Node(null, start, getGoal(), getHeuristic(), getAlgo(), getStart());
        } else {
            initialNode = new Node(null, start);
        }


        insert(initialNode); // Insert initial node to the frontier.

        loopFrontier();

        failure(); // if path was not found -> print failure.
    }

    /**
     * Loop and explore the frontier. If goal is found, its path, cost, and explored nodes are printed.
     * Otherwise, it continues exploring the frontier until its empty.
     */
    public abstract void loopFrontier();

    /**
     * Expand a node by finding its suitable successors (next possible moves).
     *
     * @param node the node to be expanded.
     * @return an ArrayList containing all the legal and available successors of the node passed in.
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

    /**
     * Ensures that the state being explored is not contained already in the frontier or was previously explored.
     * If it is not, then it is added to the successors ArrayList passed in.
     *
     * @param state      the state being explored.
     * @param successors the successors ArrayList - where we store all the suitable successors.
     * @param parent     the parent node of the state.
     */
    public abstract void addSuitableSuccessors(Coord state, ArrayList<Node> successors, Node parent);


    /**
     * Inserts all the successors to the frontier.
     *
     * @param successors the successors ArrayList containing all the suitable successors.
     */
    public abstract void insertAll(ArrayList<Node> successors);

    /**
     * Insert a node to the frontier.
     *
     * @param node the node to be added.
     */
    public abstract void insert(Node node);

    /**
     * Removes the first element of the frontier (the one with the lowest F_Cost currently in the frontier).
     *
     * @return the node removed.
     */
    public abstract Node removeFromFrontier();

    /**
     * Get states of nodes that are included in the explored list.
     *
     * @return the states of the nodes explored.
     */
    public ArrayList<Coord> getExploredStates() {
        ArrayList<Coord> exploredStates = new ArrayList<>();

        explored.stream().forEach(node -> exploredStates.add(node.getState()));

        return exploredStates;
    }

    /**
     * Check if the state passed in is the goal state.
     *
     * @param state the coordinates of the state passed in.
     * @return true if it is the goal, false otherwise.
     */
    public boolean goalTest(Coord state) {
        return state.equals(getGoal());
    }

    /**
     * Find the next possible moves for the state passed in. Add the successor states in a List according to the tie
     * breaking constraints specified.
     *
     * @param state
     * @return
     */
    public ArrayList<Coord> successor(Coord state) {
        boolean upwards = isTriangleUpwards(state); // Find if triangle points upwards (direction).

        ArrayList<Coord> successorStates = new ArrayList<>(); // Initialise ArrayList to hold all the successor states.

        int row = state.getR(); // Get row of state passed in.
        int col = state.getC(); // Get column of state passed in.

        Coord stateLeft = new Coord(row, col - 1); // Initialise the left state.
        Coord stateRight = new Coord(row, col + 1); // Initialise the right state.
        Coord stateVertical;

        boolean down;

        if (upwards) {
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

    //TODO: maybe move this to the Coord class.
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
    public void printOutput(Node node) {
        Stack<Coord> pathStates = node.getPath(start);

        // Print path, path cost, and number of nodes explored.
        while (!pathStates.isEmpty()) {
            System.out.print(pathStates.pop());
        }
        System.out.println("\n" + node.getPathCost(start)); // Print path cost.
        System.out.println(getExplored().size()); // Print nodes explored.

        System.exit(0); // Exit system.
    }
    /**
     * If the search could not find a solution, print fail message, the explored size, and then exit the system.
     */
    public void failure() {
        System.out.println("fail");
        System.out.println(getExplored().size());
        System.exit(0); // Exit system.
    }

}

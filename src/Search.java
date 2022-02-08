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
    private boolean foundSolution = false;


    public Search(Map map, Coord start, Coord goal) {
        this.map = map.getMap();
        this.start = start;
        this.goal = goal;
    }

    /**
     * Create a new Search.
     *
     * @param map       the map in the selected configuration.
     * @param start     the starting coordinates.
     * @param goal      the goal coordinates.
     * @param heuristic the chosen heuristic.
     */
    public Search(Map map, Coord start, Coord goal, char heuristic) {
        this.map = map.getMap();
        this.start = start;
        this.goal = goal;
        this.heuristic = heuristic;
    }

    /**
     * Set it to true once a solution is found.
     * @param foundSolution true if a solution is found, false otherwise.
     */
    public void setFoundSolution(boolean foundSolution) {
        this.foundSolution = foundSolution;
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
     *
     * @param algo the algorithm selected.
     */
    public void setAlgo(String algo) {
        this.algo = algo;
    }

    /**
     * Get heuristic selected by the user.
     *
     * @return heuristic selected.
     */
    public char getHeuristic() {
        return heuristic;
    }

    /**
     * Iterate through the nodes of the frontier, add the state of each node to the frontierStates ArrayList
     * and then return it.
     *
     * @param frontier the frontier that we want to get its states.
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
     * @param state    the state that we are looking.
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
     * Get a node from an explored ArrayList that has the same state as the one passed in.
     *
     * @param explored the explored to be iterated.
     * @param state the state that we are looking.
     * @return the node that is included in the explored ArrayList.
     */
    public Node getNodeInExplored(ArrayList<Node> explored, Coord state) {
        for (Node node : explored) {
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
     * Add a node to the explored ArrayList passed in.
     *
     * @param node     the node to be added.
     * @param explored the explored arraylist to add a node to.
     */
    public void addExplored(Node node, ArrayList<Node> explored) {
        explored.add(node);
    }

    /**
     * Print all the states currently in the frontier.
     *
     * @param frontier the frontier to print.
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
        if ((getAlgo().equals("BestF")) || (getAlgo().equals("AStar"))) {
            initialNode = new Node(null, start, getGoal(), getHeuristic(), getAlgo(), getStart());
        } else {
            initialNode = new Node(null, start);
        }

        loopFrontier(initialNode);

        // if solution was not found -> print failure.
        if (!foundSolution) {
            failure();
        }

    }

    /**
     * Loop and explore the frontier. If goal is found, its path, cost, and explored nodes are printed.
     * Otherwise, it continues exploring the frontier until its empty.
     *
     * @param initialNode the initial node to be added before constructing the search tree.
     */
    public abstract void loopFrontier(Node initialNode);

    /**
     * Expand a node by finding its suitable successors (next possible moves).
     *
     * @param node       the node to be expanded.
     * @param frontier   the frontier to be passed in the addSuitableSuccessors to check that a state was not already
     *                   explored or in the frontier.
     * @param frontierNo the number of frontier. Used for BIDIRECTIONAL because it uses 2 frontiers.
     * @return an ArrayList containing all the legal and available successors of the node passed in.
     */
    public ArrayList<Node> expand(Node node, Collection<Node> frontier, int frontierNo) {

        ArrayList<Coord> nextStates = successor(node.getState()); // Assign all the next legal states to an ArrayList.

        ArrayList<Node> successors = new ArrayList<>(); // ArrayList to hold the successor nodes.

        // Iterate through the next states.
        for (Coord state : nextStates) {
            addSuitableSuccessors(frontier, frontierNo, state, successors, node);
        }
        return successors;
    }

    /**
     * Ensures that the state being explored is not contained already in the frontier or was previously explored.
     * If it is not, then it is added to the successors ArrayList passed in.
     *
     * @param frontier   the frontier to make the check if node is contained already.
     * @param frontierNo the number of frontier. Used for BIDIRECTIONAL to get the states of the appropriate frontier,
     *                   as it uses 2 different frontiers.
     * @param state      the state being explored.
     * @param successors the successors ArrayList - where we store all the suitable successors.
     * @param parent     the parent node of the state.
     */
    public abstract void addSuitableSuccessors(Collection<Node> frontier, int frontierNo, Coord state, ArrayList<Node> successors, Node parent);


    /**
     * Inserts all the successors to the frontier.
     *
     * @param successors the successors ArrayList containing all the suitable successors.
     */
    public abstract void insertAll(ArrayList<Node> successors);

    /**
     * Insert a node to the frontier.
     *
     * @param node     the node to be added.
     * @param frontier the frontier to add the node to.
     */
    public void insert(Node node, Collection<Node> frontier) {
        frontier.add(node); // Add node.
    }

    /**
     * Removes the first element of the frontier (the one with the lowest F_Cost currently in the frontier).
     *
     * @return the node removed.
     */
    public abstract Node removeFromFrontier();

    /**
     * Get states of nodes that are included in the explored list.
     *
     * @param explored the arrayList that contains the explored states to be iterated.
     * @return the states of the nodes explored.
     */
    public ArrayList<Coord> getExploredStates(ArrayList<Node> explored) {
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
     * @param state the state checked.
     * @return an ArrayList containing the next possible moves/states.
     */
    public ArrayList<Coord> successor(Coord state) {
        int downwards = state.getTriangleDirection(); // get if triangle direction is downwards.

        ArrayList<Coord> successorStates = new ArrayList<>(); // Initialise ArrayList to hold all the successor states.

        int row = state.getR(); // Get row of state passed in.
        int col = state.getC(); // Get column of state passed in.

        Coord stateLeft = new Coord(row, col - 1); // Initialise the left state.
        Coord stateRight = new Coord(row, col + 1); // Initialise the right state.
        Coord stateVertical;

        boolean assignBelow; // vertical state is the one below (down) or not? (the one above).

        if (downwards == 1) {
            stateVertical = new Coord(row - 1, col); // Assign the upwards state in the stateVertical.
            assignBelow = false;
        } else {
            stateVertical = new Coord(row + 1, col); // Assign the downward state in the stateVertical.
            assignBelow = true;
        }

        // Tie breaking.
        successorStates.add(stateRight); // Add the right position (1st priority).

        // If the vertical state is down, then add it second (2nd priority)
        if (assignBelow) {
            successorStates.add(stateVertical);
        }

        successorStates.add(stateLeft);  // Add the left position (3rd priority)

        // If the vertical state is upwards, then add it last (4th priority)
        if (!assignBelow) {
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
     * @param states the states passed in from the successor function.
     * @return only the legal states from those passed in.
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
            if ((row >= 0 && col >= 0) && (row <= rows && col <= columns) && (map[row][col] != 1) && (map[row][col] != 2)) {
                legalStates.add(state); // add state to the legal states ArrayList.
            }
        }

        return legalStates;
    }

    /**
     * Print final output (when goal node is reached).
     *
     * @param node the node from which we get the path to the start.
     */
    public void printOutput(Node node) {
        Stack<Coord> pathStates = node.getPath(start);

        // Print path, path cost, and number of nodes explored.
        while (!pathStates.isEmpty()) {
            System.out.print(pathStates.pop());
        }
        System.out.println("\n" + node.getPathCost(start)); // Print path cost.
        System.out.println(getExplored().size()); // Print nodes explored.
//        System.exit(0); // Exit system.
    }

    /**
     * If the search could not find a solution, print fail message, the explored size, and then exit the system.
     */
    public void failure() {
        System.out.println("fail");
        System.out.println(getExplored().size());
//        System.exit(0); // Exit system.
    }

}

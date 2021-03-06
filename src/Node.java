import java.util.Stack;


/**
 * Creates a node in the search tree.
 *
 * @author 210017984.
 */
public class Node {

    // Initialise Node variables.
    private Coord state;
    private Node parent;
    private int depth;
    private Coord goal;
    private double h_Cost = 0;
    private double f_Cost = 0;

    /**
     * Create a new node.
     *
     * @param parent the parent node.
     * @param state  the coordinates for the state of the node.
     */
    public Node(Node parent, Coord state) {
        this.state = state;
        this.parent = parent;
        this.depth = calculateDepth(); // calculate depth of node using the calculateDepth function.
    }

    /**
     * Create a new node (used for informed search).
     *
     * @param parent    the parent node.
     * @param state     the coordinates for the state of the node.
     * @param goal      the coordinates of the goal state.
     * @param heuristic the heuristic to be used.
     * @param algo      the algorithm to be used -> important for calculating the f_cost.
     * @param start     the coordinates of the starting position -> important for calculating the f_cost.
     */
    public Node(Node parent, Coord state, Coord goal, char heuristic, String algo, Coord start) {
        this.state = state;
        this.parent = parent;
        this.depth = calculateDepth();
        this.goal = goal;
        this.h_Cost = heuristicScore(heuristic);
        this.f_Cost = calculateFCost(algo, start);
    }

    /**
     * Get coordinates - state of the node.
     *
     * @return the state of the node.
     */
    public Coord getState() {
        return state;
    }

    /**
     * Get the parent of the node.
     *
     * @return the parent of the node.
     */
    public Node getParent() {
        return parent;
    }

    /**
     * Get depth of the node.
     *
     * @return the depth of the node.
     */
    public int getDepth() {
        return depth;
    }

    /**
     * Get the cost of the path up to the current node from the starting position.
     *
     * @param start the coordinates of the starting position.
     * @return the cost of the path.
     */
    public float getPathCost(Coord start) {
        return getPath(start).size() - 1;
    }

    /**
     * Get the heuristic cost (h_Cost) of the node.
     *
     * @return the h_Cost of the node.
     */
    public double getH_Cost() {
        return h_Cost;
    }

    /**
     * Get the function cost (f_Cost) of the node.
     *
     * @return the f_Cost of the node.
     */
    public double getF_Cost() {
        return f_Cost;
    }

    /**
     * Calculate depth.
     *
     * @return the depth of the node.
     */
    private int calculateDepth() {
        // If parent == null (initial case), then return 1. Otherwise return the depth of the parent node + 1.
        return (parent == null) ? 1 : parent.depth + 1;
    }

    /**
     * Calculate f_cost depending on the algorithm used.
     *
     * @param algo  the algorithm used.
     * @param start the coordinates of the starting position.
     * @return the calculated f_cost.
     */
    private double calculateFCost(String algo, Coord start) {
        switch (algo) {
            case "BestF":
                return getH_Cost();
            case "AStar":
                // AStar combines heuristic cost with path cost.
                return getH_Cost() + getPathCost(start);
        }
        return 0;
    }

    /**
     * Get path from a start node to the current one.
     *
     * @param start the coordinates of the starting position.
     * @return a Stack that contains the path from starting position to the goal.
     */
    public Stack<Coord> getPath(Coord start) {
        Stack<Coord> pathStates = new Stack<>(); // Stack that holds the states in the path.

        Boolean initial = false; // flag that determines if the coord are the initial.
        Node tempNode = this; // Assign node passed in to the new tempNode variable.

        pathStates.push(getState()); // Add the goal state (final node) to the stack.

        // Iterate until we reach the starting node.
        while (!initial) {
            tempNode = tempNode.getParent();
            // Special case for when the intersection node is the same as the starting position (bidirectional search).
            if (tempNode == null) {
                break;
            }
            pathStates.push(tempNode.getState());

            // if the parent node's state equals the starting node, then end while loop.
            if (tempNode.getState().equals(start)) {
                initial = true;
            }
        }

        return pathStates;
    }

    /**
     * Calculate heuristic cost. Available heuristics include Manhattan cartesian, Manhattan triangle,
     * Euclidean distance, and Chebyshev distance.
     *
     * @param heuristic the heuristic being used.
     * @return manhattan distance.
     */
    private double heuristicScore(char heuristic) {
        int deltaX = goal.getR() - getState().getR();
        int deltaY = goal.getC() - getState().getC();

        switch (heuristic) {
            case 'M':
                return Math.abs(deltaX) + Math.abs(deltaY); // return manhattan distance (Cartesian Coordinates).
            case 'T':
                return manhattanTriangle(); // return manhattan distance (Triangle Grid).
            case 'E':
                return Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2)); // return Euclidian distance.
            case 'C':
                return Math.max(Math.abs(deltaX), Math.abs(deltaY)); // return Chebyshev distance.

        }
        return 0;
    }

    /**
     * Compute the manhattan distance using the triangle grid method. Following the calculations specified in the
     * lectures.
     *
     * @return the manhattan distance using the triangle grid method.
     */
    private double manhattanTriangle() {
        double a = -state.getR();
        double b = (state.getR() + state.getC() - state.getTriangleDirection()) / 2;
        double c = (state.getR() + state.getC() - state.getTriangleDirection()) / 2 - state.getR() + state.getTriangleDirection();

        double aG = -goal.getR();
        double bG = (goal.getR() + goal.getC() - goal.getTriangleDirection()) / 2;
        double cG = (goal.getR() + goal.getC() - goal.getTriangleDirection()) / 2 - goal.getR() + goal.getTriangleDirection();

        return Math.abs(aG - a) + Math.abs(bG - b) + Math.abs(cG - c);

    }


}

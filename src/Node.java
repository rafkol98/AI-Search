import java.util.Stack;

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
        return getPath(start).size() - 1; // -1 is to remove the initial node from the cost.
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
                //TODO: debug this!
//                System.out.println("H_Cost" + getH_Cost());
//                System.out.println("Path_Cost" + getPathCost(start));
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
            pathStates.push(tempNode.getState());

            // if the parent node's state equals the starting node, then end while loop.
            if (tempNode.getState().equals(start)) {
                initial = true;
            }
        }

        return pathStates;
    }

    /**
     * Calculate manhattan distance from the two cartesian coordinates passed in.
     *
     * @param heuristic the heuristic being used.
     * @return manhattan distance.
     */
    private double heuristicScore(char heuristic) {
        int deltaX = getState().getC() - goal.getC();
        int deltaY = getState().getR() - goal.getR();

        switch (heuristic) {
            // Manhattan distance.
            case 'M':
                return Math.abs(deltaX) + Math.abs(deltaY); // return manhattan distance.
            // Euclidian distance.
            case 'E':
                return Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2)); // return manhattan distance.
            // Chebyshev distance.
            case 'C':
                return Math.max(Math.abs(deltaX), Math.abs(deltaY)); // return manhattan distance.

        }
        return 0;
    }

}

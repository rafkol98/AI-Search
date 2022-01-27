import java.util.Stack;

public class Node {


    private Coord state;
    private Node parent;
    private int depth;
    private Coord goal;
    private double h_Cost;
    private double f_Cost;

    /**
     *
     * @param parent
     * @param state
     */
    public Node (Node parent, Coord state) {
        this.state = state;
        this.parent = parent;
        this.depth = calculateDepth();
    }

    /**
     *
     * @param state
     * @param parent
     * @param depth
     * @param goal
     */
    public Node (Coord state, Node parent, int depth, Coord goal, char heuristic, String algo, Coord start) {
        this.state = state;
        this.parent = parent;
        this.depth = depth;
        this.goal = goal;
        this.h_Cost = heuristicScore(heuristic);
        this.f_Cost = calculateFCost(algo, start);
    }

    /**
     *
     * @return
     */
    public Coord getState() {
        return state;
    }

    /**
     *
     * @return
     */
    public Node getParent() {
        return parent;
    }

    /**
     *
     * @return
     */
    public int getDepth() {
        return depth;
    }

    /**
     *
     * @param start
     * @return
     */
    public float getPathCost(Coord start) {
        return  getPath(start).size() - 1; // -1 is to remove the initial node from the cost.
    }

    /**
     *
     * @return
     */
    public double getH_Cost() {
        return h_Cost;
    }

    /**
     *
     * @return
     */
    public double getF_Cost() {
        return f_Cost;
    }

    /**
     *
     * @return
     */
    private int calculateDepth() {
        // Handle the initial case (when parent == null).
        if(parent == null) {
            return 1;
        }
        // Return number of steps along the path from the initial state.
        else {
            return parent.depth + 1;
        }
    }

    /**
     * Calculate f_cost depending on the algorithm used.
     * @return
     */
    private double calculateFCost(String algo, Coord start) {
        switch (algo) {
            case "BestF":
                return getH_Cost();
            case "AStar":
                return getH_Cost() + getPathCost(start);
        }
            return 0;
    }

    /**
     * Get path from a start node to the current one.
     * @param start
     * @return
     */
    public Stack<Coord> getPath(Coord start) {
        Stack<Coord> pathStates = new Stack<>(); // ArrayList that holds the states in the path.

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
     * @return manhattan distance.
     */
    private double heuristicScore(char heuristic) {
        int deltaX = getState().getC() - goal.getC();
        int deltaY = getState().getR() - goal.getR();

        switch (heuristic) {
            // Manhattan distance.
            case 'M':
                return Math.abs(deltaX) +  Math.abs(deltaY); // return manhattan distance.
            // Euclidian distance.
            case 'E':
                return Math.sqrt(Math.pow(deltaX,2) +  Math.pow(deltaY,2)); // return manhattan distance.
            // Chebyshev distance.
            case 'C':
                return Math.max(Math.abs(deltaX), Math.abs(deltaY)); // return manhattan distance.

        }
       return 0;
    }

}

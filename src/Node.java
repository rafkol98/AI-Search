import java.util.Stack;

public class Node {


    private Coord state;
    private Node parent;
    private int depth;
    private int pathCost;

    public Node (Node parent, Coord state) {
        this.state = state;
        this.parent = parent;
        this.depth = calculateDepth();
        this.pathCost = calculatePathCost();
    }

    public Coord getState() {
        return state;
    }

    public Node getParent() {
        return parent;
    }

    public int getDepth() {
        return depth;
    }

    public int getPathCost() {

        return pathCost;
    }

    // Depth = number of steps along the path from the initial state.
    public int calculateDepth() {
        // Handle the initial case (when parent == null).
        if(parent == null) {
            return 1;
        } else {
            return parent.depth + 1;
        }
    }

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

    //TODO: cost function.
    // Calculate path cost.
    public int calculatePathCost() {
//        return parent.pathCost + cost(, state);
        return 0;
    }

    /**
     * Calculate manhattan distance from the two cartesian coordinates passed in.
     * @param g goal coordinate.
     * @return manhattan distance.
     */
    public int getScore(Coord g, String heuristic) {
        switch (heuristic) {
            case "M":
                int deltaX = getState().getC() - g.getC();
                int deltaY = getState().getR() - g.getR();

                return Math.abs(deltaX) +  Math.abs(deltaY); // return manhattan distance.
        }
       return 0;
    }

}

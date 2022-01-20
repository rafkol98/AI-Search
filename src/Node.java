public class Node {

    //TODO:
    // How do I implement the STATE[node] from pseudo code?
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
        return parent.depth + 1;
    }

    //TODO: cost function.
    // Calculate path cost.
    public int calculatePathCost() {
        return parent.pathCost + cost(, state);
    }
}

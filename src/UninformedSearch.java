import java.util.ArrayList;
import java.util.LinkedList;
import java.util.stream.Collectors;

public class UninformedSearch extends Search {

    private LinkedList<Node> frontier;

    /**
     * Create an uninformed search instance.
     * @param map
     * @param start
     * @param goal
     */
    public UninformedSearch(Map map, Coord start, Coord goal) {
        super(map, start, goal);
        this.frontier = new LinkedList<>();
    }


    @Override
    public void loopFrontier() {
        // While the frontier is not empty, loop through it.
        while (!frontier.isEmpty()) {
            printFrontier(); // print frontier.

            Node currentNode = removeFromFrontier(); // Remove first node from frontier.
            addExplored(currentNode); // Add current node to explored.

            if (goalTest(currentNode.getState(), getGoal())) {
                printGoal(currentNode); // print the final goal output.
            } else {
                insertAll(expand(currentNode)); // insert to the frontier all nodes returned from the expand function.
            }
        }

    }

    /**
     * Expand the search tree by creating a node for all legal states reachable from the current node.
     * @param node
     * @return
     */
    @Override
    public ArrayList<Node> expand(Node node) {
        ArrayList<Coord> nextStates = successor(node.getState()); // Assign all the next legal states to an ArrayList.

        ArrayList<Node> successors = new ArrayList<>(); // ArrayList to hold the successor nodes.

        // Iterate through the next states.
        for (Coord state : nextStates) {
            // if state is not contained in a node of explored or frontier.
            if (!getFrontierStates().contains(state) && !getExploredStates().contains(state)) {
                Node nd = new Node(node, state);
                successors.add(nd);
            }
        }
        return successors;
    }

    public void insert(Node node) {
        frontier.add(node); // Add node.
    }

    /**
     * Insert all successor nodes passed to the frontier.
     * @param successors
     */
    @Override
    public void insertAll(ArrayList<Node> successors) {

        for (Node node : successors) {
            switch (getAlgo()) {
                case "BFS":
                    frontier.addLast(node);  // Add current node last - first in, first out.
                    break;
                case "DFS":
                    frontier.addFirst(node); // Add current node first - first in, last out.
                    break;
            }
        }
    }

    /**
     * Iterate through the nodes of the frontier, add state of all the nodes to the frontierStates ArrayList.
     * @return
     */
    public ArrayList<Coord> getFrontierStates() {
        ArrayList<Coord> frontierStates = new ArrayList<>();

        for (Node node : frontier) {
            frontierStates.add(node.getState());
        }
        return frontierStates;
    }

    public Node removeFromFrontier() {
        return frontier.poll();
    }

    /**
     * Print elements that are currently in the frontier.
     */
    public void printFrontier() {
        System.out.println("["+frontier.stream().map(n->n.getState().toString()).collect(Collectors.joining(","))+"]");
    }

}


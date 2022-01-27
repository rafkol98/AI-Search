import java.util.ArrayList;

public class UninformedSearch extends Search {

    /**
     * Create an uninformed search instance.
     * @param map
     * @param start
     * @param goal
     */
    public UninformedSearch(Map map, Coord start, Coord goal) {
        super(map, start, goal);
    }

    /**
     * The function used to search for a solution in a tree.
     * @return
     */
    @Override
    public void treeSearch(String algo) {
        Node initialNode = new Node(null, getStart()); // Create initial node.

        insert(initialNode, algo); // Insert initial node to the frontier.

        // While the frontier is not empty, loop through it.
        while (!getFrontier().isEmpty()) {
            printFrontier(); // print frontier.

            Node currentNode = removeFromFrontier(); // Remove first node from frontier.
            addExplored(currentNode); // Add current node to explored.

            if (goalTest(currentNode.getState(), getGoal())) {
                printGoal(currentNode); // print the final goal output.
            } else {
                insertAll(expand(currentNode), algo); // insert to the frontier all nodes returned from the expand function.
            }
        }

        failure(); // if path was not found -> print failure.

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

    /**
     * Insert all successor nodes passed to the frontier.
     * @param successors
     * @param algo
     */
    @Override
    public void insertAll(ArrayList<Node> successors, String algo) {

        for (Node node : successors) {
            switch (algo) {
                case "BFS":
                    getFrontier().addLast(node);  // Add current node last - first in, first out.
                    break;
                case "DFS":
                    getFrontier().addFirst(node); // Add current node first - first in, last out.
                    break;
            }
        }
    }


}


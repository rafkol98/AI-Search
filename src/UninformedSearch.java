import java.util.ArrayList;

public class UninformedSearch extends Search {

    /**
     *
     * @param map
     * @param start
     * @param goal
     */
    public UninformedSearch(Map map, Coord start, Coord goal) {
        super(map, start, goal);
    }

    /**
     *
     * @return
     */
    @Override
    public Node treeSearch(String algo) {
        Node initialNode = new Node(null, getStart());

        // Insert initial node to the frontier.
        insert(initialNode, algo);

        //Print initial state.
        printFrontier();

        while (!getFrontier().isEmpty()) {
            Node currentNode = removeFromFrontier(); // Remove first node from frontier.
            addExplored(currentNode); // Add current node to explored.

            if (goalTest(currentNode.getState(), getGoal())) {
                printGoal(currentNode);
                return currentNode;
            } else {
                insertAll(expand(currentNode), algo);
                printFrontier();
            }
        }

        //TODO CHANGE!
        return initialNode;
    }

    /**
     *
     * @param node
     * @return
     */
    @Override
    public ArrayList<Node> expand(Node node) {
        ArrayList<Coord> nextStates = successor(node.getState());

        ArrayList<Node> successors = new ArrayList<>();

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


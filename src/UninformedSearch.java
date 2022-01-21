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
    public Node treeSearch() {
        Node initialNode = new Node(null, getStart());

        // Insert initial node to the frontier.
        insertInFrontier(initialNode);

        while (!getFrontier().isEmpty()) {
            // Remove first node from frontier.
            Node currentNode = removeFromFrontier();
            // Add current node to explored.
            addExplored(currentNode);

            if (goalTest(currentNode.getState(), getGoal())) {
                return currentNode;
            } else {
                insertAll(expand(currentNode));
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
            if (!getFrontier().contains(state) && !getExplored().contains(state)) {
                Node nd = new Node(node, state);
                successors.add(nd);
            }
        }

        return successors;
    }


}


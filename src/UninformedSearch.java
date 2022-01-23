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
        insertInFrontier(initialNode, algo);

        while (!getFrontier().isEmpty()) {
            // Remove first node from frontier.
            Node currentNode = removeFromFrontier();
            // Add current node to explored.
            addExplored(currentNode);

            if (goalTest(currentNode.getState(), getGoal())) {
                System.out.println("GOAL!");
                return currentNode;
            } else {
                insertAll(expand(currentNode), algo);
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

    @Override
    public void insertAll(ArrayList<Node> successors, String algo) {
        System.out.println("Kalethike to insertALL");
        for (Node node : successors) {
            switch (algo) {
                case "BFS":
                    getFrontier().addFirst(node);
                    break;
                case "DFS":
                    getFrontier().addLast(node);
                    break;
            }
        }
    }

    @Override
    public void insertInFrontier(Node node, String algo) {
        getFrontier().add(node);
        switch (algo) {
            case "BFS":
                getFrontier().addFirst(node);
                break;
            case "DFS":
                getFrontier().addLast(node);
                break;
        }
    }

}


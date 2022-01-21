import java.util.ArrayList;

public class UninformedSearch extends Search {


    public UninformedSearch(Map map, Coord start, Coord goal) {
        super(map, start, goal);
    }

    @Override
    public Node treeSearch() {
        Node initialNode = new Node(null, getStart());

        // Insert initial node to the frontier.
        insertInFrontier(initialNode);

        ArrayList<Node> explored = new ArrayList<>();

        while (!getFrontier().isEmpty()) {
            // Remove first node from frontier.
            Node currentNode = removeFromFrontier();
            // Add current node to explored.
            explored.add(currentNode);

            if (goalTest(currentNode.getState(), getGoal())) {
                return currentNode;
            } else {
                insertAll(expand(currentNode));
            }
        }

        //TODO CHANGE!
        return initialNode;
    }

    @Override
    public ArrayList<Node> expand(Node node) {
        ArrayList<Coord> nextStates = successor(node.getState());

        ArrayList<Node> successors = new ArrayList<>();

        for (Coord state : nextStates) {
            Node nd = new Node(node, state);
            successors.add(nd);
        }

        return successors;
    }


}


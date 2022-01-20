import java.util.LinkedList;
import java.util.Queue;

public class Search {

    private Map map;
    private Coord start;
    private Coord goal;
    Queue<Node> frontier;


    public Search(Map map, Coord start, Coord goal) {
        this.map = map;
        this.start = start;
        this.goal = goal;
        this.frontier = new LinkedList<>();
    }

    // TODO return failure or success.
    public Node treeSearch() {

        Node initialNode = new Node(null, start);

        // Insert initial node to the frontier.
        insertInFrontier(initialNode);

        while (!frontier.isEmpty()) {
            // Remove first node from frontier.
            Node currentNode = removeFromFrontier();

            if (goalTest(currentNode.getState(), goal)) {
                return currentNode;
            } else {
//                frontier ← INSERT-ALL (EXPAND(nd, problem, frontier))
                expand(currentNode, map, frontier);
            }
        }
    }
//    public void

    /**
     * Insert node to frontier.
     */
    public void insertInFrontier(Node node) {
        frontier.add(node);
    }

    public Node removeFromFrontier() {
        return frontier.poll();
    }

    public boolean goalTest(Coord state, Coord goal) {
        return state.equals(goal);
    }

    public Node[] expand() {

    }

    //
    public Node[] successor() {

    }



}

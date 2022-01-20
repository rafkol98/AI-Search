import java.lang.reflect.Array;
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


    public Node[] expand(Node node, Map map, Queue<Node> frontier) {
//        next states ← SUCCESSOR-FN(STATE[node],problem)
//        successors ← empty set
//        for each state in next states
//        nd ← MAKE-NODE(node,state) add nd to successors
//        end for
//        return successors
        Node[] states = successor();

    }

    //
    public Node[] successor(Coord state, Map map) {
        //From a cell,
        // the agent moves in the triangle map in 3 directions, Left, Right, and Downif the triangle points upwards
        // and Left, Right, and Up if the triangle points downwards,

        Node[] successorStates = [];

        //STEP 1: find direction that triangle points.
        boolean direction = triangleDirection();

        //STEP 2: depending on the direction use -1 +1 on rows and columns to get the successor states.

        //STEP 3: If map coordinates (row and col) are 1 then dont add that staate to the succesor


        return successorStates;
    }



}

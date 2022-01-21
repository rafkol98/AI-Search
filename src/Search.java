import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class Search {

    Queue<Node> frontier;
    private int[][] map;
    private Coord start;
    private Coord goal;


    public Search(Map map, Coord start, Coord goal) {
        this.map = map.getMap();
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
                insertAll(expand(currentNode));
            }
        }

        //TODO CHANGE!
        return initialNode;
    }
//    public void

    public void insertAll(ArrayList<Node> successors) {
        for (Node node : successors) {
            frontier.add(node);
        }
    }

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


    public ArrayList<Node> expand(Node node) {
        ArrayList<Coord> nextStates = successor(node.getState());

        ArrayList<Node> successors = new ArrayList<>();

        for (Coord state : nextStates) {
            Node nd = new Node(node, state);
            successors.add(nd);
        }

        return successors;
    }

    //
    public ArrayList<Coord> successor(Coord state) {
        // Find direction that triangle points.
        boolean upwards = isTriangleUpwards(state);

        return getMoves(state, upwards);
    }

    //TODO: maybe you can add a new property 'Direction'  in the NODE class.

    /**
     * Get if a triangle is upwards or downwards facing.
     *
     * @param state
     * @return if triangle is upwards or downwards pointing.
     */
    public boolean isTriangleUpwards(Coord state) {
        // Get row and column of state.
        int row = state.getR();
        int col = state.getC();

        boolean upwards;

        // if row and column have modulo of 0 with 2, then the arrow is upwards facing.
        if ((row % 2 == 0 && col % 2 == 0) || (row % 2 == 1 && col % 2 == 1)) {
            upwards = true;
        } else {
            upwards = false;
        }

        return upwards;
    }

    // CONDITIONS:
    // GET MAP -> MAP CONTAINS THE COORDINATES && THEY ARE NOT EQUAL TO 1!
    public ArrayList<Coord> getMoves(Coord state, boolean upwards) {

        ArrayList<Coord> successorStates = new ArrayList<>();

        // Get row and column of state.
        int row = state.getR();
        int col = state.getC();

        Coord stateLeft = new Coord(row - 1, col);
        Coord stateRight = new Coord(row + 1, col);
        Coord stateVertical;

        if (upwards) {
            stateVertical = new Coord(row, col + 1);
        } else {
            stateVertical = new Coord(row, col - 1);
        }

        successorStates.add(stateLeft);
        successorStates.add(stateRight);
        successorStates.add(stateVertical);

        return keepOnlyLegalStates(successorStates);
    }

    // Can I move to that state??
    public ArrayList<Coord> keepOnlyLegalStates(ArrayList<Coord> states) {

        ArrayList<Coord> legalStates = new ArrayList<>();

        for (Coord state : states) {
            // Get row and column of state passed in.
            int row = state.getR();
            int col = state.getC();

            // TODO: make sure its -1.
            int rows = map.length - 1;
            int columns = map[0].length - 1;

            // Check if row and column are bigger or equal than 0,
            // if they are less or equal than the rows and columns length.
            // and if the state is not a 1 (land) on the map.
            if ((row >= 0 && col >= 0) && (row <= rows && col <= columns) && (map[row][col] != 1)) {
                legalStates.add(state);
            }
        }

        return legalStates;
    }

}

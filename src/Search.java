import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public abstract class Search {

    private LinkedList<Node> frontier;
    private int[][] map;
    private Coord start;
    private Coord goal;
    private ArrayList<Node> explored = new ArrayList<>();

    public Search(Map map, Coord start, Coord goal) {
        this.map = map.getMap();
        this.start = start;
        this.goal = goal;
        this.frontier = new LinkedList<>();
    }


    public LinkedList<Node> getFrontier() {
        return frontier;
    }

    /**
     * Get states of nodes that are included in the frontier.
     * @return
     */
    public ArrayList<Coord> getFrontierStates() {
        ArrayList<Coord> frontierStates = new ArrayList<>();
        // Iterate through the nodes of the frontier, add state of all the nodes to
        // the frontierStates ArrayList.
        for (Node node : frontier) {
            frontierStates.add(node.getState());
        }
        return frontierStates;
    }

    /**
     * Get states of nodes that are included in the explored list.
     * @return
     */
    public ArrayList<Coord> getExploredStates() {
        ArrayList<Coord> exploredStates = new ArrayList<>();
        // Iterate through the nodes of the frontier, add state of all the nodes to
        // the frontierStates ArrayList.
        for (Node node : explored) {
            exploredStates.add(node.getState());
        }
        return exploredStates;
    }


    public int[][] getMap() {
        return map;
    }

    public Coord getStart() {
        return start;
    }

    public Coord getGoal() {
        return goal;
    }

    public ArrayList<Node> getExplored() {
        return explored;
    }

    public void addExplored(Node node) {
        explored.add(node);
    }

    public abstract Node treeSearch(String algo);

    public abstract ArrayList<Node> expand(Node node);

    public abstract void insertAll(ArrayList<Node> successors, String algo);

    /**
     * Insert node to frontier.
     */
    public abstract void insertInFrontier(Node node, String algo);

    public Node removeFromFrontier() {
        return frontier.poll();
    }

    public boolean goalTest(Coord state, Coord goal) {
        return state.equals(goal);
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
    public ArrayList<Coord> getMoves(Coord state, boolean upwardsTriangleDirection) {

        ArrayList<Coord> successorStates = new ArrayList<>();

        // Get row and column of state.
        int row = state.getR();
        int col = state.getC();

        Coord stateLeft = new Coord(row - 1, col);
        Coord stateRight = new Coord(row + 1, col);
        Coord stateVertical;

        boolean down;

        if (upwardsTriangleDirection) {
            stateVertical = new Coord(row, col + 1);
            down = false;
        } else {
            stateVertical = new Coord(row, col - 1);
            down = true;
        }

        // Tie breaking.
        // Add the right position (1st priority).
        successorStates.add(stateRight);

        // If the vertical state is down, then add it second (2nd priority)
        if (down) {
            successorStates.add(stateVertical);
        }

        // Add the left position (3rd priority)
        successorStates.add(stateLeft);

        // If the vertical state is upwards, then add it last (4th priority)
        if (!down) {
            successorStates.add(stateVertical);
        }

        // Call the keep only legal states function to only add the legal states out of these in the frontier.
        return keepOnlyLegalStates(successorStates);
    }

    // Can I move to that state?? Is it legal?
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

    public void printFrontier() {
        System.out.print("[");
        for (Node node : frontier) {
            System.out.print(node.getState());
        }
        System.out.print("] \n");
    }

}

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Stack;
import java.util.stream.Collectors;

//Support for BFS and DFS.
public class BidirectionalSearch {

    // Initialise frontier.
    private LinkedList<Node> frontier;
    private LinkedList<Node> frontier2;

    private boolean intersection = false;
    private Coord intersectionCoords;

    // Initialise variables.
    private int[][] map;
    private Coord start;
    private Coord goal;

    private ArrayList<Node> explored = new ArrayList<>();
    private ArrayList<Node> explored2 = new ArrayList<>();

    /**
     * @param map
     * @param start
     * @param goal
     */
    public BidirectionalSearch(Map map, Coord start, Coord goal) {
        this.map = map.getMap();
        this.start = start;
        this.goal = goal;
        this.frontier = new LinkedList<>();
        this.frontier2 = new LinkedList<>();
    }

    public Node getNodeBasedOnState(ArrayList<Node> explored, Coord state) {

        for (Node node : explored) {
            if (node.getState().equals(state)) {
                System.out.println("TEST: "+node.getState() +" ==? " + state);
                return node;
            }
        }
        return null;
    }


    /**
     * Iterate through the nodes of the frontier, add the state of each node to the frontierStates ArrayList.
     *
     * @return an ArrayList containing all the states of the frontier.
     */
    public ArrayList<Coord> getFrontierStates(int frontierNo) {
        ArrayList<Coord> frontierStates = new ArrayList<>();
        if (frontierNo == 1) {
            // Iterate through the frontier, adding each node's state to the frontierStates ArrayList.
            for (Node node : frontier) {
                frontierStates.add(node.getState());
            }
        } else {
            // Iterate through the frontier, adding each node's state to the frontierStates ArrayList.
            for (Node node : frontier2) {
                frontierStates.add(node.getState());
            }
        }

        return frontierStates;
    }

    //******* CHANGED
    /**
     * Add a node to the explored ArrayList.
     *
     * @param node the node to be added.
     */
    public void addExplored(Node node, int frontierNo) {
        if (frontierNo == 1) {
            explored.add(node);
        } else {
            explored2.add(node);
        }
    }

    /**
     * Print all the states currently in the frontier.
     *
     * @param frontier the frontier to be printed.
     */
    public void printFrontier(Collection<Node> frontier) {
        System.out.println("[" + frontier.stream().map(n -> n.getState().toString()).collect(Collectors.joining(",")) + "]");
    }

    //******* CHANGED
    /**
     * Construct the search tree to find the goal.
     *
     */
    public void treeSearch() {

        Node initialNode = new Node(null, start); // Create initial node.
        Node endNode = new Node(null, goal); // Create initial node.

        insert(initialNode, frontier); // Insert initial node to the frontier.
        insert(endNode, frontier2);

        loopFrontier();

        failure(); // if path was not found -> print failure.
    }

    //******* CHANGED
    /**
     * Loop and explore the frontier. If goal is found, its path, cost, and explored nodes are printed.
     * Otherwise, it continues exploring the frontier until its empty.
     */
    public void loopFrontier() {
        // While the frontier is not empty, loop through it.
        while (!frontier.isEmpty() && !frontier2.isEmpty() && !intersection) {
            printFrontier(frontier); // print frontier.
            printFrontier(frontier2); // print frontier.
            System.out.println("--------------");

            Node currentNode = removeFromFrontier(frontier);
            Node currentNode2  = removeFromFrontier(frontier2);

            // Add current node to explored.
            addExplored(currentNode, 1);
            addExplored(currentNode2, 2);

            if (intersect(currentNode.getState()) || intersect(currentNode2.getState())) {
                printGoal(getNodeBasedOnState(explored, intersectionCoords), getNodeBasedOnState(explored2, intersectionCoords)); // print the final goal output.
            } else {
                insertAll(expand(currentNode, 1), frontier); // insert to the frontier all nodes returned from the expand function.
                insertAll(expand(currentNode2, 2), frontier2);
            }
        }
    }

    //******* CHANGED
    /**
     * Expand a node by finding its suitable successors (next possible moves).
     *
     * @param node the node to be expanded.
     * @return an ArrayList containing all the legal and available successors of the node passed in.
     */
    public ArrayList<Node> expand(Node node, int frontierNo) {

        ArrayList<Coord> nextStates = successor(node.getState()); // Assign all the next legal states to an ArrayList.

        ArrayList<Node> successors = new ArrayList<>(); // ArrayList to hold the successor nodes.

        // Iterate through the next states.
        for (Coord state : nextStates) {
            addSuitableSuccessors(frontierNo,state, successors, node);
        }
        return successors;
    }

    /**
     * Ensures that the state being explored is not contained already in the frontier or was previously explored.
     * If it is not, then it is added to the successors ArrayList passed in.
     *
     * @param state      the state being explored.
     * @param successors the successors ArrayList - where we store all the suitable successors.
     * @param parent     the parent node of the state.
     */
    public void addSuitableSuccessors(int frontierNo, Coord state, ArrayList<Node> successors, Node parent) {
        // if state is not contained in a node of explored or frontier.
        if (!getFrontierStates(frontierNo).contains(state) && !getExploredStates(frontierNo).contains(state)) {
            Node nd = new Node(parent, state);
            successors.add(nd);
        }
    }

    //******* CHANGED
    /**
     * Inserts all the successors to the frontier.
     *
     * @param successors the successors ArrayList containing all the suitable successors.
     */
    public void insertAll(ArrayList<Node> successors, LinkedList<Node> frontier) {
        for (Node node : successors) {
            frontier.addLast(node);
        }
    }

    //******* CHANGED
    /**
     * Insert a node to the frontier.
     *
     * @param node the node to be added.
     */
    public void insert(Node node, Collection<Node> frontier) {
        frontier.add(node); // add node.
    }

    /**
     * Removes the first element of the frontier (the one with the lowest F_Cost currently in the frontier).
     *
     * @return the node removed.
     */
    public Node removeFromFrontier(LinkedList<Node> frontier) {
        return frontier.poll();
    }

    /**
     * Get states of nodes that are included in the explored list.
     *
     * @return the states of the nodes explored.
     */
    public ArrayList<Coord> getExploredStates(int frontierNo) {
        ArrayList<Coord> exploredStates = new ArrayList<>();

        if (frontierNo == 1) {
            // Iterate through the nodes of the frontier, add state of all the nodes to
            // the frontierStates ArrayList.
            for (Node node : explored) {
                exploredStates.add(node.getState());
            }
        } else {
            // Iterate through the nodes of the frontier, add state of all the nodes to
            // the frontierStates ArrayList.
            for (Node node : explored2) {
                exploredStates.add(node.getState());
            }
        }

        return exploredStates;
    }

    /**
     * Check if the state passed in is the goal state.
     *
     * @param state the coordinates of the state passed in.
     * @return true if it is the goal, false otherwise.
     */
    public boolean intersect(Coord state) {
        if (getExploredStates(1).contains(state) && getExploredStates(2).contains(state)) {
            System.out.println("INTERSECTION");
            System.out.println("Explored 1 :" + getExploredStates(1).toString());
            System.out.println("Explored 2 :" + getExploredStates(2).toString());
            System.out.println(state.getR() +" , "+state.getC());
            intersectionCoords = state; // store intersected state's coord.
            return true;
        } else {
            return false;
        }
    }

    /**
     * @param state
     * @return
     */
    public ArrayList<Coord> successor(Coord state) {
        boolean upwards = isTriangleUpwards(state); // Find if triangle points upwards (direction).

        ArrayList<Coord> successorStates = new ArrayList<>(); // Initialise ArrayList to hold all the successor states.

        int row = state.getR(); // Get row of state passed in.
        int col = state.getC(); // Get column of state passed in.

        Coord stateLeft = new Coord(row, col - 1); // Initialise the left state.
        Coord stateRight = new Coord(row, col + 1); // Initialise the right state.
        Coord stateVertical;

        boolean down;

        if (upwards) {
            stateVertical = new Coord(row + 1, col); // Assign the downward state in the stateVertical.
            down = true;
        } else {
            stateVertical = new Coord(row - 1, col); // Assign the upwards state in the stateVertical.
            down = false;
        }

        // Tie breaking.
        successorStates.add(stateRight); // Add the right position (1st priority).

        // If the vertical state is down, then add it second (2nd priority)
        if (down) {
            successorStates.add(stateVertical);
        }

        successorStates.add(stateLeft);  // Add the left position (3rd priority)

        // If the vertical state is upwards, then add it last (4th priority)
        if (!down) {
            successorStates.add(stateVertical);
        }

        return keepOnlyLegalStates(successorStates); // Keep and return only the legal states out of the ones added.
    }

    /**
     * Get if a triangle is upwards or downwards facing.
     *
     * @param state
     * @return if triangle is upwards or downwards pointing.
     */
    public boolean isTriangleUpwards(Coord state) {
        int row = state.getR(); // Get row of state passed in.
        int col = state.getC(); // Get column of state passed in.

        boolean upwards; // flag to determine if triangle faces upwards or downwards.

        // if row and column have modulo of 0 with 2, then the arrow is upwards facing.
        if ((row % 2 == 0 && col % 2 == 0) || (row % 2 == 1 && col % 2 == 1)) {
            upwards = true;
        } else {
            upwards = false;
        }

        return upwards;
    }


    /**
     * This method ensures that only legal states are kept.
     * It ensures that both row and column are greater or equal than 0 (we cannot have negative coordinates) and less
     * or equal than the map's row and column boundaries.
     * It finally checks that the state is not an island i.e. has a value of 1 on the map.
     *
     * @param states
     * @return
     */
    public ArrayList<Coord> keepOnlyLegalStates(ArrayList<Coord> states) {

        ArrayList<Coord> legalStates = new ArrayList<>();

        // Iterate through the states passed in, keep only the legal states.
        for (Coord state : states) {

            int row = state.getR(); // Get row of current state.
            int col = state.getC(); // Get column of current state.

            int rows = map.length - 1;
            int columns = map[0].length - 1;

            // Check that the current state is legal.
            if ((row >= 0 && col >= 0) && (row <= rows && col <= columns) && (map[row][col] != 1)) {
                legalStates.add(state); // add state to the legal states ArrayList.
            }
        }

        return legalStates;
    }

    //TODO: have to fix this!!!
    /**
     * Print final output (when goal node is reached).
     *
     */
    public void printGoal(Node node1, Node node2) {
        Stack<Coord> pathStates = node1.getPath(start);
        Stack<Coord> pathStates2 = node2.getPath(goal);
        reverseStack(pathStates2);
        pathStates2.pop(); // remove first element (as it is the intersection).

        //TODO: TEMPORARY - MERGE THEM!
        // Print path, path cost, and number of nodes explored.
        while (!pathStates.isEmpty()) {
            System.out.print(pathStates.pop());
        }
        while (!pathStates2.isEmpty()) {
            System.out.print(pathStates2.pop());
        }
        System.out.println();

//        System.out.println("\n" + node.getPathCost(intersectionCoords)); // Print path cost.
//        System.out.println(getExplored().size()); // Print nodes explored.

        System.exit(0); // Exit system.
    }

    /**
     * If the search could not find a solution, print fail message, the explored size, and then exit the system.
     */
    public void failure() {
        System.out.println("fail");
        //TODO: print explored.
//        System.out.println(getExplored().size());
        System.exit(0); // Exit system.
    }

    // Recursive function to insert an item at the bottom of a given stack
    public static void insertAtBottom(Stack<Coord> s, Coord state)
    {
        if (s.empty())
        {
            s.push(state);
            return;
        }
        Coord top = s.pop();
        insertAtBottom(s, state);
        s.push(top);
    }

    // Recursive function to reverse a given stack
    public static void reverseStack(Stack<Coord> s)
    {
        // base case: stack is empty
        if (s.empty()) {
            return;
        }
        Coord state = s.pop();
        reverseStack(s);

        insertAtBottom(s, state);
    }



}

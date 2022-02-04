import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Stack;
import java.util.stream.Collectors;

//Support for BFS and DFS.
public class BidirectionalSearch extends UninformedSearch {

    // Initialise frontier.
    private LinkedList<Node> frontier;
    private LinkedList<Node> frontier2;

    private boolean intersection = false;
    private Coord intersectionCoords;

    private ArrayList<Node> explored = new ArrayList<>();
    private ArrayList<Node> explored2 = new ArrayList<>();

    /**
     * @param map
     * @param start
     * @param goal
     */
    public BidirectionalSearch(Map map, Coord start, Coord goal, char heuristic) {
        super(map, start, goal, heuristic);
        this.frontier = new LinkedList<>();
        this.frontier2 = new LinkedList<>();
    }

    public Node getNodeBasedOnState(ArrayList<Node> explored, Coord state) {

        for (Node node : explored) {
            if (node.getState().equals(state)) {
                return node;
            }
        }
        return null;
    }

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
    @Override
    public void printFrontier(Collection<Node> frontier) {
        System.out.print("[" + frontier.stream().map(n -> n.getState().toString()).collect(Collectors.joining(",")) + "]");
    }

    /**
     * Construct the search tree to find the goal.
     *
     */
    public void treeSearch() {

        Node initialNode = new Node(null, getStart()); // Create initial node.
        Node endNode = new Node(null, getGoal()); // Create initial node.

        insert(initialNode, frontier); // Insert initial node to the frontier.
        insert(endNode, frontier2);

        loopFrontier();

        failure(); // if path was not found -> print failure.
    }

    /**
     * Loop and explore the frontier. If goal is found, its path, cost, and explored nodes are printed.
     * Otherwise, it continues exploring the frontier until its empty.
     */
    @Override
    public void loopFrontier() {
        // While the frontier is not empty, loop through it.
        while (!frontier.isEmpty() && !frontier2.isEmpty() && !intersection) {
            printFrontier(frontier); // print frontier.
            System.out.print(" , ");
            printFrontier(frontier2); // print frontier2.
            System.out.println();

            Node currentNode = removeFromFrontier(frontier);
            Node currentNode2  = removeFromFrontier(frontier2);

            // Add current node to explored.
            addExplored(currentNode, 1);
            addExplored(currentNode2, 2);

            if (intersect(currentNode.getState()) || intersect(currentNode2.getState())) {
                printGoal(getNodeBasedOnState(explored, intersectionCoords), getNodeBasedOnState(explored2, intersectionCoords)); // print the final goal output.
            } else {
                insertAll(expand(currentNode, frontier,1), frontier); // insert to the frontier all nodes returned from the expand function.
                insertAll(expand(currentNode2, frontier2,2), frontier2);
            }
        }
    }

    /**
     * Expand a node by finding its suitable successors (next possible moves).
     *
     * @param node the node to be expanded.
     * @return an ArrayList containing all the legal and available successors of the node passed in.
     */
    public ArrayList<Node> expand(Node node, LinkedList<Node> frontier, int frontierNo) {

        ArrayList<Coord> nextStates = successor(node.getState()); // Assign all the next legal states to an ArrayList.

        ArrayList<Node> successors = new ArrayList<>(); // ArrayList to hold the successor nodes.

        // Iterate through the next states.
        for (Coord state : nextStates) {
            addSuitableSuccessors(frontier, frontierNo,state, successors, node);
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
    public void addSuitableSuccessors(LinkedList<Node> frontier, int frontierNo, Coord state, ArrayList<Node> successors, Node parent) {
        // if state is not contained in a node of explored or frontier.
        if (!getFrontierStates(frontier).contains(state) && !getExploredStates(frontierNo).contains(state)) {
            Node nd = new Node(parent, state);
            successors.add(nd);
        }
    }

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

        // Iterate through the nodes of the explored (depending on frontier no.), add state of all the nodes to
        // the exploredStates ArrayList.
        if (frontierNo == 1) {
            for (Node node : explored) {
                exploredStates.add(node.getState());
            }
        } else {
            for (Node node : explored2) {
                exploredStates.add(node.getState());
            }
        }

        return exploredStates;
    }

    /**
     * Check if the state was explored by both sub-searches.
     *
     * @param state the coordinates of the state passed in.
     * @return true if explored by both, false otherwise.
     */
    public boolean intersect(Coord state) {
        if (getExploredStates(1).contains(state) && getExploredStates(2).contains(state)) {
            intersectionCoords = state; // store intersected state's coord.
            return true;
        }
        return false;
    }

    /**
     * Print final output (when goal node is reached).
     *
     */
    public void printGoal(Node node1, Node node2) {
        Stack<Coord> pathStates = node1.getPath(getStart());
        Stack<Coord> pathStates2 = node2.getPath(getGoal());
        reverseStack(pathStates2);
        pathStates2.pop(); // remove first element (as it is the intersection).

        while (!pathStates.isEmpty()) {
            System.out.print(pathStates.pop());
        }
        while (!pathStates2.isEmpty()) {
            System.out.print(pathStates2.pop());
        }
        System.out.println("\n" + node1.getPathCost(getStart()) + " , " + node2.getPathCost(getGoal()));
        System.out.println(explored.size() + " , " + explored2.size());

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

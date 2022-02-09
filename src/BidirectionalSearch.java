import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Stack;

/**
 * The Bidirectional class extends UninformedSearch. This is because it has some unique characteristics
 * when compared to other UninformedSearch algorithms such as two frontiers, two explored lists, and two
 * simultaneous searches.
 *
 * @author 210017984.
 */
public class BidirectionalSearch extends UninformedSearch {

    // Initialise frontier.
    private LinkedList<Node> frontier2;

    private boolean intersection = false;
    private Coord intersectionCoords;

    private ArrayList<Node> explored2 = new ArrayList<>();


    /**
     * Create an uninformed search instance - BestF and AStar algorithms.
     *
     * @param map   the map passed in.
     * @param start the starting coordinates.
     * @param goal  the goal coordinates.
     */
    public BidirectionalSearch(Map map, Coord start, Coord goal) {
        super(map, start, goal);
        this.frontier2 = new LinkedList<>();
    }

    /**
     * Recursive function that adds states at the bottom of a stack.
     *
     * @param stack the stack to insert states at the bottom to.
     * @param state the state to be added.
     */
    private static void insertAtBottom(Stack<Coord> stack, Coord state) {
        if (stack.empty()) {
            stack.push(state);
            return;
        }
        Coord top = stack.pop();
        insertAtBottom(stack, state); // recursive call.
        stack.push(top);
    }

    /**
     * A recursive function that reverses a given stack.
     * Used to reverse the path from the second sub-tree.
     *
     * @param stack the stack to be reversed.
     */
    private static void reverse(Stack<Coord> stack) {
        // if stack is empty, then the stack was reversed.
        if (stack.empty()) {
            return;
        }
        Coord state = stack.pop();
        reverse(stack); // recursive call.
        insertAtBottom(stack, state);
    }

    /**
     * Loop and explore the frontier. If goal is found, its path, cost, and explored nodes are printed.
     * Otherwise, it continues exploring the frontier until its empty.
     *
     * @param initialNode the initial node to be added before constructing the search tree.
     */
    @Override
    public void loopFrontier(Node initialNode) {
        insert(initialNode, getFrontier()); // Insert initial node to the original frontier.
        Node endNode = new Node(null, getGoal()); // Create end node.
        insert(endNode, frontier2); // insert end node to the inverse frontier.

        // While the frontier is not empty, loop through it.
        while (!getFrontier().isEmpty() && !frontier2.isEmpty() && !intersection) {
            printFrontier(getFrontier()); // print frontier.
            printFrontier(frontier2); // print frontier2.
            if (isPrint()) {
                System.out.println();
            }


            Node currentNode = removeFromFrontier(getFrontier());
            Node currentNode2 = removeFromFrontier(frontier2);

            // Add current node to explored.
            addExplored(currentNode, getExplored());
            addExplored(currentNode2, explored2);

            if (intersect(currentNode.getState()) || intersect(currentNode2.getState())) {
                // Get node of the intersection coordinates from each explored list (explored & explored2).
                setFoundSolution(true);
                printOutput(getNodeInExplored(getExplored(), intersectionCoords), getNodeInExplored(explored2, intersectionCoords)); // print the final goal output.
                break;
            } else {
                // insert to the frontier all nodes returned from the expand function.
                insertAll(expand(currentNode, getFrontier(), 1), getFrontier());
                // insert to the second frontier the nodes returned from the second search.
                insertAll(expand(currentNode2, frontier2, 2), frontier2);
            }
        }
    }

    /**
     * Ensures that the state being explored is not contained already in the frontier or was previously explored.
     * If it is not, then it is added to the successors ArrayList passed in.
     *
     * @param frontier   the frontier to make the check if node is contained already.
     * @param frontierNo the number of frontier. Used for BIDIRECTIONAL to get the states of the appropriate frontier,
     *                   as it uses 2 different frontiers.
     * @param state      the state being explored.
     * @param successors the successors ArrayList - where we store all the suitable successors.
     * @param parent     the parent node of the state.
     */
    @Override
    public void addSuitableSuccessors(Collection<Node> frontier, int frontierNo, Coord state, ArrayList<Node> successors, Node parent) {
        ArrayList<Node> tempExplored = frontierNo == 1 ? getExplored() : explored2; // get appropriate explored arraylist.

        // if state is not contained in a node of explored or frontier.
        if (!getFrontierStates(frontier).contains(state) && !getExploredStates(tempExplored).contains(state)) {
            Node nd = new Node(parent, state);
            successors.add(nd);
        }
    }

    /**
     * Inserts all the successors to the frontier.
     *
     * @param successors the successors ArrayList containing all the suitable successors.
     * @param frontier   the frontier to which to add all the successors (from the 2 frontiers).
     */
    public void insertAll(ArrayList<Node> successors, LinkedList<Node> frontier) {
        for (Node node : successors) {
            frontier.addLast(node);
        }
    }

    /**
     * Removes the first element of the frontier (the one with the lowest F_Cost currently in the frontier).
     *
     * @param frontier the frontier from which to remove a state (from the 2 frontiers).
     * @return the node removed.
     */
    public Node removeFromFrontier(LinkedList<Node> frontier) {
        return frontier.poll();
    }

    /**
     * Check if the state was explored by both sub-searches.
     *
     * @param state the coordinates of the state passed in.
     * @return true if explored by both, false otherwise.
     */
    public boolean intersect(Coord state) {
        if (getExploredStates(getExplored()).contains(state) && getExploredStates(explored2).contains(state)) {
            intersectionCoords = state; // store intersected state's coord.
            return true;
        }
        return false;
    }

    /**
     * Print final output (when goal node is reached).
     *
     * @param node1 the common node found from the first sub-tree.
     * @param node2 the common node found from the second sub-tree.
     */
    public void printOutput(Node node1, Node node2) {
        Stack<Coord> pathStates = node1.getPath(getStart());
        Stack<Coord> pathStates2 = node2.getPath(getGoal());
        reverse(pathStates2);
        pathStates2.pop(); // remove first element (as it is the intersection).

        nodesExplored = getExplored().size() + explored2.size();
        pathCost = node1.getPathCost(getStart()) + node2.getPathCost(getGoal());

        // if print flag is true, then print the output.
        if (isPrint()) {
            while (!pathStates.isEmpty()) {
                System.out.print(pathStates.pop());
            }
            while (!pathStates2.isEmpty()) {
                System.out.print(pathStates2.pop());
            }
            System.out.println("\n" + node1.getPathCost(getStart()) + " , " + node2.getPathCost(getGoal()));
            System.out.println(getExplored().size() + " , " + explored2.size());
        }
    }

}

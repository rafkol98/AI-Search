import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.stream.Collectors;

/**
 * The UninformedSearch class implementing the functionalities for both BFS and DFS.
 *
 * @author 210017984.
 */
public class UninformedSearch extends Search {

    // Initialise frontier.
    private LinkedList<Node> frontier;

    /**
     * Get the frontier. Used from the bidirectional class.
     *
     * @return the frontier.
     */
    public LinkedList<Node> getFrontier() {
        return frontier;
    }

    /**
     * Create an uninformed search instance - BestF and AStar algorithms.
     *
     * @param map   the map passed in.
     * @param start the starting coordinates.
     * @param goal  the goal coordinates.
     * @param heuristic the chosen heuristic.
     */
    public UninformedSearch(Map map, Coord start, Coord goal, char heuristic) {
        super(map, start, goal, heuristic);
        this.frontier = new LinkedList<>();
    }

    /**
     * Print all the states currently in the frontier.
     *
     * @param frontier the frontier to print.
     */
    @Override
    public void printFrontier(Collection<Node> frontier) {
        System.out.println("[" + frontier.stream().map(n -> n.getState().toString()).collect(Collectors.joining(",")) + "]");
    }

    /**
     * Loop and explore the frontier. If goal is found, its path, cost, and explored nodes are printed.
     * Otherwise, it continues exploring the frontier until its empty.
     *
     * @param initialNode the initial node to be added before constructing the search tree.
     */
    @Override
    public void loopFrontier(Node initialNode) {
        insert(initialNode, frontier); // Insert initial node to the frontier.

        // While the frontier is not empty, loop through it.
        while (!frontier.isEmpty()) {
            printFrontier(frontier); // print frontier.

            Node currentNode = removeFromFrontier(); // Remove first node from frontier.
            addExplored(currentNode, getExplored()); // Add current node to explored.

            if (goalTest(currentNode.getState())) {
                printOutput(currentNode); // print the final goal output.
            } else {
                insertAll(expand(currentNode, frontier, 0)); // insert to the frontier all nodes returned from the expand function.
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
        // if state is not contained in a node of explored or frontier.
        if (!getFrontierStates(frontier).contains(state) && !getExploredStates(getExplored()).contains(state)) {
            Node nd = new Node(parent, state);
            successors.add(nd);
        }
    }


    /**
     * Inserts all the successors to the frontier.
     *
     * @param successors the successors ArrayList containing all the suitable successors.
     */
    @Override
    public void insertAll(ArrayList<Node> successors) {
        if (getAlgo().equals("DFS")) {
            ArrayList<Node> reverseList = new ArrayList<>();
            for (int i = successors.size() - 1; i >= 0; i--) {
                reverseList.add(successors.get(i));
            }

            for (Node node : reverseList) {
                frontier.addFirst(node);
            }

        } else if (getAlgo().equals("BFS")) {
            for (Node node : successors) {
                frontier.addLast(node);  // Add current node last - first in, first out.

            }
        }
    }

    /**
     * Removes the first element of the frontier (the one entered first based on the tie-breaking constraints).
     *
     * @return the node removed.
     */
    public Node removeFromFrontier() {
        return frontier.poll();
    }

}


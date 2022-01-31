import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.stream.Collectors;

public class UninformedSearch extends Search {

    // Initialise frontier.
    private LinkedList<Node> frontier;

    /**
     * Create an uninformed search instance - BestF and AStar algorithms.
     *
     * @param map   the map passed in.
     * @param start the starting coordinates.
     * @param goal  the goal coordinates.
     */
    public UninformedSearch(Map map, Coord start, Coord goal) {
        super(map, start, goal);
        this.frontier = new LinkedList<>();
    }

    /**
     * Print all the states currently in the frontier.
     */
    @Override
    public void printFrontier(Collection<Node> frontier) {
        System.out.println("[" + frontier.stream().map(n -> n.getState().toString()).collect(Collectors.joining(",")) + "]");
    }

    /**
     * Loop and explore the frontier. If goal is found, its path, cost, and explored nodes are printed.
     * Otherwise, it continues exploring the frontier until its empty.
     */
    @Override
    public void loopFrontier() {
        // While the frontier is not empty, loop through it.
        while (!frontier.isEmpty()) {
            printFrontier(frontier); // print frontier.

            Node currentNode = removeFromFrontier(); // Remove first node from frontier.
            addExplored(currentNode); // Add current node to explored.

            if (goalTest(currentNode.getState())) {
                printGoal(currentNode); // print the final goal output.
            } else {
                insertAll(expand(currentNode)); // insert to the frontier all nodes returned from the expand function.
            }
        }
    }

    /**
     * Ensures that the state being explored is not contained already in the frontier or was previously explored.
     * If it is not, then it is added to the successors ArrayList passed in.
     *
     * @param state      the state being explored.
     * @param successors the successors ArrayList - where we store all the suitable successors.
     * @param parent     the parent node of the state.
     */
    @Override
    public void addSuitableSuccessors(Coord state, ArrayList<Node> successors, Node parent) {
        // if state is not contained in a node of explored or frontier.
        if (!getFrontierStates(frontier).contains(state) && !getExploredStates().contains(state)) {
            Node nd = new Node(parent, state);
            successors.add(nd);
        }
    }

    /**
     * Insert a node to the frontier.
     *
     * @param node the node to be added.
     */
    public void insert(Node node) {
        frontier.add(node); // Add node.
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


import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

public class InformedSearch extends Search {

    // Initialise frontier.
    private PriorityQueue<Node> frontier;

    /**
     * Create an uninformed search instance - BestF and AStar algorithms.
     *
     * @param map   the map passed in.
     * @param start the starting coordinates.
     * @param goal  the goal coordinates.
     */
    public InformedSearch(Map map, Coord start, Coord goal, char heuristic) {
        super(map, start, goal, heuristic);
        Comparator<Node> comparator = Comparator.comparing(Node::getF_Cost); // Comparator to sort the nodes based on their F_cost
        this.frontier = new PriorityQueue<>(comparator);
    }


    /**
     * Print all the states currently in the frontier.
     */
    @Override
    public void printFrontier(Collection<Node> frontier) {
        DecimalFormat df = new DecimalFormat("0.00"); // Get cost in 2 decimal places.
        System.out.println("[" + frontier.stream().map(n -> n.getState().toString() + ":" + df.format(n.getF_Cost())).collect(Collectors.joining(",")) + "]");
    }

    /**
     * Loop and explore the frontier. If goal is found, its path, cost, and explored nodes are printed.
     * Otherwise, it continues exploring the frontier until its empty.
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
     * @param state      the state being explored.
     * @param successors the successors ArrayList - where we store all the suitable successors.
     * @param parent     the parent node of the state.
     */
    @Override
    public void addSuitableSuccessors(Collection<Node> frontier, int frontierNo, Coord state, ArrayList<Node> successors, Node parent) {
        Node nd = new Node(parent, state, getGoal(), getHeuristic(), getAlgo(), getStart()); //TODO: pass in heuristic
        // if state is not contained in a node of explored or frontier.
        if (!getFrontierStates(frontier).contains(state) && !getExploredStates(getExplored()).contains(state)) {
            successors.add(nd);
        } else if ((getAlgo() == "AStar") && getFrontierStates(frontier).contains(state) && getNodeInFrontier(frontier, state) != null && (getNodeInFrontier(frontier, state).getPathCost(getStart()) > nd.getPathCost(getStart()))) {
            replaceNodeInFrontier(state, nd); // replace old node with the new one with the lower path cost.
        }
    }

    /**
     * Inserts all the successors to the frontier.
     *
     * @param successors the successors ArrayList containing all the suitable successors.
     */
    @Override
    public void insertAll(ArrayList<Node> successors) {
        for (Node node : successors) {
            frontier.add(node);
        }
    }

    /**
     * Removes the first element of the frontier (the one with the lowest F_Cost currently in the frontier).
     *
     * @return the node removed.
     */
    @Override
    public Node removeFromFrontier() {
        return frontier.poll();
    }

    /**
     * Replace node in the frontier with a new node with lower cost.
     *
     * @param state   the state we are looking.
     * @param newNode the new node that will replace the old.
     */
    private void replaceNodeInFrontier(Coord state, Node newNode) {
        if (frontier.removeIf(i -> i.getState().equals(state))) {
            frontier.add(newNode);
        }
    }

}

import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;
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
    public InformedSearch(Map map, Coord start, Coord goal) {
        super(map, start, goal);
        Comparator<Node> comparator = Comparator.comparing(Node::getF_Cost); // Comparator to sort the nodes based on their F_cost
        this.frontier = new PriorityQueue<>(comparator);
    }

    /**
     * Iterate through the nodes of the frontier, add the state of each node to the frontierStates ArrayList.
     *
     * @return an ArrayList containing all the states of the frontier.
     */
    @Override
    public ArrayList<Coord> getFrontierStates() {
        ArrayList<Coord> frontierStates = new ArrayList<>();
        // Iterate through the frontier, adding each node's state to the frontierStates ArrayList.
        for (Node node : frontier) {
            frontierStates.add(node.getState());
        }
        return frontierStates;
    }

    /**
     * Loop and explore the frontier. If goal is found, its path, cost, and explored nodes are printed.
     * Otherwise, it continues exploring the frontier until its empty.
     */
    @Override
    public void loopFrontier() {
        // While the frontier is not empty, loop through it.
        while (!frontier.isEmpty()) {
            printFrontier(); // print frontier.

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
     * Insert a node to the frontier.
     *
     * @param node the node to be added.
     */
    @Override
    public void insert(Node node) {
        frontier.add(node);
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
        if (!getFrontierStates().contains(state) && !getExploredStates().contains(state)) {
            Node nd = new Node(parent, state, getGoal(), 'M', getAlgo(), getStart()); //TODO: pass in heuristic
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
     * Print all the states currently in the frontier.
     */
    @Override
    public void printFrontier() {
        System.out.println("[" + frontier.stream().map(n -> n.getState().toString() + ":" + n.getF_Cost()).collect(Collectors.joining(",")) + "]");
    }


//    /**
//     * Helper function that helped me debug that the priorityQueue works as expected.
//     */
//    public void debugFrontier() {
//        System.out.println("-- DEBUG --");
//        PriorityQueue<Node> tempFrontier = new PriorityQueue<>(frontier);
//        while (!tempFrontier.isEmpty()) {
//            System.out.println(tempFrontier.poll().getState());
//        }
//    }

}

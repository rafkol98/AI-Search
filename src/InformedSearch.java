import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.stream.Collectors;

public class InformedSearch extends Search{

    private PriorityQueue<Node> frontier;

    public InformedSearch(Map map, Coord start, Coord goal) {
        super(map, start, goal);
        this.frontier = new PriorityQueue<>(new Comparator<Node>() {
            //TODO: maybe move this to a new class.
            @Override
            public int compare(Node n1, Node n2) {
                if (n1.getF_Cost() < n2.getF_Cost()) {
                    return -1;
                } else if (n1.getF_Cost() > n2.getF_Cost()){
                    return 1;
                }
                return 0;
            }
        });
    }

    @Override
    public ArrayList<Coord> getFrontierStates() {
        ArrayList<Coord> frontierStates = new ArrayList<>();

        for (Node node : frontier) {
            frontierStates.add(node.getState());
        }
        return frontierStates;
    }


    /**
     *
     */
    @Override
    public void loopFrontier() {
        // While the frontier is not empty, loop through it.
        while (!frontier.isEmpty()) {
            printFrontier(); // print frontier.

            Node currentNode = removeFromFrontier(); // Remove first node from frontier.
            addExplored(currentNode); // Add current node to explored.

            if (goalTest(currentNode.getState(), getGoal())) {
                printGoal(currentNode); // print the final goal output.
            } else {
                insertAll(expand(currentNode)); // insert to the frontier all nodes returned from the expand function.
            }
        }
    }

    /**
     *
     * @param node
     */
    @Override
    public void insert(Node node) {
        frontier.add(node);
    }

    @Override
    public void addSuitableSuccessors(Coord state, ArrayList<Node> successors, Node node) {
        // if state is not contained in a node of explored or frontier.
        if (!getFrontierStates().contains(state) && !getExploredStates().contains(state)) {
            Node nd = new Node(node, state, getGoal(), 'M', getAlgo(), getStart()); //TODO: pass in heuristic
            successors.add(nd);
        }
    }

    /**
     *
     * @param successors
     */
    @Override
    public void insertAll(ArrayList<Node> successors) {
        for (Node node : successors) {
            frontier.add(node);
        }
    }

    /**
     *
     * @return
     */
    @Override
    public Node removeFromFrontier() {
        return frontier.poll();
    }

    /**
     *
     */
    @Override
    public void printFrontier() {
        System.out.println("["+frontier.stream().map(n->n.getState().toString() + ":" +n.getF_Cost()).collect(Collectors.joining(","))+"]");
    }

}

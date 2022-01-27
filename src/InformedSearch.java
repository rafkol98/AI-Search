import java.util.ArrayList;
import java.util.LinkedList;
import java.util.PriorityQueue;

public class InformedSearch extends Search{

    private PriorityQueue<Node> frontier;

    public InformedSearch(Map map, Coord start, Coord goal) {
        super(map, start, goal);
        this.frontier = new PriorityQueue<>();
    }

    @Override
    public ArrayList<Coord> getFrontierStates() {
        return null;
    }
    

    @Override
    public void loopFrontier(String algo) {
        // While the frontier is not empty, loop through it.
        while (!frontier.isEmpty()) {
            printFrontier(); // print frontier.

            Node currentNode = removeFromFrontier(); // Remove first node from frontier.
            addExplored(currentNode); // Add current node to explored.

            if (goalTest(currentNode.getState(), getGoal())) {
                printGoal(currentNode); // print the final goal output.
            } else {
                insertAll(expand(currentNode), algo); // insert to the frontier all nodes returned from the expand function.
            }
        }
    }

    @Override
    public void insert(Node node) {

    }

    @Override
    public ArrayList<Node> expand(Node node) {
        return null;
    }

    @Override
    public void insertAll(ArrayList<Node> successors, String algo) {
    }



    @Override
    public Node removeFromFrontier() {
        return null;
    }

    @Override
    public void printFrontier() {

    }


}

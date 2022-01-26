import java.util.ArrayList;

public class InformedSearch extends Search{

    public InformedSearch(Map map, Coord start, Coord goal) {
        super(map, start, goal);
    }

    @Override
    public void treeSearch(String algo) {

    }

    @Override
    public ArrayList<Node> expand(Node node) {
        return null;
    }

    @Override
    public void insertAll(ArrayList<Node> successors, String algo) {
    }


}

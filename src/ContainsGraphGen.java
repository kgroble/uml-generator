import java.util.List;


public class ContainsGraphGen extends GraphGenDecorator {

    public ContainsGraphGen(GraphGenerator graphGen) {
        super(graphGen);
    }

    @Override
    public boolean addClassCells(List<String> classNames, Graph graph) {
        return false;
    }

    @Override
    public void addEdges(Graph graph) {

    }

}
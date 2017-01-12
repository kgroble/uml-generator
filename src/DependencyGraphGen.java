import java.util.List;

/**
 * Created by lewis on 1/11/17.
 */
public class DependencyGraphGen extends GraphGenDecorator {
    public DependencyGraphGen(GraphGenerator graphGen) {
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

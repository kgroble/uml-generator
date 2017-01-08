import java.util.List;

public abstract class GraphGenDecorator extends GraphGenerator {
    protected GraphGenerator graphGen;

    public GraphGenDecorator(GraphGenerator graphGen) {
        super(graphGen.recursive, graphGen.access);
        this.graphGen = graphGen;
    }

    @Override
    public abstract boolean addClassCells(List<String> classNames, Graph graph);

    @Override
    public abstract void addEdges(Graph graph);
}

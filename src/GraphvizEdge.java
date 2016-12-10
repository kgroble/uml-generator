
public class GraphvizEdge extends GraphvizElement {
    private String fromNode;
    private String toNode;

    @Override
    public String toGraphviz() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getIdentifier() {
        return this.fromNode + "-" + this.toNode;
    }

}

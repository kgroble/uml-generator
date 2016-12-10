import java.util.ArrayList;
import java.util.List;

public class Graph {
    private List<ClassCell> cells;
    private List<Edge> edges;

    public Graph() {
        this.cells = new ArrayList<>();
        this.edges = new ArrayList<>();
    }

    // Copy constructor for use by copy()
    private Graph(List<ClassCell> cells, List<Edge> edges) {
        this.cells = new ArrayList<>();
        this.edges = new ArrayList<>();

        this.cells.addAll(cells);
        this.edges.addAll(edges);
    }

    /**
     * Adds the passed ClassCell to the Graph. If there already exists a
     * ClassCell with the same name, this fails to add the ClassCell and returns
     * false.
     * 
     * @param cell
     *            The ClassCell to add to the list.
     * @return True if successfully added, false if the list already contained
     *         the class.
     */
    public boolean addClass(ClassCell cell) {
        if (this.cells.contains(cell)) {
            return false;
        }

        this.cells.add(cell);

        return true;
    }

    /**
     * Returns a copy of the list of ClassCells in the Graph.
     *
     * @return copy of the list of ClassCells.
     */
    public List<ClassCell> getCells() {
        List<ClassCell> copy = new ArrayList<>();
        copy.addAll(this.cells);
        return copy;
    }

    /**
     * Returns a copy of the list of Edges in the Graph.
     *
     * @return copy of the list of Edges.
     */
    public List<Edge> getEdges() {
        List<Edge> copy = new ArrayList<>();
        copy.addAll(this.edges);
        return copy;
    }

    /**
     * Returns a new copy of this Graph.
     *
     * @return A deep copy of this Graph.
     */
    public Graph copy() {
        return new Graph(this.cells, this.edges);
    }
}
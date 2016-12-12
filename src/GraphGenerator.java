import java.io.IOException;
import java.util.List;

public class GraphGenerator {
    /**
     * Defines what access level should be allowed in the graph.
     */
    public enum AccessLevel {
        PUBLIC, PRIVATE, PROTECTED
    }

    private boolean recursive;
    private AccessLevel access;

    /**
     * Create a new GraphGenerator with the given specifications on its
     * operation.
     *
     * @param recursive True if the generator should automatically read all of
     * the given classes super classes, false if it should only generate using
     * the passed classes.
     * @param access The lowest level of access at which to pull data from.
     */
    public GraphGenerator(boolean recursive, AccessLevel access) {
        this.recursive = recursive;
        this.access = access;
    }

    /**
     * Use the given list of /fully featured/ class names to generate and return
     * a full graph of classes and relations. Since the class names are fully
     * featured, in order to pass List into this method, you would need the
     * string "java.util.List".
     *
     * @param The list of /fully featured/ class names to generate a graph from.
     * @return An appropriate graph for the parameters of this GraphGenerator
     * based off of the classes passed by classNames.
     */
    public Graph execute(List<String> classNames) {
        Graph graph = new Graph();

        for (String className : classNames) {
            try {
                graph.addClass(new ClassCell(className));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // TODO Add edges to the Graph.

        return graph;
    }
}

package patterns;

import graph.ClassCell;
import graph.Edge;
import graph.Graph;
import org.objectweb.asm.Opcodes;

/**
 * Created by lewis on 1/22/17.
 */
public class InheritancePattern extends Pattern {
    @Override
    public Graph detect(Graph graphToSearch) {
        Graph g = new Graph();
        ClassCell superCell;
        for (ClassCell cell : graphToSearch.getCells()) {
            if (cell.getSuper() != null && (cell.getSuper().access & Opcodes.ACC_ABSTRACT) == 0
                    && (cell.getSuper().access & Opcodes.ACC_INTERFACE) == 0
                    && (superCell = graphToSearch.containsNode(cell.getSuper())) != null) {
                g.addClass(cell);
                g.addClass(superCell);
                for (Edge edge : cell.getEdges()) {
                    System.out.println("Checking " + edge);
                    if (edge.getDestination().equals(superCell)) {
                        System.out.println(edge);
                        g.addEdge(edge);
                        break;
                    }
                }
            }
        }
        return g;
    }
}

package patterns;

import graph.ClassCell;
import graph.Edge;
import graph.Graph;
import org.objectweb.asm.Opcodes;

/**
 * Created by lewis on 2/3/17.
 */
public class ViolateDepInvPatternDecorator extends PatternDecorator {

    @Override
    public Graph detect(Graph graphToSearch) {
        Graph g = new Graph();
        for (ClassCell cell : graphToSearch.getCells()) {
            for (Edge edge : cell.getEdges()) {
                if (edge.getRelation() != Edge.Relation.DEPENDS) {
                    continue;
                }

                if ((edge.getDestination().getAccess() & (Opcodes.ACC_ABSTRACT | Opcodes.ACC_INTERFACE)) == 0) {
                    g.addClass(cell);
                    g.addEdge(edge);
                }
            }
        }

        return g;
    }
}

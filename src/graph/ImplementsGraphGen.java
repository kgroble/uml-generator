package graph;
import graph.Edge.Relation;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import org.objectweb.asm.tree.ClassNode;

public class ImplementsGraphGen extends GraphGenDecorator {
    public ImplementsGraphGen(GraphGenerator graphGen) {
        super(graphGen);
    }

    @Override
    protected boolean genObjects(List<String> classNames, Graph graph) {
        boolean changed = false;

        Queue<ClassCell> classesToImplement = new LinkedList<>();
        classesToImplement.addAll(graph.getCells());
        Set<String> addedClasses = new HashSet<>();
        addedClasses.addAll(graph.getCellNames());

        ClassCell currentClass;

        while (!classesToImplement.isEmpty()) {
            currentClass = classesToImplement.remove();
            try {
                for (ClassNode implementedNode : currentClass.getImplements()) {
                    ClassCell implementedCell = new ClassCell(implementedNode.name, this.access);
                    if  (graph.containsNode(implementedNode) == null) {
                        graph.addClass(implementedCell);
                        classesToImplement.add(implementedCell);
                        changed = true;
                    }

                    graph.addEdge(new Edge(currentClass, implementedCell, Edge.Relation.IMPLEMENTS));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return changed;
    }
}

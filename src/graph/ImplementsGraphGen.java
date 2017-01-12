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
    public boolean addClassCells(List<String> classNames, Graph graph) {
        boolean retBool = this.graphGen.addClassCells(classNames, graph);

        if (!this.recursive) {
            return false;
        }

        boolean changed;

        do {
            changed = false;

            Queue<ClassCell> classesToImplement = new LinkedList<>();
            classesToImplement.addAll(graph.getCells());
            Set<String> addedClasses = new HashSet<>();
            addedClasses.addAll(graph.getCellNames());

            ClassCell currentClass;

            while (!classesToImplement.isEmpty()) {
                currentClass = classesToImplement.remove();
                try {
                    for (ClassNode implementedNode : currentClass.getImplements()) {
                        if  (graph.containsNode(implementedNode) == null) {
                            ClassCell implementedCell = new ClassCell(implementedNode.name, this.access);
                            graph.addClass(implementedCell);
                            classesToImplement.add(implementedCell);
                            changed = true;
                            retBool = true;
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } while (changed && this.graphGen.addClassCells(new LinkedList<>(), graph));

        return retBool;
    }

    @Override
    public void addEdges(Graph graph) {
        this.graphGen.addEdges(graph);

        for (ClassCell cell : graph.getCells()) {
            for (ClassNode destNode : cell.getImplements()) {
                ClassCell destCell = graph.containsNode(destNode);
                if (destCell != null) {
                    graph.addEdge(new Edge(cell, destCell, Edge.Relation.IMPLEMENTS));
                }
            }
            
        }

    }
}

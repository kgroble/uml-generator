package graph;

import java.util.List;
import java.util.Queue;
import java.util.LinkedList;
import java.util.Set;
import java.util.HashSet;
import java.io.IOException;

public class SuperGraphGen extends GraphGenDecorator {
    public SuperGraphGen(GraphGenerator graphGen) {
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

            Queue<ClassCell> classesToSuper = new LinkedList<>();
            classesToSuper.addAll(graph.getCells());
            Set<String> addedClasses = new HashSet<>();
            addedClasses.addAll(graph.getCellNames());

            ClassCell currentClass;

            while (!classesToSuper.isEmpty()) {
                currentClass = classesToSuper.remove();
                try {
                    if (currentClass.getSuper() != null
                        && graph.containsNode(currentClass.getSuper()) == null) {
                        currentClass = new ClassCell(currentClass.getSuper().name,
                                                     this.access);
                        graph.addClass(currentClass);
                        classesToSuper.add(currentClass);
                        changed = true;
                        retBool = true;
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

        ClassCell dest;
        for (ClassCell cell : graph.getCells()) {
            dest = graph.containsNode(cell.getSuper());
            if (dest != null) {
                graph.addEdge(new Edge(cell, dest, Edge.Relation.EXTENDS));
            }
        }

    }
}

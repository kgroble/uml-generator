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
    public void addClassCells(List<String> classNames, Graph graph) {
        this.graphGen.addClassCells(classNames, graph);

        if (!this.recursive) {
            return;
        }

        Queue<ClassCell> classesToSuper = new LinkedList<>();
        classesToSuper.addAll(graph.getCells());
        Set<String> addedClasses = new HashSet<>();
        addedClasses.addAll(classNames);

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
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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

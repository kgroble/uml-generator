package graph;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import org.objectweb.asm.tree.ClassNode;

/**
 * Created by lewis on 1/11/17.
 */
public class DependencyGraphGen extends GraphGenDecorator {
    private Graph edgesToAdd;
    private Graph lastCalled;
    
    public DependencyGraphGen(GraphGenerator graphGen) {
        super(graphGen);
    }

    @Override
    public boolean addClassCells(List<String> classNames, Graph graph) {
        edgesToAdd = new Graph();
        lastCalled = graph;

        boolean retBool = this.graphGen.addClassCells(classNames, graph);
        boolean changed;

        do {
            changed = false;

            Queue<ClassCell> classesToCheck = new LinkedList<>();
            classesToCheck.addAll(graph.getCells());
            Set<String> addedClasses = new HashSet<>();
            addedClasses.addAll(graph.getCellNames());

            ClassCell currentClass;
            while (!classesToCheck.isEmpty()) {
                currentClass = classesToCheck.remove();

                try {
                    Queue<FieldTuple> fieldsToCheck = new LinkedList<>();
                    Field field;
                    for (Field f : currentClass.getDependencies()) {
                        fieldsToCheck.add(new FieldTuple(f, Edge.Cardinality.ONE));
                    }
                    while(!fieldsToCheck.isEmpty()) {
                        FieldTuple fieldTuple = fieldsToCheck.remove();
                        field = fieldTuple.field;
                        ClassNode type = field.getType();

                        if (type != null) {
                            ClassCell referencedCell = new ClassCell(type.name, this.access);

                            if (recursive && graph.containsNode(type) == null) {
                                graph.addClass(referencedCell);
                                System.out.println("adding dependency class: " + referencedCell.getName());
                                classesToCheck.add(referencedCell);
                                changed = true;
                                retBool = true;
                            }

                            if (!type.name.equals(currentClass.getName())) {
                                if (graph.containsNode(type) != null
                                        && !edgesToAdd.containsEdge(currentClass, referencedCell, Edge.Relation.DEPENDS, fieldTuple.cardinality)) {
                                    edgesToAdd.addEdge(new Edge(currentClass, referencedCell, Edge.Relation.DEPENDS, fieldTuple.cardinality));                                        
                                }
                                if (Collection.class.isAssignableFrom(Class.forName(type.name.replace("/", ".")))) {
                                    fieldTuple.cardinality = Edge.Cardinality.MANY;
                                }
                            }
                        }

                        if (field.getTemplate() != null) {
                            for (Field f : field.getTemplate()) {
                                fieldsToCheck.add(new FieldTuple(f, fieldTuple.cardinality));
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        } while (changed && this.graphGen.addClassCells(new LinkedList<>(), graph));

        return retBool;
    }

    @Override
    public void addEdges(Graph graph) {
        this.graphGen.addEdges(graph);

        if (graph == lastCalled) {
            for (Edge edge : edgesToAdd.getEdges()) {
                graph.addEdge(edge);
            }
        }
    }
    
    private class FieldTuple {
        public Field field;
        public Edge.Cardinality cardinality;
        
        public FieldTuple(Field field, Edge.Cardinality cardinality) {
            this.field = field;
            this.cardinality = cardinality;
        }
    }
}

package graph;
import org.objectweb.asm.tree.ClassNode;


import java.io.IOException;
import java.util.*;


public class AssociationGraphGen extends GraphGenDecorator {
    private Graph edgesToAdd;
    private Graph lastCalled;

    public AssociationGraphGen(GraphGenerator graphGen) {
        super(graphGen);
        edgesToAdd = new Graph();
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
                    for (Field f : currentClass.getFields()) {
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
                                classesToCheck.add(referencedCell);
                                changed = true;
                                retBool = true;
                            }

                            if (!type.name.equals(currentClass.getName())) {
                                if (graph.containsNode(type) != null
                                        && !edgesToAdd.containsEdge(currentClass, referencedCell, Edge.Relation.ASSOCIATION, fieldTuple.cardinality)) {
                                    edgesToAdd.addEdge(new Edge(currentClass, referencedCell, Edge.Relation.ASSOCIATION, fieldTuple.cardinality));                                        
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
}

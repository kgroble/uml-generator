import org.objectweb.asm.tree.ClassNode;

import java.io.IOException;
import java.util.*;


public class ContainsGraphGen extends GraphGenDecorator {
    private List<Edge> edgesToAdd;
    private Graph lastCalled;

    public ContainsGraphGen(GraphGenerator graphGen) {
        super(graphGen);
        edgesToAdd = new ArrayList<>();
    }

    @Override
    public boolean addClassCells(List<String> classNames, Graph graph) {
        edgesToAdd = new ArrayList<>();
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
                    Queue<Field> fieldsToCheck = new LinkedList<>();
                    Field field;
                    fieldsToCheck.addAll(currentClass.getFields());
                    while(!fieldsToCheck.isEmpty()) {
                        field = fieldsToCheck.remove();
                        ClassNode type = field.getType();

                        if (type != null) {
                            ClassCell referencedCell = new ClassCell(type.name, this.access);

                            if (recursive && graph.containsNode(type) == null) {
                                graph.addClass(referencedCell);
                                classesToCheck.add(referencedCell);
                                changed = true;
                                retBool = true;
                            }

                            if (graph.containsNode(type) != null) {
                                edgesToAdd.add(new Edge(currentClass, referencedCell, Edge.Relation.CONTAINS, Edge.Cardinality.ONE));
                            }
                        }

                        if (field.getTemplate() != null) {
                            fieldsToCheck.addAll(field.getTemplate());
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

        if (graph == lastCalled) {
            for (Edge edge : edgesToAdd) {
                graph.addEdge(edge);
            }
        }
    }
}

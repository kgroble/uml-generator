package patterns;

import java.io.IOException;
import java.util.List;

import org.objectweb.asm.tree.MethodNode;

import graph.AccessLevel;
import graph.ClassCell;
import graph.Edge;
import graph.Field;
import graph.Graph;
import graphviz.GraphvizEdge;
import graphviz.GraphvizElement;
import graphviz.GraphvizNode;

public class AdapterPattern extends Pattern {
    
    @Override
    public List<GraphvizElement> toGraphviz(Graph detected) {
        String adapteeName = "", targetName = "", adapterName = "";
        for (ClassCell c : detected.getCells()) {
            if (c instanceof AdapteeCell) {
                adapteeName = c.getName();
            } else if (c instanceof TargetCell) {
                targetName = c.getName();
            } else if (c instanceof AdapterCell) {
                adapterName = c.getName();
            }
        }
        
        String edgeNames = adapterName.compareTo(adapteeName) < 0 ? adapterName + "-" + adapteeName : adapteeName + "-" + adapterName;
        String adaptsName = edgeNames + "<" + Edge.Relation.ASSOCIATION + ">";

        List<GraphvizElement> gvElements = super.toGraphviz(detected);
        for (GraphvizElement gvE : gvElements) {
            if (gvE instanceof GraphvizNode) {
                gvE.addAttribute("style", "\"filled\"");
                gvE.addAttribute("fillcolor", "\"red\"");
                if (gvE.getIdentifier().equals(adapteeName)) {
                    String label = gvE.getAttribute("label");
                    label = "<{" + "&lt;&lt;Adaptee&gt;&gt;<br align=\"center\"/>" + label.substring(2);
                    gvE.addAttribute("label", label);
                } else if (gvE.getIdentifier().equals(adapterName)) {
                    String label = gvE.getAttribute("label");
                    label = "<{" + "&lt;&lt;Adapter&gt;&gt;<br align=\"center\"/>" + label.substring(2);
                    gvE.addAttribute("label", label);
                } else if (gvE.getIdentifier().equals(targetName)) {
                    String label = gvE.getAttribute("label");
                    label = "<{" + "&lt;&lt;Target&gt;&gt;<br align=\"center\"/>" + label.substring(2);
                    gvE.addAttribute("label", label);
                }
            } else if (gvE instanceof GraphvizEdge
                        && gvE.getIdentifier().equals(adaptsName)) {
                gvE.addAttribute("label", "\"&lt;&lt;adapts&gt;&gt;\"");
            }
        }
        return gvElements;
    }
    
    @Override
    public Graph detect(Graph graphToSearch) {
        Graph detected = new Graph();
        
        for (ClassCell potentialAdapter : graphToSearch.getCells()) {
            for (Edge e : potentialAdapter.getEdges()) {
                ClassCell target;
                if (e.getRelation() == Edge.Relation.EXTENDS
                        || e.getRelation() == Edge.Relation.IMPLEMENTS) {
                    Edge targetEdge = e;
                    target = e.getDestination();
                    boolean overrides = true;
                    for (MethodNode n : target.getMethods(AccessLevel.PUBLIC)) {
                        boolean found = false;
                        for (MethodNode n2 : potentialAdapter.getMethods(AccessLevel.PUBLIC)) {
                            if (n2.name.equals(n.name)) {
                                found = true;
                                break;
                            }
                        }
                        if (!found) {
                            overrides = false;
                            break;
                        }
                    }
                    
                    if (!overrides) {
                        continue;
                    }
                    
                    ClassCell adaptee;
                    for (Edge e2: potentialAdapter.getEdges()) {
                        if (e2.getRelation() == Edge.Relation.ASSOCIATION) {
                            Edge adaptsEdge = e2;
                            adaptee = e2.getDestination();
                            if (adaptee.equals(target)) {
                                continue;
                            }
                            boolean constructorFound = false;
                            for (MethodNode n : potentialAdapter.getMethods(AccessLevel.PUBLIC)) {
                                if (n.name.equals("<init>")) {
                                    String sig = n.desc;
                                    int startArgs = sig.indexOf('(');
                                    int endArgs = sig.indexOf(')');
                                    
                                    if (endArgs - startArgs > 1) {
                                        String args = sig.substring(startArgs + 1, endArgs);
                                        Field field = new Field(args);
                                        if (field.getType().name.equals(adaptee.getName())) {
                                            constructorFound = true;
                                            break;
                                        }
                                    }
                                }
                            }
                            if (constructorFound) {
                                try {
                                    TargetCell targetCell = new TargetCell(target.getName(), target.getRenderAccess());
                                    AdapterCell adapterCell = new AdapterCell(potentialAdapter.getName(), potentialAdapter.getRenderAccess());
                                    AdapteeCell adapteeCell = new AdapteeCell(adaptee.getName(), adaptee.getRenderAccess());
                                    detected.addClass(targetCell);
                                    detected.addClass(adapterCell);
                                    detected.addClass(adapteeCell);
                                    detected.addEdge(adaptsEdge);
                                    detected.addEdge(targetEdge);
                                } catch (IOException e1) {
                                    e1.printStackTrace();
                                }
                                
                            }
                        }
                    }
                }
            }
        }
        
        return detected;
    }

    
    private class TargetCell extends ClassCell {
        public TargetCell(String name, AccessLevel renderAccess) throws IOException {
            super(name, renderAccess);
        }}
    
    private class AdapterCell extends ClassCell {
        public AdapterCell(String name, AccessLevel renderAccess) throws IOException {
            super(name, renderAccess);
        }}
    
    private class AdapteeCell extends ClassCell {
        public AdapteeCell(String name, AccessLevel renderAccess) throws IOException {
            super(name, renderAccess);
        }}
}

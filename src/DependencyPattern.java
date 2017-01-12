import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DependencyPattern extends Pattern {

    /**
     * This is used to detect ONLY Edge.Relation.CONTAINS relationships.
     * As such, the toGraphviz() method only handles these.
     */
    @Override
    public Graph detect(Graph graphToSearch) {
        Graph result = new Graph();
        
        for(Edge edge : graphToSearch.getEdges()){
            if(edge.getRelation() == Edge.Relation.DEPENDS
               && !graphToSearch.containsEdge(edge.getOrigin(), edge.getDestination(), Edge.Relation.ASSOCIATION)){
                result.addEdge(edge);
            }
        }
        
        return result;
    }
    
    @Override
    public List<GraphvizElement> toGraphviz(Graph detected){
        List<GraphvizElement> elements = new ArrayList<>();
        Map<Edge, GraphvizEdge> edgeToGVEdge = new HashMap<>();
        
        // Assume this only deals with edges
        for(Edge edge : detected.getEdges()) {
            String from = edge.getOrigin().getPrettyName();
            String to = edge.getDestination().getPrettyName();
            boolean wasDuplicate = false;
            
            for(Edge otherEdge : edgeToGVEdge.keySet()){
                if(from.equals(otherEdge.getDestination().getPrettyName())
                        && to.equals(otherEdge.getOrigin().getPrettyName())
                        && edge.getRelation() == Edge.Relation.DEPENDS
                        && otherEdge.getRelation() == Edge.Relation.DEPENDS){
                    edgeToGVEdge.get(otherEdge).addAttribute("dir", "\"both\"");
                    edgeToGVEdge.get(otherEdge).addAttribute("taillabel", "\"" + edge.getCardinality().toString() + "\"");
                    wasDuplicate = true;
                    break;
                }
            }

            if(!wasDuplicate){
                GraphvizEdge gvEdge = new GraphvizEdge(from, to, edge.getRelation().toString());
                gvEdge.addAttribute("headlabel", "\"" + edge.getCardinality().toString() + "\"");
                gvEdge.addAttribute("labeldistance", "1.7");
                gvEdge.addAttribute("style", "\"dashed\"");
                elements.add(gvEdge);
                edgeToGVEdge.put(edge, gvEdge); 
            }
            
        }

        return elements;
    }

}

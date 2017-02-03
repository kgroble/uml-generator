package patterns;

import graph.Graph;
import graphviz.GraphvizEdge;
import graphviz.GraphvizElement;

import java.util.List;

/**
 * Created by lewis on 2/3/17.
 */
public class PurplePatternDecorator extends PatternDecorator {
    @Override
    public List<GraphvizElement> toGraphviz(Graph detected) {
        List<GraphvizElement> elements = super.toGraphviz(detected);
        for (GraphvizElement element : elements) {
            if (element instanceof GraphvizEdge) {
            }
            element.addAttribute("color", "\"purple\"");
        }

        return elements;
    }
}

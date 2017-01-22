package patterns;

import graph.Graph;
import graphviz.GraphvizElement;

import java.util.List;

/**
 * Created by lewis on 1/22/17.
 */
public class OrangePatternDecorator extends PatternDecorator {

    public OrangePatternDecorator() {
        super();
    }

    public OrangePatternDecorator(Pattern pattern) {
        super(pattern);
    }

    @Override
    public List<GraphvizElement> toGraphviz(Graph detected) {
        List<GraphvizElement> elements = super.toGraphviz(detected);
        for (GraphvizElement element : elements) {
            element.addAttribute("color", "\"orange\"");
        }

        return elements;
    }
}

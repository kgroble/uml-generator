import java.util.List;
import java.util.ArrayList;

public abstract class Pattern {

    public List<GraphvizElement> toGraphviz(Graph detected) {
        List<GraphvizElement> elements = new ArrayList<>();

        for (ClassCell cell : detected.getCells()) {
            GraphvizNode node = new GraphvizNode(cell.getPrettyName());
            node.addAttribute("shape", "\"record\"");
            node.addAttribute("label", "\"{WeatherData|//fields|//methods}\"");
            elements.add(node);
        }

        return elements;
    }

    public abstract Graph detect(Graph graphToSearch);
}

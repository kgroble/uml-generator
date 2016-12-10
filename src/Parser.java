import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Parser {
    private Map<Integer, Pattern> patterns;

    public Parser() {
        this.patterns = new HashMap<>();
    }

    public List<GraphvizElement> parseGraph(Graph graph) {
        List<GraphvizElement> parsedElements = new ArrayList<GraphvizElement>();
        List<Integer> priorities = new ArrayList<>(this.patterns.keySet());
        Collections.sort(priorities);
        for(int i = priorities.size() - 1; i >= 0; i--) {
            Pattern pattern = this.patterns.get(priorities.get(i));
            Graph detectedPattern = pattern.detect(graph);
            for(GraphvizElement element : pattern.toGraphviz(detectedPattern)) {
                GraphvizElement.addOrReplaceElement(parsedElements, element);
            }
        }
        return parsedElements;
    }

    public boolean addPattern(Pattern pattern, Integer priority) {
        if (this.patterns.containsKey(priority) || this.patterns.containsValue(pattern)) {
            return false;
        }

        patterns.put(priority, pattern);
        return true;
    }

    public boolean removePattern(Pattern pattern) {
        for(Integer priority : this.patterns.keySet()) {
            if (this.patterns.get(priority).equals(pattern)) {
                return this.patterns.remove(priority, pattern);
            }
        }
        
        return false;
    }
}

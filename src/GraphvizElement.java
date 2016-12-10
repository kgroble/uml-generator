import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GraphvizElement {
    protected Map<String, String> attributes;

    public GraphvizElement() {
        this.attributes = new HashMap<>();
    }

    public void addAttribute(String var, String val) {
        this.attributes.put(var, val);
    }

    public void removeAttribute(String var) {
        this.attributes.remove(var);
    }

    public String getAttribute(String var) {
        return this.attributes.get(var);
    }

    public abstract String toGraphviz();
    public abstract String getIdentifier();
    
    public static void addOrReplaceElement(List<GraphvizElement> list, GraphvizElement element) {
        for(GraphvizElement existingElement : list) {
            if (existingElement.getIdentifier().equals(element.getIdentifier())) {
                list.remove(existingElement);
                break;
            }
        }
        list.add(element);
    }
    
    protected static String sanitizeGraphvizString(String unsanitized) {
        String sanitized = unsanitized.replace("<", "\\<");
        sanitized = sanitized.replace(">", "\\>");
        return sanitized;
    }
}

import java.util.HashMap;
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
}

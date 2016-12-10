import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Parser {
	private Map<Pattern, Integer> patterns;
	
	public Parser() {
		this.patterns = new HashMap<>();
	}
	
	public List<GraphvizElement> parseGraph(Graph graph) {
		return null;
	}
	
	public boolean addPattern(Pattern pattern, Integer priority) {
		if(this.patterns.containsKey(pattern) || this.patterns.containsValue(priority)) {
			return false;
		}
		
		patterns.put(pattern, priority);
		return true;
	}
	
	public boolean removePattern(Pattern pattern) {
		return this.patterns.remove(pattern) != null;
	}
}

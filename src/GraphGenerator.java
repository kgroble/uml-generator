import java.io.IOException;
import java.util.List;

public class GraphGenerator {
  public enum AccessLevel {
    PUBLIC,
    PRIVATE,
    PROTECTED
  }
  
	private boolean recursive;
  private AccessLevel access;
	
	public GraphGenerator(boolean recursive, AccessLevel access) {
		this.recursive = recursive;
    this.access = access;
	}
	
	public Graph execute(List<String> classNames) {
    Graph graph = new Graph();
    
    for (String className : classNames) {
        try {
            graph.addClass(new ClassCell(className));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
		return graph;
	}
}

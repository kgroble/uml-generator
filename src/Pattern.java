import java.util.List;
import java.util.ArrayList;

public abstract class Pattern {
	private Graph detected;
	
	public Pattern(Graph graphToSearch) {
		this.detected = this.detect(graphToSearch);
	}
	
	public List<GraphvizElement> toGraphviz() {
    List<GraphvizElement> elements = new ArrayList<>();
    
    for (ClassCell cell : this.detected.getCells()) {
      elements.add(new GraphvizNode(cell.getPrettyName()));
    }

    return elements;
  }
	
	public Graph getDetectedGraph() {
		return this.detected.copy();
	}
	
	public abstract Graph detect(Graph graphToSearch);
}

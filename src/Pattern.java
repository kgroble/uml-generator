import java.util.List;

public abstract class Pattern {
	private Graph detected;
	
	public Pattern(Graph graphToSearch) {
		this.detected = this.detect(graphToSearch);
	}
	
	public abstract List<GraphvizElement> toGraphviz();
	
	public Graph getDetectedGraph() {
		return this.detected;
	}
	
	public abstract Graph detect(Graph graphToSearch);
}

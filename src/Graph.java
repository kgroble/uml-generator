import java.util.ArrayList;
import java.util.List;

public class Graph {
	private List<ClassCell> cells;
	private List<Edge> edges;
	
	public Graph() {
		this.cells = new ArrayList<>();
		this.edges = new ArrayList<>();
	}
}

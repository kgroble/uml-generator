import java.util.List;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

public class ClassCell {
	private ClassNode classNode;
	private List<Edge> edges;
	
	public String getName() {
		return null;
	}
	
	public int getAccess() {
		return 0;
	}
	
	public List<FieldNode> getFields() {
		return null;
	}
	
	public List<MethodNode> getMethods() {
		return null;
	}
	
	public List<ClassNode> getImplements() {
		return null;
	}
	
	public ClassNode getSuper() {
		return null;
	}
}

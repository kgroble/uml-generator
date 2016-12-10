import java.io.IOException;
import java.util.List;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

public class ClassCell {
	private ClassNode classNode;
	private List<Edge> edges;

    public ClassCell(String name) throws IOException {
        ClassReader reader = new ClassReader(name);
        classNode = new ClassNode();
        reader.accept(classNode, ClassReader.EXPAND_FRAMES);
    }
  
	public String getName() {
		return classNode.name;
	}

	public String getPrettyName() {
        String[] splitted = getName().split("/");
        return splitted[splitted.length - 1];
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

  @Override
  public boolean equals(Object other) {
    return other instanceof ClassCell &&
      ((ClassCell) other).getName().equals(this.getName());
  }
}

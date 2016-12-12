import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

public class ClassCell {
    private ClassNode classNode;
    private List<Edge> edges;

    /**
     * Initializes a new ClassCell for the given class.
     *
     * @param name The /fully featured/ name of the class to be input. For
     * example, to pass List you would need the string "java.util.List".
     */
    public ClassCell(String name) throws IOException {
        ClassReader reader = new ClassReader(name);
        classNode = new ClassNode();
        reader.accept(classNode, ClassReader.EXPAND_FRAMES);
        this.edges = new ArrayList<>();
    }

    /**
     * Returns the /fully featured/ name of the class stored by this cell.
     *
     * @return The fully featured name of class stored in this cell. For
     * example, for List this will return the string "java/util/List".
     */
    public String getName() {
        return classNode.name;
    }

    /**
     * Returns the user-friendly name of the class stored by this cell.
     *
     * @return The user-friendly name of the class. For example, List will
     * return the string "List".
     */
    public String getPrettyName() {
        String[] splitted = getName().split("/");
        return splitted[splitted.length - 1];
    }

    /**
     * Returns an integer indicating the access level of the class. This integer
     * should be used with the org.asm access mask.
     *
     * @return A masked integer representing the access level of the stored
     * class.
     */
    public int getAccess() {
        // TODO Implement method
        return 0;
    }

    /**
     * Returns a list of all the fields in the stored class.
     *
     * @return A list of all the fields in the stored class.
     */
    public List<FieldNode> getFields() {
        return classNode.fields;
    }

    /**
     * Returns a list of all the methods in the stored class.
     *
     * @return A list of all the methods in the stored class.
     */
    public List<MethodNode> getMethods() {
        return classNode.methods;
    }

    /**
     * Returns a list of all the interfaces the stored class implements. Note
     * that the interfaces are stored as ClassNodes just like regular classes.
     *
     * @return A list of all the interfaces the stored class implements.
     */
    public List<ClassNode> getImplements() {
        List<ClassNode> implementedInterfaces = new ArrayList<>();
        ClassReader reader;
        ClassNode interfaceNode;
        
        for(Object interfaceName : this.classNode.interfaces) {
            try {
                reader = new ClassReader((String) interfaceName);
                interfaceNode = new ClassNode();
                reader.accept(interfaceNode, ClassReader.EXPAND_FRAMES);
                implementedInterfaces.add(interfaceNode);
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }
        }

        return implementedInterfaces;
    }

    /**
     * Returns the class that the stored class extends.
     *
     * @return The class that the stored class extends or
     *         null if ClassReader creation failed.
     */
    public ClassNode getSuper() {
        ClassReader reader;
        ClassNode superNode = new ClassNode();
        
        try {
            reader = new ClassReader(this.classNode.superName);
            
            reader.accept(superNode, ClassReader.EXPAND_FRAMES);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return superNode;
    }

    /**
     * Tests if this ClassCell is equal to another, specifically by comparing
     * the fully featured names of both for equality.
     *
     * @return True if this object stores the same class as the passed object,
     * false otherwise.
     */
    @Override
    public boolean equals(Object other) {
        return other instanceof ClassCell && ((ClassCell) other).getName().equals(this.getName());
    }
    
    public boolean hasNode(ClassNode otherNode) {
        return this.classNode.name.equals(otherNode.name);
    }

    public void addEdge(Edge e) {
        this.edges.add(e);
    }
}

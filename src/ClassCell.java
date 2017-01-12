import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;

public class ClassCell {
    private ClassNode classNode;
    private List<Edge> edges;
    private AccessLevel renderAccess;

    /**
     * Initializes a new ClassCell for the given class.
     *
     * @param name The /fully featured/ name of the class to be input. For
     * example, to pass List you would need the string "java.util.List".
     */
    public ClassCell(String name, AccessLevel renderAccess) throws IOException {
        ClassReader reader = new ClassReader(name);
        classNode = new ClassNode();
        reader.accept(classNode, ClassReader.EXPAND_FRAMES);
        this.edges = new ArrayList<>();
        this.renderAccess = renderAccess;
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
        // qualified or not?
//        String[] splitted = getName().split("/");
//        return splitted[splitted.length - 1];
        return Type.getObjectType(this.classNode.name).getClassName();
    }

    /**
     * Returns an integer indicating the access level of the class. This integer
     * should be used with the org.asm access mask.
     *
     * @return A masked integer representing the access level of the stored
     * class.
     */
    public int getAccess() {
        return this.classNode.access;
    }

    /**
     * Returns a list of all the fields in the stored class that are at or
     * above the render access.
     *
     * @return A list of all the fields in the stored class.
     */
    public List<FieldNode> getFieldNodes() {
        List<FieldNode> retFields = new ArrayList<>();

        for (Object field : classNode.fields) {
            if (AccessLevel.hasAccess(((FieldNode) field).access, this.renderAccess)) {
                retFields.add((FieldNode) field);
            }
        }

        return retFields;
    }

    public List<Field> getFields() {
        List<Field> retFields = new ArrayList();

        for (FieldNode fieldNode : getFieldNodes()) {
            if (fieldNode.signature != null) {
                retFields.add(new Field(fieldNode.signature));
            } else {
                retFields.add(new Field(fieldNode.desc));
            }
        }

        return retFields;
    }

    /**
     * Returns a list of all the methods in the stored class that are at or
     * above the render access.
     *
     * @return A list of all the methods in the stored class.
     */
    public List<MethodNode> getMethods() {
        List<MethodNode> retMethods = new ArrayList<>();

        for (Object method : classNode.methods) {
            if (AccessLevel.hasAccess(((MethodNode) method).access, this.renderAccess)) {
                retMethods.add((MethodNode) method);
            }
        }

        return retMethods;
    }

    private List<Field> getInnerDependencies(MethodNode methodNode) {
        List<Field> dependencies = new ArrayList<>();
        Set<String> included = new HashSet<>();

        if (methodNode.localVariables != null) {
            for (LocalVariableNode localVar : methodNode.localVariables) {
//                System.out.print(classNode.name + ":" + methodNode.name + " uses " + localVar.name + ": ");
                // TODO Check if primitives will break this.
                dependencies.add(new Field(localVar.signature != null ? localVar.signature : localVar.desc));
            }
        }

        if (methodNode.instructions != null) {
            for (int i = 0; i < methodNode.instructions.size(); i++) {
                AbstractInsnNode absInsn = methodNode.instructions.get(i);
                switch (absInsn.getType()) {
                    case AbstractInsnNode.METHOD_INSN:
                        MethodInsnNode insn = (MethodInsnNode) absInsn;
                        if (insn.name.equals("<init>") && !included.contains(insn.owner)) {
//                            System.out.println(insn.owner + "." + insn.name + ": " + insn.desc);
                            included.add(insn.owner);
                            dependencies.add(new Field("L" + insn.owner + ";"));
                        }
                        break;
                    default:
                        break;
                }
            }
        }

        return dependencies;
    }

    public List<Field> getDependencies() {
        List<Field> dependencies = new ArrayList<>();
        Set<String> included = new HashSet<>();

        for (MethodNode methodNode : getMethods()) {
            dependencies.addAll(getInnerDependencies(methodNode));
        }

        return dependencies;
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

        if(this.classNode.superName == null
                || (this.classNode.access & Opcodes.ACC_INTERFACE) != 0){
            return null;
        }

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
        return other instanceof ClassCell
            && ((ClassCell) other).getName().equals(this.getName());
    }

    /**
     * Checks if the ClassNode stored in this cell is the same as the otherNode
     * passed in.
     *
     * @param otherNode The node to compare this cell to
     * @return True if otherNode represents the same class as this cell, false
     * otherwise
     */
    public boolean hasNode(ClassNode otherNode) {
        return otherNode != null && this.classNode.name.equals(otherNode.name);
    }

    /**
     * Adds an Edge to this cell.
     *
     * @param e The Edge to add to this cell
     */
    public void addEdge(Edge e) {
        this.edges.add(e);
    }

    /**
     * Creates and returns a new list of Strings representing all the classes
     * this cell relates to in any way. The returned names are the fully
     * featured class names.
     *
     * @return A new list of all the fully featured class names of all the
     * classes this cell relates to in any way.
     */
    public List<String> getAllRelatives() {
        List<String> result = new ArrayList<String>();

        if(this.classNode.superName != null
                && (this.classNode.access & Opcodes.ACC_INTERFACE) == 0){
            result.add(this.classNode.superName);
        }


        List<String> interfaces = this.classNode.interfaces;
        result.addAll(interfaces);

        // Inner classes?

        return result;
    }
}

package patterns;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import graph.AccessLevel;
import graph.ClassCell;
import graph.Field;
import graph.Graph;

public class SingletonPattern extends Pattern{

    @Override
    public Graph detect(Graph graphToSearch) {
        Graph g = new Graph();
        
        boolean hasPublicConstructor;
        boolean hasSelfInstance;
        ClassNode node;
        for (ClassCell cell : graphToSearch.getCells()) {
            node = cell.getClassNode();
            if ((node.access & Opcodes.ACC_ABSTRACT) == 0
                    && (node.access & Opcodes.ACC_INTERFACE) == 0
                    && (node.access & Opcodes.ACC_ENUM) == 0) {
                hasPublicConstructor = false;
                for (MethodNode method : cell.getMethods(AccessLevel.PRIVATE)) {
                    if (method.name.equals("<init>")
                            && AccessLevel.hasAccess(method.access, AccessLevel.PUBLIC)) {
                        hasPublicConstructor = true;
                        break;
                    }
                }
                
                hasSelfInstance = false;
                for (Field field : cell.getFields(AccessLevel.PRIVATE)) {
                    if (field.getType() != null 
                            && field.getType().name.equals(cell.getName())) {
                        hasSelfInstance = true;
                    }
                }
                
                if (!hasPublicConstructor && hasSelfInstance) {
                    g.addClass(cell);
                }
            }
                
        }
        return g;
    }

}

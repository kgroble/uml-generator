package patterns;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import graph.ClassCell;
import graph.Graph;

public class SingletonPattern extends Pattern{

    @Override
    public Graph detect(Graph graphToSearch) {
        Graph g = new Graph();
        
        boolean singleton;
        ClassNode node;
        for (ClassCell cell : graphToSearch.getCells()) {
            node = cell.getClassNode();
            if ((node.access & Opcodes.ACC_ABSTRACT) == 0
                    && (node.access & Opcodes.ACC_INTERFACE) == 0
                    && (node.access & Opcodes.ACC_ENUM) == 0) {
                singleton = true;
                for (MethodNode method : cell.getMethods()) {
                    if (method.name.equals("<init>") 
                            && (method.access & Opcodes.ACC_PUBLIC) != 0) {
                        singleton = false;
                        break;
                    }
                }
                
                if (singleton) {
                    g.addClass(cell);
                }
            }
                
        }
        return g;
    }

}

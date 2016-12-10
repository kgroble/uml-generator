import java.util.List;
import java.util.ArrayList;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

public abstract class Pattern {

    public List<GraphvizElement> toGraphviz(Graph detected) {
        List<GraphvizElement> elements = new ArrayList<>();

        for (ClassCell cell : detected.getCells()) {
            GraphvizNode node = new GraphvizNode(cell.getPrettyName());
            node.addAttribute("shape", "\"record\"");
            
            String fields = "";
            List<FieldNode> fieldList = cell.getFields();
            
            for(FieldNode fieldNode : fieldList){
                fields += translateFieldNode(fieldNode);
            }
            
            String methods = "";
            List<MethodNode> methodList = cell.getMethods();
            
            for(MethodNode methodNode : methodList){
                methods += translateMethodNode(methodNode);
            }
            
            node.addAttribute("label", "\"{" + cell.getPrettyName() + "|" + fields + "|" + methods + "}\"");
            elements.add(node);
        }

        return elements;
    }

    public abstract Graph detect(Graph graphToSearch);
    
    private String translateFieldNode(FieldNode node){
        String result = "";
        
        // Modifiers such as static are currently ignored.
        
        result += getAccessChar(node.access) + " ";
        
        result += node.name + ": " + Type.getType(node.desc).getClassName() + "\\l";
        
        return result;
    }
    
    private String translateMethodNode(MethodNode node){
        String result = "";
        
        // Modifiers such as static are currently ignored.
        
        result += getAccessChar(node.access) + " ";
        
        String arguments = "(";
        for(Type argType : Type.getArgumentTypes(node.desc)){
            arguments += argType.getClassName() + " ";
            
        }
        arguments = arguments.trim();
        arguments += ")";
        
        
        result += node.name 
                + arguments
                + ": " 
                + Type.getReturnType(node.desc).getClassName() 
                + "\\l";
        
        return result;
    }
    
    private char getAccessChar(int access){
        if((access & Opcodes.ACC_PUBLIC) > 0){
            return '+';
        }else if((access & Opcodes.ACC_PRIVATE) > 0){
            return '-';
        }else if((access & Opcodes.ACC_PROTECTED) > 0){
            return '#';
        }
        return ' ';
        
    }
    
}

import java.util.List;
import java.util.ArrayList;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

public abstract class Pattern {

    /**
     * Creates a new GraphvizElement list that holds all the abstract
     * information necessary to draw the passed graph.
     *
     * @param detected The graph to generate GraphvizElement's for.
     * @return A new GraphvizElement list for detected.
     */
    public List<GraphvizElement> toGraphviz(Graph detected) {
        List<GraphvizElement> elements = new ArrayList<>();
        String cellName;

        //global params
        GraphvizGlobalParams params = new GraphvizGlobalParams();
        params.addAttribute("rankdir", "BT");
        elements.add(params);

        // nodes
        for (ClassCell cell : detected.getCells()) {
            cellName = cell.getPrettyName();
            GraphvizNode node = new GraphvizNode(cellName);
            node.addAttribute("shape", "\"record\"");

            String fields = "";
            List<FieldNode> fieldList = cell.getFields();

            for(FieldNode fieldNode : fieldList) {
                fields += translateFieldNode(fieldNode);
            }

            String methods = "";
            List<MethodNode> methodList = cell.getMethods();

            for(MethodNode methodNode : methodList){
                methods += translateMethodNode(methodNode);
            }

            if ((cell.getAccess() & Opcodes.ACC_ABSTRACT) != 0 || (cell.getAccess() & Opcodes.ACC_INTERFACE) != 0) {
                cellName = "<I>" + cellName + "</I>"; // Special '@' is used to distinguish from non-html
                // <>'s to
                // sanitize.
            }

            node.addAttribute("label", "<{" + cellName + "|" + fields + "|" + methods + "}>");
            elements.add(node);
        }

        //edges
        for(Edge edge : detected.getEdges()) {
            String from = edge.getOrigin().getPrettyName();
            String to = edge.getDestination().getPrettyName();

            GraphvizEdge gvEdge = new GraphvizEdge(from, to);
            switch (edge.getRelation()) {
            case IMPLEMENTS:
                gvEdge.addAttribute("arrowhead", "\"onormal\"");
                gvEdge.addAttribute("style", "\"dashed\"");
                break;
            case EXTENDS:
                gvEdge.addAttribute("arrowhead", "\"onormal\"");
                break;
            default:
                System.err.println("Unrecognized relation: " + edge.getRelation());
                break;
            }
            elements.add(gvEdge);
        }

        return elements;
    }

    /**
     * Creates a new Graph with all the nodes from graphToSearch that fit this
     * pattern. If there are multiple occurences of the pattern, it will return
     * all of them in one disconnected Graph.
     *
     * @param graphToSearch The Graph to search through for the pattern.
     * @return A new Graph containing all the detected nodes and edges.
     */
    public abstract Graph detect(Graph graphToSearch);

    /**
     * Translates a FieldNode into a Graphviz string.
     *
     * @param node The FieldNode to convert
     * @return A Graphviz String representing the passed FieldNode
     */
    private String translateFieldNode(FieldNode node){
        String result = "";

        // Modifiers such as static are currently ignored.

        result += getAccessChar(node.access) + " ";

        result += node.name + ": " + Type.getType(node.desc).getClassName() + "<br align=\"left\"/>";

        return result;
    }

    /**
     * Translates a MethodNode into a Graphviz string.
     *
     * @param node The MethodNode to convert
     * @return A Graphviz String representing the passed MethodNode
     */
    private String translateMethodNode(MethodNode node){
        String result = "";

        // Modifiers such as static are currently ignored.

        result += getAccessChar(node.access) + " ";

        String arguments = "(";
        List<String> argTypes = new ArrayList<String>();
        for(Type argType : Type.getArgumentTypes(node.desc)){
            argTypes.add(argType.getClassName());

        }
        arguments += String.join(", ", argTypes);
        arguments += ")";


        result += node.name
                + arguments
                + ": "
                + Type.getReturnType(node.desc).getClassName()
                + "<br align=\"left\"/>";

        return result;
    }

    /**
     * Looks up which access character should be used for a given access level.
     *
     * @param access The access bitfield to be masked with Opcodes.
     * @return A char correlating to that level of access.
     */
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

import java.util.List;
import java.util.ArrayList;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

public abstract class Pattern {
    private class ParsedSignature {
        public String parsedString;
        public int endIndex;

        public ParsedSignature(String parsedString, int endIndex) {
            this.parsedString = parsedString;
            this.endIndex = endIndex;
        }
    }

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
            List<FieldNode> fieldList = cell.getFieldNodes();

            for(FieldNode fieldNode : fieldList) {
                fields += translateFieldNode(fieldNode);
            }

            String methods = "";
            List<MethodNode> methodList = cell.getMethods();

            for(MethodNode methodNode : methodList){
                methods += translateMethodNode(methodNode);
            }

            if ((cell.getAccess() & Opcodes.ACC_ABSTRACT) != 0 || (cell.getAccess() & Opcodes.ACC_INTERFACE) != 0) {
                cellName = "<I>" + cellName + "</I>";
            }

            node.addAttribute("label", "<{" + cellName + "|" + fields + "|" + methods + "}>");
            elements.add(node);
        }

        //edges
        for(Edge edge : detected.getEdges()) {
            String from = edge.getOrigin().getPrettyName();
            String to = edge.getDestination().getPrettyName();

            GraphvizEdge gvEdge = new GraphvizEdge(from, to, edge.getRelation().toString());
            switch (edge.getRelation()) {
            case IMPLEMENTS:
                gvEdge.addAttribute("arrowhead", "\"onormal\"");
                gvEdge.addAttribute("style", "\"dashed\"");
                break;
            case EXTENDS:
                gvEdge.addAttribute("arrowhead", "\"onormal\"");
                break;
            case ASSOCIATION:
            case DEPENDS:
                continue;
            default:
                System.err.println("Pattern::Unrecognized relation: " + edge.getRelation());
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
        String type;
        String signature = node.signature;
        if (signature == null) {
            type = Type.getType(node.desc).getClassName();
        } else {
            type = parseSignature(signature).parsedString;
        }

        result += node.name + ": " + type + "<br align=\"left\"/>";

        return result;
    }

    private ParsedSignature parseSignature(String signature) {
        if (signature == null || signature.length() == 0) {
            return new ParsedSignature("", 0);
        }

        List<String> typeNames = new ArrayList<>();
        String brackets = "";
        int leadIndex = 0;

        while (leadIndex < signature.length()
               && signature.charAt(leadIndex) != ';') {
            switch (signature.charAt(leadIndex)) {
            case '[':
                brackets = "[]";
                break;
            case 'T':
                typeNames.add("T" + brackets);
                brackets = "";
                break;
            case 'E':
                typeNames.add("E" + brackets);
                brackets = "";
                break;
            case 'L':
                ParsedSignature parsed
                    = parseClassSignature(signature.substring(leadIndex + 1));
                leadIndex += parsed.endIndex + 1;
                typeNames.add(parsed.parsedString + brackets);
                brackets = "";
                break;
            default:
                String type = Type.getType(Character.toString(signature.charAt(leadIndex))).getClassName();
                type = (type == null) ? Character.toString(signature.charAt(leadIndex)) : type;
                typeNames.add(type + brackets);
                brackets = "";
                break;
            }

            leadIndex++;
        }

        return new ParsedSignature(String.join(", ", typeNames).replace("/", "."), leadIndex);
    }

    private ParsedSignature parseClassSignature(String signature) {
        String objName = null;
        int leadIndex = 0;

        while (leadIndex < signature.length()
               && signature.charAt(leadIndex) != ';') {
            if (signature.charAt(leadIndex) == '<') {
                int openIndex = leadIndex;
                objName = signature.substring(0, leadIndex) + "&lt;";
                int angleBracketsCt = 1;

                leadIndex++;
                while (angleBracketsCt != 0) {
                    switch (signature.charAt(leadIndex)) {
                    case '>':
                        angleBracketsCt--;
                        break;
                    case '<':
                        angleBracketsCt++;
                        break;
                    default:
                        break;
                    }
                    leadIndex++;
                }

                ParsedSignature template = parseSignature(signature.substring(openIndex + 1, leadIndex - 1));
                objName += template.parsedString + "&gt;";
                break;
            }

            leadIndex++;
        }

        if (objName == null) {
            objName = signature.substring(0, leadIndex);
        }

        return new ParsedSignature(objName, leadIndex);
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
        String returnType;
        String signature = node.signature;
        if (signature == null) {
            returnType = Type.getReturnType(node.desc).getClassName();
            List<String> argTypes = new ArrayList<>();
            for (Type argType : Type.getArgumentTypes(node.desc)){
                argTypes.add(argType.getClassName());
            }
            arguments += String.join(", ", argTypes) + ")";
        } else {
            int startArgs = signature.indexOf('(');
            int endArgs = signature.indexOf(')');
            if (endArgs - startArgs > 1) {
                arguments += parseSignature(signature.substring(startArgs + 1, endArgs)).parsedString;
            }
            arguments += ")";

            returnType = signature.substring(endArgs + 1);
            if (returnType.startsWith("L")) {
                returnType = parseSignature(returnType).parsedString;
            } else {
                returnType = Type.getReturnType(node.desc).getClassName();
            }
        }

        result += node.name
            + arguments
            + ": "
            + returnType
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

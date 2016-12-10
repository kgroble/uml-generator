
public class GraphvizGlobalParams extends GraphvizElement {

    @Override
    public String toGraphviz() {
        String code = "";
        for(String attribute : this.attributes.keySet()) {
            code += attribute + " = " + attributes.get(attribute) + ";\n";
        }
        return code;
    }

    @Override
    public String getIdentifier() {
        return "global-parameters";
    }

}

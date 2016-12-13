import java.util.ArrayList;
import java.util.List;

public class UMLGeneratorApp {
    public static void main(String[] args) {
        List<String> classNames = new ArrayList<>();
        boolean recursive = false;
        GraphGenerator.AccessLevel accessLevel = GraphGenerator.AccessLevel.PRIVATE;
        for (String className : args) {
            switch(className){
            case "-r":
            case "--recursive":
                recursive = true;
                break;
            case "--public":
                accessLevel = GraphGenerator.AccessLevel.PUBLIC;
                break;
            case "--private":
                accessLevel = GraphGenerator.AccessLevel.PRIVATE;
                break;
            case "--protected":
                accessLevel = GraphGenerator.AccessLevel.PROTECTED;
            default:
                classNames.add(className);
                break;
            }
            
        }
        GraphGenerator generator = new GraphGenerator(recursive, accessLevel);
        Graph g = generator.execute(classNames);
        Parser parser = new Parser();
        Pattern idPattern = new IdentityPattern();
        parser.addPattern(idPattern, 0);
        List<GraphvizElement> elements = parser.parseGraph(g);
        Exporter fileExporter = new FileExporter("./output/out.dot");
        fileExporter.export(elements);
    }
}

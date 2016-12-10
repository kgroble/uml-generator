import java.util.ArrayList;
import java.util.List;

public class UMLGeneratorApp {
    public static void main(String[] args) {
        System.out.println("UML Parser running!");
        List<String> classNames = new ArrayList<>();
        for (String className : args) {
            classNames.add(className);
        }
        classNames.add("java.util.List");
        GraphGenerator generator = new GraphGenerator(true, GraphGenerator.AccessLevel.PRIVATE);
        Graph g = generator.execute(classNames);
        Parser parser = new Parser();
        Pattern idPattern = new IdentityPattern();
        parser.addPattern(idPattern, 0);
        List<GraphvizElement> elements = parser.parseGraph(g);
        Exporter fileExporter = new FileExporter("./output/out.dot");
        fileExporter.export(elements);
    }
}

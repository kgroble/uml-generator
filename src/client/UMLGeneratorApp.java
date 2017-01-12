package client;
import exporters.Exporter;
import exporters.FileExporter;
import graph.AccessLevel;
import graph.AssociationGraphGen;
import graph.DependencyGraphGen;
import graph.Graph;
import graph.GraphGenerator;
import graph.ImplementsGraphGen;
import graph.SuperGraphGen;
import graphviz.GraphvizElement;

import java.util.ArrayList;
import java.util.List;

import patterns.AssociationPattern;
import patterns.DependencyPattern;
import patterns.IdentityPattern;
import patterns.Parser;
import patterns.Pattern;

public class UMLGeneratorApp {
    public static void main(String[] args) {
        List<String> classNames = new ArrayList<>();
        boolean recursive = false;
        AccessLevel accessLevel = AccessLevel.PRIVATE;
        for (String className : args) {
            switch(className) {
            case "-r":
            case "--recursive":
                recursive = true;
            break;
            case "--public":
                accessLevel = AccessLevel.PUBLIC;
                break;
            case "--private":
                accessLevel = AccessLevel.PRIVATE;
                break;
            case "--protected":
                accessLevel = AccessLevel.PROTECTED;
            default:
                classNames.add(className);
                break;
            }
        }

        GraphGenerator generator = new GraphGenerator(recursive, accessLevel);
        generator = new SuperGraphGen(new ImplementsGraphGen(generator));
        generator = new AssociationGraphGen(generator);
        generator = new DependencyGraphGen(generator);
        Graph g = generator.execute(classNames);
        Parser parser = new Parser();
        Pattern idPattern = new IdentityPattern();
        parser.addPattern(idPattern, 0);
        parser.addPattern(new DependencyPattern(), 1);
        parser.addPattern(new AssociationPattern(), 2);
        List<GraphvizElement> elements = parser.parseGraph(g);
        Exporter fileExporter = new FileExporter("./output/out.dot");
        fileExporter.export(elements);
    }
}

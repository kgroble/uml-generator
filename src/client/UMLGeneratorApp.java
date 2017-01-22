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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import patterns.AssociationPattern;
import patterns.DependencyPattern;
import patterns.IdentityPattern;
import patterns.Parser;
import patterns.Pattern;

public class UMLGeneratorApp {
    public static void main(String[] args) {
        try {
            ConfigSettings.setupConfig(args);
        } catch (IOException e) {
            System.err.println("Failed to read settings file.");
        }

        GraphGenerator generator = new GraphGenerator(ConfigSettings.getRecursive(), ConfigSettings.getAccessLevel());
        generator = new ImplementsGraphGen(generator);
        generator = new SuperGraphGen(generator);
        generator = new AssociationGraphGen(generator);
        generator = new DependencyGraphGen(generator);

        Graph g = generator.execute(ConfigSettings.getWhiteList());

        Parser parser = new Parser();
        List<Pattern> patterns = ConfigSettings.getPatterns();
        parser.addPattern(new IdentityPattern(), 0);
        for (int i = 0; i < patterns.size(); i++) {
            parser.addPattern(patterns.get(i), i + 1);
        }

        List<GraphvizElement> elements = parser.parseGraph(g);
        Exporter fileExporter = new FileExporter("./output/out.dot");
        fileExporter.export(elements);
    }
}

package exporters;
import graphviz.GraphvizElement;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;

public class FileExporter implements Exporter {
    private String outFile;

    /**
     * Creates a new FileExporter and sets its output file to file.
     *
     * @param file The file at which to output the Graphviz code.
     */
    public FileExporter(String file) {
        this.outFile = file;
    }

    @Override
    public void export(List<GraphvizElement> elements) {
        try {
            PrintWriter writer = new PrintWriter(this.outFile);

            writer.println("digraph uml {");
            for(GraphvizElement element : elements) {
                writer.print(element.toGraphviz());
            }
            writer.println("}");

            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

}

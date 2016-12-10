import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;

public class FileExporter implements Exporter {
    private String outFile;
    
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

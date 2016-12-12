import java.util.List;

public interface Exporter {
    /**
     * Exports the given GraphviElements into some final presentable format for
     * the end user.
     *
     * @param elements A list of all the GraphvizElements to export.
     */
    public void export(List<GraphvizElement> elements);
}

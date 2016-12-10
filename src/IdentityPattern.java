import java.util.List;

/**
 * Created by lewis on 12/10/16.
 */
public class IdentityPattern extends Pattern {
    public IdentityPattern(Graph graphToSearch) {
        super(graphToSearch);
    }

    @Override
    public Graph detect(Graph graphToSearch) {
      return graphToSearch.copy();
    }
}

public class Edge {
    public enum Relation {

    }

    private ClassCell originClass;
    private ClassCell destClass;
    private Relation relation;

    public Edge(ClassCell origin, ClassCell dest, Relation relation) {
        this.originClass = origin;
        this.destClass = dest;
        this.relation = relation;
    }
}

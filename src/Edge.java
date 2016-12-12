public class Edge {
    /**
     * Defines the different types of relations a connection between two
     * ClassCells can be.
     */
    public enum Relation {
        IMPLEMENTS,
        EXTENDS
    }

    private ClassCell originClass;
    private ClassCell destClass;
    private Relation relation;

    /**
     * Creates a new Edges with the given information.
     *
     * @param origin The class that will start the /tail/ of the arrow. This
     * class is the subject of whatever relation is being made.
     * @param dest The class that will start the /head/ of the arrow. This class
     * is the object of whatever relation is being made.
     * @param relation What the relation is between these two objects that this
     * Edge represents.
     */
    public Edge(ClassCell origin, ClassCell dest, Relation relation) {
        this.originClass = origin;
        this.destClass = dest;
        this.relation = relation;
    }
    
    public ClassCell getOrigin() {
        return this.originClass;
    }
    
    public ClassCell getDestination() {
        return this.destClass;
    }
}

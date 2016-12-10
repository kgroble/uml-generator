public abstract class PatternDecorator extends Pattern {
    protected Pattern innerPattern;

    public PatternDecorator(Pattern pattern) {
        this.innerPattern = pattern;
    }

    @Override
    public Graph detect(Graph graphToSearch) {
        return this.innerPattern.detect(graphToSearch);
    }

}

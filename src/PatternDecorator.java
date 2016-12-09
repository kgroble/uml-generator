public abstract class PatternDecorator extends Pattern {
	protected Pattern innerPattern;
	
	public PatternDecorator(Graph graphToSearch, Pattern pattern) {
		super(graphToSearch);
		this.innerPattern = pattern;
	}

	@Override
	public Graph detect(Graph graphToSearch) {
		return this.innerPattern.detect(graphToSearch);
	}

}

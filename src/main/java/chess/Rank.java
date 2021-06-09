package chess;

public enum Rank {
	eigth,
	seventh,
	sixth,
	fifth,
	fourth,
	third,
	second,
	first;
	
	public Rank add(int n) {
		return Rank.values()[this.ordinal()+n];
	}
	
	public boolean leq(Rank other) {
		return this.ordinal() <= other.ordinal();
	}
	
	public boolean geq(Rank other) {
		return this.ordinal() >= other.ordinal();
	}
}

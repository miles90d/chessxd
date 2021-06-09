package chess;

public enum File {
	a, b, c, d, e, f, g, h;
	
	public File add(int n) {
		return File.values()[this.ordinal()+n];
	}
	
	public boolean leq(File other) {
		return this.ordinal() <= other.ordinal();
	}
	
	public boolean geq(File other) {
		return this.ordinal() >= other.ordinal();
	}
}

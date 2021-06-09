package chess;

//castling rights binary encoding

/*
 bin  dec
 
0001    1  white king can castle to the king side
0010    2  white king can castle to the queen side
0100    4  black king can castle to the king side
1000    8  black king can castle to the queen side
examples
1111       both sides an castle both directions
1001       black king => queen side
           white king => king side
*/

public enum Castle {
	wk(1), wq(2), bk(4), bq(8);
	
	private final int value;

	private Castle(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return this.value;
	}
}

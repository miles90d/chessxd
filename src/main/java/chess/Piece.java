package chess;

public enum Piece {
	P, N, B, R, Q, K,
	p, n, b, r, q, k;
	
	public static Piece fromChar(char c) {
		switch(c) {
		case 'P':
			return Piece.P;
		case 'N':
			return Piece.N;
		case 'B':
			return Piece.B;
		case 'R':
			return Piece.R;
		case 'Q':
			return Piece.Q;
		case 'K':
			return Piece.K;
		case 'p':
			return Piece.p;
		case 'n':
			return Piece.n;
		case 'b':
			return Piece.b;
		case 'r':
			return Piece.r;
		case 'q':
			return Piece.q;
		case 'k':
			return Piece.k;
		}
		
		throw new IllegalStateException("No piece found");
	}
}

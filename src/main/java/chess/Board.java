package chess;
import static java.lang.System.out;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
public class Board {
	
					/**********************************\
					 ==================================
					 
					          USEFUL CONTSTANTS
					 
					 ==================================
					\**********************************/
	
	@SuppressWarnings("unused")
		private static final String EMPTY_FEN = "8/8/8/8/8/8/8/8 w - - ";
	@SuppressWarnings("unused")
	private static final String START_FEN = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";	
	@SuppressWarnings("unused")
	private static final String TRICKY_FEN = "r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - 0 1 ";
	@SuppressWarnings("unused")
	private static final String CMK_FEN = "r2q1rk1/ppp2ppp/2n1bn2/2b1p3/3pP3/3P1NPP/PPP1NPB1/R1BQ1RK1 b - - 0 9 ";
	
	private static final char[] ASCII_PIECES = {
			'P', 'N', 'B', 'R', 'Q', 'K',
			'p', 'n', 'b', 'r', 'q', 'k'
	};
	
	/*
    castling   move     in      in
       right update     binary  decimal

	king & rooks didn't move:     1111 & 1111  =  1111    15

		   white king  moved:     1111 & 1100  =  1100    12
	 white king's rook moved:     1111 & 1110  =  1110    14
	white queen's rook moved:     1111 & 1101  =  1101    13

			black king moved:     1111 & 0011  =  1011    3
	 black king's rook moved:     1111 & 1011  =  1011    11
	black queen's rook moved:     1111 & 0111  =  0111    7

*/	
	private static final char[] CASTLING_RIGHTS = {
		7, 15, 15, 15,  3, 15, 15, 11,
		15, 15, 15, 15, 15, 15, 15, 15,
		15, 15, 15, 15, 15, 15, 15, 15,
		15, 15, 15, 15, 15, 15, 15, 15,
		15, 15, 15, 15, 15, 15, 15, 15,
		15, 15, 15, 15, 15, 15, 15, 15,
		15, 15, 15, 15, 15, 15, 15, 15,
		13, 15, 15, 15, 12, 15, 15, 14	
	};
	
	@SuppressWarnings("unused")
	private static final char[] unicodePieces = {
			'♙', '♘', '♗', '♖', '♕', '♔',
			'♟', '♞', '♝', '♜', '♛', '♚',
	};
					/**********************************\
					 ==================================
					 
					            Chess board
					 
					 ==================================
					\**********************************/
	
	/*
	                            WHITE PIECES
	        Pawns                  Knights              Bishops
	        
	  8  0 0 0 0 0 0 0 0    8  0 0 0 0 0 0 0 0    8  0 0 0 0 0 0 0 0
	  7  0 0 0 0 0 0 0 0    7  0 0 0 0 0 0 0 0    7  0 0 0 0 0 0 0 0
	  6  0 0 0 0 0 0 0 0    6  0 0 0 0 0 0 0 0    6  0 0 0 0 0 0 0 0
	  5  0 0 0 0 0 0 0 0    5  0 0 0 0 0 0 0 0    5  0 0 0 0 0 0 0 0
	  4  0 0 0 0 0 0 0 0    4  0 0 0 0 0 0 0 0    4  0 0 0 0 0 0 0 0
	  3  0 0 0 0 0 0 0 0    3  0 0 0 0 0 0 0 0    3  0 0 0 0 0 0 0 0
	  2  1 1 1 1 1 1 1 1    2  0 0 0 0 0 0 0 0    2  0 0 0 0 0 0 0 0
	  1  0 0 0 0 0 0 0 0    1  0 1 0 0 0 0 1 0    1  0 0 1 0 0 1 0 0
	     a b c d e f g h       a b c d e f g h       a b c d e f g h
	         Rooks                 Queens                 King
	  8  0 0 0 0 0 0 0 0    8  0 0 0 0 0 0 0 0    8  0 0 0 0 0 0 0 0
	  7  0 0 0 0 0 0 0 0    7  0 0 0 0 0 0 0 0    7  0 0 0 0 0 0 0 0
	  6  0 0 0 0 0 0 0 0    6  0 0 0 0 0 0 0 0    6  0 0 0 0 0 0 0 0
	  5  0 0 0 0 0 0 0 0    5  0 0 0 0 0 0 0 0    5  0 0 0 0 0 0 0 0
	  4  0 0 0 0 0 0 0 0    4  0 0 0 0 0 0 0 0    4  0 0 0 0 0 0 0 0
	  3  0 0 0 0 0 0 0 0    3  0 0 0 0 0 0 0 0    3  0 0 0 0 0 0 0 0
	  2  0 0 0 0 0 0 0 0    2  0 0 0 0 0 0 0 0    2  0 0 0 0 0 0 0 0
	  1  1 0 0 0 0 0 0 1    1  0 0 0 1 0 0 0 0    1  0 0 0 0 1 0 0 0
	     a b c d e f g h       a b c d e f g h       a b c d e f g h
	                            BLACK PIECES
	        Pawns                  Knights              Bishops
	        
	  8  0 0 0 0 0 0 0 0    8  0 1 0 0 0 0 1 0    8  0 0 1 0 0 1 0 0
	  7  1 1 1 1 1 1 1 1    7  0 0 0 0 0 0 0 0    7  0 0 0 0 0 0 0 0
	  6  0 0 0 0 0 0 0 0    6  0 0 0 0 0 0 0 0    6  0 0 0 0 0 0 0 0
	  5  0 0 0 0 0 0 0 0    5  0 0 0 0 0 0 0 0    5  0 0 0 0 0 0 0 0
	  4  0 0 0 0 0 0 0 0    4  0 0 0 0 0 0 0 0    4  0 0 0 0 0 0 0 0
	  3  0 0 0 0 0 0 0 0    3  0 0 0 0 0 0 0 0    3  0 0 0 0 0 0 0 0
	  2  0 0 0 0 0 0 0 0    2  0 0 0 0 0 0 0 0    2  0 0 0 0 0 0 0 0
	  1  0 0 0 0 0 0 0 0    1  0 0 0 0 0 0 0 0    1  0 0 0 0 0 0 0 0
	     a b c d e f g h       a b c d e f g h       a b c d e f g h
	         Rooks                 Queens                 King
	  8  1 0 0 0 0 0 0 1    8  0 0 0 1 0 0 0 0    8  0 0 0 0 1 0 0 0
	  7  0 0 0 0 0 0 0 0    7  0 0 0 0 0 0 0 0    7  0 0 0 0 0 0 0 0
	  6  0 0 0 0 0 0 0 0    6  0 0 0 0 0 0 0 0    6  0 0 0 0 0 0 0 0
	  5  0 0 0 0 0 0 0 0    5  0 0 0 0 0 0 0 0    5  0 0 0 0 0 0 0 0
	  4  0 0 0 0 0 0 0 0    4  0 0 0 0 0 0 0 0    4  0 0 0 0 0 0 0 0
	  3  0 0 0 0 0 0 0 0    3  0 0 0 0 0 0 0 0    3  0 0 0 0 0 0 0 0
	  2  0 0 0 0 0 0 0 0    2  0 0 0 0 0 0 0 0    2  0 0 0 0 0 0 0 0
	  1  0 0 0 0 0 0 0 0    1  0 0 0 0 0 0 0 0    1  0 0 0 0 0 0 0 0
	     a b c d e f g h       a b c d e f g h       a b c d e f g h
	                             OCCUPANCIES
	     White occupancy       Black occupancy       All occupancies
	  8  0 0 0 0 0 0 0 0    8  1 1 1 1 1 1 1 1    8  1 1 1 1 1 1 1 1
	  7  0 0 0 0 0 0 0 0    7  1 1 1 1 1 1 1 1    7  1 1 1 1 1 1 1 1
	  6  0 0 0 0 0 0 0 0    6  0 0 0 0 0 0 0 0    6  0 0 0 0 0 0 0 0
	  5  0 0 0 0 0 0 0 0    5  0 0 0 0 0 0 0 0    5  0 0 0 0 0 0 0 0
	  4  0 0 0 0 0 0 0 0    4  0 0 0 0 0 0 0 0    4  0 0 0 0 0 0 0 0
	  3  0 0 0 0 0 0 0 0    3  0 0 0 0 0 0 0 0    3  0 0 0 0 0 0 0 0
	  2  1 1 1 1 1 1 1 1    2  0 0 0 0 0 0 0 0    2  1 1 1 1 1 1 1 1
	  1  1 1 1 1 1 1 1 1    1  0 0 0 0 0 0 0 0    1  1 1 1 1 1 1 1 1
	                            ALL TOGETHER
	                        8  ♜ ♞ ♝ ♛ ♚ ♝ ♞ ♜
	                        7  ♟︎ ♟︎ ♟︎ ♟︎ ♟︎ ♟︎ ♟︎ ♟︎
	                        6  . . . . . . . .
	                        5  . . . . . . . .
	                        4  . . . . . . . .
	                        3  . . . . . . . .
	                        2  ♙ ♙ ♙ ♙ ♙ ♙ ♙ ♙
	                        1  ♖ ♘ ♗ ♕ ♔ ♗ ♘ ♖
	                           a b c d e f g h
	*/	
	
	private BitBoard bitboards[];
	private BitBoard wOccupancy;
	private BitBoard bOccupancy;
	private BitBoard nOccupancy;
	private List<Move> moves;
	private Color side;
	Square enpassant;
	int castle;
	
	private BitBoard bitboardsSave[] = null;
	private BitBoard wOccupancySave = null;
	private BitBoard bOccupancySave = null;
	private BitBoard nOccupancySave = null;
	private Color sideSave;
	Square enpassantSave;
	int castleSave;
	
	public Board() {
		bitboards = new BitBoard[12];
		for(int i = 0; i < 12; i++) {
			bitboards[i] = new BitBoard();
		}
		moves = new ArrayList<>();
		
		
		wOccupancy = new BitBoard();
		bOccupancy = new BitBoard();
		nOccupancy = new BitBoard();
		
		//side = Color.white;
		//enpassant = null;
		//castle = 0b1111;
		
		this.parseFen(START_FEN);
		this.generateMoves();
		
	};
	
	/**
	 * 
	 * @return last generated set of potentially legal moves.
	 */
	public List<Move> getMoves() {
		return moves;
	}
	
	public Color getSide() {
		return side;
	}
	
	
	public void print() {
		out.println();
		
		for(var rank: Rank.values()) {
			for(var file: File.values()) {
				var square = Square.getSquare(rank, file);
				
				if(file == File.a)
					out.printf("  %d ", 8 - rank.ordinal());
				
				Piece piece = null;

				for(var bbPiece: Piece.values())  {
					if(bitboards[bbPiece.ordinal()].get(square)) 
						piece = bbPiece;
				}
				
				out.printf(" %c ", (piece == null) ? '.' : ASCII_PIECES[piece.ordinal()]);
			}
			
			out.println();
		}
		
		out.printf("\n     a  b  c  d  e  f  g  h\n");
		
		out.printf("%s to move\n", (side == Color.white) ? "white" : "black");
		out.printf("Enpassant: %s\n", (enpassant != null) ? enpassant.toString() : "no");
		out.printf("Castling: %c%c%c%c\n\n",
				((castle & Castle.wk.getValue()) != 0) ? 'K' : '-',
				((castle & Castle.wq.getValue()) != 0) ? 'Q' : '-',
				((castle & Castle.bk.getValue()) != 0) ? 'k' : '-',
				((castle & Castle.bq.getValue()) != 0) ? 'q' : '-');
	}

	
	public void parseFen(String fen) {
		
		//reset board state
		for(int i = 0; i < 12; i++) {
			bitboards[i] = new BitBoard();
		}
		wOccupancy = new BitBoard();
		bOccupancy = new BitBoard();
		nOccupancy = new BitBoard();
		castle = 0;
		enpassant = null;
		
		
		int index = 0;
		
		for(int rank = 0; rank < 8; rank++) {
			for(int file = 0; file < 8; file++) {
				
				int square = 8*rank+file;
				
				if(Character.isLetter(fen.charAt(index))) {
					Piece piece = Piece.fromChar(fen.charAt(index));
					bitboards[piece.ordinal()].set(square);
					index++;
				}
				
				if(Character.isDigit(fen.charAt(index))) {
					int offset = Character.getNumericValue(fen.charAt(index));
					
					Piece piece = null;
					
					for(var bbPiece: Piece.values()) {
						if(bitboards[bbPiece.ordinal()].get(square))
							piece = bbPiece;
					}
					
					if(piece == null) {
						file--;
					}
					
					file += offset;
					
					index++;
				}
				
				if(fen.charAt(index) == '/')
					index++;
			}
		}
		
		//goto side to move
		index++;
		
		//parse side to move
		side = (fen.charAt(index) == 'w') ? Color.white : Color.black;
		
		//goto castling rights
		index+=2;
		
		//parse castling rights
		while(fen.charAt(index) != ' ') {
			
			switch(fen.charAt(index)) {
			case 'K':
				castle |= Castle.wk.getValue();
				break;
			case 'Q':
				castle |= Castle.wq.getValue();
				break;
			case 'k':
				castle |= Castle.bk.getValue();
				break;
			case 'q':
				castle |= Castle.bq.getValue();
				break;
			case '-':
				break;
			}
			
			index++;
		}
		
		
		// goto enpassant
		index++;
		
		if(fen.charAt(index) != '-') {
			int file = fen.charAt(index) - 'a';
			int rank = 8 - (Character.getNumericValue(fen.charAt(index+1)));
			
			enpassant = Square.values()[8*rank+file];
			
		} else {
			enpassant = null;
		}
		
		// white occupancy
		
		for(var piece: Arrays.copyOfRange(Piece.values(), 0, 6)) {
			wOccupancy = wOccupancy.or(bitboards[piece.ordinal()]);
		}
		
		// black occupancy
		for(var piece: Arrays.copyOfRange(Piece.values(), 6, 12)) {
			bOccupancy = bOccupancy.or(bitboards[piece.ordinal()]);
		}
		
		
		// both occupancy
		nOccupancy = wOccupancy.or(bOccupancy);
		
		//out.printf("fen: '%s'\n", fen.substring(index));
		
		
	}
	
	public boolean isSquareAttacked(Square square, Color side) {
		
		//atacked by white pawn
		if((side == Color.white) &&
				(AttackTableFactory.Leaper.get("BPAWN")[square.ordinal()].and(bitboards[Piece.P.ordinal()]).flag()))
			return true;
		
		//attacked by balck pawn
		if((side == Color.black) &&
				(AttackTableFactory.Leaper.get("WPAWN")[square.ordinal()].and(bitboards[Piece.p.ordinal()]).flag()))
			return true;
		
		
		//attacked by knight
		if(AttackTableFactory.Leaper
				.get("KNIGHT")[square.ordinal()]
						.and(((side == Color.white) ? bitboards[Piece.N.ordinal()] : bitboards[Piece.n.ordinal()])).flag())
			return true;
		
		//attacked by bishop
		if(AttackTableFactory.Slider.get("BISHOP", square, nOccupancy)
				.and(((side == Color.white) ? bitboards[Piece.B.ordinal()] : bitboards[Piece.b.ordinal()])).flag())
			return true;
		
		//attacked by rook
		if(AttackTableFactory.Slider.get("ROOK", square, nOccupancy)
				.and(((side == Color.white) ? bitboards[Piece.R.ordinal()] : bitboards[Piece.r.ordinal()])).flag())
			return true;
		
		//attacked by queen
		if(AttackTableFactory.Slider.get("QUEEN", square, nOccupancy)
				.and(((side == Color.white) ? bitboards[Piece.Q.ordinal()] : bitboards[Piece.q.ordinal()])).flag())
			return true;
		
		
		
		//attacked by king
		if(AttackTableFactory.Leaper
				.get("KING")[square.ordinal()]
						.and(((side == Color.white) ? bitboards[Piece.K.ordinal()] : bitboards[Piece.k.ordinal()])).flag())
			return true;
		
		return false;
	}
	
	public void printAttackedSquares(Color side) {
		out.println();
		
		for(var rank: Rank.values()) {
			for(var file: File.values()) {
				var square = Square.getSquare(rank, file);
				
				if(file == File.a) {
					out.printf("  %d ", 8 - rank.ordinal());
				}
				
				out.printf(" %d ", isSquareAttacked(square, side) ? 1 : 0);
			}
			
			out.println();
		}
		
		out.printf("\n     a  b  c  d  e  f  g  h\n");

	}
	
	
	/**
	 * Generates potentially legal moves for given board state's side, depending on board state
	 */
	public List<Move> generateMoves() {
		Square src, dst;
		
		BitBoard bitboard, attacks;
		
		//reset moves
		moves = new ArrayList<>();
		
		for(var piece: Piece.values()) {
			bitboard = BitBoard.fromBitBoard(bitboards[piece.ordinal()]);
			
			
			// pawn, king castling moves
			if(side == Color.white) {
				if(piece == Piece.P) {
					while(bitboard.flag()) {						
						src = Square.fromInt(bitboard.ones().first());
						
						dst =  Square.fromInt(bitboard.ones().first() - 8);
						
						
						// quiet pawn moves
						if(!(dst.ordinal() < Square.a8.ordinal()) && !nOccupancy.get(dst)) {
							
							//pawn promotion
							if(src.ordinal() >= Square.a7.ordinal() && src.ordinal() <= Square.h7.ordinal()) {
								
				
//								out.printf("%s%sq		pawn promotion\n", src.toString(), dst.toString());
//								out.printf("%s%sr	 	pawn promotion\n", src.toString(), dst.toString());
//								out.printf("%s%sb 		pawn promotion\n", src.toString(), dst.toString());
//								out.printf("%s%sn		pawn promotion\n", src.toString(), dst.toString());
								
								moves.add(new Move(src, dst, piece, Piece.Q, false, false, false, false));
								moves.add(new Move(src, dst, piece, Piece.R, false, false, false, false));
								moves.add(new Move(src, dst, piece, Piece.B, false, false, false, false));
								moves.add(new Move(src, dst, piece, Piece.N, false, false, false, false));


								
							} else {
								// pawn push
								
//								out.printf("%s%s		pawn push\n", src.toString(), dst.toString());
								moves.add(new Move(src, dst, piece, null, false, false, false, false));
								
								// double pawn push
								if((src.ordinal() >= Square.a2.ordinal() && src.ordinal() <= Square.h2.ordinal()) && !nOccupancy.get(dst.ordinal() - 8)) {
//									out.printf("%s%s		double pawn push\n", src.toString(), Square.fromInt(dst.ordinal() - 8));
									moves.add(new Move(src, Square.fromInt(dst.ordinal() - 8), piece, null, false, true, false, false));

								}
								
							}
						}
						
						// capture pawn moves
						
						attacks = AttackTableFactory.Leaper.get("WPAWN")[src.ordinal()].and(bOccupancy);
						
						while(attacks.flag()) {
							dst = Square.fromInt(attacks.ones().first());
							
							if(src.ordinal() >= Square.a7.ordinal() && src.ordinal() <= Square.h7.ordinal()) {
								
								
//								out.printf("%s%sq		pawn promotion capture\n", src.toString(), dst.toString());
//								out.printf("%s%sr		pawn promotion capture\n", src.toString(), dst.toString());
//								out.printf("%s%sb		pawn promotion capture\n", src.toString(), dst.toString());
//								out.printf("%s%sn		pawn promotion capture\n", src.toString(), dst.toString());
								moves.add(new Move(src, dst, piece, Piece.Q, true, false, false, false));
								moves.add(new Move(src, dst, piece, Piece.R, true, false, false, false));
								moves.add(new Move(src, dst, piece, Piece.B, true, false, false, false));
								moves.add(new Move(src, dst, piece, Piece.N, true, false, false, false));


								
							} else {
								// pawn push
//								out.printf("%s%s		pawn capture\n", src.toString(), dst.toString());
								moves.add(new Move(src, dst, piece, null, true, false, false, false));
							}
							
							attacks.clear(dst);
						}
						
						// enpassant
						if(enpassant != null) {
							BitBoard enpassantAttacks = AttackTableFactory.Leaper.get("WPAWN")[src.ordinal()]
									.and(BitBoard.fromLong(1).shiftLeft(enpassant.ordinal()));		
							
							
							if(enpassantAttacks.flag()) {
								Square dstEnpassant = Square.fromInt(enpassantAttacks.ones().first());
								
//								out.printf("%s%s		pawn enpassant\n", src.toString(), dstEnpassant.toString());
								moves.add(new Move(src, dstEnpassant, piece, null, true, false, true, false));

							}
							
						}
						
						
						bitboard.clear(src);
					}
						
				}
				
				//castling
				if(piece == Piece.K) {
					//king side castling
					if((castle & Castle.wk.getValue()) != 0) {
						//check for empty space between
						if(!(nOccupancy.get(Square.f1)) && !(nOccupancy.get(Square.g1))) {
							// check if king is in check or path is attacked
							
							if(!isSquareAttacked(Square.e1, Color.black) 
									&& !isSquareAttacked(Square.f1, Color.black)) {
								
//								out.printf("e1g1		castle\n");
								moves.add(new Move(Square.e1, Square.g1, piece, null, false, false, false, true));

							}
							
							
						}
					}
					
					//queen side castling
					if((castle & Castle.wq.getValue()) != 0) {
						//check for empty space between
						if(!(nOccupancy.get(Square.d1)) && !(nOccupancy.get(Square.c1)) && !(nOccupancy.get(Square.b1))) {
							// check if king is in check or path is attacked
							
							if(!isSquareAttacked(Square.e1, Color.black) 
									&& !isSquareAttacked(Square.d1, Color.black)) {
								
//								out.printf("e1c1		castle\n");
								moves.add(new Move(Square.e1, Square.c1, piece, null, false, false, false, true));

							}
							
							
						}
					}
				}
				
				
			} else {
				if(piece == Piece.p) {
					while(bitboard.flag()) {
						
						
						src = Square.fromInt(bitboard.ones().first());
						
						dst =  Square.fromInt(bitboard.ones().first() + 8);
						
						
						// quiet pawn moves
						if(!(dst.ordinal() > Square.h1.ordinal()) && !nOccupancy.get(dst)) {
							
							//pawn promotion
							if(src.ordinal() >= Square.a2.ordinal() && src.ordinal() <= Square.h2.ordinal()) {
								
				
//								out.printf("%s%sq		pawn promotion\n", src.toString(), dst.toString());
//								out.printf("%s%sr		pawn promotion\n", src.toString(), dst.toString());
//								out.printf("%s%sb		pawn promotion\n", src.toString(), dst.toString());
//								out.printf("%s%sn		pawn promotion\n", src.toString(), dst.toString());
								
								moves.add(new Move(src, dst, piece, Piece.q, false, false, false, false));
								moves.add(new Move(src, dst, piece, Piece.r, false, false, false, false));
								moves.add(new Move(src, dst, piece, Piece.b, false, false, false, false));
								moves.add(new Move(src, dst, piece, Piece.n, false, false, false, false));


								
							} else {
								// pawn push
								
//								out.printf("%s%s		pawn push\n", src.toString(), dst.toString());
								moves.add(new Move(src, dst, piece, null, false, false, false, false));
								
								
								// double pawn push
								if((src.ordinal() >= Square.a7.ordinal() && src.ordinal() <= Square.h7.ordinal()) && !nOccupancy.get(dst.ordinal() + 8)) {
//									out.printf("%s%s		double pawn push\n", src.toString(), Square.fromInt(dst.ordinal() + 8));
									moves.add(new Move(src, Square.fromInt(dst.ordinal() + 8), piece, null, false, true, false, false));
								}
								
							}
						}
						
						// capture pawn moves
						
						attacks = AttackTableFactory.Leaper.get("BPAWN")[src.ordinal()].and(wOccupancy);
						
						while(attacks.flag()) {
							dst = Square.fromInt(attacks.ones().first());
							
							if(src.ordinal() >= Square.a2.ordinal() && src.ordinal() <= Square.h2.ordinal()) {
								
								
//								out.printf("%s%sq		pawn promotion capture\n", src.toString(), dst.toString());
//								out.printf("%s%sr		pawn promotion capture\n", src.toString(), dst.toString());
//								out.printf("%s%sb		pawn promotion capture\n", src.toString(), dst.toString());
//								out.printf("%s%sn		pawn promotion capture\n", src.toString(), dst.toString());
								moves.add(new Move(src, dst, piece, Piece.q, true, false, false, false));
								moves.add(new Move(src, dst, piece, Piece.r, true, false, false, false));
								moves.add(new Move(src, dst, piece, Piece.b, true, false, false, false));
								moves.add(new Move(src, dst, piece, Piece.n, true, false, false, false));


								
							} else {
								// pawn push
//								out.printf("%s%s		pawn capture\n", src.toString(), dst.toString());
								moves.add(new Move(src, dst, piece, null, true, false, false, false));

							}
							attacks.clear(dst);
						}
						
						// enpassant
						if(enpassant != null) {
							
							//BitBoard enpassantAttacks = AttackTableFactory.Leaper.get("BPAWN")[src.ordinal()]
							//		.and(BitBoard.fromLong(1 << enpassant.ordinal()));					
							
							BitBoard enpassantAttacks = AttackTableFactory.Leaper.get("BPAWN")[src.ordinal()]
									.and(BitBoard.fromLong(1).shiftLeft(enpassant.ordinal()));		
							
							
							if(enpassantAttacks.flag()) {
								Square dstEnpassant = Square.fromInt(enpassantAttacks.ones().first());
								
//								out.printf("%s%s		pawn enpassant\n", src.toString(), dstEnpassant.toString());
								moves.add(new Move(src, dstEnpassant, piece, null, true, false, true, false));

							}
							
						}
						
						
						
						bitboard.clear(src);
					}
					
					
				}
				
				//castling
				if(piece == Piece.k) {
					//king side castling
					if((castle & Castle.bk.getValue()) != 0) {
						//check for empty space between
						if(!(nOccupancy.get(Square.f8)) && !(nOccupancy.get(Square.g8))) {
							// check if king is in check or path is attacked
							
							if(!isSquareAttacked(Square.e8, Color.white) 
									&& !isSquareAttacked(Square.f8, Color.white)) {
								
//								out.printf("e8g8		castle\n");
								moves.add(new Move(Square.e8, Square.g8, piece, null, false, false, false, true));

							}
							
							
						}
					}
					
					//queen side castling
					if((castle & Castle.bq.getValue()) != 0) {
						//check for empty space between
						if(!(nOccupancy.get(Square.d8)) && !(nOccupancy.get(Square.c8)) && !(nOccupancy.get(Square.b8))) {
							// check if king is in check or path is attacked
							
							if(!isSquareAttacked(Square.e8, Color.white) 
									&& !isSquareAttacked(Square.d8, Color.white)) {
								
//								out.printf("e8c8		castle\n");
								moves.add(new Move(Square.e8, Square.c8, piece, null, false, false, false, true));

							}
							
							
						}
					}
				}
				
			}
			
			// knight moves
			
			if((side == Color.white) ? piece == Piece.N : piece == Piece.n) {
				while(bitboard.flag()) {
					src = Square.fromInt(bitboard.ones().first());
					
					attacks = AttackTableFactory.Leaper.get("KNIGHT")[src.ordinal()]
							.and((side == Color.white) ? wOccupancy.not() : bOccupancy.not());
					
					
					while(attacks.flag()) {
						dst = Square.fromInt(attacks.ones().first());
						
						// quiet move
						if(! ((side == Color.white ? bOccupancy : wOccupancy).get(dst)) ) {
//							out.printf("%s%s		knight quiet move\n", src.toString(), dst.toString());	
							moves.add(new Move(src, dst, piece, null, false, false, false, false));

						} else {
							// capture move
//							out.printf("%s%s		knight capture\n", src.toString(), dst.toString());
							moves.add(new Move(src, dst, piece, null, true, false, false, false));

						}
						
						
						attacks.clear(dst);
					}
					
					bitboard.clear(src);
				}
			}
			
			// bishop moves
			if((side == Color.white) ? piece == Piece.B : piece == Piece.b) {
				while(bitboard.flag()) {
					src = Square.fromInt(bitboard.ones().first());
					
					attacks = AttackTableFactory.Slider.get("BISHOP", src, nOccupancy)
							.and((side == Color.white) ? wOccupancy.not() : bOccupancy.not());
					
					
					while(attacks.flag()) {
						dst = Square.fromInt(attacks.ones().first());
						
						// quiet move
						if(! ((side == Color.white ? bOccupancy : wOccupancy).get(dst)) ) {
//							out.printf("%s%s		bishop quiet move\n", src.toString(), dst.toString());	
							moves.add(new Move(src, dst, piece, null, false, false, false, false));
						} else {
							// capture move
//							out.printf("%s%s		bishop capture\n", src.toString(), dst.toString());
							moves.add(new Move(src, dst, piece, null, true, false, false, false));
						}
						
						
						attacks.clear(dst);
					}
					
					bitboard.clear(src);
				}
			}
			// rook moves
			if((side == Color.white) ? piece == Piece.R : piece == Piece.r) {
				while(bitboard.flag()) {
					src = Square.fromInt(bitboard.ones().first());
					
					attacks = AttackTableFactory.Slider.get("ROOK", src, nOccupancy)
							.and((side == Color.white) ? wOccupancy.not() : bOccupancy.not());
					
					
					while(attacks.flag()) {
						dst = Square.fromInt(attacks.ones().first());
						
						// quiet move
						if(! ((side == Color.white ? bOccupancy : wOccupancy).get(dst)) ) {
//							out.printf("%s%s		rook quiet move\n", src.toString(), dst.toString());
							moves.add(new Move(src, dst, piece, null, false, false, false, false));

						} else {
							// capture move
//							out.printf("%s%s		rook capture\n", src.toString(), dst.toString());
							moves.add(new Move(src, dst, piece, null, true, false, false, false));

						}
						
						
						attacks.clear(dst);
					}
					
					bitboard.clear(src);
				}
			}
			// queen moves
			if((side == Color.white) ? piece == Piece.Q : piece == Piece.q) {
				while(bitboard.flag()) {
					src = Square.fromInt(bitboard.ones().first());
					
					attacks = AttackTableFactory.Slider.get("QUEEN", src, nOccupancy)
							.and((side == Color.white) ? wOccupancy.not() : bOccupancy.not());
					
					
					while(attacks.flag()) {
						dst = Square.fromInt(attacks.ones().first());
						
						// quiet move
						if(! ((side == Color.white ? bOccupancy : wOccupancy).get(dst)) ) {
//							out.printf("%s%s		queen quiet move\n", src.toString(), dst.toString());	
							moves.add(new Move(src, dst, piece, null, false, false, false, false));

						} else {
							// capture move
//							out.printf("%s%s		queen capture\n", src.toString(), dst.toString());
							moves.add(new Move(src, dst, piece, null, true, false, false, false));

						}
						
						
						attacks.clear(dst);
					}
					
					bitboard.clear(src);
				}
			}
			
			// king moves
			if((side == Color.white) ? piece == Piece.K : piece == Piece.k) {
				while(bitboard.flag()) {
					src = Square.fromInt(bitboard.ones().first());
					
					attacks = AttackTableFactory.Leaper.get("KING")[src.ordinal()]
							.and((side == Color.white) ? wOccupancy.not() : bOccupancy.not());
					
					
					while(attacks.flag()) {
						dst = Square.fromInt(attacks.ones().first());
						
						// quiet move
						if(! ((side == Color.white ? bOccupancy : wOccupancy).get(dst)) ) {
//							out.printf("%s%s		king quiet move\n", src.toString(), dst.toString());	
							moves.add(new Move(src, dst, piece, null, false, false, false, false));
						} else {
							// capture move
//							out.printf("%s%s		king capture\n", src.toString(), dst.toString());
							moves.add(new Move(src, dst, piece, null, true, false, false, false));
						}
						
						
						attacks.clear(dst);
					}
					
					bitboard.clear(src);
				}
			}
			
		}
		
		return moves;
	}
	
	public void printMoves() {
		
	    out.printf("\n      move    piece     capture   double    enpass    castling\n\n");
		moves.stream().forEach(Move::print);
		out.println("Total: " + moves.size());
	}
	
	public BitBoard[] getBitboards() {
		return this.bitboards;
	}
	
	public void saveState() {
		bitboardsSave = new BitBoard[12];
	
		for(int i = 0; i < 12; i++) {
			bitboardsSave[i] = BitBoard.fromBitBoard(bitboards[i]);
		}
		
		wOccupancySave = BitBoard.fromBitBoard(wOccupancy);
		bOccupancySave = BitBoard.fromBitBoard(bOccupancy);
		nOccupancySave = BitBoard.fromBitBoard(nOccupancy);
		sideSave = side;
		enpassantSave = enpassant;
		castleSave = castle;
	}
	
	public void takeBack() {
		if(bitboardsSave != null) {
			for(int i = 0; i < 12; i++) {
				bitboards[i] = BitBoard.fromBitBoard(bitboardsSave[i]);
			}
			
			wOccupancy = BitBoard.fromBitBoard(wOccupancySave);
			bOccupancy = BitBoard.fromBitBoard(bOccupancySave);
			nOccupancy = BitBoard.fromBitBoard(nOccupancySave);
			enpassant = enpassantSave;
			side = sideSave;
			castle = castleSave;
			return;
		}
		
		throw new IllegalStateException("No move to take back");
	
	}
	
	/**
	 * Updates board state with move if move does not cause moving side's king to be in check.
	 * 
	 * @param move
	 * @return true if the move that has been made does not cause the moving side's king to be in check, false if it does
	 */
	@SuppressWarnings("incomplete-switch")
	public boolean makeMove(Move move) {
		saveState();
		
		Square src = move.src();
		Square dst = move.dst();
		Piece piece = move.piece();
		Piece promotedPiece = move.promotedPiece();
		boolean captureFlag = move.captureFlag();
		boolean doublePushFlag = move.doublePushFlag();
		boolean enpassantFlag = move.enpassantFlag();
		boolean castleFlag = move.castleFlag();
		
		bitboards[piece.ordinal()].clear(src);
		bitboards[piece.ordinal()].set(dst);
		
		if(captureFlag) {
			for(var bbPiece: Arrays.copyOfRange(Piece.values(), (side == Color.white) ? 6 : 0, (side == Color.white) ? 12 : 6)) {
				if(bitboards[bbPiece.ordinal()].get(dst)) {
					bitboards[bbPiece.ordinal()].clear(dst);
					break;
				}
			}
		}
		
		if(promotedPiece != null) {
			// erase pawn from target square
			
			bitboards[(side == Color.white) ? Piece.P.ordinal() : Piece.p.ordinal()].clear(dst);
			bitboards[promotedPiece.ordinal()].set(dst);
		}
		
		if(enpassantFlag) {
			if(side == Color.white) {
				bitboards[Piece.p.ordinal()].clear(Square.fromInt(dst.ordinal() + 8));
			} else 
				bitboards[Piece.P.ordinal()].clear(Square.fromInt(dst.ordinal() - 8));
		}
		
		enpassant = null;
		
		if(doublePushFlag) {
			if(side == Color.white) {
				enpassant = Square.fromInt(dst.ordinal() + 8);
			} else {
				enpassant = Square.fromInt(dst.ordinal() - 8);
				
			}
		}
		
		if(castleFlag) {
			switch(dst) {
			//white king side
			case g1 -> {
				bitboards[Piece.R.ordinal()].clear(Square.h1);
				bitboards[Piece.R.ordinal()].set(Square.f1);
			}
			//white queen side
			case c1 -> {
				bitboards[Piece.R.ordinal()].clear(Square.a1);
				bitboards[Piece.R.ordinal()].set(Square.d1);
			}
			//black king side
			case g8 -> {
				bitboards[Piece.r.ordinal()].clear(Square.h8);
				bitboards[Piece.r.ordinal()].set(Square.f8);
			}
			//black queen side
			case c8 -> {
				bitboards[Piece.r.ordinal()].clear(Square.a8);
				bitboards[Piece.r.ordinal()].set(Square.d8);
			}
			}
			
			
		}
		
		//update castling rights
		
		castle &= CASTLING_RIGHTS[src.ordinal()];
		castle &= CASTLING_RIGHTS[dst.ordinal()];
		
		//update occupancies
		wOccupancy = new BitBoard();
		bOccupancy = new BitBoard();
		nOccupancy = new BitBoard();
		
		for(var bbPiece: Arrays.copyOfRange(Piece.values(), 0, 6)) {
			wOccupancy = wOccupancy.or(bitboards[bbPiece.ordinal()]);
		}
		
		for(var bbPiece: Arrays.copyOfRange(Piece.values(), 6, 12)) {
			bOccupancy = bOccupancy.or(bitboards[bbPiece.ordinal()]);
		}
		
		nOccupancy = wOccupancy.or(bOccupancy);
		
		
		//update side
		side = (side == Color.white) ? Color.black : Color.white;
		
		//check if king is in check after move
		if(isSquareAttacked((side == Color.white) ? Square.fromInt(bitboards[Piece.k.ordinal()].ones().first()) :
			Square.fromInt(bitboards[Piece.K.ordinal()].ones().first()),side)) {
			takeBack();
			
			return false;
		}  else {
			return true;
		}
		
		
	}

}
 
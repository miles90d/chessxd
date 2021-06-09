package chess;

import static org.joou.Unsigned.ulong;

import org.joou.ULong;

public class AttackTableFactory {
	
	/**********************************\
	 ==================================
	 
	          USEFUL CONTSTANTS
	 
	 ==================================
	\**********************************/
	
	

	/*				 A FILE
	 *    8  1  0  0  0  0  0  0  0 
		  7  1  0  0  0  0  0  0  0 
		  6  1  0  0  0  0  0  0  0 
		  5  1  0  0  0  0  0  0  0 
		  4  1  0  0  0  0  0  0  0 
		  3  1  0  0  0  0  0  0  0 
		  2  1  0  0  0  0  0  0  0 
		  1  1  0  0  0  0  0  0  0 
		
		     a  b  c  d  e  f  g  h    
	 * 
	 */
	
	private static final BitBoard A_FILE = BitBoard.fromByteArray(new byte[]{0b00000001,
																			0b00000001,
																			0b00000001,
																			0b00000001,
																			0b00000001,
																			0b00000001,
																			0b00000001,
																			0b00000001});
	private static final BitBoard NOT_A_FILE = A_FILE.not();
	
	/*
	 * 			B FILE
	 *   
		  8  0  1  0  0  0  0  0  0 
		  7  0  1  0  0  0  0  0  0 
		  6  0  1  0  0  0  0  0  0 
		  5  0  1  0  0  0  0  0  0 
		  4  0  1  0  0  0  0  0  0 
		  3  0  1  0  0  0  0  0  0 
		  2  0  1  0  0  0  0  0  0 
		  1  0  1  0  0  0  0  0  0 
		
		     a  b  c  d  e  f  g  h
	 * 
	 * 
	 */
	
	private static final BitBoard B_FILE = BitBoard.fromByteArray(new byte[]{(byte) 0b00000010,
																			(byte) 0b00000010,
																			(byte) 0b00000010,
																			(byte) 0b00000010,
																			(byte) 0b00000010,
																			(byte) 0b00000010,
																			(byte) 0b00000010,
																			(byte) 0b00000010});
	private static final BitBoard NOT_B_FILE = B_FILE.not();
	private static final BitBoard NOT_AB_FILE = NOT_A_FILE.and(NOT_B_FILE);
	/*
	 *   			H FILE
		  8  0  0  0  0  0  0  0  1 
		  7  0  0  0  0  0  0  0  1 
		  6  0  0  0  0  0  0  0  1 
		  5  0  0  0  0  0  0  0  1 
		  4  0  0  0  0  0  0  0  1 
		  3  0  0  0  0  0  0  0  1 
		  2  0  0  0  0  0  0  0  1 
		  1  0  0  0  0  0  0  0  1 
		
		     a  b  c  d  e  f  g  h

	 * 
	 * 
	 * 
	 */
	
	
	private static final BitBoard H_FILE = BitBoard.fromByteArray(new byte[]{(byte) 0b10000000,
																			(byte) 0b10000000,
																			(byte) 0b10000000,
																			(byte) 0b10000000,
																			(byte) 0b10000000,
																			(byte) 0b10000000,
																			(byte) 0b10000000,
																			(byte) 0b10000000});
	private static final BitBoard NOT_H_FILE = H_FILE.not();
	/*
	 * 			G FILE
		  8  0  0  0  0  0  0  1  0 
		  7  0  0  0  0  0  0  1  0 
		  6  0  0  0  0  0  0  1  0 
		  5  0  0  0  0  0  0  1  0 
		  4  0  0  0  0  0  0  1  0 
		  3  0  0  0  0  0  0  1  0 
		  2  0  0  0  0  0  0  1  0 
		  1  0  0  0  0  0  0  1  0 
		
		     a  b  c  d  e  f  g  h
	 * 
	 */
	
	private static final BitBoard G_FILE = BitBoard.fromByteArray(new byte[]{(byte) 0b01000000,
																			(byte) 0b01000000,
																			(byte) 0b01000000,
																			(byte) 0b01000000,
																			(byte) 0b01000000,
																			(byte) 0b01000000,
																			(byte) 0b01000000,
																			(byte) 0b01000000});
	
	private static final BitBoard NOT_G_FILE = G_FILE.not();
	
	private static final BitBoard NOT_HG_FILE = NOT_H_FILE.and(NOT_G_FILE);

	
	public static class Leaper {
		private static BitBoard wPawnAttacks[] = null;
		private static BitBoard bPawnAttacks[] = null;
		private static BitBoard knightAttacks[] = null;
		private static BitBoard kingAttacks[] = null;
		public static BitBoard[] get(String type) {
			
			
			if(wPawnAttacks == null)
				init();
			
			switch(type) {
			case "WPAWN":
				return wPawnAttacks;
			case "BPAWN":
				return bPawnAttacks;
			case "KNIGHT":
				return knightAttacks;
			case "KING":
				return kingAttacks;
			
			}
			throw new IllegalStateException("Incorrect type");
		}
		private static void init() {
			wPawnAttacks = new BitBoard[64];
			bPawnAttacks = new BitBoard[64];
			knightAttacks = new BitBoard[64];
			kingAttacks = new BitBoard[64];
			
			
			var squares = Square.values();
			for(var square: squares) {
				
				//pawns
				wPawnAttacks[square.ordinal()] = maskPawnAttacks(Color.white, square);
				bPawnAttacks[square.ordinal()] = maskPawnAttacks(Color.black, square);
				
				knightAttacks[square.ordinal()] = maskKnightAttacks(square);
				kingAttacks[square.ordinal()] = maskKingAttacks(square);
			}
		}
		private static BitBoard maskPawnAttacks(Color color, Square square) {
			var attacks = new BitBoard();
			
			var pieces = new BitBoard();
			pieces.set(square);
			
			if(color == Color.white) {
				
				/*
				 *  Right attacks are allowed to go to H file,
				 *  but wouldn't make sense to end up on the A file,
				 *  vice versa
				 */
				
				if(pieces.shiftRight(7).and(NOT_A_FILE).flag()) 
					attacks = attacks.or(pieces.shiftRight(7));
				if(pieces.shiftRight(9).and(NOT_H_FILE).flag()) 
					attacks = attacks.or(pieces.shiftRight(9));
			} else {
				if(pieces.shiftLeft(7).and(NOT_H_FILE).flag()) {
					attacks = attacks.or(pieces.shiftLeft(7));

				}
				if(pieces.shiftLeft(9).and(NOT_A_FILE).flag()) 
					attacks = attacks.or(pieces.shiftLeft(9));
			}
			
			return attacks;
		}
		
		private static BitBoard maskKnightAttacks(Square square) {
			var attacks = new BitBoard();
			var pieces = new BitBoard();
			
			pieces.set(square);
			
			if(pieces.shiftRight(17).and(NOT_H_FILE).flag()) 
				attacks = attacks.or(pieces.shiftRight(17));
			if(pieces.shiftRight(15).and(NOT_A_FILE).flag()) 
				attacks = attacks.or(pieces.shiftRight(15));
			if(pieces.shiftRight(10).and(NOT_HG_FILE).flag()) 
				attacks = attacks.or(pieces.shiftRight(10));
			if(pieces.shiftRight(6).and(NOT_AB_FILE).flag())
				attacks = attacks.or(pieces.shiftRight(6));
			
			if(pieces.shiftLeft(17).and(NOT_A_FILE).flag()) 
				attacks = attacks.or(pieces.shiftLeft(17));
			if(pieces.shiftLeft(15).and(NOT_H_FILE).flag()) 
				attacks = attacks.or(pieces.shiftLeft(15));
			if(pieces.shiftLeft(10).and(NOT_AB_FILE).flag()) 
				attacks = attacks.or(pieces.shiftLeft(10));
			if(pieces.shiftLeft(6).and(NOT_HG_FILE).flag())
				attacks = attacks.or(pieces.shiftLeft(6));
			
			return attacks;
		}
		
		public static BitBoard maskKingAttacks(Square square) {
			var attacks = new BitBoard();
			var pieces = new BitBoard();
			
			pieces.set(square);
			
			
			if(pieces.shiftRight(8).flag()) 
				attacks = attacks.or(pieces.shiftRight(8));
			if(pieces.shiftRight(9).and(NOT_H_FILE).flag()) 
				attacks = attacks.or(pieces.shiftRight(9));
			if(pieces.shiftRight(7).and(NOT_A_FILE).flag())
				attacks = attacks.or(pieces.shiftRight(7));
			if(pieces.shiftRight(1).and(NOT_H_FILE).flag())
				attacks = attacks.or(pieces.shiftRight(1));
			
			if(pieces.shiftLeft(8).flag()) 
				attacks = attacks.or(pieces.shiftLeft(8));
			if(pieces.shiftLeft(9).and(NOT_A_FILE).flag()) 
				attacks = attacks.or(pieces.shiftLeft(9));
			if(pieces.shiftLeft(7).and(NOT_H_FILE).flag())
				attacks = attacks.or(pieces.shiftLeft(7));
			if(pieces.shiftLeft(1).and(NOT_A_FILE).flag())
				attacks = attacks.or(pieces.shiftLeft(1));
			return attacks;
		}
		
	}
	
	
	public static class Slider {
		private static ULong rookMagic[] = {
				 ulong(0x8a80104000800020L),
				 ulong(0x0140002000100040L),
				 ulong(0x02801880a0017001L),
				 ulong(0x0100081001000420L),
				 ulong(0x0200020010080420L),
				 ulong(0x03001c0002010008L),
				 ulong(0x8480008002000100L),
				 ulong(0x2080088004402900L),
				 ulong(0x0000800098204000L),
				 ulong(0x2024401000200040L),
				 ulong(0x0100802000801000L),
				 ulong(0x0120800800801000L),
				 ulong(0x0208808088000400L),
				 ulong(0x0002802200800400L),
				 ulong(0x2200800100020080L),
				 ulong(0x0801000060821100L),
				 ulong(0x0080044006422000L),
				 ulong(0x0100808020004000L),
				 ulong(0x12108a0010204200L),
				 ulong(0x0140848010000802L),
				 ulong(0x0481828014002800L),
				 ulong(0x8094004002004100L),
				 ulong(0x4010040010010802L),
				 ulong(0x0000020008806104L),
				 ulong(0x0100400080208000L),
				 ulong(0x2040002120081000L),
				 ulong(0x0021200680100081L),
				 ulong(0x0020100080080080L),
				 ulong(0x0002000a00200410L),
				 ulong(0x0000020080800400L),
				 ulong(0x0080088400100102L),
				 ulong(0x0080004600042881L),
				 ulong(0x4040008040800020L),
				 ulong(0x0440003000200801L),
				 ulong(0x0004200011004500L),
				 ulong(0x0188020010100100L),
				 ulong(0x0014800401802800L),
				 ulong(0x2080040080800200L),
				 ulong(0x0124080204001001L),
				 ulong(0x0200046502000484L),
				 ulong(0x0480400080088020L),
				 ulong(0x1000422010034000L),
				 ulong(0x0030200100110040L),
				 ulong(0x0000100021010009L),
				 ulong(0x2002080100110004L),
				 ulong(0x0202008004008002L),
				 ulong(0x0020020004010100L),
				 ulong(0x2048440040820001L),
				 ulong(0x0101002200408200L),
				 ulong(0x0040802000401080L),
				 ulong(0x4008142004410100L),
				 ulong(0x02060820c0120200L),
				 ulong(0x0001001004080100L),
				 ulong(0x020c020080040080L),
				 ulong(0x2935610830022400L),
				 ulong(0x0044440041009200L),
				 ulong(0x0280001040802101L),
				 ulong(0x2100190040002085L),
				 ulong(0x80c0084100102001L),
				 ulong(0x4024081001000421L),
				 ulong(0x00020030a0244872L),
				 ulong(0x0012001008414402L),
				 ulong(0x02006104900a0804L),
				 ulong(0x0001004081002402L),
			};
			
		private static ULong bishopMagic[] = {
				 ulong(0x0040040844404084L),
				 ulong(0x002004208a004208L),
				 ulong(0x0010190041080202L),
				 ulong(0x0108060845042010L),
				 ulong(0x0581104180800210L),
				 ulong(0x2112080446200010L),
				 ulong(0x1080820820060210L),
				 ulong(0x03c0808410220200L),
				 ulong(0x0004050404440404L),
				 ulong(0x0000021001420088L),
				 ulong(0x24d0080801082102L),
				 ulong(0x0001020a0a020400L),
				 ulong(0x0000040308200402L),
				 ulong(0x0004011002100800L),
				 ulong(0x0401484104104005L),
				 ulong(0x0801010402020200L),
				 ulong(0x00400210c3880100L),
				 ulong(0x0404022024108200L),
				 ulong(0x0810018200204102L),
				 ulong(0x0004002801a02003L),
				 ulong(0x0085040820080400L),
				 ulong(0x810102c808880400L),
				 ulong(0x000e900410884800L),
				 ulong(0x8002020480840102L),
				 ulong(0x0220200865090201L),
				 ulong(0x2010100a02021202L),
				 ulong(0x0152048408022401L),
				 ulong(0x0020080002081110L),
				 ulong(0x4001001021004000L),
				 ulong(0x800040400a011002L),
				 ulong(0x00e4004081011002L),
				 ulong(0x001c004001012080L),
				 ulong(0x8004200962a00220L),
				 ulong(0x8422100208500202L),
				 ulong(0x2000402200300c08L),
				 ulong(0x8646020080080080L),
				 ulong(0x80020a0200100808L),
				 ulong(0x2010004880111000L),
				 ulong(0x623000a080011400L),
				 ulong(0x42008c0340209202L),
				 ulong(0x0209188240001000L),
				 ulong(0x400408a884001800L),
				 ulong(0x00110400a6080400L),
				 ulong(0x1840060a44020800L),
				 ulong(0x0090080104000041L),
				 ulong(0x0201011000808101L),
				 ulong(0x1a2208080504f080L),
				 ulong(0x8012020600211212L),
				 ulong(0x0500861011240000L),
				 ulong(0x0180806108200800L),
				 ulong(0x4000020e01040044L),
				 ulong(0x300000261044000aL),
				 ulong(0x0802241102020002L),
				 ulong(0x0020906061210001L),
				 ulong(0x5a84841004010310L),
				 ulong(0x0004010801011c04L),
				 ulong(0x000a010109502200L),
				 ulong(0x0000004a02012000L),
				 ulong(0x500201010098b028L),
				 ulong(0x8040002811040900L),
				 ulong(0x0028000010020204L),
				 ulong(0x06000020202d0240L),
				 ulong(0x8918844842082200L),
				 ulong(0x4010011029020020L),
			};
		public static final int bishopRelevancy[] = {
			 6, 5, 5, 5, 5, 5, 5, 6,
			 5, 5, 5, 5, 5, 5, 5, 5,
			 5, 5, 7, 7, 7, 7, 5, 5,
			 5, 5, 7, 9, 9, 7, 5, 5,
			 5, 5, 7, 9, 9, 7, 5, 5,
			 5, 5, 7, 7, 7, 7, 5, 5,
			 5, 5, 5, 5, 5, 5, 5, 5,
			 6, 5, 5, 5, 5, 5, 5, 6
		};
		
		public static final int rookRelevancy[] = {
			 12, 11, 11, 11, 11, 11, 11, 12,
			 11, 10, 10, 10, 10, 10, 10, 11,
			 11, 10, 10, 10, 10, 10, 10, 11,
			 11, 10, 10, 10, 10, 10, 10, 11,
			 11, 10, 10, 10, 10, 10, 10, 11,
			 11, 10, 10, 10, 10, 10, 10, 11,
			 11, 10, 10, 10, 10, 10, 10, 11,
			 12, 11, 11, 11, 11, 11, 11, 12
		};
		
		public static BitBoard rookMasks[] = null;
		public static BitBoard bishopMasks[] = null;
		public static BitBoard bishopAttacks[][] = null;
		public static BitBoard rookAttacks[][] = null;
				
		public static BitBoard get(String type, Square square, BitBoard occupancy) {
			
			if(rookMasks == null) {
				rookMasks = new BitBoard[64];
				bishopMasks = new BitBoard[64];
				bishopAttacks = new BitBoard[64][512];
				rookAttacks = new BitBoard[64][4096];
				init(true);
				init(false);
			}
			
			switch(type) {
			case "BISHOP":
				occupancy = occupancy.and(bishopMasks[square.ordinal()]);
				occupancy = BitBoard.fromLong(occupancy.toLong() * bishopMagic[square.ordinal()].longValue());
				occupancy = occupancy.shiftRight(64 - bishopRelevancy[square.ordinal()]);				
				return bishopAttacks[square.ordinal()][(int) occupancy.toLong()];
			case "ROOK":
				occupancy = occupancy.and(rookMasks[square.ordinal()]);
				occupancy = BitBoard.fromLong(occupancy.toLong() * rookMagic[square.ordinal()].longValue());
				occupancy = occupancy.shiftRight(64 - rookRelevancy[square.ordinal()]);
				
				return rookAttacks[square.ordinal()][(int) occupancy.toLong()];
			case "QUEEN":
				return get("BISHOP", square, occupancy).or(get("ROOK", square, occupancy));
			}

			throw new IllegalStateException("Incorrect type");
		}
		
		
		public static void init(boolean bishop) {
			

			
			for(var square: Square.values()) {
				bishopMasks[square.ordinal()] = maskBishopAttacks(square);
				rookMasks[square.ordinal()] = maskRookAttacks(square);
				
				BitBoard attackMask = bishop ? BitBoard.fromBitBoard(bishopMasks[square.ordinal()]) :
					BitBoard.fromBitBoard(rookMasks[square.ordinal()]);
				
				int relevancy = attackMask.ones().count();
				
				
				int occupancyIndices = (1 << relevancy);
								
				for(int index = 0; index < occupancyIndices; index++) {
					if(bishop) {
						BitBoard occupancy = setOccupancy(index, attackMask);
						int magicIndex = (int) ((occupancy.toLong() * bishopMagic[square.ordinal()].longValue())
								>>> (64 - bishopRelevancy[square.ordinal()]));
						
						bishopAttacks[square.ordinal()][magicIndex] = dynamicBishopAttacks(square, occupancy);
					} else {
						BitBoard occupancy = setOccupancy(index, attackMask);
						int magicIndex = (int) ((occupancy.toLong() * rookMagic[square.ordinal()].longValue())
								>>> (64 - rookRelevancy[square.ordinal()]));
						
						rookAttacks[square.ordinal()][magicIndex] = dynamicRookAttacks(square, occupancy);
					}
				}
				
			}
		}
		
		



		public static BitBoard maskBishopAttacks(Square square) {
			var attacks = new BitBoard();
			Rank r, tr = square.getRank();
			File f, tf = square.getFile();
			
			
			
			var tmp = BitBoard.fromLong(1);
			
			
			try {
				for(r = tr.add(1) , f = tf.add(1); r.leq(Rank.second) && f.leq(File.g); r=r.add(1), f=f.add(1)) {
					attacks = attacks.or(tmp.shiftLeft(Square.getSquare(r, f).ordinal()));	
				}
			} catch (ArrayIndexOutOfBoundsException e) {}
			
			
			try {
				for(r = tr.add(-1) , f = tf.add(1); r.geq(Rank.seventh) && f.leq(File.g); r=r.add(-1), f=f.add(1)) {
					attacks = attacks.or(tmp.shiftLeft(Square.getSquare(r, f).ordinal()));	
				}
			} catch (ArrayIndexOutOfBoundsException e) {}
			
			try {
				for(r = tr.add(1) , f = tf.add(-1); r.leq(Rank.second) && f.geq(File.b); r=r.add(1), f=f.add(-1)) {
					attacks = attacks.or(tmp.shiftLeft(Square.getSquare(r, f).ordinal()));	
				}
			} catch (ArrayIndexOutOfBoundsException e) {}
			
			try {
				for(r = tr.add(-1) , f = tf.add(-1); r.geq(Rank.seventh) && f.geq(File.b); r=r.add(-1), f=f.add(-1)) {
					attacks = attacks.or(tmp.shiftLeft(Square.getSquare(r, f).ordinal()));	
				}
			} catch (ArrayIndexOutOfBoundsException e) {}
			
			return attacks;
		}

		public static BitBoard maskRookAttacks(Square square) {
			var attacks = new BitBoard();
			Rank r, tr = square.getRank();
			File f, tf = square.getFile();
			
			
			var tmp = BitBoard.fromLong(1);
			
			try {
				for (r = tr.add(1); r.leq(Rank.second); r = r.add(1)) {
					attacks = attacks.or(tmp.shiftLeft(Square.getSquare(r, tf).ordinal()));
				}
			} catch (ArrayIndexOutOfBoundsException e) { }
			
			try {
				for (r = tr.add(-1); r.geq(Rank.seventh); r = r.add(-1)) {
					attacks = attacks.or(tmp.shiftLeft(Square.getSquare(r, tf).ordinal()));
				}
			} catch (ArrayIndexOutOfBoundsException e) { }
			
			try {
				for (f = tf.add(1); f.leq(File.g); f = f.add(1)) {
					attacks = attacks.or(tmp.shiftLeft(Square.getSquare(tr, f).ordinal()));
				}
			} catch (ArrayIndexOutOfBoundsException e) { }
			
			try {
				for (f = tf.add(-1); f.geq(File.b); f = f.add(-1)) {
					attacks = attacks.or(tmp.shiftLeft(Square.getSquare(tr, f).ordinal()));
				}
			} catch (ArrayIndexOutOfBoundsException e) { }
			
			return attacks;
		}
		
		public static BitBoard dynamicBishopAttacks(Square square, BitBoard block) {
			var attacks = new BitBoard();
			Rank r, tr = square.getRank();
			File f, tf = square.getFile();


			var tmp = BitBoard.fromLong(1);
			
			try {
				for(r = tr.add(1) , f = tf.add(1); r.leq(Rank.first) && f.leq(File.h); r=r.add(1), f=f.add(1)) {
					attacks = attacks.or(tmp.shiftLeft(Square.getSquare(r, f).ordinal()));	
					if(tmp.shiftLeft(Square.getSquare(r, f).ordinal()).and(block).flag())
						break;
				}
			} catch (ArrayIndexOutOfBoundsException e) {}
			
			
			try {
				for(r = tr.add(-1) , f = tf.add(1); r.geq(Rank.eigth) && f.leq(File.h); r=r.add(-1), f=f.add(1)) {
					attacks = attacks.or(tmp.shiftLeft(Square.getSquare(r, f).ordinal()));
					if(tmp.shiftLeft(Square.getSquare(r, f).ordinal()).and(block).flag())
						break;
				}
			} catch (ArrayIndexOutOfBoundsException e) {}
			
			try {
				for(r = tr.add(1) , f = tf.add(-1); r.leq(Rank.first) && f.geq(File.a); r=r.add(1), f=f.add(-1)) {
					attacks = attacks.or(tmp.shiftLeft(Square.getSquare(r, f).ordinal()));	
					if(tmp.shiftLeft(Square.getSquare(r, f).ordinal()).and(block).flag())
						break;
				}
			} catch (ArrayIndexOutOfBoundsException e) {}
			
			try {
				for(r = tr.add(-1) , f = tf.add(-1); r.geq(Rank.eigth) && f.geq(File.a); r=r.add(-1), f=f.add(-1)) {
					attacks = attacks.or(tmp.shiftLeft(Square.getSquare(r, f).ordinal()));
					if(tmp.shiftLeft(Square.getSquare(r, f).ordinal()).and(block).flag())
						break;
				}
			} catch (ArrayIndexOutOfBoundsException e) {}
			
			return attacks;
		}
		
		public static BitBoard dynamicRookAttacks(Square square, BitBoard block) {
			var attacks = new BitBoard();
			Rank r, tr = square.getRank();
			File f, tf = square.getFile();
		
			
			var tmp = BitBoard.fromLong(1);
			
			try {
				for (r = tr.add(1); r.leq(Rank.first); r = r.add(1)) {
					attacks = attacks.or(tmp.shiftLeft(Square.getSquare(r, tf).ordinal()));
					if(tmp.shiftLeft(Square.getSquare(r, tf).ordinal()).and(block).flag())
						break;
				}
			} catch (ArrayIndexOutOfBoundsException e) { }
			
			try {
				for (r = tr.add(-1); r.geq(Rank.eigth); r = r.add(-1)) {
					attacks = attacks.or(tmp.shiftLeft(Square.getSquare(r, tf).ordinal()));
					if(tmp.shiftLeft(Square.getSquare(r, tf).ordinal()).and(block).flag())
						break;
				}
			} catch (ArrayIndexOutOfBoundsException e) { }
			
			try {
				for (f = tf.add(1); f.leq(File.h); f = f.add(1)) {
					attacks = attacks.or(tmp.shiftLeft(Square.getSquare(tr, f).ordinal()));
					if(tmp.shiftLeft(Square.getSquare(tr, f).ordinal()).and(block).flag())
						break;
				}
			} catch (ArrayIndexOutOfBoundsException e) { }
			
			try {
				for (f = tf.add(-1); f.geq(File.a); f = f.add(-1)) {
					attacks = attacks.or(tmp.shiftLeft(Square.getSquare(tr, f).ordinal()));
					if(tmp.shiftLeft(Square.getSquare(tr, f).ordinal()).and(block).flag())
						break;	
				}
			} catch (ArrayIndexOutOfBoundsException e) { }
			
			return attacks;
		}
		
		public static BitBoard setOccupancy(int n, BitBoard attackMask) {
			BitBoard occupancy = new BitBoard();
			
			attackMask = BitBoard.fromBitBoard(attackMask);
			
			
			int obits = attackMask.ones().count();
			
			Square square;
			
			for(int count = 0; count < obits; count++) {
				square = Square.fromInt(attackMask.ones().first());
				attackMask.clear(square);
				
				if(BitBoard.fromLong(1).shiftLeft(count).and(n).flag()) {
					occupancy = occupancy.or(BitBoard.fromLong(1).shiftLeft(square.ordinal()));
				}
			}
			
			return occupancy;
		}
		
		
//		private static void initMagic() {
//			var squares = Square.values();
//			
//			for(var square: squares) {
//				out.printf(" ulong(0x%016xL),\n", PseudoRNG.findMagic(square, AttackTableFactory.Slider.rookRelevancy[square.ordinal()], false).longValue());
//			}
//			
//			out.println("====================================");
//			
//			for(var square: squares) {
//				out.printf(" ulong(0x%016xL),\n", PseudoRNG	.findMagic(square, AttackTableFactory.Slider.bishopRelevancy[square.ordinal()], true).longValue());
//			}
//		}
	}
}

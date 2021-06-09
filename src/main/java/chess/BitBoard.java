package chess;

import static java.lang.System.out;

import java.util.BitSet;

import com.tomgibara.bits.BitStore.BitMatches;
import com.tomgibara.bits.BitVector;

public class BitBoard {
	private BitVector bv;
	
	public BitBoard() {
		this.bv = new BitVector(64);
	}
	
	public BitBoard(BitVector bv) {
		this.bv = bv;
	}
	
	public static BitBoard fromBitSet(BitSet bs) {
		return new BitBoard(BitVector.fromBitSet(bs, 64));
	}
	
	public static BitBoard fromByteArray(byte[] arr) {
		return new BitBoard(BitVector.fromByteArray(arr, 64));
	}
	
	public static BitBoard fromBitBoard(BitBoard bb) {
		return new BitBoard(BitVector.fromStore(bb.bv));
	}
	
	public BitMatches ones() {
		return bv.ones();
	}
	
	public boolean get(Square square) {
		return bv.getBit(square.ordinal());
	}
	
	public boolean get(int n) {
		return bv.getBit(n);
	}
	
	public BitBoard set(Square square) {
		bv.setBit(square.ordinal(), true);
		return this;

	}
	
	public BitBoard set(int n) {
		bv.setBit(n, true);
		return this;
	}
	
	public BitBoard clear(Square square) {
		bv.setBit(square.ordinal(), false);
		return this;

	}
	
	public BitBoard clear(int n) {
		bv.setBit(n, false);
		return this;

	}
	
	public void clear() {
		bv.clear();
	}
	
	public BitBoard shiftLeft(int n) {
		var tmp = BitVector.fromStore(bv);
		tmp.shift(n, false);
		return new BitBoard(tmp);
	}
	
	public BitBoard shiftRight(int n) {
		var tmp = BitVector.fromStore(bv);
		tmp.shift(-n, false);
		return new BitBoard(tmp);
	}
	
	public BitBoard and(long other) {
		var tmp = BitVector.fromStore(bv);
		tmp.and().withLong(0, other);
		return new BitBoard(tmp);
	}
	
	public BitBoard and(BitBoard other) {
		var tmp = BitVector.fromStore(bv);
		tmp.and().withStore(other.bv);
		return new BitBoard(tmp);
	}
	
	public BitBoard or(BitBoard other) {
		var tmp = BitVector.fromStore(bv);
		tmp.or().withStore(other.bv);
		return new BitBoard(tmp);
	}
	
	public BitBoard or(long other) {
		var tmp = BitVector.fromStore(bv);
		tmp.or().withLong(0, other);
		return new BitBoard(tmp);
	}
	
	
	public BitBoard xor(BitBoard other) {
		var tmp = BitVector.fromStore(bv);
		tmp.xor().withStore(other.bv);
		return new BitBoard(tmp);
	}
	
	public BitBoard xor(long other) {
		var tmp = BitVector.fromStore(bv);
		tmp.xor().withLong(0, other);
		return new BitBoard(tmp);
	}
	
	
	public BitBoard not() {
		var	 tmp = BitVector.fromStore(bv);
		tmp.flip();
		return new BitBoard(tmp);
	}
	
	public boolean flag() {
		//return bv.asNumber().longValue() != 0;
		return bv.ones().count() != 0;
	}
	
	public int size() {
		return bv.size();
	}
	
	public BitSet toBitSet() {
		return bv.toBitSet();
	}
	
	public long toLong() {
		return bv.asNumber().longValue();
	}
	
	public static BitBoard fromLong(long n) {
		return fromBitSet(Bits.convert(n));
	}
	
	public Number asNumber() {
		return bv.asNumber();
	}
	
	public boolean equals(BitBoard other) {
		return this.bv.equals(other.bv);
	}
	
	public void print() {
		Rank[] ranks = Rank.values();
		File[] files = File.values();
		Square square;
		
		out.println();
		
		for(Rank rank: ranks) {
			for(File file: files) {
				square = Square.getSquare(rank, file);
				
				if(file == File.a)
					out.printf("  %d ", 8-rank.ordinal());
				
				out.printf(" %d ", this.get(square) ? 1 : 0);
			}
			
			out.println();
		}
		
		out.printf("\n     a  b  c  d  e  f  g  h\n");
		
	}
	
	public static class Bits {

		  public static BitSet convert(long value) {
		    BitSet bits = new BitSet();
		    int index = 0;
		    while (value != 0L) {
		      if (value % 2L != 0) {
		        bits.set(index);
		      }
		      ++index;
		      value = value >>> 1;
		    }
		    return bits;
		  }

		  public static long convert(BitSet bits) {
		    long value = 0L;
		    for (int i = 0; i < bits.length(); ++i) {
		      value += bits.get(i) ? (1L << i) : 0L;
		    }
		    return value;
		  }
		}
	
	
}

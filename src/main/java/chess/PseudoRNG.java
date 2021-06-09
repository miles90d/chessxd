package chess;

import static org.joou.Unsigned.uint;
import static org.joou.Unsigned.ulong;


import org.joou.UInteger;
import org.joou.ULong;


public class PseudoRNG {

	private static UInteger state = uint(1804289383);

	// my god i hate java
	public static UInteger getU32() {
		var n = state.intValue();
		
		n ^= n << 13;
		n ^= n >>> 17;
		n ^= n << 5;
		
		state = uint(n);
		
		return uint(n);
	}
	
	public static ULong getU64() {
		ULong n1, n2, n3, n4;
		n1 = ulong(getU32().longValue() & 0xFFFF);
		n2 = ulong(getU32().longValue() & 0xFFFF);
		n3 = ulong(getU32().longValue() & 0xFFFF);
		n4 = ulong(getU32().longValue() & 0xFFFF);
		
		return ulong(n1.longValue() | (n2.longValue() << 16) | (n3.longValue() << 32) | (n4.longValue() << 48));

	}
	
	public static ULong getMagic() {
		return ulong(PseudoRNG.getU64().longValue() & PseudoRNG.getU64().longValue() & PseudoRNG.getU64().longValue());
	}
	
	public static ULong findMagic(Square square, int relevancy, boolean bishop) {
		var occupancies = new BitBoard[4096];
		
		var attacks = new BitBoard[4096];
		
		var usedAttacks = new BitBoard[4096];
		
		var attackMask = bishop ? AttackTableFactory.Slider.maskBishopAttacks(square) : AttackTableFactory.Slider.maskRookAttacks(square);
		
		int occupancyIndices = 1 << relevancy;
		
		for(int i = 0; i < occupancyIndices; i++) {
			occupancies[i] = AttackTableFactory.Slider.setOccupancy(i, attackMask);
			
			attacks[i] = bishop ? AttackTableFactory.Slider.dynamicBishopAttacks(square, occupancies[i]) :
									AttackTableFactory.Slider.dynamicRookAttacks(square, occupancies[i]);
		}
		

		
		for(int randomCount = 0; randomCount < 100000000; randomCount++) {
			ULong magic = getMagic();
			
			if(BitBoard.fromLong(attackMask.toLong() * magic.longValue())
					.and(0xFF00000000000000L)
					.ones()
					.count() < 6) continue;
			
			usedAttacks = new BitBoard[4096];
			
			int index;
			boolean fail;
			for(index = 0, fail = false; !fail && index < occupancyIndices; index++) {
				int magicIndex = (int) (occupancies[index].toLong() * magic.longValue() >>> (64 - relevancy));
				
				if(usedAttacks[magicIndex] == null)
					usedAttacks[magicIndex] = BitBoard.fromBitBoard(attacks[index]);
				else if(!(usedAttacks[magicIndex].equals(attacks[index]))){
					fail = true;
				}
			}
			
			if(!fail)
				return magic;
		}
		
		throw new IllegalStateException("Magic number failed");
	}
	
}
 
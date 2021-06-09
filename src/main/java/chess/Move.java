package chess;
import static java.lang.System.out;


@SuppressWarnings("preview")
public record Move(
		Square src,
		Square dst,
		Piece piece,
		Piece promotedPiece,
		boolean captureFlag,
		boolean doublePushFlag,
		boolean enpassantFlag,
		boolean castleFlag) {
	
	public void printUCI() {
		out.printf("%s%s%s\n",
				src.toString(),
				dst.toString(),
				promotedPiece != null ? promotedPiece.toString().toLowerCase() : "");
	}
	
	public void print() {
		
		
        out.printf("      %s%s%s   %s         %d         %d         %d         %d\n",
        		src.toString(),
                dst.toString(),
                promotedPiece != null ? promotedPiece.toString().toLowerCase() : " ",
                piece.toString(),
                captureFlag ? 1 : 0,
                doublePushFlag ? 1 : 0,
                enpassantFlag ? 1 : 0,
                castleFlag ? 1 : 0);
	}
	
}

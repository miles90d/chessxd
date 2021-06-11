package chess;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class ChessListener implements MouseListener {

	private Square src;
	private Square dst;
	private Board board;
	private int squareSize;
	private ChessPanel panel;
	
	private boolean dstMode = false;
	
	public ChessListener(Board board, int boardSize, ChessPanel panel) {
		this.board = board;
		this.squareSize = boardSize / 8;
		this.panel = panel;
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		var file = File.values()[e.getX() / squareSize];
		var rank = Rank.values()[e.getY() / squareSize];
		var square = Square.getSquare(rank, file);
		
		if(!dstMode) {
			Piece pieceClicked = null;
			for(var piece: Piece.values()) {
				if(board.getBitboards()[piece.ordinal()].get(square)) {
					pieceClicked = piece;
				}
			}
			
			if(pieceClicked != null && pieceClicked.isColor(this.board.getSide())) {
				src = Square.getSquare(rank, file);
				panel.highlightSquare(src);
				System.out.println("src: " + src.toString());
				dstMode = true;
			}

		} else {
			dst = Square.getSquare(rank, file);
			Piece pieceClicked = null;
			
			for(var piece: Piece.values()) {
				if(board.getBitboards()[piece.ordinal()].get(dst)) {
					pieceClicked = piece;
				}
			}
			
			System.out.println("dst: " + dst.toString());
			
			var moves = board.generateMoves();
			for(var move: moves) {
				if(move.src() == src && move.dst() == dst) {
					if(move.promotedPiece() != null) {
						Piece promotionPiece = null;
						ImageIcon pIcon = board.getSide() == Color.white ? new ImageIcon(panel.wP) : new ImageIcon(panel.bP);
						JFrame promotionSelect = new JFrame();
						
						String[] choices = {"Queen", "Rook", "Bishop", "Knight"};
						String choice = (String)JOptionPane.showInputDialog(promotionSelect, "Promote your pawn!",
								"Promotion", JOptionPane.PLAIN_MESSAGE, pIcon,
								choices, "Queen");
						
						switch(choice) {
						case "Queen" -> promotionPiece = (this.board.getSide() == Color.white) ? Piece.Q : Piece.q;
						case "Bishop" -> promotionPiece = (this.board.getSide() == Color.white) ? Piece.B : Piece.b;
						case "Knight" -> promotionPiece = (this.board.getSide() == Color.white) ? Piece.N : Piece.n;
						case "Rook" -> promotionPiece = (this.board.getSide() == Color.white) ? Piece.R : Piece.r;
						}
					
						board.makeMove(new Move(src, dst, move.piece(), promotionPiece, move.captureFlag(), false, false, false));
					} else {
						board.makeMove(move);
					}
					panel.clearHighlight();
					panel.repaint();
					dstMode = false;
					return;
				}		
			}	
			
			if(pieceClicked != null && pieceClicked.isColor(this.board.getSide())) {
				src = Square.getSquare(rank, file);
				panel.highlightSquare(src);
				System.out.println("src: " + src.toString());
			}
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	

}

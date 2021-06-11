package chess;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

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
		
		
		if(!dstMode) {
			src = Square.getSquare(rank, file);
			System.out.println("src: " + src.toString());
			dstMode = true;
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
					board.makeMove(move);
					panel.repaint();
					dstMode = false;
					return;
				}		
			}	
			
			if(pieceClicked != null) {
				src = Square.getSquare(rank, file);
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

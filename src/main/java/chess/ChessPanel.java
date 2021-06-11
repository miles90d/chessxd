package chess;


import static java.lang.System.out;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class ChessPanel extends JPanel {
	
	
	private final int boardSize, squareSize;
	private final Image wP, wN, wB, wR, wQ, wK,
							bP, bN, bB, bR, bQ, bK;
	
	private final Image boardImg;
	private Board board;
	
	
	
	public ChessPanel(int boardSize, Board board) throws IOException {
		this.boardSize = boardSize;
		this.squareSize = boardSize / 8;
		
		this.wP = getScaledImage(ImageIO.read(getClass().getClassLoader().getResource("wP.svg")), squareSize);
		this.wN = getScaledImage(ImageIO.read(getClass().getClassLoader().getResource("wN.svg")), squareSize);
		this.wB = getScaledImage(ImageIO.read(getClass().getClassLoader().getResource("wB.svg")), squareSize);
		this.wR = getScaledImage(ImageIO.read(getClass().getClassLoader().getResource("wR.svg")), squareSize);
		this.wQ = getScaledImage(ImageIO.read(getClass().getClassLoader().getResource("wQ.svg")), squareSize);
		this.wK = getScaledImage(ImageIO.read(getClass().getClassLoader().getResource("wK.svg")), squareSize);
		
		this.bP = getScaledImage(ImageIO.read(getClass().getClassLoader().getResource("bP.svg")), squareSize);
		this.bN = getScaledImage(ImageIO.read(getClass().getClassLoader().getResource("bN.svg")), squareSize);
		this.bB = getScaledImage(ImageIO.read(getClass().getClassLoader().getResource("bB.svg")), squareSize);
		this.bR = getScaledImage(ImageIO.read(getClass().getClassLoader().getResource("bR.svg")), squareSize);
		this.bQ = getScaledImage(ImageIO.read(getClass().getClassLoader().getResource("bQ.svg")), squareSize);
		this.bK = getScaledImage(ImageIO.read(getClass().getClassLoader().getResource("bK.svg")), squareSize);
		
		
		this.boardImg = getScaledImage(ImageIO.read(getClass().getClassLoader().getResource("horsey.jpg")), boardSize);
		
		this.board = board;


		
	}
	
	public void paint(Graphics g) {
		int x = 0, y = 0;
		
		
		g.drawImage(this.boardImg, 0, 0, null);
		for(var rank: Rank.values()) {
			x = 0;
			for(var file: File.values()) {
				var square = Square.getSquare(rank, file);
				
				Piece piece = null;

				for(var bbPiece: Piece.values())  {
					if(board.getBitboards()[bbPiece.ordinal()].get(square)) 
						piece = bbPiece;
				}
				
				if(piece != null) {
					switch(piece) {
					case P -> g.drawImage(this.wP, x, y, null);
					case N -> g.drawImage(this.wN, x, y, null);
					case B -> g.drawImage(this.wB, x, y, null);
					case R -> g.drawImage(this.wR, x, y, null);
					case Q -> g.drawImage(this.wQ, x, y, null);
					case K -> g.drawImage(this.wK, x, y, null);
					case p -> g.drawImage(this.bP, x, y, null);
					case n -> g.drawImage(this.bN, x, y, null);
					case b -> g.drawImage(this.bB, x, y, null);
					case r -> g.drawImage(this.bR, x, y, null);
					case q -> g.drawImage(this.bQ, x, y, null);
					case k -> g.drawImage(this.bK, x, y, null);
					}
				}
				
				x += squareSize;
				
			}
			
			y += squareSize;
			
		}
		
		
		
	}

	private static Image getScaledImage(Image src, int n) {
		BufferedImage resized = new BufferedImage(n, n, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = resized.createGraphics();

		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.drawImage(src, 0, 0, n, n, null);
		g.dispose();
		return resized;
	}
}

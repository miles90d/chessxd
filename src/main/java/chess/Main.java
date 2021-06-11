package chess;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.IOException;

import javax.swing.JFrame;

public class Main {
	
	private static final int WIDTH = 600, HEIGHT = 600;
	
	public static void main(String[] args) throws IOException {
		Board board = new Board();
		
		ChessPanel panel = new ChessPanel(WIDTH, board, new Dimension(WIDTH, HEIGHT));
		panel.addMouseListener(new ChessListener(board, WIDTH, panel));
		JFrame mainFrame = new JFrame("chessxd");
		mainFrame.setLayout(new BorderLayout());
		mainFrame.add(panel, BorderLayout.CENTER);
		mainFrame.pack();
		mainFrame.setResizable(false);
		mainFrame.setVisible(true);

		
		
				
	}
}

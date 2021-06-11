package chess;

import java.awt.Dimension;
import java.io.IOException;

import javax.swing.JFrame;

public class Main {
	
	private static final int WIDTH = 600, HEIGHT = 600;
	
	public static void main(String[] args) throws IOException {

		Board board = new Board();
		board.generateMoves();
		
		JFrame window = new JFrame("Chess"); // Create Frame and give it the method named "window"
		var windowDimension = new Dimension(WIDTH+10, HEIGHT+33);
		window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		window.setResizable(false);
		window.setSize(windowDimension);
		window.setPreferredSize(windowDimension);
		window.setMinimumSize(windowDimension);
		window.setMaximumSize(windowDimension);
		
		
		ChessPanel panel = new ChessPanel(WIDTH, board);
		panel.addMouseListener(new ChessListener(board, WIDTH, panel));
		//This will add the panel we just made.
		window.setContentPane(panel);
	
		
		//This will set the size of the window.
		
		

		System.out.println(window.getSize().toString());

		window.setVisible(true);

		
		
				
	}
}

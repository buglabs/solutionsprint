package connect4.ui;

import java.awt.Color;

import javax.swing.JPanel;

public class BoardPanel extends JPanel {

	private int boundsize = 35;
	private int discsize = 25;
	
	Disc[][] board;
	private int rowsNumber;
	private int columnsNumber;
	private static Color boardBackground = Color.WHITE ;

	public BoardPanel(int rowsNumber, int columnsNumber) {
		this.setBackground(boardBackground);
		this.setLayout(null);
		this.rowsNumber = rowsNumber;
		this.columnsNumber = columnsNumber;
		initBoard();
	}

	private void initBoard() {
		board = new Disc[rowsNumber][columnsNumber];
		for (int i = 0; i < board.length; i++)
			for (int j = 0; j < board[0].length; j++) {
				board[i][j] = new Disc(Color.WHITE, 20, 20);
				board[i][j].setBounds(j * 50, i * 50, 50, 50);
				this.add(board[i][j]);
			}
	}

	public void update(int[][] a) {
		for (int i = 0; i < board.length; i++)
			for (int j = 0; j < board[0].length; j++) {
				if (a[i][j] == 0)
					board[i][j].setColor(Color.WHITE);
				else if (a[i][j] == 1)
					board[i][j].setColor(Color.RED);
				else
					board[i][j].setColor(Color.BLUE);
			}
	}
}

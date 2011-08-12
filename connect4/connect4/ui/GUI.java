package connect4.ui;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;
import javax.swing.JOptionPane;



import connect4.engine.InvalidColumnIndexException;
import connect4.engine.Player;

public class GUI extends AbstractUserInterface implements KeyListener, MouseListener {


	JFrame frame;
	
	BoardPanel boardPanel;

	public GUI() {
		frame = new JFrame();
		frame.setLayout(null);		
		frame.getContentPane().setBackground(Color.WHITE);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	private void initBoard() {
		boardPanel = new BoardPanel(super.getEngine().getRowsNumber(),super.getEngine().getColumnsNumber());
	}

	@Override
	public void show() {
		frame.setVisible(true);
	}

	@Override
	public void start() {
		initBoard();
		frame.setBounds(50, 50, 50 * super.getEngine().getColumnsNumber(), 50 * super.getEngine()
				.getRowsNumber() + 30);
		frame.addKeyListener(this);
		frame.addMouseListener(this);
		boardPanel.setBounds(0, 0, frame.getWidth(), frame.getHeight());
		frame.getContentPane().add(boardPanel);
	}

	@Override
	public void updateBoard() {
		boardPanel.update(super.getEngine().getBoard().get2dArray());
	}
	
	@Override
	public void gameOver() {
		frame.removeMouseListener(this);
		frame.removeKeyListener(this);
		frame.dispose();
	}

	@Override
	public void keyTyped(KeyEvent key) {
		
	}

	@Override
	public void keyReleased(KeyEvent key) {
		int i = key.getKeyCode() - 49;
		try {
			put(i);
		} catch (InvalidColumnIndexException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent ee) {
		try {
			put(ee.getX() / 50);
		} catch (InvalidColumnIndexException e1) {
			e1.printStackTrace();
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}
}

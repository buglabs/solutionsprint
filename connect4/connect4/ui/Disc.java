package connect4.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;

import javax.swing.JLabel;

public class Disc extends JLabel {

	private Color c;
	private int width;
	private int height;
	private final Paint borderColor = Color.BLACK;

	public Disc(Color c, int width, int height) {
		this.c = c;
		this.height = height;
		this.width = width;
	}

	public void setColor(Color c) {
		this.c = c;
		repaint();
	}
	
	@Override
	public void paintComponent(Graphics g) {
		Graphics2D ga = (Graphics2D) g;
		
		ga.setPaint(c);
		Shape circle = new Ellipse2D.Float(0, 0, width, height);
		ga.fill(circle);
		ga.draw(circle);
		
		ga.setPaint(borderColor);
		Shape circle2 = new Ellipse2D.Float(0, 0, width, height);
		ga.draw(circle2);
	}
}

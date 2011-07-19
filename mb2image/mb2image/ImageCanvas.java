package mb2image;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

public class ImageCanvas extends Canvas {
	
	private static final long serialVersionUID = -9119167926427852857L;
	private Image img;

	public ImageCanvas (Image img){
		this.img = img;
	}

	public void paint(Graphics g){
		g.drawImage(img, 0, 0, this);
	}
	
	public void update (Graphics g){
		paint(g);
	}

 	public Dimension getMinimumSize()
 	{
 		return getPreferredSize();
 	}
 	
 	public Dimension getPreferredSize()
 	{
 		int w, h;
 
 		while ((w = img.getWidth(this)) == -1);
 		while ((h = img.getHeight(this)) == -1);
 
 		return new Dimension(w, h);
 	}

	public Image getImg() {
		return img;
	}

	public void setImg(Image img) {
		this.img = img;
	}
	
}

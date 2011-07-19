package mb2image;

import java.awt.BorderLayout;
import java.awt.Graphics2D;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import com.buglabs.application.ServiceTrackerHelper.ManagedRunnable;
import com.buglabs.bug.module.lcd.pub.IModuleDisplay;

public class mb2image implements ManagedRunnable, WindowStateListener {
	private IModuleDisplay display;
	private JFrame frame;
	private ImageCanvas ic;

	private final static int imageWidth  = 2048;
	private final static int imageHeight = 1536;
	private final static int IMG_WIDTH = 320;
	private final static int IMG_HEIGHT = 280;
	private BufferedImage image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);
	private BufferedImage resizedImage = new BufferedImage(IMG_WIDTH, IMG_HEIGHT, BufferedImage.TYPE_INT_RGB);
	private final int [] buf = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
	@Override
	public void run(Map<Object, Object> services) {
		// TODO Auto-generated method stub
		display 			= (IModuleDisplay) 			services.get(IModuleDisplay.class.getCanonicalName());
		frame = new JFrame();
		frame.addWindowStateListener(this);		
		frame.setVisible(true);

		while(image!=null){
			try {
				// Create a URL for the image's location
				URL url = new URL("http://motherbug2.local/service/mom_ping");

				// Get the image
				image = ImageIO.read(url);
			} catch (MalformedURLException e) {
			} catch (IOException e) {
			}


			Graphics2D g = resizedImage.createGraphics();
			g.drawImage(image, 0, 0, IMG_WIDTH, IMG_HEIGHT, null);
			g.dispose();
			if(ic!=null)
				ic.repaint();
			else
				ic = new ImageCanvas(resizedImage);
			frame.add(ic, BorderLayout.CENTER);
			frame.setSize(320, 280);
			try {
				Thread.sleep(4000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	@Override
	public void shutdown() {
		// TODO Auto-generated method stub
		if (frame != null) {
			frame.dispose();
			frame = null;
		}
	}

	@Override
	public void windowStateChanged(WindowEvent we) {
		// TODO Auto-generated method stub
		if (we.getNewState() == WindowEvent.WINDOW_CLOSING) {
			shutdown();
		}
	}

}
